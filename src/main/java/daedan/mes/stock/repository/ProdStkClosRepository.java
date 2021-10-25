package daedan.mes.stock.repository;


import daedan.mes.stock.domain.ProdStkClos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;


public interface ProdStkClosRepository extends JpaRepository<ProdStkClos, Long> {
   ProdStkClos findByCustNoAndClosDtAndWhNoAndProdNoAndUsedYn(Long custNo,Date closDt, Long whNo, Long prodNo, String y);
}
