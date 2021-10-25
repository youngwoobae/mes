package daedan.mes.haccp.common.datamgr.io;

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
