package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.notice.entity.TemplateEntity
import com.proper.enterprise.platform.notice.repository.TemplateRepository
import com.proper.enterprise.platform.notice.vo.TemplateVO
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class TemplateServiceTest extends AbstractJPATest {

    @Autowired
    TemplateService templateService

    @Autowired
    TemplateRepository templateRepository


    @Test
    void getTemplate1() {
        TemplateEntity templateEntity = new TemplateEntity()
        templateEntity.setCode("code")
        templateEntity.setName("name")
        templateEntity.setTitle("title-test")
        String template = "订单号:{orderNum};病历号:{clinicNum}"
        templateEntity.setTemplate(template)
        templateEntity.setDescription("orderNum : 订单号, clinicNum : 病历号")
        DataDicLiteBean business = new DataDicLiteBean("NOTICE_BUSINESS", "TEST")
        templateEntity.setCatelog(business)
        DataDicLiteBean type = new DataDicLiteBean("NOTICE_CHANNEL", "TEXT")
        templateEntity.setType(type)
        templateRepository.save(templateEntity)

        Map<String, String> templateParams = new HashMap<>()
        templateParams.put("orderNum", "123456")
        templateParams.put("clinicNum", "654321")

        TemplateVO template1 = templateService.getTemplate(business, 'code', type, templateParams)
        assert '订单号:123456;病历号:654321' == template1.getTemplate()
    }

    @Test
    void getTemplate() {
        getTemplateInit()
        Map<String, String> templateParams = new HashMap<>()
        templateParams.put("name", "wanghp")
        templateParams.put("idcard", "2306241989XXXXXXXX55")
        templateParams.put("package", "blood")
        templateParams.put("date", "20180801")
        templateParams.put("address", "shenyang")
        templateParams.put("startDate", "2018-08-08 13:00")
        templateParams.put("endDate", "2018-08-08 17:30")
        templateParams.put("time", "4")
        templateParams.put("url", "http://docs.easemob.com/im/start")
        templateParams.put("note", "请悉知")
        DataDicLiteBean business = new DataDicLiteBean("NOTICE_BUSINESS", "TEST")
        Map<String, TemplateVO> templates = templateService.getTemplates("code", templateParams)
        assert templates.size() == 2
        String content = templates.get("PUSH").getTemplate()
        assert content.indexOf("{") == -1
        content = templates.get("EMAIL").getTemplate()
        assert content.indexOf("{") == -1
    }

    void getTemplateInit() {
        TemplateEntity templateEntity = new TemplateEntity()
        templateEntity.setCode("code")
        templateEntity.setName("name")
        templateEntity.setTitle("title-push")
        String template = "体检预约成功！体检人:{name};身份证号:{idcard};体检套餐:{package};体检日期:{date};体检地点:{address};备注:请于体检当日携带有效证件，早晨7：50空腹，到体检中心。"
        templateEntity.setTemplate(template)
        templateEntity.setDescription("name : 体检人, idcard : 身份证号, package : 体检套餐, date : 体检日期, address : 体检地点")
        DataDicLiteBean business = new DataDicLiteBean("NOTICE_BUSINESS", "TEST")
        templateEntity.setCatelog(business)
        DataDicLiteBean type = new DataDicLiteBean("NOTICE_CHANNEL", "PUSH")
        templateEntity.setType(type)
        templateRepository.save(templateEntity)

        TemplateEntity templateEntity2 = new TemplateEntity()
        templateEntity2.setCode("code")
        templateEntity2.setName("name")
        templateEntity2.setTitle("title-email")
        template = "【{name}】于【{startDate}】至【{endDate}】请假【{time}】小时，<a href='{url}'>{note}</a>"
        templateEntity2.setTemplate(template)
        templateEntity2.setDescription("name : 姓名, startDate : 起始日期, endDate : 结束日期, time : 持续时间, url : 连接，note : 说明")
        business = new DataDicLiteBean("NOTICE_BUSINESS", "TEST")
        templateEntity2.setCatelog(business)
        type = new DataDicLiteBean("NOTICE_CHANNEL", "EMAIL")
        templateEntity2.setType(type)
        templateRepository.save(templateEntity2)
    }

}
