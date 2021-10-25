package daedan.mes.equip.repository;

import daedan.mes.equip.domain.EquipMatr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipMatrRepository extends JpaRepository<EquipMatr, Long> {
    EquipMatr findByMatrNo(Long matrNo);

}
