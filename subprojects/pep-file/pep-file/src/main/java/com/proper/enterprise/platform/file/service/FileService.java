package com.proper.enterprise.platform.file.service;

import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;
import com.proper.enterprise.platform.file.api.File;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileService extends BaseJpaService<File, String> {

    /**
     * 保存文件
     *
     * @param file 文件
     * @return 文件
     * @throws IOException io异常
     */
    File save(MultipartFile file) throws IOException;

    /**
     * 删除文件
     *
     * @param ids 文件id
     * @return true false
     * @throws IOException io异常
     */
    boolean deleteByIds(String ids) throws IOException;

    /**
     * 修改文件
     *
     * @param id   文件id
     * @param file 文件
     * @return 文件
     * @throws IOException io异常
     */
    File update(String id, MultipartFile file) throws IOException;

    /**
     * 下载文件
     *
     * @param id       文件id
     * @param request  请求
     * @param response 响应
     * @throws IOException io异常
     */
    void download(String id, HttpServletRequest request, HttpServletResponse response) throws IOException;


}
