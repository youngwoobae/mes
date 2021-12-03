package daedan.mes.dash.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface DashMapper {

    List<Map<String, Object>>getToDayProduction(Map<String, Object> paraMap);

    List<Map<String, Object>> getToDayQuality(Map<String, Object> paraMap);

    List<Map<String, Object>> ToDayStoreQuality(Map<String, Object> paraMap);

    List<Map<String, Object>> getGraphData(Map<String, Object> paraMap);

    List<Map<String, Object>> getScadaTmprSpotList(Map<String, Object> paraMap);

    List<Map<String, Object>> getScadaTmprList(Map<String, Object> paraMap);

    List<Map<String, Object>> getDashTotalMakeIndc(Map<String, Object> paraMap);

    List<Map<String, Object>> getDashTotalMakeIndcRslt(Map<String, Object> paraMap);


    List<Map<String, Object>> getUtilsAndTransAndErrorList(Map<String, Object> paraMap);

    List<Map<String, Object>> getPhoneDashPreMonthList(Map<String, Object> paraMap);

    List<Map<String, Object>> getPhoneDashMonthList(Map<String, Object> paraMap);

    List<Map<String, Object>> getPhoneDashRsltInfo(Map<String, Object> paraMap);

    int getPhoneDashRsltInfoCount(Map<String, Object> paraMap);


    List<Map<String, Object>> getMetalLog(Map<String, Object> paraMap);

    int getMetalLogCount(Map<String, Object> paraMap);
}
