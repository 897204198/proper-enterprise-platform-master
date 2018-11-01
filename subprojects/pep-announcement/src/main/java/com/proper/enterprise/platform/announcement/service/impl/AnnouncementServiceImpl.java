package com.proper.enterprise.platform.announcement.service.impl;

import com.proper.enterprise.platform.announcement.entity.AnnouncementEntity;
import com.proper.enterprise.platform.announcement.repository.AnnouncementRepository;
import com.proper.enterprise.platform.announcement.service.AnnouncementService;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    /**
     * 通过ID查找公告信息.
     *
     * @param id 公告信息ID.
     * @return 公告信息.
     */
    @Override
    public AnnouncementEntity getById(String id) {
        return announcementRepository.findOne(id);
    }

    /**
     * 保存BaseInfo.
     *
     * @param noticeInfo 公告信息.
     * @return 保存后的公告信息.
     */
    @Override
    public AnnouncementEntity save(AnnouncementEntity noticeInfo) {
        return announcementRepository.save(noticeInfo);
    }

    @Override
    public AnnouncementEntity addNoticeinfo(AnnouncementEntity noticeInfo) throws Exception {
        return announcementRepository.save(noticeInfo);
    }

    /**
     * 通过id列表查找全部公告信息.
     */
    @Override
    public List<AnnouncementEntity> findAll() {
        return (List<AnnouncementEntity>) announcementRepository.findAll();
    }

    /**
     * 通过id列表查找公告信息列表.
     *
     * @param idList id列表.
     */
    @Override
    public List<AnnouncementEntity> findAllByIdList(List<String> idList) {
        return (List<AnnouncementEntity>) announcementRepository.findAll(idList);
    }

    /**
     * 删除公告信息列表.
     *
     * @param noticeInfoList 公告信息列表.
     */
    @Override
    public void delete(List<AnnouncementEntity> noticeInfoList) {
        announcementRepository.delete(noticeInfoList);
    }

    @Override
    public void deleteById(String id) {
        AnnouncementEntity announcementEntity = this.getById(id);
        if (announcementEntity != null) {
            announcementRepository.delete(announcementEntity);
        }
    }

    @Override
    public List<AnnouncementEntity> findLatestValidNoticesByInfoType(String infoType) {
        return announcementRepository.findLatestValidNoticesByInfoType(infoType, DateUtil.getTimestamp());
    }

    @Override
    public List<AnnouncementEntity> findLatestNotices(String infoType, String title) {
        List<AnnouncementEntity> notices = announcementRepository.findByInfoTypeAndTitleLikeOrderByBeginTimeDesc(infoType, "%"
                + StringUtil.defaultString(title));
        return notices;
    }

    @Override
    public DataTrunk<AnnouncementEntity> findLatestNoticesPage(String infoType, String title, Pageable pageable) {
        Page<AnnouncementEntity> noticePage = announcementRepository.findByInfoTypeAndTitleLikeOrderByBeginTimeDesc(infoType,
                "%" + StringUtil.defaultString(title) + "%", pageable);
        return new DataTrunk<>(noticePage.getContent(), noticePage.getTotalElements());
    }

    @Override
    public AnnouncementEntity updateNoticeInfoById(String id, AnnouncementEntity announcementEntity) {
        AnnouncementEntity infoEntity = this.getById(id);
        if (infoEntity != null) {
            infoEntity.setTitle(announcementEntity.getTitle());
            infoEntity.setInfoType(announcementEntity.getInfoType());
            infoEntity.setInfo(announcementEntity.getInfo());
            infoEntity.setBeginTime(announcementEntity.getBeginTime());
            infoEntity.setEndTime(announcementEntity.getEndTime());
            infoEntity.setInfoStatus(announcementEntity.getInfoStatus());
        } else {
            infoEntity = announcementEntity;
        }
        return announcementRepository.save(infoEntity);
    }
}