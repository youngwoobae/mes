package daedan.mes.proc.repository;

import daedan.mes.make.domain.MakeIndcProc;
import daedan.mes.proc.domain.ProcBrnch;
import daedan.mes.proc.domain.ProcInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcBrnchRepository  extends JpaRepository<ProcBrnch, Long> {
    ProcBrnch findByCustNoAndProcBrnchNoAndUsedYn(Long custNo,long procBrnchNo, String y);
    ProcBrnch findByCustNoAndBrnchNoAndProcCdAndUsedYn(Long custNo,Long brnchNo, Long procCd, String y);
    List<ProcBrnch> findAllByCustNoAndBrnchNoAndUsedYnOrderByProcSeq(Long custNo, Long brnchNo, String y);
    List<ProcInfo> findAllByCustNoAndBrnchNoAndUsedYn(Long custNo, Long brnchNo, String y);
}
