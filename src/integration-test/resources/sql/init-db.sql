insert into facility_delegation (municipality_id, created, updated, business_engagement_org_id, delegated_to, id, owner)
values  ('2281', '2025-07-11 10:03:26.342668', null, '5591628136', 'f2949c12-bb83-406a-be96-bb8628f14612', '24504e65-08cf-4bc3-8f4f-a07204748c13', '81471222-5798-11e9-ae24-57fa13b361e1'),
        ('1984', '2025-07-11 10:04:09.623724', null, '5591628136', '81471222-5798-11e9-ae24-57fa13b361e2', '78ee675d-4ab5-4c9c-a80a-5c508f1c55af', '81471222-5798-11e9-ae24-57fa13b361e1'),
        ('2281', '2025-07-11 10:03:07.426032', null, '5591628136', '81471222-5798-11e9-ae24-57fa13b361e2', 'abdf934d-5696-41c9-84f5-28c79202d6c5', '81471222-5798-11e9-ae24-57fa13b361e1'),
        ('2281', '2025-07-11 10:03:49.215221', null, '5591628136', 'f2949c12-bb83-406a-be96-bb8628f14612', 'cbba5149-82db-4e41-a2bb-f1ca396dc961', 'adb574c3-16a7-430e-a0de-823592e0db92');

insert into facility_delegation_facilities (facility_delegation_id, facility)
values  ('abdf934d-5696-41c9-84f5-28c79202d6c5', 'facility-1'),
        ('abdf934d-5696-41c9-84f5-28c79202d6c5', 'facility-2'),
        ('abdf934d-5696-41c9-84f5-28c79202d6c5', 'facility-3'),
        ('24504e65-08cf-4bc3-8f4f-a07204748c13', 'facility-1'),
        ('24504e65-08cf-4bc3-8f4f-a07204748c13', 'facility-2'),
        ('24504e65-08cf-4bc3-8f4f-a07204748c13', 'facility-3'),
        ('cbba5149-82db-4e41-a2bb-f1ca396dc961', 'facility-420'),
        ('cbba5149-82db-4e41-a2bb-f1ca396dc961', 'facility-421'),
        ('cbba5149-82db-4e41-a2bb-f1ca396dc961', 'facility-422'),
        ('78ee675d-4ab5-4c9c-a80a-5c508f1c55af', 'facility-69'),
        ('78ee675d-4ab5-4c9c-a80a-5c508f1c55af', 'facility-70'),
        ('78ee675d-4ab5-4c9c-a80a-5c508f1c55af', 'facility-71');
