package daedan.mes.cmmn.io.datamgr.errdata;

import daedan.mes.common.error_handle.CustomErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class ErrDataController {
    @Autowired
    ErrDataService errDataService;

    @Autowired
    ErrDataMapper errDataMapper;

    /**
     * 실시간 데이터를 검증코드별로 분기하여 이탈검증
     *
     * param ListMap 조회 파라미터
     * return void
     */
    public void errDataStartVerify(List<Map<String, Object>> paramList) {
        try {
            if(null != paramList && paramList.size() > 0) {
                switch(paramList.get(0).get("verfTypeCode").toString()){
                    // 가열지속(온도, 속도)
                    case "H01" :
                        errDataService.heatingContinuing(paramList);
                        break;
                    // 가열이벤트(온도, 공정시간)
                    case "H02" :
                        errDataService.heatingEvent(paramList);
                        break;
                    // 가열품온(온도)
                    case "H03" :
                        errDataService.heatingMaterial(paramList);
                        break;
                    // 가열압력이벤트(온도, 압력, 공정시간)
                    case "H04" :
                        errDataService.heatingPressureEvent(paramList);
                        break;
                    // 냉장냉동(온도)
                    case "C01" :
                        errDataService.coldStorageFrozen(paramList);
                        break;
                    // 금속검출(1,2)
                    case "D01" :
                        errDataService.metalDetection(paramList);
                        break;
                }
            }
        } catch (SQLException e) {
            CustomErrorHandler.handle(this.getClass().getSimpleName(), e);
        } catch (ParseException e) {
            CustomErrorHandler.handle(this.getClass().getSimpleName(), e);
        }
    }

    /**
     * 수집데이터를 일지상세데이터 및 일지데이터 테이블에 등록
     *
     * param ListMap 조회 파라미터
     * return void
     */
    public void monDataInsert(List<Map<String, Object>> paramList) {
        try {
            errDataService.monDataInsert(paramList);
        } catch (SQLException e) {
            CustomErrorHandler.handle(this.getClass().getSimpleName(), e);
        }
    }

}
