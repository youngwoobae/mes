package daedan.mes.stock.repository;


import daedan.mes.stock.domain.MatrStk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatrStkRepository extends JpaRepository<MatrStk, Long> {

    MatrStk findByCustNoAndMatrNoAndUsedYn(Long custNo,Long matrNo,String yn);
    MatrStk findByCustNoAndWhNoAndMatrNoAndUsedYn(Long custNo,Long whNo, Long matrNo,String yn);

    MatrStk findByCustNoAndWhNoAndMatrNoAndStkQtyAndUsedYn(Long custNo,Long whNo, Long matrNo, Float stkQty, String usedYn);

    MatrStk findByCustNoAndMatrStkNo(Long custNo,Long matrStkNo);


    MatrStk findByCustNoAndMatrNoAndWhNoAndUsedYn(Long custNo,Long matrNo, Long whNo, String y);

    MatrStk findByCustNoAndMatrStkNoAndWhNoAndUsedYn(Long custNo,Long matrStkNo, Long whNo, String y);

    MatrStk findByCustNoAndMatrStkNoAndUsedYn(Long custNo,Long matrStkNo, String y);
}
