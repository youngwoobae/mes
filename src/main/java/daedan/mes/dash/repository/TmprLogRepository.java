package daedan.mes.dash.repository;

import daedan.mes.dash.domain.TmprLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmprLogRepository extends JpaRepository<TmprLog, Long> {
    TmprLog findByCustNoAndSpotNoAndUsedYn(Long custNo, Long spotNo, String y);
}
