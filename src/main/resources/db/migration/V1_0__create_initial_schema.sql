create table facility_delegation
(
    municipality_id            varchar(4)  null,
    created                    datetime(6) not null,
    updated                    datetime(6) null,
    business_engagement_org_id varchar(20) null,
    delegated_to               varchar(36) not null,
    id                         varchar(36) not null primary key,
    owner                      varchar(36) not null,
    constraint uk_delegated_to_owner
        unique (delegated_to, owner, municipality_id)
);

create index idx_municipality_id_delegated_to
    on facility_delegation (municipality_id, delegated_to);

create index idx_municipality_id_owner
    on facility_delegation (municipality_id, owner);

create table facility_delegation_facilities
(
    facility_delegation_id varchar(36)  not null,
    facility               varchar(256) null,
    constraint fk_facility_delegation_facilities_facility_delegation_id
        foreign key (facility_delegation_id) references facility_delegation (id)
);

