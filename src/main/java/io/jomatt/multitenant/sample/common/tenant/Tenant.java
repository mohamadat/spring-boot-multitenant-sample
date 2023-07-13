package io.jomatt.multitenant.sample.common.tenant;

import io.jomatt.multitenant.sample.common.config.CurrentTenantResolver;
import io.quantics.multitenant.tenantdetails.TenantSchemaDetails;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Entity
@Setter
@Table(name = "tenant", schema = CurrentTenantResolver.DEFAULT_SCHEMA)
public class Tenant implements TenantSchemaDetails {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private String id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "schema", nullable = false, unique = true)
    private String schema;

    @NotNull
    @Column(name = "issuer", nullable = false, unique = true)
    private String issuer;

    public Tenant(TenantDto tenantDto) {
       this.setIssuer(tenantDto.getName());
       this.setName(tenantDto.getName());
       this.setSchema(tenantDto.getName());
        this.setId(tenantDto.getName());
    }

    @Override
    public String getJwkSetUrl() {
        return this.issuer + "/protocol/openid-connect/certs";
    }

}
