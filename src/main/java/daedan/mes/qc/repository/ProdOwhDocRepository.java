package daedan.mes.qc.repository;

import daedan.mes.qc.domain.ProdOwhDoc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdOwhDocRepository extends JpaRepository<ProdOwhDoc, Long> {
    ProdOwhDoc findByCustNoAndDocNoAndUsedYn(Long custNo, Long docNo, String y);
}
