package daedan.mes.ord.repository;

import daedan.mes.ord.domain.OrdInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface OrdRepository  extends JpaRepository<OrdInfo, Long> {
    OrdInfo findByCustNoAndOrdNoAndUsedYn(Long custNo,long ordNo,String yn);
    OrdInfo findByCustNoAndOrdNmAndUsedYn(Long custNo,String ordNm,String yn);
    OrdInfo findByCustNoAndCmpyNoAndOrdDtAndUsedYn(Long custNo,Long cmpyNo, Date closDt, String y);

    OrdInfo findByCustNoAndOrdNmAndOrdDtAndUsedYn(Long custNo,String ordNm, Date ordDt, String y);
}
