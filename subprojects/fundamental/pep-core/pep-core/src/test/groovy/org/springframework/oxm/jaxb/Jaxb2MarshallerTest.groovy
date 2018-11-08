package org.springframework.oxm.jaxb

import com.proper.enterprise.platform.configs.PEPConfiguration
import com.proper.enterprise.platform.core.Req
import com.proper.enterprise.platform.test.AbstractSpringTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.oxm.Marshaller
import org.springframework.oxm.Unmarshaller
import org.springframework.test.context.ContextConfiguration

import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

@ContextConfiguration(classes = PEPConfiguration)
class Jaxb2MarshallerTest extends AbstractSpringTest {

    @Autowired
    private Marshaller marshaller = null

    @Autowired
    private Unmarshaller unmarshaller = null

    @Test
    void marshal() {
        def m = new Req()
        m.setHosId('11')
        m.setIp('192.168.1.1')

        Writer writer = new StringWriter()
        marshaller.marshal(m, new StreamResult(writer))

        def u = (Req) unmarshaller.unmarshal(new StreamSource(new StringReader(writer.toString())))

        assert m.hosId == u.hosId
        assert m.ip == u.ip
    }

}
