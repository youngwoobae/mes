package daedan.mes.spot.repository;


import daedan.mes.spot.domain.SpotInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotInfoRepository extends JpaRepository<SpotInfo, Long> {
    SpotInfo findByCustNoAndSpotNoAndUsedYn(Long custNo, Long spotNo, String y);
}
