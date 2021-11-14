package daedan.mes.ord.repository;

import daedan.mes.ord.domain.OrdInfo18;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Ord18Repository extends JpaRepository<OrdInfo18, Long> {

    OrdInfo18 findByCustNoAndOrdMhNoAndUsedYn(Long custNo, Long ordMhNo, String y);
}
