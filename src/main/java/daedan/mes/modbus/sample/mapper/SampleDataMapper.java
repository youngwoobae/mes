package daedan.mes.modbus.sample.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Mapper
@Component
public interface SampleDataMapper {

    Long getHstrTime(Map<String, Object> paraMap);
}
