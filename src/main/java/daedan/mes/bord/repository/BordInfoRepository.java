package daedan.mes.bord.repository;

import daedan.mes.bord.domain.BordInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BordInfoRepository extends JpaRepository<BordInfo, Long> {

    BordInfo findByCustNoAndBordNo(Long custNo, Long bordNo);
}
