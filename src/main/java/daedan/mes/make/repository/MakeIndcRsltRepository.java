package daedan.mes.make.repository;

import daedan.mes.make.domain.MakeIndcRslt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface MakeIndcRsltRepository extends JpaRepository<MakeIndcRslt, Long> {
    MakeIndcRslt findByCustNoAndIndcNoAndUsedYn(Long custNo, Long indcNo,String yn);

    MakeIndcRslt findByCustNoAndIndcNoAndMakeDtAndUsedYn(Long custNo, Long indcNo, Date makeDt, String y);

    MakeIndcRslt findByCustNoAndIndcRsltNoAndUsedYn(Long custNo, Long indcRsltNo, String y);
}
