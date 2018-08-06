package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.notice.entity.TemplateEntity
import com.proper.enterprise.platform.notice.repository.TemplateRepository
import com.proper.enterprise.platform.notice.vo.TemplateVO
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class TemplateServiceTest extends AbstractTest {

    @Autowired
    TemplateService templateService

    @Autowired
    TemplateRepository templateRepository

    @Test
    void getTemplate() {
        getTemplateInit()
        Map<String, String> templateParams = new HashMap<>()
        templateParams.put("name", "wanghp")
        templateParams.put("idcard", "2306241989XXXXXXXX55")
        templateParams.put("package", "blood")
        templateParams.put("date", "20180801")
        templateParams.put("address", "shenyang")
        DataDicLiteBean business = new DataDicLiteBean("NOTICE_BUSINESS", "TEST")
        Map<String, TemplateVO> templates = templateService.getTemplates(business, "code", templateParams)
        assert templates.size() == 1
        String content = templates.get("PUSH").getTemplate()
        assert content.indexOf("{") == -1
    }

    void getTemplateInit() {
        TemplateEntity templateEntity = new TemplateEntity()
        templateEntity.setCode("code")
        templateEntity.setName("name")
        templateEntity.setTitle("title1")
        String template = "体检预约成功！体检人:{name};身份证号:{idcard};体检套餐:{package};体检日期:{date};体检地点:{address};备注:请于体检当日携带有效证件，早晨7：50空腹，到体检中心。"
        templateEntity.setTemplate(template)
        templateEntity.setDescription("name : 体检人, idcard : 身份证号, package : 体检套餐, date : 体检日期, address : 体检地点")
        DataDicLiteBean business = new DataDicLiteBean("NOTICE_BUSINESS", "TEST")
        templateEntity.setCatelog(business)
        DataDicLiteBean type = new DataDicLiteBean("NOTICE_CHANNEL", "PUSH")
        templateEntity.setType(type)
        templateRepository.save(templateEntity)
    }

}
