CREATE TABLE IF NOT EXISTS public.tenant (
    "id" varchar NOT NULL
        CONSTRAINT tenant_pk
            PRIMARY KEY,
    "name" varchar NOT NULL
        CONSTRAINT tenant_name_uc
            UNIQUE,
    "schema" varchar NOT NULL
        CONSTRAINT tenant_schema_uc
            UNIQUE,
    "issuer" varchar NOT NULL
        CONSTRAINT tenant_issuer_uc
            UNIQUE
);

INSERT INTO public.tenant
VALUES ('tenant1', 'Tenant 1', 'tenant1', 'https://idp.example.org/tenant-1'),
       ('tenant2', 'Tenant 2', 'tenant2', 'https://idp.example.org/tenant-2');

CREATE SCHEMA IF NOT EXISTS "tenant1";
CREATE SCHEMA IF NOT EXISTS "tenant2";

UPDATE tenant
SET issuer = 'http://localhost:9090/realms/tenant-1' WHERE id = 'tenant1';
UPDATE tenant
SET issuer = 'http://localhost:9090/realms/tenant-2' WHERE id = 'tenant2';