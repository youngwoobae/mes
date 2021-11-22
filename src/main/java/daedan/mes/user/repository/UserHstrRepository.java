package daedan.mes.user.repository;


import daedan.mes.user.domain.UserWork;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHstrRepository extends JpaRepository<UserWork, Long> {
    UserWork findByCustNoAndWorkNoAndUsedYn(Long custNo, Long workNo, String y);
}
