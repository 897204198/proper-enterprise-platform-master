package com.proper.enterprise.platform.core.jpa.copy;

import com.proper.enterprise.platform.core.jpa.copy.entity.CopyAEntity;
import com.proper.enterprise.platform.core.jpa.copy.entity.CopyBEntity;
import com.proper.enterprise.platform.core.jpa.copy.service.CopyAService;
import com.proper.enterprise.platform.core.jpa.copy.service.CopyBService;
import com.proper.enterprise.platform.core.jpa.copy.vo.CopyAVO;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.test.AbstractJPATest;
import com.proper.enterprise.platform.test.annotation.NoTx;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CopyTest extends AbstractJPATest {

    @Autowired
    CopyAService copyAService;

    @Autowired
    CopyBService copyBService;

    @Test
    @NoTx
    public void testCopySingle() {
        CopyAEntity a = new CopyAEntity();
        a.setName("a");
        CopyBEntity b = new CopyBEntity();
        b.setBname("b");
        copyBService.save(b);
        a.setCb(b);
        a = copyAService.save(a);
        a = copyAService.findOne(a.getId());
        CopyAVO copyAVO = new CopyAVO();
        BeanUtil.copyProperties(a, copyAVO, "cb.ca");
        validate("b".equals(copyAVO.getCb().getBname()));
    }

    private void validate(boolean ifTrue) {
        if (!ifTrue) {
            throw new RuntimeException();
        }
    }
}
