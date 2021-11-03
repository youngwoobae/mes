package daedan.mes.spot.repository;


import daedan.mes.spot.domain.SpotInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpotInfoRepository extends JpaRepository<SpotInfo, Long> {
    SpotInfo findByCustNoAndSpotNoAndUsedYn(Long custNo, Long spotNo, String y);
    SpotInfo findAllByCustNoAndSpotNoAndUsedYn(Long custNo, Long spotNo, String y);
}
