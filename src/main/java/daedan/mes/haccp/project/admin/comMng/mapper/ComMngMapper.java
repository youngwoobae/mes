package daedan.mes.haccp.project.admin.comMng.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface ComMngMapper {
    List<Map<String, Object>> selectCodeList(Map<String, Object> param);

    Map<String, Object> companyCnt(Map<String, Object> param);

    Map<String, Object>  selectCompanyInfo(Map<String, Object> param);

    Map<String, Object> insertCompany(Map<String, Object> param);

    int updateCompany(Map<String, Object> param);

    List<Map<String, Object>> selectItmtypM(Map<String, Object> param);

    List<Map<String, Object>> selectItmtypS(Map<String, Object> param);

    List<Map<String, Object>> selectCompanyItemList(String lcnsNo);

    Map<String, Object> checkItemInfo(Map<String, Object> param);

    void insertCompanyItem(Map<String, Object> param);

    void updateCompanyItem(Map<String, Object> param);

    Object selectItemInfo(Map<String, Object> param);

    void deleteItemInfo(Map<String, Object> param);

    void mergeCompanyInfo(Map<String, Object> resultLcnsMap);

    void deleteOldCompanyInfo(String lcnsNo);

    void deleteOldCompanyItemInfo(String lcnsNo);
}
