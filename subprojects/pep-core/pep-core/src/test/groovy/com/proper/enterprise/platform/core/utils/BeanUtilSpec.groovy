package com.proper.enterprise.platform.core.utils

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

}
