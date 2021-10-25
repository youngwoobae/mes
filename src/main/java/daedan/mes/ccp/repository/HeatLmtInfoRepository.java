package daedan.mes.ccp.repository;

import daedan.mes.ccp.domain.HeatLmtInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public interface HeatLmtInfoRepository extends JpaRepository<HeatLmtInfo, Long> {
    Map<String,Object> findById(long lmtNo);
    HeatLmtInfo findByLmtNo(Long lmtNo);
    HeatLmtInfo findByHeatTpAndUsedYn(Long heatTp, String y);
}
