package com.proper.enterprise.platform.core.copy

import com.proper.enterprise.platform.core.copy.api.B
import com.proper.enterprise.platform.core.utils.BeanUtil
import spock.lang.Specification

class BeanUtilSpec extends Specification {

    def single() {
        AVO avo = new AVO()
        avo.setName("12313")
        BVO bvo = new BVO()
        bvo.setSex("sex")
        avo.setBv(bvo)
        BVO bvo1 = new BVO()
        bvo1.setSex("1")
        BVO bvo2 = new BVO()
        bvo2.setSex("2")
        List<B> bvos = new ArrayList<>()
        bvos.add(bvo1)
        bvos.add(bvo2)
        avo.setBs(bvos)

        ABO abo = new ABO()
        BeanUtil.copyProperties(avo, abo)

        expect:
        assert "12313" == abo.getName()
        assert "sex" == abo.getBv().getSex()
        assert "1" == abo.getBs().get(0).getSex()
        assert "2" == abo.getBs().get(1).getSex()
    }

    def ignore() {
        AVO avo = new AVO()
        avo.setName("12313")
        BVO bvo = new BVO()
        bvo.setSex("sex")
        avo.setBv(bvo)
        BVO bvo1 = new BVO()
        bvo1.setSex("1")
        BVO bvo2 = new BVO()
        bvo2.setSex("2")
        List<B> bvos = new ArrayList<>()
        bvos.add(bvo1)
        bvos.add(bvo2)
        avo.setBs(bvos)

        ABO aboName = new ABO()
        BeanUtil.copyProperties(avo, aboName, "name")
        ABO aboIgnoreBvSex = new ABO()
        BeanUtil.copyProperties(avo, aboIgnoreBvSex, "bv.sex")
        ABO aboIgnoreBsSex = new ABO()
        BeanUtil.copyProperties(avo, aboIgnoreBsSex, "bs.sex")

        expect:
        assert aboName.getName() == null
        assert aboIgnoreBvSex.getBv().getSex() == null
        assert aboIgnoreBsSex.getBs().get(0).getSex() == null
    }

}
