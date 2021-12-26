package daedan.mes.stock.repository;

import daedan.mes.stock.domain.ProdStkHstr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdStkHstrRepository   extends JpaRepository<ProdStkHstr, Long> {
    ProdStkHstr findByCustNoAndWhNoAndProdNoAndUsedYn(Long custNo,Long chngWhNo, Long prodNo, String usedYn);
}
