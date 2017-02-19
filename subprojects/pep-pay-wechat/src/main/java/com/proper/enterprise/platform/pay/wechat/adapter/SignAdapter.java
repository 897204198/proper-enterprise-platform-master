package com.proper.enterprise.platform.pay.wechat.adapter;

import com.proper.enterprise.platform.core.utils.ConfCenter;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.digest.MD5;
import com.proper.enterprise.platform.pay.wechat.constants.WechatConstants;
import com.proper.enterprise.platform.pay.wechat.model.WechatOrderReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.TreeSet;

public class SignAdapter extends XmlAdapter<String, WechatOrderReq> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignAdapter.class);

    @Override
    public WechatOrderReq unmarshal(String v) throws Exception {
        return null;
    }

    @Override
    public String marshal(WechatOrderReq v) throws Exception {
        return marshalObject(v, WechatOrderReq.class);
    }

    public <T> String marshalObject(T t, Class<T> clz) throws Exception {
        Field[] fields = clz.getDeclaredFields();
        Set<String> set = new TreeSet<>();
        for (Field field : fields) {
            if (!field.getName().equals("sign") && !field.getName().startsWith("$")) {
                set.add(field.getName());
            }
        }

        StringBuilder sb = new StringBuilder();
        Object value;
        for (String fieldName : set) {
            value = clz.getMethod("get" + StringUtil.capitalize(fieldName)).invoke(t);
            if (value != null) {
                if(fieldName.equals("papackage")) {
                    sb.append("package").append("=").append(value).append("&");
                } else {
                    sb.append(StringUtil.camelToSnake(fieldName)).append("=").append(value).append("&");
                }
            }
        }
        sb.append("key=" + WechatConstants.WECHAT_PAY_API_KEY);

        String sign = sb.toString();
        LOGGER.debug("Sign before MD5: {}",
                StringUtil.abbreviate(sign,
                    Integer.parseInt(ConfCenter.get("pay.wechat.abbreviate.maxWidth"))));

        return MD5.md5Hex(sign).toUpperCase();
    }

}
