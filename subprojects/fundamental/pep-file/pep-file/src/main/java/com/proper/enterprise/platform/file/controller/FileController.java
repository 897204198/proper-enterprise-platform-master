package com.proper.enterprise.platform.file.controller;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
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
import java.net.URLDecoder;
import java.util.Collection;

@RestController
@RequestMapping("/file")
@Api(tags = "/file")
public class FileController extends BaseController {

    private FileService fileService;

    private CoreProperties coreProperties;

    @Autowired
    public FileController(FileService fileService, CoreProperties coreProperties) {
        this.fileService = fileService;
        this.coreProperties = coreProperties;
    }

    @PostMapping
    @ApiOperation(value = "‍上传文件", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> uploadFile(@ApiParam(value = "‍文件", required = true) MultipartFile file,
                                             @ApiParam("‍文件路径（显示用，不代表文件实际存储在磁盘上的路径）") String path,
                                             @RequestParam(defaultValue = "false") Boolean rename) throws IOException {
        if (StringUtil.isNotEmpty(path)) {
            path = URLDecoder.decode(path, coreProperties.getCharset());
        }
        return responseOfPost(fileService.save(file, path, rename).getId());
    }

    @PostMapping(path = "/{id}")
    @ApiOperation(value = "‍更新文件", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> updateFile(@ApiParam(value = "‍文件id", required = true) @PathVariable String id,
                                             @ApiParam(value = "‍文件", required = true) MultipartFile file) throws IOException {
        return responseOfPost(fileService.update(id, file).getId());
    }

    @PutMapping(path = "/{id}")
    @ApiOperation(value = "‍更新文件名称")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> fileRename(@ApiParam(value = "‍文件id", required = true) @PathVariable String id,
                                             @ApiParam(value = "‍文件名", required = true) String fileName,
                                             @ApiParam(value = "‍是否更改文件类型") @RequestParam(defaultValue = "false") Boolean resetFileType)
            throws IOException {
        return responseOfPut(fileService.updateFileName(id, fileName, resetFileType).getId());
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "‍根据id获取文件", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void download(@ApiParam(value = "‍文件id", required = true) @PathVariable String id, HttpServletRequest request, HttpServletResponse
            response) throws IOException {
        fileService.download(id, request, response);
    }

    @GetMapping(path = "/{id}/meta")
    @ApiOperation("‍根据id获取元文件")
    public ResponseEntity<FileVO> metaInfo(@ApiParam(value = "‍文件id", required = true) @PathVariable String id) {
        FileVO fileVO = BeanUtil.convert(fileService.findById(id), FileVO.class);
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
    @ApiOperation(value = "‍创建文件夹")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> uploadFileDir(@ApiParam(value = "‍文件夹信息", required = true) @RequestBody FileVO fileVO) {
        return responseOfPost(fileService.saveDir(fileVO).getId());
    }

    @PutMapping(path = "/dir/{id}")
    @ApiOperation(value = "‍更新文件夹")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updateDir(@ApiParam(value = "‍文件夹id", required = true) @PathVariable String id,
                                            @ApiParam(value = "‍文件夹信息", required = true) @RequestBody FileVO fileVO) {
        fileVO.setId(id);
        return responseOfPut(fileService.updateDir(fileVO).getId());
    }

    @DeleteMapping(path = "/dir")
    @ApiOperation(value = "‍删除文件夹")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteFileDir(@ApiParam(value = "‍文件夹信息Id列表(使用\",\"分割)", required = true) @RequestParam String ids)
            throws IOException {
        return responseOfDelete(fileService.deleteFileDirByIds(ids));
    }

    @GetMapping(path = "/dir")
    @ApiOperation(value = "‍获取路径下所有文件和文件夹列表")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "order", value = "‍排序", paramType = "query", dataType = "String")
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Collection<FileVO>> getFileDir(@ApiParam(value = "‍路径", required = true) String path,
                                                         @ApiParam(value = "‍文件或文件夹名称") String fileName) {
        return responseOfGet(fileService.findFileDir(path, fileName, getSort()));
    }

}
