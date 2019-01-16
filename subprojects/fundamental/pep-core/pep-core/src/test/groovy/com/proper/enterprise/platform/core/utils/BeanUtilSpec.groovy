package com.proper.enterprise.platform.core.utils

import com.proper.enterprise.platform.core.utils.copy.ABO
import com.proper.enterprise.platform.core.utils.copy.AVO
import com.proper.enterprise.platform.core.utils.copy.BBO
import com.proper.enterprise.platform.core.utils.copy.BVO
import com.proper.enterprise.platform.core.utils.model.A
import com.proper.enterprise.platform.core.utils.model.B
import com.proper.enterprise.platform.core.utils.model.C
import spock.lang.Specification

class BeanUtilSpec extends Specification {


    def "convertTest"() {

        B b = new B()
        b.setName("bname")
        Set<String> strs = new HashSet<>()
        strs.add("1")
        strs.add("1")
        b.setStrs(strs)
        Set<C> cs = new HashSet<>()
        C c = new C()
        c.setSort(1)
        cs.add(c)
        b.setCs(cs)
        b.setCv(c)
        b.setCstr('a')

        def map = [:]
        map.put('name', 'bname')
        map.put('strs', strs)
        map.put('cs', cs)
        map.put('cv', c)

        A a = BeanUtil.convert(b, A.class)
        A a2 = BeanUtil.convert(map, A.class, ['cv', 'cstr'] as String[])
        expect:
        assert a.getName() == "bname"
        assert a.getStrs().size() == 1
        assert a.getStrs()[0] == "1"
        assert a.getCs().size() == 1
        assert a.getCs()[0].sort == 1
        assert a.getCv().getSort() == 1
        assert null == a.getCstr()

        assert a2.getName() == "bname"
        assert a2.getStrs().size() == 1
        assert a2.getStrs()[0] == "1"
        assert a2.getCs().size() == 1
        assert a2.getCs()[0].sort == 1
        assert null == a2.getCv()
        assert null == a2.getCstr()
    }

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
        List<com.proper.enterprise.platform.core.utils.copy.api.B> bvos = new ArrayList<>()
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
        List<com.proper.enterprise.platform.core.utils.copy.api.B> bvos = new ArrayList<>()
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
        List<com.proper.enterprise.platform.core.utils.copy.api.B> bvos = new ArrayList<>()
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
        BeanUtil.copyProperties(avoNullBv, aboIgnoreNull)
        expect:
        assert aboName.getName() == null
        assert aboIgnoreBvSex.getBv().getSex() == null
        assert aboIgnoreBsSex.getBs().get(0).getSex() == null
        assert aboIgnoreNull.getBv().getSex() == "sex"
        assert aboIgnoreNull.getName() == "123"

    }
}
