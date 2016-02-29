INSERT INTO pep_auth_organizations
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name)
VALUES
('o1', 'test', '2016-02-26 20:16:00', 'test', '2016-02-26 20:16:00', 'o1', 'org1');

INSERT INTO pep_auth_organizations
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent_org_id)
VALUES
('o2', 'test', '2016-02-26 20:16:00', 'test', '2016-02-26 20:16:00', 'o2', 'org2', 'o1');

INSERT INTO pep_auth_organizations
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent_org_id)
VALUES
('o3', 'test', '2016-02-26 20:16:00', 'test', '2016-02-26 20:16:00', 'o3', 'org3', 'o2');

INSERT INTO pep_auth_positions
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, belong_org_id)
VALUES
('1', 'test', '2016-02-26 20:16:00', 'test', '2016-02-26 20:16:00', 'pos1', 'position1', 'o1');

INSERT INTO pep_auth_positions
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent_position_id, belong_org_id)
VALUES
('2', 'test', '2016-02-26 20:16:00', 'test', '2016-02-26 20:16:00', 'pos2', 'position2', '1', 'o1');

INSERT INTO pep_auth_positions
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent_position_id, belong_org_id)
VALUES
('3', 'test', '2016-02-26 20:16:00', 'test', '2016-02-26 20:16:00', 'pos3', 'position3', '2', 'o2');