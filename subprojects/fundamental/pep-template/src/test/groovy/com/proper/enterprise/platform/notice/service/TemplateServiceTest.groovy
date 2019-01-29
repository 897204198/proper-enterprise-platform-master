package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.template.entity.TemplateEntity
import com.proper.enterprise.platform.template.repository.TemplateRepository
import com.proper.enterprise.platform.template.service.TemplateService
import com.proper.enterprise.platform.template.vo.TemplateDetailVO
import com.proper.enterprise.platform.template.vo.TemplateVO
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class TemplateServiceTest extends AbstractJPATest {

    @Autowired
    TemplateService templateService

    @Autowired
    TemplateRepository templateRepository

    @Test
    void getTemplate() {
        TemplateEntity templateDocument = new TemplateEntity()
        templateDocument.code = "code"
        templateDocument.name = "name"
        templateDocument.description = "orderNum : 订单号, clinicNum : 病历号"
        templateDocument.setMuti(false)
        templateDocument.catalog = "TEST"
        List<TemplateDetailVO> details = new ArrayList<>()
        TemplateDetailVO detail = new TemplateDetailVO()
        detail.title = "title-test"
        detail.template = "订单号:{orderNum};病历号:{clinicNum}"
        detail.type = "PUSH"
        details.add(detail)
        templateDocument.details = details
        templateRepository.save(templateDocument)

        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("orderNum", "123456")
        templateParams.put("clinicNum", "654321")
        TemplateVO templates = templateService.getTemplate('code', templateParams)
        assert '订单号:123456;病历号:654321' == templates.getDetail().getTemplate()

        TemplateVO existTemplates = templateService.get(templateDocument.id)
        assert '订单号:{orderNum};病历号:{clinicNum}' == existTemplates.getDetail().getTemplate()
    }

    @Test
    void getTemplates() {
        TemplateEntity templateDocument = new TemplateEntity()
        templateDocument.code = "code2"
        templateDocument.name = "name2"
        templateDocument.description = "name : 体检人, idcard : 身份证号, package : 体检套餐, date : 体检日期, address : 体检地点"
        templateDocument.setMuti(true)
        templateDocument.catalog = "TEST"
        List<TemplateDetailVO> details = new ArrayList<>()
        TemplateDetailVO detail = new TemplateDetailVO()
        detail.title = "title-push"
        detail.template = "体检预约成功！体检人:{name};身份证号:{idcard};体检套餐:{package};体检日期:{date};体检地点:{address};备注:请于体检当日携带有效证件，早晨7：50空腹，到体检中心。"
        detail.type = "PUSH"
        TemplateDetailVO detail2 = new TemplateDetailVO()
        detail2.title = "title-email"
        detail2.template = "【{name}】于【{startDate}】至【{endDate}】请假【{time}】小时，<a href='{url}'>{note}</a>"
        detail2.type = "EMAIL"
        details.add(detail)
        details.add(detail2)
        templateDocument.details = details
        templateRepository.save(templateDocument)

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
        TemplateVO templates = templateService.getTemplate("code2", templateParams)
        List<TemplateDetailVO> list = templates.getDetails()
        assert list.size() == 2
        for (TemplateDetailVO templateDetailVO : list) {
            assert templateDetailVO != null
            assert templateDetailVO.getTemplate().indexOf("{") == -1
        }

        TemplateVO existTemplates = templateService.get(templateDocument.id)
        List<TemplateDetailVO> existList = existTemplates.getDetails()
        assert existList.size() == 2
        for (TemplateDetailVO templateDetailVO : existList) {
            assert templateDetailVO != null
            assert templateDetailVO.getTemplate().indexOf("{") != -1
        }
    }

}
