package daedan.mes.dash.repository;

import daedan.mes.dash.domain.RcvTmpr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RcvTmprRepository  extends JpaRepository<RcvTmpr, Long> {
    RcvTmpr findByCustNoAndEquipNoAndUsedYn(Long custNo, Long equipNo, String y);
}
