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
  },
  "MobileOA" : {
    "device" : {
      "android" : {
        "huawei" : {
          "the_app_id" : "100029163",
          "the_app_secret" : "a31f53301ed9f45e94530235dd933d25"
        },
        "xiaomi" : {
          "the_app_secret" : "I8Xlf7O2cBTHGxLJnICSjw==",
          "the_app_package" : "com.proper.icmp"
        }
      },
      "ios" : {
        "apns" : {
          "env_product" : false,
          "keystore_password" : "123456",
          "keystore_filename" : "icmp.p12",
          "topic" : "com.proper.icmp"
        }
      }
    },
    "desc" : "掌上办公测试",
    "msg_save_days" : 3,
    "max_send_count" : 5,
    "secretKey" : "b2024e00064bc5d8db70fdee087eae4f"
  },"MobileOADev" : {
        "device" : {
            "android" : {
                "huawei" : {
                    "the_app_id" : "100213965",
                    "the_app_secret" : "cb5b99c684477aaa3b6a28b2c7cbe7b2"
                },
                "xiaomi" : {
                    "the_app_secret" : "RGW+NA+T2ucpEX0a6bxyhA==",
                    "the_app_package" : "com.proper.icmp.dev"
                }
            },
            "ios" : {
                "apns" : {
                    "env_product" : true,
                    "keystore_password" : "1234",
                    "keystore_filename" : "icmp_dev_pro.p12",
                    "topic" : "com.proper.icmp.dev"
                }
            }
        },
        "desc" : "掌上办公测试",
        "msg_save_days" : 3,
        "max_send_count" : 5,
        "secretKey" : "b2024e00064bc5d8db70fdee087eae4f"
    },"MobileOADemo" : {
        "device" : {
            "android" : {
                "huawei" : {
                    "the_app_id" : "100214089",
                    "the_app_secret" : "93efe2d8b719b05b2cc0152e6fb7ca8e"
                },
                "xiaomi" : {
                    "the_app_secret" : "I3d5bFQBQV5jo/awWyqnew==",
                    "the_app_package" : "com.proper.icmp.demo"
                }
            },
            "ios" : {
                "apns" : {
                    "env_product" : true,
                    "keystore_password" : "1234",
                    "keystore_filename" : "icmp_demo_pro.p12",
                    "topic" : "com.proper.icmp.demo"
                }
            }
        },
        "desc" : "掌上办公测试",
        "msg_save_days" : 3,
        "max_send_count" : 5,
        "secretKey" : "b2024e00064bc5d8db70fdee087eae4f"
    },"MobileOAPr" : {
        "device" : {
            "android" : {
                "huawei" : {
                    "the_app_id" : "100244217",
                    "the_app_secret" : "794c3813d787d0a0f717908a130835ce"
                },
                "xiaomi" : {
                    "the_app_secret" : "YEyyT4YeTaVjMipXfmsNeg==",
                    "the_app_package" : "com.proper.icmp.pr"
                }
            },
            "ios" : {
                "apns" : {
                    "env_product" : true,
                    "keystore_password" : "1234",
                    "keystore_filename" : "icmp_pr_pro.p12",
                    "topic" : "com.proper.icmp.pr"
                }
            }
        },
        "desc" : "掌上办公测试",
        "msg_save_days" : 3,
        "max_send_count" : 5,
        "secretKey" : "b2024e00064bc5d8db70fdee087eae4f"
    }
};
