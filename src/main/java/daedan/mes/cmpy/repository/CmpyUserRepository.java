package daedan.mes.cmpy.repository;

import daedan.mes.cmpy.domain.CmpyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CmpyUserRepository extends JpaRepository<CmpyUser, Long> {

}
