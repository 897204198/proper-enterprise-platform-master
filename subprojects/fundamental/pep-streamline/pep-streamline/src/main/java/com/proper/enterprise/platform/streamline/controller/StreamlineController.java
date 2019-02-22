package com.proper.enterprise.platform.streamline.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.streamline.service.StreamlineService;
import com.proper.enterprise.platform.streamline.sdk.constants.StreamlineConstant;
import com.proper.enterprise.platform.streamline.sdk.request.SignRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/streamline")
@Api(tags = "/streamline")
public class StreamlineController extends BaseController {

    @Autowired
    private StreamlineService streamlineService;

    @PostMapping
    @ApiOperation("‍‍注册服务")
    @ApiImplicitParam(name = "signRequest", value = "‍签名参数", required = true, dataType = "SignRequest")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity addSign(@RequestBody SignRequest signRequest) {
        streamlineService.addSign(signRequest.getBusinessId(), signRequest.getUserName(),
            signRequest.getPassword(), signRequest.getServiceKey());
        return responseOfPost(null);
    }

    @PostMapping("/signs")
    @ApiOperation("‍批量注册服务")
    @ApiImplicitParam(name = "signRequests", value = "‍签名参数集合", required = true, allowMultiple = true, dataType = "SignRequest")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity addSigns(@RequestBody Collection<SignRequest> signRequests) {
        streamlineService.addSigns(signRequests);
        return responseOfPost(null);
    }

    @DeleteMapping(value = "/{businessId}")
    @ApiOperation("‍根据业务Id删除已注册服务")
    @ApiImplicitParam(name = "businessId", value = "‍业务Id", required = true, type = "String")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(@PathVariable String businessId) {
        streamlineService.deleteSigns(businessId);
        return responseOfDelete(true);
    }

    @DeleteMapping
    @ApiOperation("‍根据业务Id批量删除已注册服务")
    @ApiImplicitParam(name = "businessIds", value = "‍业务Id集合(, 分隔)", required = true, type = "String")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteByBusinessIds(@RequestParam String businessIds) {
        streamlineService.deleteSigns(businessIds);
        return responseOfDelete(true);
    }

    @PutMapping
    @ApiOperation("‍根据业务Id以及待更新内容更新已注册服务")
    @ApiImplicitParam(name = "signRequest", value = "‍签名参数", required = true, dataType = "SignRequest")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity put(@RequestBody SignRequest signRequest) {
        streamlineService.updateSign(signRequest.getUserName(), signRequest.getPassword(),
            signRequest.getBusinessId());
        return responseOfPut(null);
    }

    @PutMapping("/signs")
    @ApiOperation("‍根据业务Id以及待更新内容批量更新已注册服务")
    @ApiImplicitParam(name = "signRequests", value = "‍签名参数集合", required = true, allowMultiple = true, dataType = "SignRequest")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity putSigns(@RequestBody Collection<SignRequest> signRequests) {
        streamlineService.updateSigns(signRequests);
        return responseOfPut(null);
    }

    @GetMapping(value = "/{userName}/{password}")
    @ApiOperation("‍根据用户名和密码获取服务标识")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "userName", value = "‍用户名", required = true, type = "String"),
        @ApiImplicitParam(name = "password", value = "‍密码", required = true, type = "String")
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> get(@PathVariable String userName, @PathVariable String password) {
        String serviceKey = streamlineService.getSign(userName, password);
        HttpHeaders headers = new HttpHeaders();
        headers.add(StreamlineConstant.SERVICE_KEY, serviceKey);
        return StringUtil.isEmpty(serviceKey) ? responseOfGet(null) : responseOfGet(serviceKey, headers);
    }

    @GetMapping(value = "/{signature}")
    @ApiOperation("‍根据签名获取服务标识")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "signature", value = "‍签名", required = true, type = "String"),
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> get(@PathVariable String signature) {
        String serviceKey = streamlineService.getSign(signature);
        HttpHeaders headers = new HttpHeaders();
        headers.add(StreamlineConstant.SERVICE_KEY, serviceKey);
        return StringUtil.isEmpty(serviceKey) ? responseOfGet(null) : responseOfGet(serviceKey, headers);
    }


    @PostMapping(value = "/signature")
    @ApiOperation("‍验证签名")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "signature", value = "‍签名", required = true, type = "String"),
    })
    public ResponseEntity<String> validSignature(@RequestBody String signature) {
        streamlineService.validSignature(signature);
        return responseOfPost(null);
    }
}
