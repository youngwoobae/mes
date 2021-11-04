package daedan.mes.stock.repository;

import daedan.mes.stock.domain.ProdStk;
import daedan.mes.stock.domain.ProdStkClos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface ProdStkRepository  extends JpaRepository<ProdStk, Long> {
    ProdStk findByCustNoAndWhNoAndProdNoAndUsedYn(Long custNo, Long whNo, Long prodNo, String y);
    ProdStk findByCustNoAndStkNoAndUsedYn(Long custNo, Long stkNo, String usedYn);
    ProdStk findByCustNoAndProdNoAndUsedYn(Long custNo, Long prodNo, String y);
}