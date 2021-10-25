package daedan.mes.spot.repository;

import daedan.mes.spot.domain.SpotEquip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotEquipRepository extends JpaRepository<SpotEquip, Long> {
    SpotEquip findByCustNoAndSpotNoAndEquipNoAndUsedYn(Long custNo,Long spotNo, Long equipNo, String y);
    SpotEquip findByCustNoAndSpotEquipNoAndUsedYn(Long custNo,Long spotEquipNo, String y);

}
