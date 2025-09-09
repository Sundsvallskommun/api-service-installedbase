ALTER TABLE IF EXISTS delegation_facility
    DROP FOREIGN KEY fk_delegation_facility_delegation;

ALTER TABLE IF EXISTS delegation_facility
    ADD facility_ref_id VARCHAR(36) NULL;

ALTER TABLE IF EXISTS delegation_facility
    ADD PRIMARY KEY (delegation_id, facility_ref_id);

ALTER TABLE IF EXISTS delegation_facility
    ADD CONSTRAINT uk_delegation_facility UNIQUE (delegation_id, facility_ref_id);

ALTER TABLE IF EXISTS facility
    ADD CONSTRAINT uk_facility_org UNIQUE (facility_id, business_engagement_org_id);

CREATE INDEX idx_facility_id ON facility (facility_id);

ALTER TABLE IF EXISTS delegation_facility
    ADD CONSTRAINT fk_delegation_facility_delegationSPA28m FOREIGN KEY (facility_ref_id) REFERENCES facility (id);

ALTER TABLE IF EXISTS delegation_facility
    DROP INDEX uk_delegation_id_facility_id;

ALTER TABLE IF EXISTS delegation_facility
    DROP COLUMN facility_id;
