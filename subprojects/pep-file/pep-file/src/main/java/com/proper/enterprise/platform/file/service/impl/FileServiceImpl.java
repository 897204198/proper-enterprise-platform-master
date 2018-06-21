package com.proper.enterprise.platform.file.service.impl;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.dfs.api.service.DFSService;
import com.proper.enterprise.platform.file.entity.FileEntity;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.proper.enterprise.platform.core.jpa.service.impl.JpaServiceSupport;
import com.proper.enterprise.platform.file.api.File;
import com.proper.enterprise.platform.file.repository.FileRepository;
import com.proper.enterprise.platform.file.service.FileService;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Service
public class FileServiceImpl extends JpaServiceSupport<File, FileRepository, String> implements FileService {

    @Value("${file.upload.maxsize}")
    private long maxSize;

    @Value("${file.upload.rootpath}")
    private String rootPath;

    @Value("${core.default_charset}")
    private String charset;

    @Autowired
    DFSService dsfService;

    @Autowired
    private FileRepository fileRepository;

    @Override
    public FileRepository getRepository() {
        return fileRepository;
    }

    @Override
    public File save(MultipartFile file) throws IOException {
        File fileEntity = buildFileEntity(file, true);
        validMaxSize(fileEntity);
        fileEntity = this.save(fileEntity);
        dsfService.saveFile(file.getInputStream(), fileEntity.getFilePath());
        return fileEntity;
    }

    @Override
    public boolean deleteByIds(String ids) throws IOException {
        if (StringUtil.isEmpty(ids)) {
            return false;
        }
        String[] idArr = ids.split(",");
        Collection<File> files = this.findAll(Arrays.asList(idArr));
        if (CollectionUtil.isEmpty(files)) {
            return false;
        }
        super.delete(files);
        for (File file : files) {
            dsfService.deleteFile(file.getFilePath());
        }
        return true;
    }

    @Override
    public File update(String id, MultipartFile file) throws IOException {
        File updateFile = this.findOne(id);
        if (null == updateFile) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.upload.put.notfind"));
        }
        return handleUpdateFile(file, updateFile);
    }


    @Override
    public void download(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = this.findOne(id);
        if (null == file) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.download.not.find"));
        }
        InputStream inputStream = dsfService.getFile(file.getFilePath());
        if (inputStream == null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.download.not.find"));
        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        OutputStream outputStream = response.getOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(file.getFileName(), charset));
        int bytesRead;
        byte[] buffer = new byte[8192];
        while ((bytesRead = bufferedInputStream.read(buffer, 0, 8192)) != -1) {
            bufferedOutputStream.write(buffer, 0, bytesRead);
        }
        bufferedOutputStream.flush();
        inputStream.close();
        bufferedInputStream.close();
        outputStream.close();
        bufferedOutputStream.close();
    }

    private File handleUpdateFile(MultipartFile file, File oldFile) throws IOException {
        File updateFile = buildFileEntity(file, false);
        updateFile.setId(oldFile.getId());
        updateFile.setFilePath(oldFile.getFilePath());
        validMaxSize(updateFile);
        updateFile = super.updateForSelective(updateFile);
        dsfService.saveFile(file.getInputStream(), updateFile.getFilePath(), true);
        return updateFile;
    }

    private File buildFileEntity(MultipartFile file, boolean buildPath) {
        File fileEntity = new FileEntity();
        fileEntity.setFileType(getFileType(file.getOriginalFilename()));
        if (buildPath) {
            fileEntity.setFilePath(createFilePath() + UUID.randomUUID() + "." + fileEntity.getFileType());
        }
        fileEntity.setFileSize(file.getSize());
        fileEntity.setFileName(file.getOriginalFilename());
        return fileEntity;
    }

    private String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    private String createFilePath() {
        return rootPath + java.io.File.separator + DateUtil.toDateString(new Date()) + java.io.File.separator;
    }

    private void validMaxSize(File file) {
        if (file.getFileSize() > maxSize) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.upload.valid.maxsize"));
        }
    }

}
