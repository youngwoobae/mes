package daedan.mes.equip.repository;

import daedan.mes.equip.domain.EquipBom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipBomRepository extends JpaRepository<EquipBom, Long> {
    EquipBom findByCustNoAndEquipNoAndMatrNo(Long custNo, Long equipNo, Long matrNo);
    void deleteByCustNoAndEquipNoAndMatrNo(Long custNo, Long equipNo, Long matrNo);
}
