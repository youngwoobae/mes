package daedan.mes.equip.repository;

import daedan.mes.equip.domain.EquipMast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipMastRepository extends JpaRepository<EquipMast, Long> {
    EquipMast findByCustNoAndEquipMastNoAndUsedYn(Long custNo, long equip_mast_no, String y);
}
