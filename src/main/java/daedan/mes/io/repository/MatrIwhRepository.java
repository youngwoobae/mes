package daedan.mes.io.repository;

import daedan.mes.io.domain.MatrIwh;
import daedan.mes.stock.domain.MatrStk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface MatrIwhRepository extends JpaRepository<MatrIwh, Long> {
    MatrIwh findByCustNoAndPursNoAndMatrNoAndUsedYn(Long custNo, Long pursNo, Long matrNo, String yn);

    MatrIwh findByCustNoAndIwhNoAndMatrNoAndPursNoAndPursMatrNo(Long custNo,Long iwhNo, Long matrNo, Long pursNo, Long pursMatrNo);


    MatrIwh findByCustNoAndMatrNoAndUsedYn(Long custNo,Long matrNo, String y);

    MatrIwh findByCustNoAndMatrNoAndWhNoAndPursNoAndPursMatrNoAndCmpyNoAndUsedYn(Long custNo,Long matrNo, Long iwhNo, Long pursNo, Long pursMatrNo, Long cmpyNo, String usedYn);

    MatrIwh findByCustNoAndPursNoAndMatrNoAndWhNoAndUsedYn(Long custNo,Long pursNo, Long matrNo, Long wh_no, String y);

    MatrIwh findByCustNoAndIwhNoAndUsedYn(Long custNo,Long iwh_no, String y);

    MatrIwh findByCustNoAndIwhDtAndMatrNoAndUsedYn(Long custNo,Date iwhDt, Long matrNo, String y);
    MatrStk deleteByIwhNo(Long iwhNo);
}
