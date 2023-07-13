package io.jomatt.multitenant.sample.common.tenant;


import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
/**
 * DTO for the Tenant Entity
 */
public class TenantDto {
    @NotNull(message = "Tenant must have name")
    private String name;
}