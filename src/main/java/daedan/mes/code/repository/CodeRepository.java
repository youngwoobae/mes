package daedan.mes.code.repository;

import daedan.mes.code.domain.CodeInfo;
//import daedan.mes.matr.domain.MatrInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<CodeInfo, Long> {
    CodeInfo findByParCodeNoAndCodeNmAndUsedYn(long parCodeNo, String codeNm,String yn);
    CodeInfo findByCodeNmAndUsedYn(String codeNm, String yn);
    CodeInfo findByCodeNoAndUsedYn(Long procCd, String y);
}
