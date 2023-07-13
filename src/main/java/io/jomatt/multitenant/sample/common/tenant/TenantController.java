package io.jomatt.multitenant.sample.common.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Tenant> createTenant(@RequestBody TenantDto tenantDto) {
            Tenant t = new Tenant(tenantDto);
            tenantRepository.save(t);
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("CREATE SCHEMA " + tenantDto.getName()).executeUpdate();
            entityManager.getTransaction().commit();
            entityManager.close();
        return ResponseEntity.status(HttpStatus.CREATED).body(t);
    }
}
