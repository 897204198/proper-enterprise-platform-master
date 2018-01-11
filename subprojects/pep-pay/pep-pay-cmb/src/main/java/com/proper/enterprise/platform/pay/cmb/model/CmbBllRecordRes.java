package com.proper.enterprise.platform.pay.cmb.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 已结账单记录
 */
@XmlRootElement(name = "BllRecord")
@XmlAccessorType(XmlAccessType.FIELD)
public class CmbBllRecordRes {

    /**
     * 商户定单号
     */
    @XmlElement(name = "BillNo")
    private String billNo;

    /**
     * 交易流水号
     */
    @XmlElement(name = "BillRfn")
    private String billRfn;

    /**
     * 商户日期，格式为YYYYMMDD
     */
    @XmlElement(name = "MchDate")
    private String mchDate;

    /**
     *  结算日期，格式为YYYYMMDD
     */
    @XmlElement(name = "StlDate")
    private String stlDate;

    /**
     *  卡类型
     */
    @XmlElement(name = "CardType")
    private String cardType;

    /**
     *  订单状态
     */
    @XmlElement(name = "BillState")
    private String billState;

    /**
     *  订单金额，格式为XX.XX
     */
    @XmlElement(name = "BillAmount")
    private String billAmount;

    /**
     *  手续费，格式为XX.XX
     */
    @XmlElement(name = "FeeAmount")
    private String feeAmount;

    /**
     *  实扣金额，格式为XX.XX
     */
    @XmlElement(name = "StlAmount")
    private String stlAmount;

    /**
     *  优惠金额，格式为XX.XX
     */
    @XmlElement(name = "DecPayAmount")
    private String decPayAmount;

    /**
     *  商户自定义参数。里面的特殊字符已经转码
     */
    @XmlElement(name = "MerchantPara")
    private String merchantPara;

    /**
     * 订单类型：A二维码支付订单，空字符则为普通订单（含一网通支付订单）
     *（如果订单类型为A，下述字段才存在）
     */
    @XmlElement(name = "BillType")
    private String billType;

    /**
     *  收货人姓名
     */
    @XmlElement(name = "Addressee")
    private String addressee;

    /**
     *  国家
     */
    @XmlElement(name = "Country")
    private String country;

    /**
     *  省份
     */
    @XmlElement(name = "Province")
    private String province;

    /**
     *  城市
     */
    @XmlElement(name = "City")
    private String city;

    /**
     *  街道地址
     */
    @XmlElement(name = "Address")
    private String address;

    /**
     *  手机号
     */
    @XmlElement(name = "Mobile")
    private String mobile;

    /**
     *  固定电话
     */
    @XmlElement(name = "Telephone")
    private String telephone;

    /**
     *  邮编
     */
    @XmlElement(name = "ZipCode")
    private String zipCode;

    /**
     *  商品详情链接
     */
    @XmlElement(name = "GoodsURL")
    private String goodsURL;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillRfn() {
        return billRfn;
    }

    public void setBillRfn(String billRfn) {
        this.billRfn = billRfn;
    }

    public String getMchDate() {
        return mchDate;
    }

    public void setMchDate(String mchDate) {
        this.mchDate = mchDate;
    }

    public String getStlDate() {
        return stlDate;
    }

    public void setStlDate(String stlDate) {
        this.stlDate = stlDate;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBillState() {
        return billState;
    }

    public void setBillState(String billState) {
        this.billState = billState;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getStlAmount() {
        return stlAmount;
    }

    public void setStlAmount(String stlAmount) {
        this.stlAmount = stlAmount;
    }

    public String getDecPayAmount() {
        return decPayAmount;
    }

    public void setDecPayAmount(String decPayAmount) {
        this.decPayAmount = decPayAmount;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getGoodsURL() {
        return goodsURL;
    }

    public void setGoodsURL(String goodsURL) {
        this.goodsURL = goodsURL;
    }

    public String getMerchantPara() {
        return merchantPara;
    }

    public void setMerchantPara(String merchantPara) {
        this.merchantPara = merchantPara;
    }
}
