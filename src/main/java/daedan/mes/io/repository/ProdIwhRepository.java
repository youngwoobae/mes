package daedan.mes.io.repository;

import daedan.mes.io.domain.ProdIwh;
import daedan.mes.io.domain.ProdOwh;
import daedan.mes.stock.domain.ProdStk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface ProdIwhRepository extends JpaRepository<ProdIwh, Long> {
    ProdIwh findByCustNoAndIwhDtAndProdNoAndUsedYn(Long custNo, Date iwhDt, Long prodNo, String y);
    ProdIwh findByCustNoAndIndcRsltNoAndUsedYn(Long custNo, Long indcRsltNo, String usedYn);
    ProdIwh findByCustNoAndIwhNoAndUsedYn(Long custNo, Long iwhNo, String n);
}
