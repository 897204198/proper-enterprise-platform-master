INSERT INTO pep_auth_person
(ID, CREATE_TIME, CREATE_USER_ID, LAST_MODIFY_TIME, LAST_MODIFY_USER_ID, GENDER, ID_CARD, NAME)
VALUES
('1', '2016-02-26 20:16:00', 'test', '2016-02-26 20:16:00', 'test', 'MALE', 'x2348092', 'person');

INSERT INTO pep_auth_users
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, username, password, person_id)
VALUES
('2', 'test', '2016-02-26 20:16:00', 'test', '2016-02-26 20:16:00', 'user1', 'e10adc3949ba59abbe56e057f20f883e', '1');

INSERT INTO pep_auth_users
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, username, password, person_id)
VALUES
('3', 'test', '2016-02-26 20:16:00', 'test', '2016-02-26 20:16:00', 'user2', 'e10adc3949ba59abbe56e057f20f883e', '1');