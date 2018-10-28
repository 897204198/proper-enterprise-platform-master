package com.proper.enterprise.platform.sequence.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.sequence.service.SequenceService;
import com.proper.enterprise.platform.sequence.vo.SequenceVO;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Api(tags = "/sequence")
@RequestMapping("/sequence")
public class SequenceController extends BaseController {

    @Autowired
    private SequenceService sequenceService;

    @PostMapping
    @ApiOperation("‍增加流水号配置")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SequenceVO> post(@RequestBody SequenceModelVO sequenceModelVO) {
        SequenceVO sequenceVO = new SequenceVO();
        BeanUtils.copyProperties(sequenceModelVO, sequenceVO);
        return responseOfPost(sequenceService.save(sequenceVO));
    }

    @DeleteMapping
    @ApiOperation("‍删除流水号配置")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(@ApiParam(value = "‍流水号配置Id(逗号分隔)", required = true) @RequestParam String ids) {
        return responseOfDelete(sequenceService.deleteByIds(ids));
    }

    @PutMapping("/{id}")
    @ApiOperation("‍更新流水号配置")
    public ResponseEntity<SequenceVO> put(@ApiParam(value = "‍ID", required = true) @PathVariable String id,
                                          @RequestBody SequenceModelVO sequenceModelVO) {
        SequenceVO sequenceVO = new SequenceVO();
        BeanUtils.copyProperties(sequenceModelVO, sequenceVO);
        return responseOfPut(sequenceService.update(id, sequenceVO));
    }

    @GetMapping
    @ApiOperation("‍流水号配置列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk> get(String sequenceCode) {
        if (isPageSearch()) {
            return responseOfGet(sequenceService.findAll(sequenceCode, getPageRequest()));
        } else {
            Collection<SequenceVO> collection = sequenceService.findAll(sequenceCode);
            DataTrunk<SequenceVO> dataTrunk = new DataTrunk<>();
            dataTrunk.setCount(collection.size());
            dataTrunk.setData(collection);
            return responseOfGet(dataTrunk);
        }
    }

    public static class SequenceModelVO {

        @ApiModelProperty(name = "‍流水号编码", required = true)
        private String sequenceCode;

        @ApiModelProperty(name = "‍流水名称", required = true)
        private String sequenceName;

        @ApiModelProperty(name = "‍规则", required = true)
        private String formula;

        @ApiModelProperty(name = "‍清零方式(不清零, 日清零, 月清零, 年清零)", required = true)
        private DataDicLiteBean clearType;

        @ApiModelProperty(name = "‍初始值")
        private String initialValue;

        public String getSequenceCode() {
            return sequenceCode;
        }

        public void setSequenceCode(String sequenceCode) {
            this.sequenceCode = sequenceCode;
        }

        public String getSequenceName() {
            return sequenceName;
        }

        public void setSequenceName(String sequenceName) {
            this.sequenceName = sequenceName;
        }

        public String getFormula() {
            return formula;
        }

        public void setFormula(String formula) {
            this.formula = formula;
        }

        public DataDicLiteBean getClearType() {
            return clearType;
        }

        public void setClearType(DataDicLiteBean clearType) {
            this.clearType = clearType;
        }

        public String getInitialValue() {
            return initialValue;
        }

        public void setInitialValue(String initialValue) {
            this.initialValue = initialValue;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }

    }
}
