package daedan.mes.matr.repository;

import daedan.mes.matr.domain.MatrInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface MatrRepository extends JpaRepository<MatrInfo, Long>  {
    MatrInfo findByCustNoAndMatrNoAndUsedYn(Long custNo, Long MartNo, String Yn);
    MatrInfo findByCustNoAndItemCdAndMatrNmAndUsedYn(Long custNo, String itemCd, String matrNm, String yn);
    MatrInfo findByCustNoAndItemCdAndMatrNmAndSzAndUsedYn(Long custNo, String itemCd, String matrNm, String sz, String yn);
    MatrInfo findByCustNoAndMatrNmAndUsedYn(Long custNo, String matrNm, String yn);
    MatrInfo findByCustNoAndMatrNoAndMatrNmAndUsedYn(Long custNo, Long matrNo, String matrNm, String y);
    MatrInfo findByCustNoAndFileNoAndUsedYn(Long custNo, Long fileNo, String y);

    MatrInfo findByCustNoAndFileNoAndMatrNoAndUsedYn(Long custNo, Long fileNo, Long matrNo, String y);

}
