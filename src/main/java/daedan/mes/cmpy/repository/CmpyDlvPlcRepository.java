package daedan.mes.cmpy.repository;

import daedan.mes.cmpy.domain.CmpyDlvPlc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CmpyDlvPlcRepository extends JpaRepository<CmpyDlvPlc, Long> {
    CmpyDlvPlc findByCustNoAndPlcNoAndUsedYn(Long custNo, Long plcNo, String y);
}
