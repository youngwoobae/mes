package daedan.mes.make.repository;

import daedan.mes.make.domain.MakeIndc;
import daedan.mes.make.domain.MakeIndcMp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface MakeMpRepository extends JpaRepository<MakeIndcMp, Long> {
    MakeIndcMp findByCustNoAndUserIdAndMpUsedDtAndUsedYn(Long custNo, Long userId, Date mpUsedDt, String yn);
    MakeIndcMp findByCustNoAndIndcNoAndUserIdAndUsedYn(Long custNo, Long indcNo, long userId, String y);
    MakeIndcMp findByCustNoAndIndcMpNoAndUsedYn(Long custNo, Long indcMpNo, String y);
}
