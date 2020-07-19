insert into external_doi_service_credentials(id, username, doi_prefix) values (0,'DEV.TUVIENNA', '10.70124')
insert into app_user (id, login, password, role, external_doi_service_credentials_id) values (nextval('hibernate_sequence'),'admin', '$2y$12$tQLlGLPG/Q5OI5C9n6VS2eqZwi925epyB4.z9pvunr8KJVh7hP7Vi', 'ROLE_ADMIN', 0)
