package com.proper.enterprise.platform.file.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.BigDecimalUtil;
import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.file.vo.FileVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/file")
@Api(tags = "/file")
public class FileController extends BaseController {

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    @ApiOperation(value = "‍上传文件", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> uploadFile(@ApiParam(value = "‍文件", required = true) MultipartFile file,
                                             @ApiParam("‍文件路径（显示用，不代表文件实际存储在磁盘上的路径）") String path) throws IOException {
        return responseOfPost(fileService.save(file, path).getId());
    }

    @PostMapping(path = "/{id}")
    @ApiOperation(value = "‍更新文件", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> updateFile(@ApiParam(value = "‍文件id", required = true) @PathVariable String id,
                                             @ApiParam(value = "‍文件", required = true) MultipartFile file) throws IOException {
        return responseOfPost(fileService.update(id, file).getId());
    }


    @GetMapping(path = "/{id}")
    @ApiOperation(value = "‍根据id获取文件", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void download(@ApiParam(value = "‍文件id", required = true) @PathVariable String id, HttpServletRequest request, HttpServletResponse
            response) throws IOException {
        fileService.download(id, request, response);
    }

    @GetMapping(path = "/{id}/meta")
    @ApiOperation("‍根据id获取元文件")
    public ResponseEntity<FileVO> metainfo(@ApiParam(value = "‍文件id", required = true) @PathVariable String id) {
        FileVO fileVO = BeanUtil.convert(fileService.findById(id), FileVO.class);
        if (fileVO != null) {
            fileVO.setFileSizeUnit(BigDecimalUtil.parseSize(fileVO.getFileSize()));
        }
        return responseOfGet(fileVO);
    }

    @DeleteMapping
    @ApiOperation("‍删除多个文件")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(@ApiParam(value = "‍文件信息Id列表(使用\",\"分割)", required = true) @RequestParam String ids) throws IOException {
        return responseOfDelete(fileService.deleteByIds(ids));
    }

    @GetMapping
    @ApiOperation("‍获取所有文件")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk<FileVO>> query() {
        return responseOfGet(fileService.findPage(), FileVO.class);
    }

    @PostMapping(path = "/dir")
    public ResponseEntity<String> uploadFileDir(@RequestBody FileVO fileVO) {
        return responseOfPost(fileService.saveDir(fileVO).getId());
    }

    @PutMapping(path = "/dir/{id}")
    public ResponseEntity<String> updateDir(@PathVariable String id, @RequestBody FileVO fileVO) {
        fileVO.setId(id);
        return responseOfPut(fileService.updateDir(fileVO).getId());
    }

    @DeleteMapping(path = "/dir")
    public ResponseEntity deleteFileDir(@RequestParam String ids) throws IOException {
        return responseOfDelete(fileService.deleteFileDirByIds(ids));
    }

    @GetMapping(path = "/dir")
    public ResponseEntity<Collection<FileVO>> getFileDir(String path, String fileName) {
        return responseOfGet(fileService.findFileDir(path, fileName));
    }

}
