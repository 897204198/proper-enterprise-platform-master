package com.proper.enterprise.platform.core.jpa.constraint

import com.proper.enterprise.platform.core.jpa.constraint.entity.FkEntity
import com.proper.enterprise.platform.core.jpa.constraint.entity.FkManyEntity
import com.proper.enterprise.platform.core.jpa.constraint.entity.UniqueEntity
import com.proper.enterprise.platform.core.jpa.constraint.repository.FkManyRepository
import com.proper.enterprise.platform.core.jpa.constraint.repository.FkRepository
import com.proper.enterprise.platform.core.jpa.constraint.repository.UniqueRepository
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class ConstraintViolationTest extends AbstractJPATest {

    @Autowired
    UniqueRepository uniqueRepository

    @Autowired
    FkRepository fkRepository

    @Autowired
    FkManyRepository fkManyRepository

    @Test
    @NoTx
    void uniqueSaveAndUpdateTest() {
        UniqueEntity uniqueEntity = new UniqueEntity()
        uniqueEntity.setCode("test")
        uniqueRepository.save(uniqueEntity)
        UniqueEntity uniqueEntity2 = new UniqueEntity()
        uniqueEntity2.setCode("test")
        try {
            uniqueRepository.save(uniqueEntity2)
        } catch (Exception e) {
            assert "code cant be repeat" == e.getMessage()
        }
        uniqueEntity2.setCode("test1")
        uniqueEntity2 = uniqueRepository.save(uniqueEntity2)
        uniqueEntity2.setCode("test")
        try {
            uniqueRepository.updateForSelective(uniqueEntity2)
        }
        catch (Exception e) {
            assert "code cant be repeat" == e.getMessage()
        }
    }

    @Test
    @NoTx
    void fkDelOneToOneTest() {
        FkEntity fkEntity = new FkEntity()
        fkEntity.setName("testFk")
        fkRepository.save(fkEntity)
        UniqueEntity uniqueEntity = new UniqueEntity()
        uniqueEntity.setFkEntity(fkEntity)
        uniqueEntity.setCode("testUq")
        uniqueRepository.save(uniqueEntity)
        try {
            fkRepository.deleteById(fkEntity.getId())
        } catch (Exception e) {
            assert "cant delete fk because have unique used" == e.getMessage()
        }
    }

    @Test
    @NoTx
    void fkDelManyToManyTest() {
        FkManyEntity fkManyEntity = new FkManyEntity()
        fkManyEntity.setName("testFk")
        fkManyRepository.save(fkManyEntity)
        List<FkManyEntity> fkManyEntities = new ArrayList<>()
        fkManyEntities.add(fkManyEntity)
        UniqueEntity uniqueEntity = new UniqueEntity()
        uniqueEntity.setFkManyEntities(fkManyEntities)
        uniqueEntity.setCode("testUqMany")
        uniqueRepository.save(uniqueEntity)
        try {
            fkManyRepository.deleteById(fkManyEntity.getId())
        } catch (Exception e) {
            assert "cant delete fk many because have unique used" == e.getMessage()
        }
    }
}
