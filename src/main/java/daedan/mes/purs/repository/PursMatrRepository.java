package daedan.mes.purs.repository;

import daedan.mes.purs.domain.PursMatr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PursMatrRepository  extends JpaRepository<PursMatr, Long> {
    PursMatr findByCustNoAndPursNoAndMatrNoAndUsedYn(Long custNo,Long pursNo, Long matrNo,String yn);
    PursMatr findByCustNoAndPursMatrNoAndUsedYn(Long custNo,Long pursMatrNo, String usedYn);
    List<PursMatr> findAllByCustNoAndPursNoAndUsedYn(Long custNo, Long pursNo, String y);
}
