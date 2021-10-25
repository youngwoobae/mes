package daedan.mes.ccp.repository;

import daedan.mes.ccp.domain.CcpGuide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CcpGuideRepository extends JpaRepository<CcpGuide, Long> {
    CcpGuide findByCcpNo(Long ccpNo);
    CcpGuide findByProcCdAndCcpCdAndUsedYn(Long procCd, String ccpCd, String y);
}
