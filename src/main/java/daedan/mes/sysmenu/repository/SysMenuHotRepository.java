package daedan.mes.sysmenu.repository;

import daedan.mes.sysmenu.domain.SysMenuHot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysMenuHotRepository extends JpaRepository<SysMenuHot, Long> {
    SysMenuHot findByCustNoAndSysMenuNoAndUserIdAndUsedYn(Long custNo, Long sysMenuNo, Long userId,String yn);

    void deleteByCustNoAndSysMenuNoAndUserIdAndUsedYn(Long custNo, Long sysMenuNo, Long userId,String yn);
}
