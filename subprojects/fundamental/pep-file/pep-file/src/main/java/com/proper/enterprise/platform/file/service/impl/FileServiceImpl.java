package com.proper.enterprise.platform.file.service.impl;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.dfs.DFSProperties;
import com.proper.enterprise.platform.dfs.api.service.DFSService;
import com.proper.enterprise.platform.file.api.File;
import com.proper.enterprise.platform.file.entity.FileEntity;
import com.proper.enterprise.platform.file.repository.FileRepository;
import com.proper.enterprise.platform.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FileServiceImpl extends AbstractJpaServiceSupport<File, FileRepository, String> implements FileService {

    private static final Integer LENGTH = 8192;

    @Autowired
    private DFSProperties dfsProperties;

    @Autowired
    private CoreProperties coreProperties;

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
        Collection<File> files = this.findAllById(Arrays.asList(idArr));
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
        File updateFile = this.findById(id);
        if (null == updateFile) {
            throw new ErrMsgException("The file to be updated was not found. id:" + id);
        }
        return handleUpdateFile(file, updateFile);
    }


    @Override
    public void download(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = this.findById(id);
        if (null == file) {
            throw new ErrMsgException("The downloaded resource was not found. id: " + id);
        }
        InputStream inputStream = dsfService.getFile(file.getFilePath());
        if (inputStream == null) {
            throw new ErrMsgException("The downloaded resource was not found. path: " + file.getFilePath());
        }
        response.setHeader("Content-disposition", "attachment;filename="
            + java.net.URLEncoder.encode(file.getFileName(), coreProperties.getCharset()));
        OutputStream outputStream = response.getOutputStream();
        commonDownLoad(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
    }

    private void commonDownLoad(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        int bytesRead;
        byte[] buffer = new byte[LENGTH];
        while ((bytesRead = bufferedInputStream.read(buffer, 0, LENGTH)) != -1) {
            bufferedOutputStream.write(buffer, 0, bytesRead);
        }
        bufferedInputStream.close();
        bufferedOutputStream.flush();
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

    private File buildFileEntity(MultipartFile file, boolean buildPath) throws IOException {
        FileEntity fileEntity = new FileEntity();
        String fileName = file.getOriginalFilename();
        if (StringUtil.isBlank(fileName)) {
            fileName = "DEFAULT_FILE_NAME_" + UUID.randomUUID() + ".tmp";
        }
        fileEntity.setFileType(getFileType(fileName));
        if (buildPath) {
            fileEntity.setFilePath(createFilePath() + UUID.randomUUID() + "." + fileEntity.getFileType());
        }
        fileEntity.setFileSize(file.getSize());
        fileEntity.setFileName(file.getOriginalFilename());
        if (isImage(fileEntity.getFileName())) {
            Map<String, String> imgExtMsg = new HashMap<>(16);
            BufferedImage image = ImageIO.read(file.getInputStream());
            imgExtMsg.put("img_width", String.valueOf(image.getWidth()));
            imgExtMsg.put("img_height", String.valueOf(image.getHeight()));
            fileEntity.setFileExtMsgMap(imgExtMsg);
        }
        return fileEntity;
    }

    private String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    private String createFilePath() {
        return dfsProperties.getRootPath() + java.io.File.separator
            + DateUtil.toLocalDateString(LocalDateTime.now()) + java.io.File.separator;
    }

    private void validMaxSize(File file) {
        if (file.getFileSize() > dfsProperties.getUploadMaxsize()) {
            throw new ErrMsgException("The file is too large");
        }
    }

    private boolean isImage(String fileName) {
        if (StringUtil.isEmpty(fileName)) {
            return false;
        }
        return fileName.endsWith(".jpg")
            || fileName.endsWith(".jpeg")
            || fileName.endsWith(".bmp")
            || fileName.endsWith(".png")
            || fileName.endsWith(".gif");
    }

}
