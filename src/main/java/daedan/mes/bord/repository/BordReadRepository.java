package daedan.mes.bord.repository;

import daedan.mes.bord.domain.BordRead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BordReadRepository extends JpaRepository<BordRead, Long> {
    BordRead findByCustNoAndBordNoAndUserId(Long custNo, Long userId, Long bordNo);
}
