package com.proper.enterprise.platform.pay.ali.service.impl;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.pay.ali.model.AliPayTradeQueryRes;
import com.proper.enterprise.platform.pay.ali.model.AliRefundRes;
import com.proper.enterprise.platform.pay.ali.model.AliRefundTradeQueryRes;
import com.proper.enterprise.platform.pay.ali.service.AliPayResService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 支付宝统一接口响应ServiceImpl
 */
@Service
public class AliPayResServiceImpl implements AliPayResService {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AliPayResServiceImpl.class);

    /**
     * 支付宝订单查询对象转换
     *
     * @param strRes 返回结果
     * @param responseKey 返回结果键值
     * @param res 对象
     * @throws Exception 参数获取异常
     */
    public Object convertMap2AliPayRes(String strRes, String responseKey, Object res) throws Exception {
        LOGGER.debug("strRes:{}", strRes);
        Map<String, Object> queryMap = JSONUtil.parse(strRes, Map.class);
        Map<String, Object> resMap = (Map<String, Object>) queryMap.get(responseKey);
        if (res instanceof AliPayTradeQueryRes) {
            res = new AliPayTradeQueryRes();
        } else if (res instanceof AliRefundRes) {
            res = new AliRefundRes();
        } else if (res instanceof AliRefundTradeQueryRes) {
            res = new AliRefundTradeQueryRes();
        }
        Field[] fields = res.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("serialVersionUID") || field.getName().equals("$jacocoData")) {
                continue;
            }
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), res.getClass());
            Method setMethod = pd.getWriteMethod();
            // 执行get方法返回一个Object
            setMethod.invoke(res, resMap.get(StringUtil.camelToSnake(field.getName())));
        }
        return res;
    }

    /**
     * 获取远程服务器ATN结果
     *
     * @param urlvalue 指定URL路径地址
     * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
     *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    public String checkUrl(String urlvalue) throws IOException {
        return new String(HttpClient.get(urlvalue).getBody(), PEPConstants.DEFAULT_CHARSET);
    }
}
