package daedan.mes.dash.repository;

import daedan.mes.dash.domain.EvtMsg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvtMsgRepository extends JpaRepository<EvtMsg, Long> {

    EvtMsg findByUnixHms(String unixHms);
}
