package daedan.mes.user.repository;

import daedan.mes.user.domain.AuthUserMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserMenuRepository extends JpaRepository<AuthUserMenu, Long> {
    AuthUserMenu findByCustNoAndAuthUserNoAndSysMenuNo(Long custNo,Long authUserNo, Long sysMenuNo);
}
