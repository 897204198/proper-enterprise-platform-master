package com.proper.enterprise.platform.core.utils

import com.proper.enterprise.platform.core.utils.model.TestUserModel
import com.proper.enterprise.platform.test.AbstractSpringTest
import org.junit.Test

class SerializationUtilTest extends AbstractSpringTest {

    @Test
    void testDeepClone() {
        List<TestUserModel> list = new ArrayList<>()
        TestUserModel t1 = new TestUserModel()
        t1.setUserId(1)
        t1.setUserName("tom")
        list.add(t1)
        TestUserModel t2 = new TestUserModel()
        t2.setUserId(2)
        t2.setUserName("jimmy")
        list.add(t2)

        List<TestUserModel> cloneList = (List<TestUserModel>)SerializationUtil.deepClone(list)
        TestUserModel t3 = new TestUserModel()
        t3.setUserId(3)
        t3.setUserName("paul")
        cloneList.add(t3)

        assert list.size() == 2
        assert cloneList.size() == 3

        assert list.get(0) != cloneList.get(0)
        assert list.get(1) != cloneList.get(1)
    }

    @Test
    void testDeserializationObject() {
        TestUserModel t1 = new TestUserModel()
        t1.setUserId(1)
        t1.setUserName("jimmy")

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream()
        ObjectOutputStream outputStream = new ObjectOutputStream(byteOut)
        outputStream.writeObject(t1)

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray())
        TestUserModel deserializationObject = (TestUserModel)SerializationUtil.deserializeObject(byteIn)
        assert deserializationObject.getUserId() == 1
        assert deserializationObject.getUserName() == "jimmy"
    }
}
