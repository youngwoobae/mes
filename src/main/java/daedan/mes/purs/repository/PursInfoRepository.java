package daedan.mes.purs.repository;

import daedan.mes.purs.domain.PursInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface PursInfoRepository extends JpaRepository<PursInfo, Long> {
    PursInfo findByCustNoAndPursDtAndDlvReqDtAndDlvDtAndUsedYn(Long custNo,Date pursDt, Date dlvReqDt, Date dlvDt, String y);
    PursInfo findByCustNoAndPursNoAndUsedYn(Long custNo,Long pursNo, String usedYn);
    PursInfo findByCustNoAndIndcNoAndUsedYn(Long custNo,Long IndcNo, String userYn);

    PursInfo findByPursNoAndUsedYn(Long pursNo, String y);
}
