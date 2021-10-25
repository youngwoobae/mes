package daedan.mes.equip.repository;

import daedan.mes.equip.domain.EquipInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipInfoRepository extends JpaRepository<EquipInfo, Long> {
    EquipInfo findByEquipNo(Long equipNo);
}
