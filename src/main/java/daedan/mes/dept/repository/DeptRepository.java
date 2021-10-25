package daedan.mes.dept.repository;

import daedan.mes.dept.domain.DeptInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptRepository extends JpaRepository<DeptInfo, Long> {

    DeptInfo findByCustNoAndDeptNmAndUsedYn(Long custNo, String deptNm,String usedYn);
}
