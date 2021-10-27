package daedan.mes.qc.repository;

import daedan.mes.qc.domain.MatrIwhDoc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatrIwhDocRepository extends JpaRepository<MatrIwhDoc, Long> {
    MatrIwhDoc findByCustNoAndDocNoAndUsedYn(Long custNo, Long docNo, String y);
}
