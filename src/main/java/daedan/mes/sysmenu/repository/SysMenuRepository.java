package daedan.mes.sysmenu.repository;

import daedan.mes.sysmenu.domain.SysMenu;
import daedan.mes.sysmenu.domain.sysMenuCust;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysMenuRepository extends JpaRepository<SysMenu, Long> {

    SysMenu findByCustNoAndSysMenuNoAndUsedYn(Long custNo,Long sysMenuNo, String y);
    SysMenu findByCustNoAndMakeSeq(Long custNo, Integer makeIdx);

    void delete(SysMenu custvo);
    SysMenu findByCustNo(Long custNo);
}
