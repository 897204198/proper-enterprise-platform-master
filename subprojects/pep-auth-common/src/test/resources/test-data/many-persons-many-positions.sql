INSERT INTO pep_auth_persons
(ID, CREATE_TIME, CREATE_USER_ID, LAST_MODIFY_TIME, LAST_MODIFY_USER_ID, GENDER, ID_CARD, NAME)
VALUES
('1', '2016-02-26 20:16:00', 'test', '2016-02-26 20:16:00', 'test', 'MALE', 'x2348092', 'person1');

INSERT INTO pep_auth_persons
(ID, CREATE_TIME, CREATE_USER_ID, LAST_MODIFY_TIME, LAST_MODIFY_USER_ID, GENDER, ID_CARD, NAME)
VALUES
('2', '2016-02-26 20:16:00', 'test', '2016-02-26 20:16:00', 'test', 'FEMALE', 'x2348093', 'person2');

INSERT INTO pep_auth_persons
(ID, CREATE_TIME, CREATE_USER_ID, LAST_MODIFY_TIME, LAST_MODIFY_USER_ID, GENDER, ID_CARD, NAME)
VALUES
('3', '2016-02-26 20:16:00', 'test', '2016-02-26 20:16:00', 'test', 'MALE', 'x2348094', 'person3');

INSERT INTO pep_auth_positions
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name)
VALUES
('1', 'test', '2016-02-26 20:16:00', 'test', '2016-02-26 20:16:00', 'pos1', 'position1');

INSERT INTO pep_auth_positions
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent_position_id)
VALUES
('2', 'test', '2016-02-26 20:16:00', 'test', '2016-02-26 20:16:00', 'pos2', 'position2', '1');

INSERT INTO pep_auth_positions
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent_position_id)
VALUES
('3', 'test', '2016-02-26 20:16:00', 'test', '2016-02-26 20:16:00', 'pos3', 'position3', '2');

INSERT INTO pep_auth_persons_positions
(person_id, position_id)
VALUES
(1, 1);

INSERT INTO pep_auth_persons_positions
(person_id, position_id)
VALUES
(1, 2);

INSERT INTO pep_auth_persons_positions
(person_id, position_id)
VALUES
(1, 3);

INSERT INTO pep_auth_persons_positions
(person_id, position_id)
VALUES
(2, 2);

INSERT INTO pep_auth_persons_positions
(person_id, position_id)
VALUES
(2, 3);

INSERT INTO pep_auth_persons_positions
(person_id, position_id)
VALUES
(3, 3);