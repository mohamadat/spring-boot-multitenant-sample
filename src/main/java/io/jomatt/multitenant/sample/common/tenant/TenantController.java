package io.jomatt.multitenant.sample.common.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Map;

@RestController
@RequestMapping("/tenants")
public class TenantController {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private final TenantService service;
    private final TenantRepository tenantRepository;

    @Autowired
    public TenantController(TenantService service, TenantRepository tenantRepository) {
        this.service = service;
        this.tenantRepository = tenantRepository;
    }

    @GetMapping
    public Iterable<Tenant> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Tenant createTenant(@RequestBody TenantDto tenantDto) throws InterruptedException {
        System.out.println("555555555");
            Tenant t = new Tenant(tenantDto);
            tenantRepository.save(t);
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("CREATE SCHEMA " + tenantDto.getName()).executeUpdate();
            entityManager.getTransaction().commit();
            entityManager.close();
        System.out.println("88888888888888888");

        String adminToken = getAdminToken();
        String realmName = createKeyLoakTenant(adminToken, t.getName());
        Thread.sleep(5000); // Sleep for 5 seconds (5000 milliseconds)
        createClient(realmName, adminToken);

        return t;
    }

    private String getAdminToken(){
        String tokenUrl = "http://localhost:9090/realms/master/protocol/openid-connect/token";
        String clientId = "admin-cli";
        String username = "fofo";
        String password = "fofo";
        String grantType = "password";

        // First, send the request to get the access token
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> tokenBody = new LinkedMultiValueMap<>();
        tokenBody.add("client_id", clientId);
        tokenBody.add("username", username);
        tokenBody.add("password", password);
        tokenBody.add("grant_type", grantType);

        HttpEntity<MultiValueMap<String, String>> tokenRequestEntity = new HttpEntity<>(tokenBody, tokenHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> tokenResponseEntity = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                tokenRequestEntity,
                Object.class
        );

        // Extract the access token from the response
        Map<String, Object> responseBody = (Map<String, Object>) tokenResponseEntity.getBody();
        return (String) responseBody.get("access_token");
    }
    private String createKeyLoakTenant(String adminToken, String realmId){
        String createRealmUrl = "http://localhost:9090/admin/realms";
        RestTemplate restTemplate = new RestTemplate();

        // Create the request body for the second request
        HttpHeaders createRealmHeaders = new HttpHeaders();
        createRealmHeaders.setContentType(MediaType.APPLICATION_JSON);
        createRealmHeaders.setBearerAuth(adminToken);

//        String createRealmRequestBody = "{ \"id\": \"newrealm\", \"realm\": \"newrealm\", \"displayName\": \"New Realm\", \"enabled\": true, \"sslRequired\": \"external\", \"registrationAllowed\": false, \"loginWithEmailAllowed\": true, \"duplicateEmailsAllowed\": false, \"resetPasswordAllowed\": false, \"editUsernameAllowed\": false, \"bruteForceProtected\": true }";
        String createRealmRequestBody = String.format("{ \"id\": \"%s\", \"realm\": \"%s\", \"displayName\": \"New Realm\", \"enabled\": true, \"sslRequired\": \"external\", \"registrationAllowed\": false, \"loginWithEmailAllowed\": true, \"duplicateEmailsAllowed\": false, \"resetPasswordAllowed\": false, \"editUsernameAllowed\": false, \"bruteForceProtected\": true }", realmId, realmId);


        HttpEntity<String> createRealmRequestEntity = new HttpEntity<>(createRealmRequestBody, createRealmHeaders);

        // Send the second request to create the realm
        ResponseEntity<String> createRealmResponseEntity = restTemplate.exchange(
                createRealmUrl,
                HttpMethod.POST,
                createRealmRequestEntity,
                String.class
        );

        // Handle the response of the second request as needed
        HttpStatus createRealmStatusCode = createRealmResponseEntity.getStatusCode();
        String createRealmResponseBody = createRealmResponseEntity.getBody();

        System.out.println("Create Realm Status Code: " + createRealmStatusCode);
        System.out.println("Create Realm Response Body: " + createRealmResponseBody);
        return realmId;

    }


    private void createClient(String realmName, String adminToken){
        String apiUrl = "http://localhost:9090/admin/realms/" + realmName + "/clients";
        String clientId = "app";
        System.out.println(apiUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("clientId", clientId);
        requestBody.add("id", "app");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Handle the response as needed
        HttpStatus statusCode = responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();

        System.out.println("Response Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);
    }
    private void createUser(){}
    private void createRole(){}
    private void createGroup(){}
}
