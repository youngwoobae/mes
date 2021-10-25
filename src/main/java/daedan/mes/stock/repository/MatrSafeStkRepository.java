package daedan.mes.stock.repository;

import daedan.mes.stock.domain.MatrSafeStk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatrSafeStkRepository extends JpaRepository<MatrSafeStk, Long> {
    MatrSafeStk findByCustNoAndMatrNoAndUsedYn(Long custNo, Long matrNo,String yn);
}
