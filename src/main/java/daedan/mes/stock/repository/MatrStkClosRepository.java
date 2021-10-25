package daedan.mes.stock.repository;


import daedan.mes.stock.domain.MatrStkClos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface MatrStkClosRepository extends JpaRepository<MatrStkClos, Long> {

    MatrStkClos findByCustNoAndMatrNoAndUsedYn(Long custNo,Long matrNo, String y);
    MatrStkClos findByCustNoAndMatrNoAndClosDtAndUsedYn(Long custNo,Long matrNo, Date date, String y);
    MatrStkClos findByCustNoAndClosDtAndWhNoAndMatrNoAndUsedYn(Long custNo,Date closDt, Long whNo, Long matrNo, String y);

}
