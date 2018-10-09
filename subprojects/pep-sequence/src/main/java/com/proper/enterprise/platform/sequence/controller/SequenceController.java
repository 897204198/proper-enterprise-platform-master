package com.proper.enterprise.platform.sequence.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
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
    public ResponseEntity<?> get(String sequenceCode) {
        return isPageSearch() ? responseOfGet(sequenceService.findAll(sequenceCode, getPageRequest()))
            : responseOfGet(sequenceService.findAll(sequenceCode));
    }

    public static class SequenceModelVO {

        @ApiModelProperty("‍流水号编码")
        private String sequenceCode;

        @ApiModelProperty("‍流水名称")
        private String sequenceName;

        @ApiModelProperty("‍规则")
        private String formula;

        @ApiModelProperty("‍清零方式(不清零, 日清零, 月清零, 年清零)")
        private DataDicLiteBean clearType;

        @ApiModelProperty("‍初始值")
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
