package daedan.mes.stock.repository;

import daedan.mes.stock.domain.MatrPos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatrPosRepository extends JpaRepository<MatrPos, Long> {

    MatrPos findByCustNoAndMatrPosNoAndStairIdxAndRowIdxAndColIdxAndUsedYn(Long custNo,Long matrPosNo, Integer stairIdx, Integer rowIdx, Integer colIdx, String usedYn);

    MatrPos findByCustNoAndMatrNoAndIwhSeqAndUsedYn(Long custNo,Long matrNo, Integer iwhSeq, String y);

    MatrPos findByCustNoAndMatrNoAndWhNoAndStairIdxAndColIdxAndRowIdxAndIwhSeq(Long custNo,Long matrNo, Long whNo, Integer stairIdx, Integer colIdx, Integer rowIdx, Integer iwhSeq);

    MatrPos findByCustNoAndMatrNoAndIwhSeqAndMatrStatAndUsedYn(Long custNo,Long matrNo, Integer iwhSeq, Long matrStat, String usedYn);

    MatrPos findByCustNoAndWhNoAndMatrNoAndStairIdxAndRowIdxAndColIdxAndIwhSeqAndMatrStatAndUsedYn(Long custNo,Long whNo, Long matrNo, Integer stairIdx, Integer rowIdx, Integer colIdx, Integer iwhSeq, Long matrStat, String usedYn);

    MatrPos findByCustNoAndMatrNoAndWhNoAndStairIdxAndColIdxAndRowIdxAndMatrStatAndUsedYn(Long custNo,Long matrNo, Long whNo, Integer stairIdx, Integer colIdx, Integer rowIdx, Long matrStat, String usedYn);

    MatrPos findByCustNoAndMatrNoAndIwhSeqAndMatrStatAndStairIdxAndRowIdxAndColIdxAndUsedYn(Long custNo,Long matrNo, Integer iwhSeq, Long matrStat, Integer stairIdx, Integer rowIdx, Integer colIdx, String usedYn);
}
