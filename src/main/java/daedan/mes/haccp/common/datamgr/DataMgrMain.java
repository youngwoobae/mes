package daedan.mes.haccp.common.datamgr;

import daedan.mes.haccp.common.datamgr.io.server.SockServer;
import daedan.mes.haccp.common.datamgr.mondata.MonDataMapper;
import daedan.mes.haccp.common.datamgr.rawdata.RawDataController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DataMgrMain implements ApplicationRunner, DisposableBean {

    //private static final Logger LOGGER = LoggerFactory.getLogger(DataMgrMain.class);

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    MonDataMapper monDataMapper ;

    /**
     * serverSocket, 공통 Queue, 데이터 수집 쓰레드를 관리
     *
     * @return void
     */

    @Override
    public void run(ApplicationArguments args) {

        /* 잠시대기 : 프로그램이 serverSocket일 경우
        Runnable rServerSocket = new SockServer();
        Thread tServerSocket = new Thread(rServerSocket);
        tServerSocket.setDaemon(true);
        tServerSocket.setName("serverSocket");
        tServerSocket.start();
        */

        /*잠시대기 : 서버 소켓으로 수신된 데이터가 담긴 큐를 poll하는 쓰레드
        Runnable rRawData = new RawDataController();
        Thread tRawData = new Thread(rRawData);
        tRawData.setDaemon(true);
        tRawData.setName("threadDemon");
        tRawData.start();
        */

        /* 품온, 금속검출을 제외한 데이터 수집 설비 목록 조회
        List<Map<String, Object>> mondataSensorList = new ArrayList<Map<String, Object>>();
        mondataSensorList = monDataMapper.selectMondataEquipList();

        if(mondataSensorList.size() > 0) {

            String mCode;
            // 수집 설비들에 대해서 각 각 쓰레드를 생성
            for(int i = 0; i < mondataSensorList.size(); i++) {

                mCode = (String) mondataSensorList.get(i).get("M_CODE");
                // SHC_CCP_EQUIP.mCode
                 //mCode = CH01 : CCP가열기1 (192.168.0.7:502)
                 //mCode = CC01 : 냉장창고1  (127.0.0.1:9085)
                 //mCode = CD01 : 금속검출기1 (127.0.0.1 :9089)
                Runnable rMonData = new MonDataController(mCode);
                Thread tMonData = new Thread(rMonData);
                tMonData.setDaemon(true);
                tMonData.setName(mCode);
                tMonData.start();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    CustomErrorHandler.handle(this.getClass().getSimpleName(), e);
                }
            }
        }
         */
    }
    /**
     * 프로그램 종료 시 CCP 쓰레드 모두 종료
     *
     * @return void
     */
    @Override
    public void destroy() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        List<Map<String, Object>> mondataSensorList = new ArrayList<Map<String, Object>>();
        mondataSensorList = monDataMapper.selectMondataEquipList();
        if(mondataSensorList.size() > 0) {
            for(int i = 0; i < mondataSensorList.size(); i++) {
                String mCode = (String) mondataSensorList.get(i).get("mCode");
                for (Thread t : threadSet) {
                    if ( t.getThreadGroup() == Thread.currentThread().getThreadGroup()) {
                        if(("serverSocket".equals(t.getName()))) {
                            t.interrupt();
                        }
                        if(("threadDemon".equals(t.getName()))) {
                            t.interrupt();
                        }
                        if((mCode.equals(t.getName()))) {
                            t.interrupt();
                        }
                    }
                }
            }
        }
    }
}
