package daedan.mes.stock.repository;

import daedan.mes.stock.domain.ProdStkLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProdStkLotRepository  extends JpaRepository<ProdStkLot, Long> , QuerydslPredicateExecutor<ProdStkLot> {
    ProdStkLot findByCustNoAndWhNoAndProdNoAndStkUtAndUsedYn(Long custNo, Long whNo, Long prodNo, Long stkUt, String y);
}
