package daedan.mes.cmmn.io.socket;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Mapper
@Component
public interface SocketIoMapper {
    Map<String, Object> selectVerfInfo(Map<String, Object> paramMap);
    List<Map<String, Object>> selectEquipInfo(String mCode);
}
