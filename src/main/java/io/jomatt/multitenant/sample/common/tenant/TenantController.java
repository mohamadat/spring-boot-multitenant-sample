package io.jomatt.multitenant.sample.common.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
    public ResponseEntity<AdminTokenDto> createTenant(@RequestBody TenantDto tenantDto) {
            Tenant t = new Tenant(tenantDto);
            tenantRepository.save(t);
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("CREATE SCHEMA " + tenantDto.getName()).executeUpdate();
            entityManager.getTransaction().commit();
            entityManager.close();


        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.add("PRIVATE-TOKEN", "xyz");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id","admin-cli");
        map.add("grant_type","password");
        map.add("username","fofo");
        map.add("password","fofo");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<AdminTokenDto> response =
                restTemplate.exchange("http://localhost:9090/realms/master/protocol/openid-connect/token",
                        HttpMethod.POST,
                        entity,
                        AdminTokenDto.class);
        System.out.println(response.toString());
        return response;
    }
}
