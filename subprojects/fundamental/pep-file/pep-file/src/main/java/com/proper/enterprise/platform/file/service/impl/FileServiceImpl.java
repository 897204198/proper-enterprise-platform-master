package com.proper.enterprise.platform.file.service.impl;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
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
        return save(file, null, false);
    }

    @Override
    public File save(MultipartFile file, String virPath, Boolean rename) throws IOException {
        File fileEntity = buildFileEntity(file, true);
        validMaxSize(fileEntity);

        ((FileEntity) fileEntity).setVirPath(buildVirPath(virPath, fileEntity.getFilePath()));
        boolean isUnique = fileOrFolderIsUnique(virPath, fileEntity.getFileName());
        if (!rename && !isUnique) {
            throw new ErrMsgException("The file or folder is exist");
        }
        if (!isUnique) {
            int count = generatedFileOrFolderCount(virPath, fileEntity.getFileName(), false);
            fileEntity.setFileName(buildFileOrFolderName(fileEntity.getFileName(), count, false));
            ((FileEntity) fileEntity).setFileCount(count);
        } else {
            ((FileEntity) fileEntity).setFileCount(0);
        }

        fileEntity = this.save(fileEntity);
        dsfService.saveFile(file.getInputStream(), fileEntity.getFilePath());

        FileVO fileVO = new FileVO();
        BeanUtil.copyProperties(fileEntity, fileVO);
        return fileVO;
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
    public File updateFileName(String id, String fileName, Boolean resetFileType) throws IOException {
        File updateFile = this.findById(id);
        if (null == updateFile) {
            throw new ErrMsgException("The file to be updated was not found");
        }
        String fileType = getFileType(fileName);
        Boolean fileTypeChanged = fileType == null || !fileType.equals(updateFile.getFileType());
        if (!resetFileType && fileTypeChanged) {
            throw new ErrMsgException("It may cause the file to be unavaliable after change the file name");
        }

        boolean isUnique = fileOrFolderIsUnique(((FileEntity) updateFile).getVirPath(), fileName);
        if (!isUnique) {
            int count = generatedFileOrFolderCount(((FileEntity) updateFile).getVirPath(), fileName, false);
            updateFile.setFileName(buildFileOrFolderName(fileName, count, false));
            ((FileEntity) updateFile).setFileCount(count);
        } else {
            updateFile.setFileName(fileName);
            updateFile.setFileType(fileType);
            ((FileEntity) updateFile).setFileCount(0);
        }
        updateFile = super.updateForSelective(updateFile);

        FileVO fileVO = new FileVO();
        BeanUtil.copyProperties(updateFile, fileVO);
        return fileVO;
    }

    @Override
    public void download(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = this.findById(id);
        if (null == file) {
            throw new ErrMsgException("The downloaded resource was not found. id: " + id);
        }
        FileVO fileVO = new FileVO();
        BeanUtil.copyProperties(file, fileVO);

        InputStream inputStream = download(id);
        response.setHeader("Content-disposition", "attachment;filename="
            + java.net.URLEncoder.encode(fileVO.getFileName(), coreProperties.getCharset()));
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
        fileDir = buildVirPath(fileDir, null);
        FileEntity fileDirEntity = new FileEntity();
        fileDirEntity.setFileName(folderName);
        fileDirEntity.setFilePath(fileDir);
        fileDirEntity.setFileType("Directory");
        fileDirEntity.setVirPath(fileDir);
        fileDirEntity.setDir(true);

        boolean isUnique = fileOrFolderIsUnique(fileDir, folderName);
        if (!isUnique) {
            int count = generatedFileOrFolderCount(fileDir, folderName, true);
            fileDirEntity.setFileName(buildFileOrFolderName(folderName, count, true));
            fileDirEntity.setFileCount(count);
        } else {
            fileDirEntity.setFileCount(0);
        }

        fileDirEntity = this.save(fileDirEntity);
        dsfService.createDir(fileDirEntity.getFilePath());

        BeanUtil.copyProperties(fileDirEntity, fileVO);
        return fileVO;
    }

    @Override
    public File updateDir(FileVO fileVO) {
        fileVO.setVirPath(buildVirPath(fileVO.getVirPath(), null));
        FileEntity fileDirOldEntity = (FileEntity) this.findById(fileVO.getId());
        String subVirtualFileOldPath = fileDirOldEntity.getVirPath()
                                       + fileDirOldEntity.getFileName();
        Boolean isSubFile = fileVO.getVirPath().startsWith(subVirtualFileOldPath + "/");
        if (isSubFile) {
            throw new ErrMsgException("The folder move error");
        }

        boolean isUnique = fileOrFolderIsUnique(fileVO.getVirPath(), fileVO.getFileName());
        if (!isUnique) {
            int count = generatedFileOrFolderCount(fileVO.getVirPath(), fileVO.getFileName(), true);
            fileDirOldEntity.setFileName(buildFileOrFolderName(fileVO.getFileName(), count, true));
            fileDirOldEntity.setFileCount(count);
        } else {
            fileDirOldEntity.setFileName(fileVO.getFileName());
            fileDirOldEntity.setFileCount(0);
        }
        fileDirOldEntity.setVirPath(fileVO.getVirPath());
        fileDirOldEntity.setFilePath(fileDirOldEntity.getVirPath() + fileDirOldEntity.getFileName());

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

        BeanUtil.copyProperties(fileDirEntity, fileVO);

        return fileVO;
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
    public Collection<FileVO> findFileDir(String virPath, String fileName, Sort sort) {
        try {
            virPath = URLDecoder.decode(virPath, coreProperties.getCharset());
        } catch (UnsupportedEncodingException e) {
            throw new ErrMsgException("The file list find failed");
        }
        if (sort == null) {
            sort = new Sort(new Sort.Order(Sort.Direction.DESC, "isDir"),
                            new Sort.Order(Sort.Direction.DESC, "lastModifyTime"));
        }
        List<FileEntity> fileEntities;
        if (StringUtil.isNotEmpty(fileName)) {
            fileEntities = fileRepository.findAllByVirPathAndFileNameContaining(virPath, fileName, sort);
        } else {
            fileEntities = fileRepository.findAllByVirPath(virPath, sort);
        }
        Collection<FileVO> files = BeanUtil.convert(fileEntities, FileVO.class);
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
        updateFile = super.updateForSelective(updateFile);
        dsfService.saveFile(file.getInputStream(), updateFile.getFilePath(), true);

        FileVO fileVO = new FileVO();
        BeanUtil.copyProperties(updateFile, fileVO);
        return fileVO;
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

    private String buildVirPath(String virPath, String filePath) {
        if (StringUtil.isNotEmpty(virPath)) {
            boolean isEndWithSlash = virPath.endsWith("/");
            if (!isEndWithSlash) {
                virPath = virPath + "/";
            }
            return virPath;
        } else {
            return filePath;
        }
    }

    private String buildFileOrFolderName(String fileName, int count, boolean isDir) {
        if (isDir) {
            return fileName + " (" + count + ")";
        } else {
            String fileType = getFileType(fileName);
            return fileName.substring(0, fileName.length() - fileType.length() - 1)
                    + " ("
                    + count
                    + ")"
                    + "."
                    + fileType;
        }
    }

    private String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    private String createFilePath() {
        return dfsProperties.getRootPath() + java.io.File.separator
            + DateUtil.toLocalDateString(LocalDateTime.now()) + java.io.File.separator;
    }

    private boolean fileOrFolderIsUnique(String virPath, String fileName) {
        FileEntity fileExistEntity =
            fileRepository.findOneByVirPathAndFileName(virPath, fileName);
        if (fileExistEntity != null) {
            return false;
        }
        return true;
    }

    private int generatedFileOrFolderCount(String virPath, String fileName, Boolean isDir) {
        String existName;
        if (isDir) {
            existName = fileName + " (%)";
        } else {
            String fileType = getFileType(fileName);
            existName = fileName.substring(0, fileName.length() - fileType.length() - 1)
                + " (%)"
                + "."
                + fileType;
        }
        List<FileEntity> fileEntities =
            fileRepository.findAllByVirPathAndFileNameLikeOrderByFileCountDesc(virPath, existName);
        if (fileEntities.size() == 0) {
            return 1;
        } else {
            return fileEntities.get(0).getFileCount() + 1;
        }
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
