INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order, is_default) VALUES ('pep_notice_channel_push', 'NOTICE_CHANNEL', 'PUSH', '推送', 0, 'Y');
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('pep_notice_channel_email', 'NOTICE_CHANNEL', 'EMAIL', '邮件', 1);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('pep_notice_channel_sms', 'NOTICE_CHANNEL', 'SMS', '短信', 2);
INSERT INTO pep_push_device (id, create_time, create_user_id, last_modify_time, last_modify_user_id, appkey, deviceid, devicetype, mstatus, other_info, push_mode, push_token, userid) VALUES ('id_d_1', '2017-05-09 13:53:57', 'PEP_SYS', '2017-05-10 12:24:48', 'PEP_SYS', 'test', 'testdevice1', 'android', '0', 'desc  device', 'huawei', 'pushtoken_huawei1', 'testuser1');
INSERT INTO pep_push_device (id, create_time, create_user_id, last_modify_time, last_modify_user_id, appkey, deviceid, devicetype, mstatus, other_info, push_mode, push_token, userid) VALUES ('id_d_2', '2017-05-09 13:53:57', 'PEP_SYS', '2017-05-10 12:24:48', 'PEP_SYS', 'test', 'testdevice2', 'android', '0', 'desc  device', 'xiaomi', 'pushtoken_xiaomi2', 'testuser1');
INSERT INTO pep_push_device (id, create_time, create_user_id, last_modify_time, last_modify_user_id, appkey, deviceid, devicetype, mstatus, other_info, push_mode, push_token, userid) VALUES ('id_d_3', '2017-05-09 13:53:57', 'PEP_SYS', '2017-05-10 12:24:48', 'PEP_SYS', 'test', 'testdevice3', 'ios', '0', 'desc  device', 'apns', 'pushtoken_apns3', 'testuser1');