-- 日期统计测试数据
INSERT INTO PEP_PUSH_NOTICE_MSG (id, create_time, create_user_id, last_modify_time, last_modify_user_id, app_key, content,  status, title, target_to, send_count, push_channel, device_type) VALUES ('m_id_1', '2018-07-25 13:07:56', 'PEP_SYS', '2018-07-25 13:07:56', 'PEP_SYS', 'test', 'content1', 'FAIL', 'title1', 'pushtoken_huawei1', '1',  'HUAWEI','ANDROID');
INSERT INTO PEP_PUSH_NOTICE_MSG (id, create_time, create_user_id, last_modify_time, last_modify_user_id, app_key, content,  status, title, target_to, send_count, push_channel, device_type) VALUES ('m_id_2', '2018-07-21 14:07:56', 'PEP_SYS', '2018-07-21 14:07:56', 'PEP_SYS', 'test', 'content1', 'FAIL', 'title1', 'pushtoken_huawei1', '1', 'HUAWEI','ANDROID');

INSERT INTO PEP_PUSH_NOTICE_MSG (id, create_time, create_user_id, last_modify_time, last_modify_user_id, app_key, content,  status, title, target_to, send_count, push_channel, device_type) VALUES ('m_id_3', '2018-07-25 15:07:56','PEP_SYS', '2018-07-25 15:07:56', 'PEP_SYS', 'test', 'content1abcdef', 'FAIL', 'title1', 'pushtoken_huawei1', '1', 'XIAOMI','ANDROID');

INSERT INTO PEP_PUSH_NOTICE_MSG (id, create_time, create_user_id, last_modify_time, last_modify_user_id, app_key, content,  status, title, target_to, send_count, push_channel, device_type) VALUES ('m_id_4', '2018-07-24 15:07:56','PEP_SYS', '2018-07-24 15:07:56', 'PEP_SYS', 'test2', 'content1abcdef',  'FAIL', 'title1', 'pushtoken_huawei1', '1', 'XIAOMI','ANDROID');

INSERT INTO PEP_PUSH_NOTICE_MSG (id, create_time, create_user_id, last_modify_time, last_modify_user_id, app_key, content,  status, title, target_to, send_count, push_channel, device_type) VALUES ('m_id_5', '2018-07-23 19:07:56', 'PEP_SYS', '2018-07-23 19:07:56', 'PEP_SYS', 'test', 'content1', 'FAIL', 'title1', 'pushtoken_huawei1', '1', 'IOS','IOS');
INSERT INTO PEP_PUSH_NOTICE_MSG (id, create_time, create_user_id, last_modify_time, last_modify_user_id, app_key, content,  status, title, target_to, send_count, push_channel, device_type) VALUES ('m_id_6', '2018-07-25 19:07:56', 'PEP_SYS', '2018-07-25 19:07:56', 'PEP_SYS', 'test', 'content1',   'SUCCESS', 'title1', 'pushtoken_huawei1', '1', 'IOS','IOS');
-- 应用配置信息
INSERT INTO PEP_NOTICE_APP (id, create_time, create_user_id, last_modify_time, last_modify_user_id,enable, app_name, app_key,  app_token, app_desc, color) VALUES ('m_id_1', '2018-07-25 13:07:56', 'PEP_SYS', '2018-07-25 13:07:56', 'PEP_SYS','y', 'test', 'test', 'testtoKen', 'testDesc', 'color');
INSERT INTO PEP_NOTICE_APP (id, create_time, create_user_id, last_modify_time, last_modify_user_id,enable, app_name, app_key,  app_token, app_desc, color) VALUES ('m_id_2', '2018-07-25 13:07:56', 'PEP_SYS', '2018-07-25 13:07:56', 'PEP_SYS', 'y','test2', 'test2', 'testtoKen2', 'testDesc2', 'color2');
INSERT INTO PEP_NOTICE_APP (id, create_time, create_user_id, last_modify_time, last_modify_user_id,enable, app_name, app_key,  app_token, app_desc, color) VALUES ('m_id_3', '2018-07-25 13:07:56', 'PEP_SYS', '2018-07-25 13:07:56', 'PEP_SYS', 'y','test3', 'test3', 'testtoKen3', 'testDesc3', 'color3');
