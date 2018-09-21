package com.proper.enterprise.platform.push.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import com.proper.enterprise.platform.push.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.service.PushMsgService;
import com.proper.enterprise.platform.push.service.PushMsgStatisticService;
import com.proper.enterprise.platform.push.vo.PushMsgStatisticVO;
import com.proper.enterprise.platform.push.vo.PushMsgVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import java.util.Map;

/**
 * 推送消息统计
 *
 * @author guozhimin
 */
@RestController
@AuthcIgnore
@RequestMapping("/push")
public class PushStatisticController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushStatisticController.class);
    @Autowired
    PushMsgStatisticService pushMsgStatisticService;
    @Autowired
    PushMsgService pushMsgService;

    @GetMapping
    @JsonView(PushMsgStatisticVO.Single.class)
    @RequestMapping("/statistic")
    public ResponseEntity<?> get(String dateType, String appkey) {
        return new ResponseEntity<>(pushMsgStatisticService.findByDateTypeAndAppkey(dateType, appkey), null, HttpStatus.OK);
    }

    @GetMapping
    @JsonView(PushMsgStatisticVO.Single.class)
    @RequestMapping("/statistic/init")
    public ResponseEntity<?> get() {
        Date date = new Date();
        return new ResponseEntity<>(pushMsgStatisticService.saveStatisticOfSomeday(
            DateUtil.toString(date, PEPConstants.DEFAULT_DATE_FORMAT)), null, HttpStatus.OK);
    }

    @GetMapping
    @JsonView(PushMsgVO.Single.class)
    @RequestMapping("/list")
    public ResponseEntity<?> getPushMsgList(final String mcontent, final String mstatus,
                                            final String appkey, final String pushMode, final int pageNo, final int pageSize) {
        PushMsgEntity entity = new PushMsgEntity();
        entity.setMcontent(mcontent);
        if (mstatus != null) {
            entity.setMstatus(PushMsgStatus.valueOf(mstatus));
        }
        entity.setAppkey(appkey);
        if (pushMode != null) {
            entity.setPushMode(PushMode.valueOf(pushMode));
        }
        Example example = Example.of(entity);

        Sort sort = new Sort(Sort.Direction.DESC, "lastModifyTime");
        PageRequest pageRequest = getPageRequest(sort);
        return responseOfGet(pushMsgService.findByDateTypeAndAppkey(example, pageRequest), PushMsgVO.class, PushMsgVO.Single.class);
    }

    @GetMapping("/statistic/pie")
    public ResponseEntity<Map<String, Object>> getPieData(String startDate, String endDate, String appKey) {
        return new ResponseEntity<>(pushMsgStatisticService.findAllWithPie(startDate, endDate, appKey), null, HttpStatus.OK);
    }
}
