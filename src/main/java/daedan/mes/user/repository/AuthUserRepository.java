package daedan.mes.user.repository;


import daedan.mes.user.domain.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    AuthUser findByCustNoAndUserIdAndAuthCd(Long custNo, long parseLong, Long authCd);
}
