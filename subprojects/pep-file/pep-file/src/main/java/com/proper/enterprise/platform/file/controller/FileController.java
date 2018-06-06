package com.proper.enterprise.platform.file.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.file.vo.FileVO;
import com.proper.enterprise.platform.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/file")
public class FileController extends BaseController {

    @Autowired
    private FileService fileService;

    @PostMapping(path = "/{modelName}")
    public ResponseEntity<FileVO> save(@PathVariable String modelName,  MultipartFile file, String fileDescription) throws IOException {
        return responseOfPost(fileService.save(modelName, file, fileDescription), FileVO.class);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String ids) throws IOException {
        return responseOfDelete(fileService.deleteByIds(ids));
    }

    @PostMapping(path = "/id/{id}")
    public ResponseEntity<FileVO> put(@PathVariable String id, MultipartFile file, String fileDescription) throws IOException {
        return responseOfPut(fileService.update(id, file, fileDescription), FileVO.class);
    }

    @GetMapping
    public ResponseEntity<?> get() {
        return isPageSearch() ? responseOfGet(fileService.findPage(), FileVO.class)
            : responseOfGet(fileService.findAll(), FileVO.class);
    }

    @AuthcIgnore
    @GetMapping(path = "/download/{id}")
    public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        fileService.download(id, request, response);
    }
}
