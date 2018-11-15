package com.proper.enterprise.platform.streamline.service.impl;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.digest.MD5;
import com.proper.enterprise.platform.streamline.api.service.StreamlineService;
import com.proper.enterprise.platform.streamline.entity.SignEntity;
import com.proper.enterprise.platform.streamline.repository.SignRepository;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StreamlineServiceImpl implements StreamlineService {

    private SignRepository signRepository;

    private I18NService i18NService;

    @Autowired
    public StreamlineServiceImpl(SignRepository signRepository, I18NService i18NService) {
        this.signRepository = signRepository;
        this.i18NService = i18NService;
    }

    @Override
    public void addSign(String businessId, String userName, String password, String serviceKey) {
        String signature = MD5.md5Hex(userName + password);
        SignEntity existSignEntity = signRepository.findBySignature(signature);
        if (existSignEntity != null && signature.equals(existSignEntity.getSignature())) {
            throw new ErrMsgException(i18NService.getMessage("streamline.userNameAndPassword.unique"));
        }
        SignEntity signEntity = new SignEntity();
        signEntity.setBusinessId(businessId);
        signEntity.setSignature(MD5.md5Hex(userName + password));
        signEntity.setServiceKey(serviceKey);
        signRepository.save(signEntity);
    }

    @Override
    public void deleteSign(String businessId) {
        signRepository.deleteByBusinessId(businessId);
    }

    @Override
    public void updateSign(String userName, String password, String businessId) {
        SignEntity existSignEntity = signRepository.findByBusinessId(businessId);
        if (existSignEntity == null) {
            throw new ErrMsgException(i18NService.getMessage("streamline.businessId.unregistered"));
        }
        existSignEntity.setSignature(MD5.md5Hex(userName + password));
        signRepository.updateForSelective(existSignEntity);
    }

    @Override
    public String getSign(String userName, String password) {
        String signature = MD5.md5Hex(userName + password);
        SignEntity signEntity = signRepository.findBySignature(signature);
        if (null == signEntity) {
            return null;
        }
        return signEntity.getServiceKey();
    }
}
