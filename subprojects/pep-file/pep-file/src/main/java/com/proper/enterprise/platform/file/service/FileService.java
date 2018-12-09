package com.proper.enterprise.platform.file.service;

import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;
import com.proper.enterprise.platform.file.api.File;
import com.proper.enterprise.platform.file.vo.FileVO;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

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
     * 保存文件
     *
     * @param file    文件
     * @param virPath 虚拟文件路径
     * @param rename  是否重命名
     * @return 文件
     * @throws IOException io异常
     */
    File save(MultipartFile file, String virPath, Boolean rename) throws IOException;

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


    /**
     * 下载文件
     *
     * @param id 文件id
     * @return 文件流
     * @throws IOException io异常
     */
    InputStream download(String id) throws IOException;

    /**
     * 新建文件夹
     *
     * @param fileVO 文件夹信息
     * @return id 文件夹id
     */
    File saveDir(FileVO fileVO);

    /**
     * 更新文件夹信息
     *
     * @param fileVO 待更新文件夹信息
     * @return id 文件夹id
     */
    File updateDir(FileVO fileVO);

    /**
     * 删除文件夹
     *
     * @param ids id集合(, 分隔)
     * @return 更新成功 true
     * @throws IOException io异常
     */
    boolean deleteFileDirByIds(String ids) throws IOException;

    /**
     * 查找文件路径下文件夹以及文件
     *
     * @param virPath  虚拟路径
     * @param fileName 文件名
     * @param sort     排序
     * @return 文件夹以及文件列表
     */
    Collection<FileVO> findFileDir(String virPath, String fileName, Sort sort);
}
