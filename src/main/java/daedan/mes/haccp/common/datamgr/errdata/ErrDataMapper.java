package daedan.mes.haccp.common.datamgr.errdata;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;
@Mapper
@Component
public interface ErrDataMapper {
    void updateRecCCpEventFlag(Map<String, Object> stringObjectMap);

    void insertShcRecCcpDesc(Map<String, Object> stringObjectMap);

    void insertShcRecCcpImprv(Map<String, Object> paramMap);

    void insertShcRecCcpAlarm(Map<String, Object> paramMap);

    void updateAlramIdShcRecCcpImprv(Map<String, Object> paramMap);

    void insertShcSensorData(Map<String, Object> paramMap);

    Map<String, Object> getEventSeqMdTest(Map<String, Object> stringObjectMap);

    Map<String, Object> getEventSeq(Map<String, Object> stringObjectMap);

    Map<String, Object> selectShcRecCcpImprv(Map<String, Object> stringObjectMap);
}
