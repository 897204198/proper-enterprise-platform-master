/**
 * 移动应用配置信息
 */
var appconfig = {
		//推送应用的唯一标识appid
		"test" : {
		"device" : {
			"android" : {
				//华为推送配置
				"huawei" : {
					"the_app_id" : "10819197",
					"the_app_secret" : "fbfe31923440e417f8fb9f4ce133e3c1"
				},
				//小米推送配置
				"xiaomi" : {
					"the_app_secret" : "2AF1VndMLqwLF/4zOHgWNw==",
					"the_app_package" : "com.proper.mobile.oa.shengjing.htest"
				}
			},
			"ios" : {
				//ios apns 推送配置
				"apns" : {
					"env_product" : false,
					"keystore_password" : "h123456",
					"keystore_filename" : "test.p12",
					"topic" : "com.proper.mobile.oa.shengjing.test"
				}
			}
		},
		//描述信息
		"desc" : "推送平台test",
		//历史消息保存天数
		"msg_save_days" : 3,
		//消息重发次数
		"max_send_count" : 5,
		//安全key,暂时未用
		"secretKey" : "b2024e00064bc5d8db70fdee087eae4f"
	}
};