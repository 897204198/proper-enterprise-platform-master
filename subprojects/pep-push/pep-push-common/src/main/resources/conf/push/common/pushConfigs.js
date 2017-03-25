/**
 * 移动应用配置信息
 */
var appconfig = {
	"test" : {
		"device" : {
			"android" : {
				"huawei" : {
					"the_app_id" : "10819197",
					"the_app_secret" : "fbfe31923440e417f8fb9f4ce133e3c1"
				},
				"xiaomi" : {
					"the_app_secret" : "2AF1VndMLqwLF/4zOHgWNw==",
					"the_app_package" : "com.proper.mobile.oa.shengjing.htest"
				}
			},
			"ios" : {
				"apns" : {
					"env_product" : false,
					"keystore_password" : "h123456",
					"keystore_filename" : "test.p12",
					"topic" : "com.proper.mobile.oa.shengjing.test"
				}
			}
		},
		"desc" : "推送平台test",
		"msg_save_days" : 3,
		"max_send_count" : 5,
		"secretKey" : "b2024e00064bc5d8db70fdee087eae4f"
	}
};