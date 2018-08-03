package com.proper.enterprise.platform.push.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.push.entity.PushChannelEntity;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class PushChannelVO extends BaseVO {

    @JsonProperty("name")
    private String channelName;

    @JsonProperty("desc")
    private String channelDesc;

    @JsonProperty("msgSaveDays")
    private Integer msgSaveDays;

    @JsonProperty("maxSendCount")
    private Integer maxSendCount;

    @JsonProperty("secretKey")
    private String secretKey;

    @JsonProperty("android")
    private Android android;

    @JsonProperty("ios")
    private IOS ios;

    @JsonProperty("diplomaId")
    private String diplomaId;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelDesc() {
        return channelDesc;
    }

    public void setChannelDesc(String channelDesc) {
        this.channelDesc = channelDesc;
    }

    public int getMsgSaveDays() {
        return msgSaveDays;
    }

    public void setMsgSaveDays(int msgSaveDays) {
        this.msgSaveDays = msgSaveDays;
    }

    public int getMaxSendCount() {
        return maxSendCount;
    }

    public void setMaxSendCount(int maxSendCount) {
        this.maxSendCount = maxSendCount;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Android getAndroid() {
        return android;
    }

    public void setAndroid(Android android) {
        this.android = android;
    }

    public IOS getIos() {
        return ios;
    }

    public void setIos(IOS ios) {
        this.ios = ios;
    }

    public String getDiplomaId() {
        return diplomaId;
    }

    public void setDiplomaId(String diplomaId) {
        this.diplomaId = diplomaId;
    }

    public static class Android implements Serializable {

        public Android() {
        }

        public Android(HuaweiBean huawei, XiaomiBean xiaomi) {
            this.huawei = huawei;
            this.xiaomi = xiaomi;
        }

        /**
         * huawei : {"the_app_id":1111111,"the_app_secret":"aaaaaaa"}
         * xiaomi : {"the_app_secret":"xxxxxxx","the_app_package":"com.xx.xx"}
         */

        private HuaweiBean huawei;
        private XiaomiBean xiaomi;

        public HuaweiBean getHuawei() {
            return huawei;
        }

        public void setHuawei(HuaweiBean huawei) {
            this.huawei = huawei;
        }

        public XiaomiBean getXiaomi() {
            return xiaomi;
        }

        public void setXiaomi(XiaomiBean xiaomi) {
            this.xiaomi = xiaomi;
        }

        public static class HuaweiBean implements Serializable {
            public HuaweiBean(String theAppId, String theAppSecret, String theAppPackage) {
                this.theAppId = theAppId;
                this.theAppSecret = theAppSecret;
                this.theAppPackage = theAppPackage;
            }

            public HuaweiBean() {
            }

            /**
             * the_app_id : 1111111
             * the_app_secret : aaaaaaa
             */


            private String theAppId;
            private String theAppSecret;
            private String theAppPackage;

            public String getTheAppPackage() {
                return theAppPackage;
            }

            public void setTheAppPackage(String theAppPackage) {
                this.theAppPackage = theAppPackage;
            }

            public String getTheAppId() {
                return theAppId;
            }

            public void setTheAppId(String theAppId) {
                this.theAppId = theAppId;
            }

            public String getTheAppSecret() {
                return theAppSecret;
            }

            public void setTheAppSecret(String theAppSecret) {
                this.theAppSecret = theAppSecret;
            }
        }

        public static class XiaomiBean implements Serializable {
            public XiaomiBean(String theAppSecret, String theAppPackage) {
                this.theAppSecret = theAppSecret;
                this.theAppPackage = theAppPackage;
            }

            public XiaomiBean() {
            }

            /**
             * the_app_secret : xxxxxxx
             * the_app_package : com.xx.xx
             */

            private String theAppSecret;
            private String theAppPackage;

            public String getTheAppSecret() {
                return theAppSecret;
            }

            public void setTheAppSecret(String theAppSecret) {
                this.theAppSecret = theAppSecret;
            }

            public String getTheAppPackage() {
                return theAppPackage;
            }

            public void setTheAppPackage(String theAppPackage) {
                this.theAppPackage = theAppPackage;
            }
        }

    }

    public static class IOS implements Serializable {

        public IOS() {
        }

        public IOS(boolean envProduct, String keystorePassword, String topic) {
            this.envProduct = envProduct;
            this.keystorePassword = keystorePassword;
            this.topic = topic;
        }

        /**
         * env_product : true
         * keystore_password : 1234
         * topic : com.proper.icmp
         */

        private boolean envProduct;
        private String keystorePassword;
        private String topic;

        public boolean isEnvProduct() {
            return envProduct;
        }

        public void setEnvProduct(boolean envProduct) {
            this.envProduct = envProduct;
        }

        public String getKeystorePassword() {
            return keystorePassword;
        }

        public void setKeystorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

    }

    public static PushChannelEntity convertVoToEntity(PushChannelVO pushChannelVO) {
        try {
            PushChannelEntity pushChannelEntity = BeanUtil.convert(pushChannelVO, PushChannelEntity.class);
            pushChannelEntity.setAndroid(JSONUtil.toJSON(pushChannelVO.getAndroid()));
            pushChannelEntity.setIos(JSONUtil.toJSON(pushChannelVO.getIos()));
            return pushChannelEntity;
        } catch (Exception e) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.push.formatting.error"));
        }
    }

    public static PushChannelVO convertEntityToVo(PushChannelEntity pushChannelEntity) {
        try {
            PushChannelVO pushChannelVO = BeanUtil.convert(pushChannelEntity, PushChannelVO.class);
            pushChannelVO.setAndroid(JSONUtil.parse(pushChannelEntity.getAndroid(), PushChannelVO.Android.class));
            pushChannelVO.setIos(JSONUtil.parse(pushChannelEntity.getIos(), PushChannelVO.IOS.class));
            return pushChannelVO;
        } catch (Exception e) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.push.formatting.error"));
        }
    }

    public static boolean checkEmpty(PushChannelVO pushChannelVO) {
        boolean isAndroidFieldEmpty = pushChannelVO.getAndroid() != null
            && (pushChannelVO.getAndroid() == null
            || pushChannelVO.getAndroid().getHuawei() == null
            || pushChannelVO.getAndroid().getXiaomi() == null
            || StringUtils.isEmpty(pushChannelVO.getAndroid().getHuawei().getTheAppId())
            || StringUtils.isEmpty(pushChannelVO.getAndroid().getHuawei().getTheAppSecret())
            || StringUtils.isEmpty(pushChannelVO.getAndroid().getHuawei().getTheAppPackage()));
        boolean isIOSEmpty = StringUtils.isNotEmpty(pushChannelVO.getDiplomaId())
            && pushChannelVO.getIos() == null;
        boolean isIOSFieldEmpty = pushChannelVO.getIos() != null
            && (StringUtils.isEmpty(pushChannelVO.getIos().getKeystorePassword())
            || StringUtils.isEmpty(pushChannelVO.getIos().getTopic()));
        if (isAndroidFieldEmpty || isIOSEmpty || isIOSFieldEmpty) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "PushChannelVO{"
            + "channelName='" + channelName + '\''
            + ", channelDesc='" + channelDesc + '\''
            + ", msgSaveDays=" + msgSaveDays
            + ", maxSendCount=" + maxSendCount
            + ", secretKey='" + secretKey + '\''
            + ", android=" + android
            + ", ios=" + ios + ", diplomaId='" + diplomaId + '\''
            + '}';
    }
}
