package com.proper.enterprise.platform.file.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.BigDecimalUtil;
import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.file.vo.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/file")
public class FileController extends BaseController {

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<String> uploadFile(MultipartFile file, String path) throws IOException {
        return responseOfPost(fileService.save(file, path).getId());
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<String> updateFile(@PathVariable String id, MultipartFile file) throws IOException {
        return responseOfPost(fileService.update(id, file).getId());
    }


    @GetMapping(path = "/{id}")
    public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        fileService.download(id, request, response);
    }

    @GetMapping(path = "/{id}/meta")
    public ResponseEntity<FileVO> metainfo(@PathVariable String id) {
        FileVO fileVO = BeanUtil.convert(fileService.findOne(id), FileVO.class);
        if (fileVO != null) {
            fileVO.setFileSizeUnit(BigDecimalUtil.parseSize(fileVO.getFileSize()));
        }
        return responseOfGet(fileVO);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String ids) throws IOException {
        return responseOfDelete(fileService.deleteByIds(ids));
    }

    @GetMapping
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
        return responseOfGet(fileService.findFileDir(path, fileName, getSort()));
    }

}
