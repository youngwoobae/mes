package daedan.mes.pqms.repository;

import daedan.mes.pqms.domain.OrdRecv;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdRecvRepository extends JpaRepository<OrdRecv, Long> {
    OrdRecv findByCustNoAndSendUtAndReqUtAndProdNoAndUsedYn(Long custNo, Long sendDt, Long reqUt, Long prodNo, String y);
}
