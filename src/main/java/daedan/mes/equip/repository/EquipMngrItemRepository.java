package daedan.mes.equip.repository;

import daedan.mes.equip.domain.EquipMngrItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipMngrItemRepository extends JpaRepository<EquipMngrItem, Long> {
    EquipMngrItem findByEquipNoAndMngrItem(Long equipNo, Long mngrItem);
}
