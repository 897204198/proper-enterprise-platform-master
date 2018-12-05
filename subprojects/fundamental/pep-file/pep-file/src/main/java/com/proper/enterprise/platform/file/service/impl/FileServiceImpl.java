package com.proper.enterprise.platform.file.service.impl;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.*;
import com.proper.enterprise.platform.dfs.DFSProperties;
import com.proper.enterprise.platform.dfs.api.service.DFSService;
import com.proper.enterprise.platform.file.api.File;
import com.proper.enterprise.platform.file.entity.FileEntity;
import com.proper.enterprise.platform.file.repository.FileRepository;
import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.file.vo.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
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
        return save(file, null);
    }

    @Override
    public File save(MultipartFile file, String virPath) throws IOException {
        File fileEntity = buildFileEntity(file, true);
        validMaxSize(fileEntity);
        validMaxNameLength(fileEntity);
        if (StringUtil.isNotEmpty(virPath)) {
            virPath = URLDecoder.decode(virPath, coreProperties.getCharset());
            FileEntity fileExistEntity = fileRepository.findOneByVirPathAndFileName(virPath, fileEntity.getFileName());
            if (fileExistEntity != null) {
                throw new ErrMsgException("A file of the same name already exists in the same path");
            }
            ((FileEntity) fileEntity).setVirPath(virPath);
        }
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
        InputStream inputStream = download(id);
        response.setHeader("Content-disposition", "attachment;filename="
            + java.net.URLEncoder.encode(file.getFileName(), coreProperties.getCharset()));
        OutputStream outputStream = response.getOutputStream();
        commonDownLoad(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
    }

    @Override
    public InputStream download(String id) throws IOException {
        File file = this.findById(id);
        if (null == file) {
            throw new ErrMsgException("The downloaded resource was not found");
        }
        InputStream inputStream = dsfService.getFile(file.getFilePath());
        if (inputStream == null) {
            throw new ErrMsgException("The downloaded resource was not found");
        }
        return inputStream;
    }

    @Override
    public File saveDir(FileVO fileVO) {
        String fileDir = fileVO.getVirPath();
        if (StringUtil.isEmpty(fileDir)) {
            throw new ErrMsgException("The file directory is empty");
        }
        String folderName = fileVO.getFileName();
        if (StringUtil.isEmpty(folderName)) {
            throw new ErrMsgException("The folder name is empty");
        }
        FileEntity fileExistEntity = fileRepository.findOneByVirPathAndFileName(fileDir, folderName);
        if (fileExistEntity != null) {
            throw new ErrMsgException("The folder is exist");
        }
        validMaxNameLength(fileVO);
        boolean isEndWithSlash = fileDir.endsWith("/");
        if (!isEndWithSlash) {
            fileDir += "/";
        }
        FileEntity fileDirEntity = new FileEntity();
        fileDirEntity.setFileName(folderName);
        fileDirEntity.setFilePath(fileDir);
        fileDirEntity.setFileType("Directory");
        fileDirEntity.setVirPath(fileDir);
        fileDirEntity.setDir(true);
        fileDirEntity = this.save(fileDirEntity);
        dsfService.createDir(fileDirEntity.getFilePath());
        return fileDirEntity;
    }

    @Override
    public File updateDir(FileVO fileVO) {
        FileEntity fileExistEntity = fileRepository.findOneByVirPathAndFileName(fileVO.getVirPath(), fileVO.getFileName());
        if (fileExistEntity != null) {
            throw new ErrMsgException("The folder is exist");
        }
        validMaxNameLength(fileVO);
        FileEntity fileDirOldEntity = (FileEntity) this.findById(fileVO.getId());
        String subVirtualFileOldPath = fileDirOldEntity.getVirPath()
                                       + fileDirOldEntity.getFileName();
        Boolean isSubFile = fileVO.getVirPath().startsWith(subVirtualFileOldPath + "/");
        if (isSubFile) {
            throw new ErrMsgException("The folder move error");
        }

        fileDirOldEntity.setFileName(fileVO.getFileName());
        fileDirOldEntity.setVirPath(fileVO.getVirPath());
        fileDirOldEntity.setFilePath(fileVO.getVirPath() + fileVO.getFileName());
        FileEntity fileDirEntity = this.updateForSelective(fileDirOldEntity);

        String subVirtualFilePath = fileDirEntity.getVirPath()
                                    + fileDirEntity.getFileName();
        dsfService.updateDir(subVirtualFileOldPath, subVirtualFilePath);

        List<FileEntity> subFiles =
            fileRepository.findAllByVirPathStartingWith(subVirtualFileOldPath + "/");
        for (FileEntity fileEntity : subFiles) {
            fileEntity.setVirPath(
                fileEntity.getVirPath()
                .replaceFirst(subVirtualFileOldPath + "/", subVirtualFilePath + "/")
            );
            this.updateForSelective(fileEntity);
        }

        return fileDirEntity;
    }

    @Override
    public boolean deleteFileDirByIds(String ids) throws IOException {
        if (StringUtil.isEmpty(ids)) {
            return false;
        }
        String[] idArr = ids.split(",");
        Collection<File> fileDirs = this.findAllById(Arrays.asList(idArr));
        if (CollectionUtil.isEmpty(fileDirs)) {
            return false;
        }
        super.delete(fileDirs);
        for (File file : fileDirs) {
            dsfService.deleteDir(file.getFilePath());
            String subVirtualFilePath = ((FileEntity) file).getVirPath()
                                        + file.getFileName();
            Collection<FileEntity> subFiles =
                fileRepository.findAllByVirPathStartingWith(subVirtualFilePath + "/");
            super.delete(subFiles);
            for (File subFile : subFiles) {
                if (((FileEntity) subFile).getDir() == null || !((FileEntity) subFile).getDir()) {
                    dsfService.deleteFile(subFile.getFilePath());
                }
            }
        }
        return true;
    }

    @Override
    public Collection<FileVO> findFileDir(String virPath, String fileName) {
        try {
            virPath = URLDecoder.decode(virPath, coreProperties.getCharset());
        } catch (UnsupportedEncodingException e) {
            throw new ErrMsgException("The file list find failed");
        }
        Sort sort = getSort();
        if (sort == null) {
            sort = new Sort(new Sort.Order(Sort.Direction.DESC, "isDir"),
                            new Sort.Order("fileName"));
        } else {
            sort.and(new Sort("isDir"));
        }
        List<FileEntity> fileEntities;
        if (StringUtil.isNotEmpty(fileName)) {
            fileEntities = fileRepository.findAllByVirPathAndFileNameContaining(virPath, fileName, sort);
        } else {
            fileEntities = fileRepository.findAllByVirPath(virPath, sort);
        }
        Collection<FileVO> files = BeanUtil.convert(fileEntities, FileVO.class);
        files.forEach(fileVO -> fileVO.setFileSizeUnit(BigDecimalUtil.parseSize(fileVO.getFileSize())));
        return files;
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
        ((FileEntity) updateFile).setVirPath(((FileEntity) oldFile).getVirPath());
        validMaxSize(updateFile);
        validMaxNameLength(updateFile);
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

    private void validMaxNameLength(File file) {
        String fileName = file.getFileName();
        try {
            if (fileName.getBytes(coreProperties.getCharset()).length > dfsProperties.getNameMaxLength()) {
                throw new ErrMsgException("The file or folder name is too long");
            }
        } catch (UnsupportedEncodingException ex) {
            throw new ErrMsgException(ex.getMessage());
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
