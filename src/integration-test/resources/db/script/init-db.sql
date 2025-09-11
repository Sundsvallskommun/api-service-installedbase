insert into delegation (municipality_id, created, updated, delegated_to, id, owner)
values ('2281', '2025-07-11 10:03:26.342668', null, 'f2949c12-bb83-406a-be96-bb8628f14612',
        '24504e65-08cf-4bc3-8f4f-a07204748c13', '81471222-5798-11e9-ae24-57fa13b361e1'),
       ('1984', '2025-07-11 10:04:09.623724', null, '81471222-5798-11e9-ae24-57fa13b361e2',
        '78ee675d-4ab5-4c9c-a80a-5c508f1c55af', '81471222-5798-11e9-ae24-57fa13b361e1'),
       ('2281', '2025-07-11 10:03:07.426032', null, '81471222-5798-11e9-ae24-57fa13b361e2',
        'abdf934d-5696-41c9-84f5-28c79202d6c5', '81471222-5798-11e9-ae24-57fa13b361e1'),
       ('2281', '2025-07-11 10:03:49.215221', null, 'f2949c12-bb83-406a-be96-bb8628f14612',
        'cbba5149-82db-4e41-a2bb-f1ca396dc961', 'adb574c3-16a7-430e-a0de-823592e0db92');

insert into facility(business_engagement_org_id, id, facility_id)
values ('5591628136', '6081da18-db02-415c-b258-bb84d93b1794', 'Facility-1'),
       ('5591628136', 'a553b70f-83ad-4dc6-ba45-40a73741837b', 'Facility-2'),
       ('5591628136', '13eb6487-00dc-4480-837b-9f3812f442cf', 'Facility-3'),
       ('5591628137', '61d270d2-9b94-4c57-895f-7088d74af6ef', 'Facility-3'),
       ('5591628136', '82f40ee5-ded4-40b8-a07b-c0d7eae19beb', 'facility-69'),
       ('5591628136', '3499c3f1-3ab2-4523-8b16-a872b932ce2e', 'facility-70'),
       ('5591628136', 'e7dff452-4dc3-407f-a7cb-ace9f932a775', 'facility-71'),
       ('5591628136', '8fe56d6c-2a28-40aa-99ba-2b901144da9e', 'facility-420'),
       ('5591628136', '926be7f8-2a0e-4dbc-9653-bb73bce0b3a0', 'facility-421'),
       ('5591628136', '769af92d-1aa5-4ba1-8573-f39d227bc392', 'facility-422');

insert into delegation_facility (delegation_ref_id, facility_ref_id)
values ('abdf934d-5696-41c9-84f5-28c79202d6c5', '6081da18-db02-415c-b258-bb84d93b1794'),
       ('abdf934d-5696-41c9-84f5-28c79202d6c5', 'a553b70f-83ad-4dc6-ba45-40a73741837b'),
       ('abdf934d-5696-41c9-84f5-28c79202d6c5', '13eb6487-00dc-4480-837b-9f3812f442cf'),
       ('abdf934d-5696-41c9-84f5-28c79202d6c5', '61d270d2-9b94-4c57-895f-7088d74af6ef'),
       ('24504e65-08cf-4bc3-8f4f-a07204748c13', '6081da18-db02-415c-b258-bb84d93b1794'),
       ('24504e65-08cf-4bc3-8f4f-a07204748c13', 'a553b70f-83ad-4dc6-ba45-40a73741837b'),
       ('24504e65-08cf-4bc3-8f4f-a07204748c13', '13eb6487-00dc-4480-837b-9f3812f442cf'),
       ('cbba5149-82db-4e41-a2bb-f1ca396dc961', '8fe56d6c-2a28-40aa-99ba-2b901144da9e'),
       ('cbba5149-82db-4e41-a2bb-f1ca396dc961', '926be7f8-2a0e-4dbc-9653-bb73bce0b3a0'),
       ('cbba5149-82db-4e41-a2bb-f1ca396dc961', '769af92d-1aa5-4ba1-8573-f39d227bc392'),
       ('78ee675d-4ab5-4c9c-a80a-5c508f1c55af', '82f40ee5-ded4-40b8-a07b-c0d7eae19beb'),
       ('78ee675d-4ab5-4c9c-a80a-5c508f1c55af', '3499c3f1-3ab2-4523-8b16-a872b932ce2e'),
       ('78ee675d-4ab5-4c9c-a80a-5c508f1c55af', 'e7dff452-4dc3-407f-a7cb-ace9f932a775');

