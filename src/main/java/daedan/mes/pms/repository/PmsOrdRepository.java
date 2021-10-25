package daedan.mes.pms.repository;

import daedan.mes.pms.domain.PmsOrd;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PmsOrdRepository extends JpaRepository<PmsOrd, Long> {
    PmsOrd findByOrdNoAndUsedYn(long ordr_no, String y);
}
