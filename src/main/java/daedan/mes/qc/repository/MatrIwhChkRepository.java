package daedan.mes.qc.repository;

import daedan.mes.qc.domain.MatrIwhChk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatrIwhChkRepository extends JpaRepository<MatrIwhChk, Long> {
    MatrIwhChk findByCustNoAndChkTpAndChkMthAndUsedYn(Long custNo,Long chkTp, Long chkMth, String y);
    MatrIwhChk findByCustNoAndChkNoAndUsedYn(Long custNo, long chkNo, String y);
}
