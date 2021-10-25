package daedan.mes.modbus.repository;

import daedan.mes.modbus.domain.MobileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MobileInfoRepository extends JpaRepository<MobileInfo, Long> {
    MobileInfo findByCustNoAndDeviceIpAndDevicePortAndDeviceNoAndUsedYn(Long custNo,String deviceIp, Integer devicePort, Integer deviceNo, String y);
    MobileInfo findByCustNoAndMoblNoAndUsedYn(Long custNo,Long moblNo, String y);
}
