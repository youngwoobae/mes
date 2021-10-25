package daedan.mes.equip.repository;

import daedan.mes.equip.domain.EquipMngrHstr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipMngrHstrRepository extends JpaRepository<EquipMngrHstr, Long> {
    EquipMngrHstr findBySpotEquipNoAndUnixHms(Long spotEquipNo, Long unixHms);
}
