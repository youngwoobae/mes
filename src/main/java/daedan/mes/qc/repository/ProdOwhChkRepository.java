package daedan.mes.qc.repository;

import daedan.mes.qc.domain.ProdOwhChk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdOwhChkRepository extends JpaRepository<ProdOwhChk, Long> {
    ProdOwhChk findByCustNoAndChkNoAndUsedYn(Long custNo, Long chkNo, String y);
}
