package daedan.mes.stock.repository;

import daedan.mes.stock.domain.MatrSumStk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatrSumStkRepository  extends JpaRepository<MatrSumStk, Long> {
    MatrSumStk findByCustNoAndMatrNoAndUsedYn(Long custNo,Long matrNo,String yn);
}
