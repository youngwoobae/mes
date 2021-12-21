package daedan.mes.sysmenu.user.repository;


import daedan.mes.sysmenu.user.domain.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    AuthUser findByCustNoAndUserIdAndAuthCd(Long custNo, long parseLong, Long authCd);
}
