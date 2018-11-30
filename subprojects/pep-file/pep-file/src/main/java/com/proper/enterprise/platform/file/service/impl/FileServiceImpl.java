package com.proper.enterprise.platform.file.service.impl;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.*;
import com.proper.enterprise.platform.dfs.api.service.DFSService;
import com.proper.enterprise.platform.file.entity.FileEntity;
import com.proper.enterprise.platform.file.vo.FileVO;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.file.api.File;
import com.proper.enterprise.platform.file.repository.FileRepository;
import com.proper.enterprise.platform.file.service.FileService;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;

@Service
public class FileServiceImpl extends AbstractJpaServiceSupport<File, FileRepository, String> implements FileService {

    private static final Integer LENGTH = 8192;

    @Value("${dfs.upload.maxsize}")
    private long maxSize;

    @Value("${dfs.upload.maxnamelength}")
    private long maxNameLength;

    @Value("${dfs.upload.rootpath}")
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
        return save(file, null);
    }

    @Override
    public File save(MultipartFile file, String virPath) throws IOException {
        File fileEntity = buildFileEntity(file, true);
        validMaxSize(fileEntity);
        validMaxNameLength(fileEntity);
        if (StringUtil.isNotEmpty(virPath)) {
            virPath = URLDecoder.decode(virPath, PEPConstants.DEFAULT_CHARSET.toString());
            FileEntity fileExistEntity = fileRepository.findOneByVirPathAndFileName(virPath, fileEntity.getFileName());
            if (fileExistEntity != null) {
                throw new ErrMsgException(I18NUtil.getMessage("dfs.upload.valid.path.duplicated"));
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
        InputStream inputStream = download(id);
        if (inputStream == null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.download.not.find"));
        }
        response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(file.getFileName(), charset));
        OutputStream outputStream = response.getOutputStream();
        commonDownLoad(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
    }

    @Override
    public InputStream download(String id) throws IOException {
        File file = this.findOne(id);
        if (null == file) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.download.not.find"));
        }
        InputStream inputStream = dsfService.getFile(file.getFilePath());
        if (inputStream == null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.download.not.find"));
        }
        return inputStream;
    }

    @Override
    public File saveDir(FileVO fileVO) {
        String fileDir = fileVO.getVirPath();
        if (StringUtil.isEmpty(fileDir)) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.filDir.isEmpty"));
        }
        String folderName = fileVO.getFileName();
        if (StringUtil.isEmpty(folderName)) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.folderName.isEmpty"));
        }
        FileEntity fileExistEntity = fileRepository.findOneByVirPathAndFileName(fileDir, folderName);
        if (fileExistEntity != null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.folder.isExist"));
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
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.folder.isExist"));
        }
        validMaxNameLength(fileVO);
        FileEntity fileDirOldEntity = (FileEntity) this.findOne(fileVO.getId());
        String subVirtualFileOldPath = fileDirOldEntity.getVirPath()
                                       + fileDirOldEntity.getFileName();
        Boolean isSubFile = fileVO.getVirPath().startsWith(subVirtualFileOldPath + "/");
        if (isSubFile) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.folder.move.error"));
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
        Collection<File> fileDirs = this.findAll(Arrays.asList(idArr));
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
            virPath = URLDecoder.decode(virPath, PEPConstants.DEFAULT_CHARSET.toString());
        } catch (UnsupportedEncodingException e) {
            throw new ErrMsgException("pep.filelist.find.failed");
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
        File fileEntity = new FileEntity();
        fileEntity.setFileType(getFileType(file.getOriginalFilename()));
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
            ((FileEntity) fileEntity).setFileExtMsgMap(imgExtMsg);
        }
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

    private void validMaxNameLength(File file) {
        String fileName = file.getFileName();
        if (fileName.getBytes(PEPConstants.DEFAULT_CHARSET).length > maxNameLength) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.upload.valid.maxnamelength"));
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
