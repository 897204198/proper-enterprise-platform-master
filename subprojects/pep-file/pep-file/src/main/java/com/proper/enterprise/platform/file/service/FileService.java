package com.proper.enterprise.platform.file.service;

import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;
import com.proper.enterprise.platform.file.api.File;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileService extends BaseJpaService<File, String> {

    File save(String modelName, MultipartFile file, String fileDescription) throws IOException;

    boolean deleteByIds(String ids) throws IOException;

    File update(String id, MultipartFile file, String fileDescription) throws IOException;

    void download(String id, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
