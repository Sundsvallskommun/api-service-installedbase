
    create table delegation (
        municipality_id varchar(4),
        created datetime(6) not null,
        updated datetime(6),
        delegated_to varchar(36) not null,
        id varchar(36) not null,
        owner varchar(36) not null,
        primary key (id)
    ) engine=InnoDB;

    create table delegation_facility (
        delegation_id varchar(36) not null,
        facility_id varchar(36) not null,
        constraint uk_delegation_id_facility_id primary key (delegation_id, facility_id)
    ) engine=InnoDB;

    create table facility (
        business_engagement_org_id varchar(20),
        id varchar(36) not null,
        facility_id varchar(256) not null,
        primary key (id)
    ) engine=InnoDB;

    create index idx_municipality_id_delegated_to 
       on delegation (municipality_id, delegated_to);

    create index idx_municipality_id_owner 
       on delegation (municipality_id, owner);

    alter table if exists delegation 
       add constraint uk_delegated_to_owner unique (delegated_to, owner, municipality_id);

    create index idx_facility_id 
       on facility (facility_id);

    alter table if exists facility 
       add constraint uk_facility_id unique (facility_id);

    alter table if exists delegation_facility 
       add constraint fk_delegation_facility_delegation 
       foreign key (facility_id) 
       references facility (id);

    alter table if exists delegation_facility 
       add constraint fk_delegation_facility_facility 
       foreign key (delegation_id) 
       references delegation (id);
