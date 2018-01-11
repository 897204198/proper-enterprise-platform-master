package com.proper.enterprise.platform.pay.cmb.utils;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.pay.cmb.constants.CmbConstants;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 一网通工具类
 */
public class CmbUtils {

    /**
     * 获取请求发起时间
     *
     * @return 生成的请求发起时间
     * @throws Exception 获取异常
     */
    public static String getCmbReqTime() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long dateToMs = Long.valueOf(sdf.parse("2000-01-01 00:00:00").getTime());
        Date dateTime = new Date();
        long currentMs = Long.valueOf(dateTime.getTime());
        return String.valueOf(currentMs - dateToMs);
    }

    /**
     * 生成带有时间戳的20位纯数字编号
     *
     * @return 带有时间戳的20位纯数字编号
     */
    public static String getTimeNo() {
        Calendar calS = Calendar.getInstance();
        calS.setTime(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(DateUtil.toString(calS.getTime(), CmbConstants.CMB_PAY_DATE_FORMAT_YYYYMMDDHHMMSSSSS));
        sb.append(RandomStringUtils.randomNumeric(3));
        return sb.toString();
    }

    /**
     * 获取SHA1加密后的字符串
     *
     * @param strSrc 需要加密的摘要
     * @param encName 算法
     * @return SHA1 加密的密文
     * @throws Exception 加密异常
     */
    public static String encrypt(String strSrc, String encName) throws Exception {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes(PEPConstants.DEFAULT_CHARSET);
        try {
            if (encName == null || encName.equals("")) {
                encName = "MD5";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            // to HexString
            strDes = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * 字节数组转换
     *
     * @param bts 字节数组
     * @return 转换后的字符串
     */
    public static String bytes2Hex(byte[] bts) {
        StringBuilder des = new StringBuilder();
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = Integer.toHexString(bts[i] & 0xFF);
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    /**
     * 获取待签名的字符串
     *
     * @param originSign 字符串
     * @return 待签名的字符串
     */
    public static String getOriginSign(String originSign) {
        StringBuilder sb = new StringBuilder();
        String key = CmbConstants.CMB_PAY_CMBKEY;
        String origin =  originSign.replace(CmbConstants.CMB_PAY_XML_HEADER, "")
            .replace("<Request>", "").replace("</Request>", "")
            .replace("<Head>", "").replace("</Head>", "")
            .replace("<Body>", "").replace("</Body>", "")
            .replace("<Hash>", "").replace("</Hash>", "");
        return sb.append(key).append(origin).toString();
    }

    /**
     * 读取流
     *
     * @param inStream 输入流
     * @return 字节数组
     * @throws Exception 读取异常
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    /**
     * 解析请求时传入的参数
     *
     * @param param 请求参数
     * @return 解析后的请求对象
     */
    public static Map<String, String> getParamObj(String param) throws Exception {
        Map<String, String> paramObj = new HashMap<>();
        String[] reqParams = param.split("\\|");
        for (String reqParam : reqParams) {
            String[] deatilParam = reqParam.split("=");
            String value1 = StringUtil.isEmpty(deatilParam[0]) ? "" : deatilParam[0];
            String value2 = StringUtil.isEmpty(deatilParam[1]) ? "" : deatilParam[1];
            paramObj.put(value1, value2);
        }
        return paramObj;
    }
}
