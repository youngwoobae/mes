package daedan.mes.stock.repository;


import daedan.mes.stock.domain.StkClos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface StkClosRepository extends JpaRepository<StkClos, Long> {
    StkClos findByCustNoAndClosDtAndProdNo(Long custNo, Date closDt,  Long prodNo);
    StkClos findByCustNoAndClosDtAndMatrNo(Long custNo, Date closDt, Long matrNo);
}
