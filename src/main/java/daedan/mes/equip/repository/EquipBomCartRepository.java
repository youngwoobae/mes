package daedan.mes.equip.repository;

import daedan.mes.equip.domain.EquipBomCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipBomCartRepository extends JpaRepository<EquipBomCart, Long> {

    void deleteByCustNoAndEquipNoAndMatrNoAndUserId(Long custNo, Long equipNo, Long matrNo, Long userId);
    EquipBomCart findByCustNoAndEquipNoAndMatrNo(Long custNo, Long equipNo, Long matrNo);
}
