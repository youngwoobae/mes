package daedan.mes.stock.repository;

import daedan.mes.stock.domain.MatrStkHstr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatrStkHstrRepository extends JpaRepository<MatrStkHstr, Long> {

    MatrStkHstr findByCustNoAndWhNoAndMatrNoAndUsedYn(Long custNo,Long trkWhNo, Long matrNo, String y);

    MatrStkHstr findByCustNoAndChngNoAndUsedYn(Long custNo,Long matrRealNo, String y);
}
