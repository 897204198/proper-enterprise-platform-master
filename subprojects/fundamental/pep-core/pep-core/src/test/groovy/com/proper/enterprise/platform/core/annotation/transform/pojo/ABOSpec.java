package com.proper.enterprise.platform.core.annotation.transform.pojo;

import com.proper.enterprise.platform.core.annotation.transform.pojo.bo.ABO;
import com.proper.enterprise.platform.core.annotation.transform.pojo.bo.BBO;
import com.proper.enterprise.platform.core.annotation.transform.pojo.vo.AVO;
import com.proper.enterprise.platform.core.annotation.transform.pojo.vo.BVO;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import org.junit.Test;

import java.util.*;

public class ABOSpec {

    private static final String VO_STR = "VO_STR";
    private static final Integer BO_TEST = 123;

    @Test
    public void testVOInitBO() {
        List<BBO> bbos = new ArrayList<>();
        bbos.add(new BBO().setDtoStrB(VO_STR).add(new ABO().setTest(BO_TEST)));
        bbos.add(new BBO().setDtoStrB(VO_STR));
        ABO abo = new ABO().setDtoStr(VO_STR).setTest(BO_TEST).setBbo(new BBO().setTest(BO_TEST)
            .setDtoStrB(VO_STR).add(new ABO().setTest(BO_TEST).setBbo(new BBO().setDtoStrB(VO_STR))));
        abo.setTestInitalizerBO(BO_TEST);
        abo.setBbos(bbos);
        AVO avo = BeanUtil.convertToVO(abo, AVO.class);

        validate(avo.getTest() == BO_TEST);
        validate(avo.getBvo().getTest() == BO_TEST);
        validate(avo.getBvo().getAs() == null);
        validate(VO_STR.equals(avo.getBvo().getVoStrB()));
        validate(avo.getBvos().size() == 2);
        validate(new ArrayList<>(avo.getBvos()).get(0).getVoStrB() == VO_STR);
        validate(String.valueOf(BO_TEST).equals(avo.getTestInitalizerVO()));
    }

    @Test
    public void testVO2BO() {
        AVO avo = new AVO().setTest(BO_TEST).setVoStr(VO_STR);
        BVO bvo = new BVO().setTest(BO_TEST).setVoStrB(VO_STR);
        avo.setBvo(bvo);
        avo.setTestInitalizerVO(String.valueOf(BO_TEST));
        Set bvos = new HashSet();
        bvos.add(new BVO().setVoStrB(VO_STR).setTest(BO_TEST));
        avo.setBvos(bvos);
        ABO abo = BeanUtil.convertToDO(avo, ABO.class);
        validate(abo.getTest() == BO_TEST);
        validate(VO_STR.equals(abo.getDtoStr()));
        validate(abo.getBbo().getTest() == BO_TEST);
        validate(VO_STR.equals(abo.getBbo().getDtoStrB()));
        validate(abo.getBbos().size() == 1);
        validate(abo.getBbos().get(0).getTest() == BO_TEST);
    }

    private void validate(boolean ifTrue) {
        if (!ifTrue) {
            throw new RuntimeException();
        }
    }
}
