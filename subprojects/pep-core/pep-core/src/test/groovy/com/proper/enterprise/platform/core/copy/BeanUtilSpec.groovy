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

    def convert() {
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
        AVO avo1 = new AVO()
        avo1.setName("222")
        List<AVO> avos = new ArrayList<>()
        avos.add(avo)
        avos.add(avo1)
        List<ABO> abos = BeanUtil.convert(avos, ABO.class)
        List<ABO> aboIgnoreBvSex = BeanUtil.convert(avos, ABO.class, "bs.sex")
        expect:
        assert 2 == abos.size()
        assert "12313" == abos.get(0).getName()
        assert "sex" == abos.get(0).getBv().getSex()
        assert "1" == abos.get(0).getBs().get(0).getSex()
        assert "2" == abos.get(0).getBs().get(1).getSex()
        assert "222" == abos.get(1).getName()
        assert null == aboIgnoreBvSex.get(0).getBs().get(0).getSex()
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
        AVO avoNullBv = new AVO()
        avoNullBv.setName("123")
        ABO aboIgnoreNull = new ABO()
        BBO bbo = new BBO()
        bbo.setSex("sex")
        aboIgnoreNull.setBv(bbo)
        BeanUtil.copyProperties(avoNullBv, aboIgnoreNull, true)
        expect:
        assert aboName.getName() == null
        assert aboIgnoreBvSex.getBv().getSex() == null
        assert aboIgnoreBsSex.getBs().get(0).getSex() == null
        assert aboIgnoreNull.getBv().getSex() == "sex"
        assert aboIgnoreNull.getName() == "123"
    }

}
