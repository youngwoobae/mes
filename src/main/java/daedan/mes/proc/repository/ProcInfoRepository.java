package daedan.mes.proc.repository;

import daedan.mes.proc.domain.ProcInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProcInfoRepository extends JpaRepository<ProcInfo, Long> {
    ProcInfo findByCustNoAndProcCdAndUsedYn(Long custNo, long procCd, String y);
}
