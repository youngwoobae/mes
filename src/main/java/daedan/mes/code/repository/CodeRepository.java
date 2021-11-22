package daedan.mes.code.repository;

import daedan.mes.code.domain.CodeInfo;
//import daedan.mes.matr.domain.MatrInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<CodeInfo, Long> {
    CodeInfo findByCodeNmAndUsedYn(String codeNm, String yn);
    CodeInfo findByCodeNoAndUsedYn(Long codeNo, String y);
    CodeInfo findByParCodeNoAndCodeNmAndUsedYn(Long fileExt, String toUpperCase, String y);

    CodeInfo findByCustNoAndCodeNoAndUsedYn(Long custNo, Long codeNo, String y);
}
