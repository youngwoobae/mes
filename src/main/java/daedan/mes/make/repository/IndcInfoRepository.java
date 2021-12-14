package daedan.mes.make.repository;

import daedan.mes.make.domain.IndcInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndcInfoRepository  extends JpaRepository<IndcInfo, Long> {
    IndcInfo findByCustNoAndIndcNoAndUsedYn(Long custNo, Long indcNo, String y);
}
