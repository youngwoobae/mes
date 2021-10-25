package daedan.mes.ord.repository;

import daedan.mes.ord.domain.OrdProd;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrdProdRepository extends JpaRepository<OrdProd, Long> {
    OrdProd findByCustNoAndOrdNoAndProdNoAndUsedYn(Long custNo,Long ordNo, Long prodNo,String yn);
    OrdProd findByCustNoAndOrdNoAndProdNoAndDlvDtAndUsedYn(Long custNo,Long ordNo, Long prodNo, Date owdate, String y);
    OrdProd findByCustNoAndOrdProdNoAndUsedYn(Long custNo,Long ordProdNo, String y);
}
