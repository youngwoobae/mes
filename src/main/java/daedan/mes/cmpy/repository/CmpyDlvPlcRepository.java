package daedan.mes.cmpy.repository;

import daedan.mes.cmpy.domain.CmpyDlvPlc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CmpyDlvPlcRepository extends JpaRepository<CmpyDlvPlc, Long> {
    CmpyDlvPlc findByCustNoAndPlcNoAndUsedYn(Long custNo, Long plcNo, String y);
    List<CmpyDlvPlc> findAllByCustNoAndCmpyNoAndUsedYn(Long custNo, Long cmpyNo, String y);
}
