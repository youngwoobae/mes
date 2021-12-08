package daedan.mes.make.repository;

import daedan.mes.make.domain.MakeIndcProc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MakeIndcProcRepository extends JpaRepository<MakeIndcProc, Long> {
    MakeIndcProc findByCustNoAndIndcNoAndProcCdAndUsedYn(Long custNo, Long indcNo, Long procCd, String y);
}
