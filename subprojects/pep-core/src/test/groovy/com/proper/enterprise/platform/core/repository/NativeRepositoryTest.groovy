package com.proper.enterprise.platform.core.repository

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class NativeRepositoryTest extends AbstractTest {

    @Autowired
    NativeRepository repo

    @Before
    public void setUp() {
        10.times { idx ->
            def sql = """
INSERT INTO pep_test_mock
(id, name, attr1)
VALUES
('$idx', 'name$idx', 'attr$idx')
"""
            repo.executeUpdate(sql)
        }
    }

    @Test
    public void commonQuery() {
        def sql = """
SELECT name, attr1
  FROM pep_test_mock
 WHERE name > 'name5'
"""
        def result = repo.executeQuery(sql)
        assert result.size() == 4
        Object[] objs = (Object[])result[0]
        assert objs.length == 2
        assert objs[0] == 'name6'
        assert objs[1] == 'attr6'
    }

    @Test
    public void namedQuery() {
        def sql = """
SELECT name, attr1
  FROM pep_test_mock
 WHERE name > :name
"""
        def result = repo.executeQuery(sql, ['name': 'name5'])
        assert result.size() == 4
        assert result[0].length == 2
        assert result[0][0] == 'name6'
        assert result[0][1] == 'attr6'
    }

    @Test
    public void namedUpdate() {
        def sql = """
UPDATE pep_test_mock
   SET attr1 = :attr
 WHERE id = :id
"""
        repo.executeUpdate(sql, ['attr': 'ATTR3', 'id': '3'])

        sql = """
SELECT attr1
  FROM pep_test_mock
 WHERE id = :id
"""
        def result = repo.executeQuery(sql, ['id': '3'])
        assert result[0] == 'ATTR3'

    }

}
