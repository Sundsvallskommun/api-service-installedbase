create table delegated_facility
(
	id                         int auto_increment primary key,
	municipality_id            varchar(4)   null,
	end                        datetime(6)  null,
	start                      datetime(6)  not null,
	business_engagement_org_id varchar(20)  null,
	delegated_to               varchar(36)  not null,
	owner                      varchar(36)  not null,
	status                     varchar(256) not null,
	constraint uk_delegated_to_owner
		unique (delegated_to, owner)
);

create index idx_delegated_to
	on delegated_facility (delegated_to);

create index idx_municipality_id
	on delegated_facility (municipality_id);

create index idx_owner
	on delegated_facility (owner);

create table delegated_facility_facilities
(
	delegated_facility_id int         not null,
	facility              varchar(256) not null,
	constraint fk_delegated_facility_facilities_delegated_facility_id
		foreign key (delegated_facility_id) references delegated_facility (id)
);

