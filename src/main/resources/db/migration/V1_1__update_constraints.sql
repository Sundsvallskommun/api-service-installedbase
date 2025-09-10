ALTER TABLE IF EXISTS delegation_facility
    DROP FOREIGN KEY fk_delegation_facility_delegation;

ALTER TABLE IF EXISTS delegation_facility
    DROP FOREIGN KEY fk_delegation_facility_facility;

ALTER TABLE delegation_facility
    CHANGE COLUMN delegation_id delegation_ref_id VARCHAR(36) NOT NULL;

ALTER TABLE delegation_facility
    CHANGE COLUMN facility_id facility_ref_id VARCHAR(36) NOT NULL;

ALTER TABLE IF EXISTS delegation_facility
    ADD PRIMARY KEY (delegation_ref_id, facility_ref_id);

ALTER TABLE IF EXISTS delegation_facility
    ADD CONSTRAINT uk_delegation_facility UNIQUE (delegation_ref_id, facility_ref_id);

ALTER TABLE IF EXISTS facility
    ADD CONSTRAINT uk_facility_id_business_engagement_org_id UNIQUE (facility_id, business_engagement_org_id);

CREATE INDEX idx_facility_id ON facility (facility_id);

ALTER TABLE IF EXISTS delegation_facility
    ADD CONSTRAINT fk_delegation_facility_delegation FOREIGN KEY (delegation_ref_id) REFERENCES delegation (id);

ALTER TABLE IF EXISTS delegation_facility
    ADD CONSTRAINT fk_delegation_facility_facility FOREIGN KEY (facility_ref_id) REFERENCES facility (id);
