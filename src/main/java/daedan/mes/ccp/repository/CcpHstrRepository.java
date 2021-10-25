package daedan.mes.ccp.repository;

import daedan.mes.ccp.domain.CcpHstr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CcpHstrRepository extends JpaRepository<CcpHstr, Long> {

    CcpHstr findByCcpNo(Long ccpNo);
}
