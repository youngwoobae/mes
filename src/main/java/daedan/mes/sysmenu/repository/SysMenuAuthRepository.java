package daedan.mes.sysmenu.repository;

import daedan.mes.sysmenu.domain.AuthSysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SysMenuAuthRepository  extends JpaRepository<AuthSysMenu, Long> {
    AuthSysMenu findByCustNoAndSysMenuNoAndAuthCd(Long custNo,Long sysMenuNo, Long authCd);
    void deleteByCustNoAndAuthCd(Long custNo, Long authCd);
}
