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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.proper.enterprise.platform.core.jpa.service.impl.JpaServiceSupport;
import com.proper.enterprise.platform.file.api.File;
import com.proper.enterprise.platform.file.repository.FileRepository;
import com.proper.enterprise.platform.file.service.FileService;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
    public File save(String modelName, MultipartFile file, String fileDescription) throws IOException {
        createFilePath(modelName);
        File fileEntity = buildFileEntity(file, modelName, fileDescription, true);
        validMaxSize(fileEntity);
        validSameName(fileEntity);
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
    public File update(String id, MultipartFile file, String fileDescription) throws IOException {
        File updateFile = this.findOne(id);
        if (null == updateFile) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.upload.put.notfind"));
        }
        if (null != file) {
            return handleUpdateFile(file, updateFile, fileDescription);
        }
        return handleOnlyUpdateDesc(updateFile, fileDescription);
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
        String fileName = file.getFileName();
        String userAgent = request.getHeader("User-Agent").toLowerCase();
        if (userAgent.contains("firefox")) {
            fileName = new String(file.getFileName().getBytes(charset), "ISO-8859-1");
        }
        response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, charset));
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

    private File handleUpdateFile(MultipartFile file, File oldFile, String fileDescription) throws IOException {
        if (StringUtil.isNull(fileDescription)) {
            fileDescription = oldFile.getFileDescription();
        }
        File updateFile = buildFileEntity(file, oldFile.getFileModule(), fileDescription, false);
        updateFile.setId(oldFile.getId());
        updateFile.setFilePath(buildUpdateFilePath(oldFile, updateFile));
        validMaxSize(updateFile);
        if (!updateFile.getFileName().equals(oldFile.getFileName())) {
            validSameName(updateFile);
        }
        String oldFilePath = oldFile.getFilePath();
        if (updateFile.getFilePath().equals(oldFile.getFilePath())) {
            updateFile = super.updateForSelective(updateFile);
            dsfService.saveFile(file.getInputStream(), updateFile.getFilePath(), true);
            return updateFile;
        }
        updateFile = super.updateForSelective(updateFile);
        dsfService.deleteFile(oldFilePath);
        dsfService.saveFile(file.getInputStream(), updateFile.getFilePath());
        return updateFile;
    }

    private File handleOnlyUpdateDesc(File updateFile, String fileDescription) {
        if (StringUtil.isNotNull(fileDescription)) {
            updateFile.setFileDescription(fileDescription);
        }
        return super.updateForSelective(updateFile);
    }

    private String buildUpdateFilePath(File oldFile, File updateFile) {
        String oldFilePath = oldFile.getFilePath();
        return oldFilePath.replace(oldFile.getFileName(), "") + updateFile.getFileName();
    }

    private File buildFileEntity(MultipartFile file, String modelName, String fileDescription, boolean buildPath) {
        File fileEntity = new FileEntity();
        fileEntity.setFileDescription(fileDescription);
        if (buildPath) {
            fileEntity.setFilePath(createFilePath(modelName) + file.getOriginalFilename());
        }
        fileEntity.setFileSize(file.getSize());
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setFileType(getFileType(file.getOriginalFilename()));
        fileEntity.setFileModule(modelName);
        return fileEntity;
    }

    private String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    private String createFilePath(String modelName) {
        return rootPath + java.io.File.separator + modelName + java.io.File.separator + DateUtil.toDateString(new Date()) + java.io.File.separator;
    }

    private void validMaxSize(File file) {
        if (file.getFileSize() > maxSize) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.upload.valid.maxsize"));
        }
    }

    private void validSameName(File file) {
        Specification<File> specification = new Specification<File>() {
            @Override
            public Predicate toPredicate(Root<File> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.and(cb.equal(root.get("filePath"), file.getFilePath()));
            }
        };
        List<File> files = this.findAll(specification);
        if (files.size() > 0) {
            throw new ErrMsgException(I18NUtil.getMessage("dfs.upload.valid.path.duplicated"));
        }
    }
}
