package daedan.mes.role.repository;

import daedan.mes.role.domain.RoleInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleInfoRepository extends JpaRepository<RoleInfo, Long> {
    RoleInfo findByCustNoAndSysMenuNoAndRoleCodeAndUsedYn(Long custNo,Long sysMenuNo, Long roleCode,String yn);

    RoleInfo findByCustNoAndSysMenuNoAndRoleNoAndUsedYn(Long custNo,Long sysMenuNo, Long roleNo,String yn);
}
