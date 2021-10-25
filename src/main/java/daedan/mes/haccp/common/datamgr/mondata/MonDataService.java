package daedan.mes.haccp.common.datamgr.mondata;

import daedan.mes.haccp.common.datamgr.errdata.ErrDataController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MonDataService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    MonDataMapper mapper;

    private Environment environment;

    /**
     * DB에서 마지막으로 축적된 데이터이면서 수집되지않은 데이터를 수집하여 일지상세데이터 및 일지데이터 테이블에 등록
     *
     * @param paramMap
     * @return void
     * @throws SQLException
     */

    public void readMonData(Map<String, Object> paramMap) throws ParserConfigurationException, SAXException, IOException, SQLException {
        log.info("readMonData.paraMap = " + paramMap.toString());
        readMonDataDB(paramMap);
    }

    /**
     * DB에서 마지막으로 축적된 데이터이면서 수집되지않은 데이터를 수집하여 일지상세데이터 및 일지데이터 테이블에 등록
     *
     * param paramMap
     * return void
     * throws SQLException
     */

     public void readMonDataDB(Map<String, Object> paramMap) throws SQLException {
        List<Map<String, Object>> monDataList =  mapper.selectLastMonData(paramMap);

        if(null != monDataList && monDataList.size() > 0) {
            ErrDataController errDataController = new ErrDataController();
            errDataController.monDataInsert(monDataList);
        }
    }
}
