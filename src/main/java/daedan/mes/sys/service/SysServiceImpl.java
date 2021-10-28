package daedan.mes.sys.service;

import daedan.mes.common.service.util.DateUtils;
import daedan.mes.haccp.common.datamgr.io.server.SockServer;
import daedan.mes.haccp.common.datamgr.rawdata.RawDataController;
import daedan.mes.haccp.common.error_handle.CustomErrorHandler;
import daedan.mes.sys.mapper.SysMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import daedan.mes.io.domain.ProdOwh;
import daedan.mes.io.domain.ProdIwh;
import daedan.mes.stock.domain.ProdStk;
import daedan.mes.make.domain.MakeIndcRslt;


import daedan.mes.io.repository.ProdOwhRepository;
import daedan.mes.io.repository.ProdIwhRepository;
import daedan.mes.stock.repository.ProdStkRepository;
import daedan.mes.make.repository.MakeIndcRsltRepository;

import javax.transaction.Transactional;
import java.util.*;

@Service("sysService")
public class SysServiceImpl implements daedan.mes.sys.service.SysService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    //@Autowired
    //MonDataMapper monDataMapper ;

    @Autowired
    private SysMapper mapper;

    @Autowired
    private ProdStkRepository prodStkRepo;

    @Autowired
    private ProdOwhRepository prodOwhRepo;

    @Autowired
    private ProdIwhRepository prodIwhRepo;

    @Autowired
    private MakeIndcRsltRepository makeIndcRsltRepo;

    @Override
    public List<Map<String, Object>> getOrdList(Map<String, Object> paraMap) {
        String tag = "sysService.getOrdList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getOrdList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getOrdProdList(Map<String, Object> paraMap) {
        String tag = "sysService.getOrdProdList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getOrdProdList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getIndcList(Map<String, Object> paraMap) {
        String tag = "sysService.getIndcList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getIndcList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getIndcMatrList(Map<String, Object> paraMap) {
        String tag = "sysService.getIndcMatrList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getIndcMatrList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getPursList(Map<String, Object> paraMap) {
        String tag = "sysService.getPursList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getPursList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getPursMatrList(Map<String, Object> paraMap) {
        String tag = "sysService.getPursMatrList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getPursMatrList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrIwhList(Map<String, Object> paraMap) {
        String tag = "sysService.getMatrIwhList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getMatrIwhList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrOwhList(Map<String, Object> paraMap) {
        String tag = "sysService.getMatrOwhList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getMatrOwhList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getIndcRsltList(Map<String, Object> paraMap) {
        String tag = "sysService.getIndcRsltList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getIndcRsltList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdIwhList(Map<String, Object> paraMap) {
        String tag = "sysService.getProdIwhList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProdIwhList(paraMap);
    }
    @Override
    public List<Map<String, Object>> getProdOwhList(Map<String, Object> paraMap) {
        String tag = "sysService.getProdIwhList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProdOwhList(paraMap);
    }
    @Override
    public List<Map<String, Object>> getResultIndcRslt(Map<String, Object> paraMap){
        String tag = "SysService.getResultIndcRslt => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getResultIndcRslt(paraMap);
    }

    @Override
    public List<Map<String, Object>> getResultProdIwhList(Map<String, Object> paraMap){
        String tag = "SysService.getResultProdIwhList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getResultProdIwhList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getResultProdOwhList(Map<String, Object> paraMap){
        String tag = "SysService.getResultProdOwhList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getResultProdOwhList(paraMap);
    }

    @Override
    public void invokeChatServer() {
        // 프로그램이 serverSocket일 경우
        Runnable rServerSocket = new SockServer();
        Thread tServerSocket = new Thread(rServerSocket);
        tServerSocket.setDaemon(true);

        if (tServerSocket.getName() == null) {
            tServerSocket.setName("serverSocket");
            tServerSocket.start();
        }

        // 서버 소켓으로 수신된 데이터가 담긴 큐를 poll하는 쓰레드
        Runnable rRawData = new RawDataController();
        Thread tRawData = new Thread(rRawData);
        tRawData.setDaemon(true);
        tRawData.setName("threadDemon");
        tRawData.start();

        /* 품온, 금속검출을 제외한 데이터 수집 설비 목록 조회
        List<Map<String, Object>> mondataSensorList = new ArrayList<Map<String, Object>>();
        mondataSensorList = monDataMapper.selectMondataEquipList();

        if (mondataSensorList.size() > 0) {

            String mCode;
            // 수집 설비들에 대해서 각 각 쓰레드를 생성
            for (int i = 0; i < mondataSensorList.size(); i++) {

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

    //rollback type에 따라 경로지정해주기.
    @Override
    public List<Map<String, Object>> rollBack(Map<String, Object> paraMap){
        String tag = "sysService.rollBack => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        String type = paraMap.get("type").toString();

        List<Map<String,Object>> rList = new ArrayList<>();

        if(type.equals("prodOwh")) {
            rList = this.rollBackProdOwh(paraMap);
        }

        if(type.equals("prodIwh")) {
            rList = this.rollBackProdIwh(paraMap);
        }

        if(type.equals("indcRslt")) {
            rList = this.rollBackIndcRslt(paraMap);
        }

        return rList;
    }

    //작업지시결과 RollBack
    private List<Map<String, Object>> rollBackIndcRslt(Map<String, Object> paraMap) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) paraMap.get("list");
        List<Map<String, Object>> rList = new ArrayList<>();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        for(Map<String, Object> el : list){
            Long indcRsltNo = Long.parseLong(el.get("indcRsltNo").toString());
            Map<String, Object> rmap = new HashMap<>();
            rmap.put("indcRsltNo", indcRsltNo);
            rList.add(rmap);
            MakeIndcRslt chkmirvo = makeIndcRsltRepo.findByCustNoAndIndcRsltNoAndUsedYn(custNo,indcRsltNo, "N");
            if(chkmirvo != null){
                chkmirvo.setUsedYn("Y");
                makeIndcRsltRepo.save(chkmirvo);
            }
        }

        return rList;
    }

    //제품입고 RollBack
    private List<Map<String, Object>> rollBackProdIwh(Map<String, Object> paraMap){
        List<Map<String, Object>> list = (List<Map<String, Object>>) paraMap.get("list");
        List<Map<String, Object>> rList = new ArrayList<>();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        for(Map<String, Object> el : list){
            Long prodNo = Long.parseLong(el.get("prodNo").toString());
            Long whNo = Long.parseLong(el.get("whNo").toString());
            Long iwhNo = Long.parseLong(el.get("iwhNo").toString());

            Map<String, Object> rMap = new HashMap<>();
            rMap.put("iwhNo", iwhNo);
            rList.add(rMap);

            ProdIwh chkpivo = prodIwhRepo.findByCustNoAndIwhNoAndUsedYn(custNo,iwhNo, "N");
            if(chkpivo != null){
                chkpivo.setModDt(DateUtils.getCurrentDateTime());
                chkpivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                chkpivo.setModIp(paraMap.get("ipaddr").toString());

                chkpivo.setUsedYn("Y");
                prodIwhRepo.save(chkpivo);
            }

            ProdStk chkpsvo = prodStkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,whNo, prodNo, "Y");
            if(chkpsvo != null){
                Float stkQty = chkpsvo.getStkQty() - Float.parseFloat(el.get("iwhQty").toString());
                chkpsvo.setStkQty(stkQty);
                chkpsvo.setModIp(paraMap.get("ipaddr").toString());
                chkpsvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                chkpsvo.setModDt(DateUtils.getCurrentDateTime());

                prodStkRepo.save(chkpsvo);
            }
        }
        return rList;
    }

    //제품출고 rollback
    private List<Map<String, Object>> rollBackProdOwh(Map<String, Object> paraMap){
        List<Map<String, Object>> list = (List<Map<String, Object>>) paraMap.get("list");
        List<Map<String, Object>> rList = new ArrayList<>();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        for(Map<String, Object> el : list){
            Map<String, Object> returnMap = new HashMap<>();

            Long prodNo = Long.parseLong(el.get("prodNo").toString());
            Long whNo = Long.parseLong(el.get("whNo").toString());
            Long owhNo = Long.parseLong(el.get("owhNo").toString());

            returnMap.put("prodNo", prodNo);
            returnMap.put("whNo", whNo);
            returnMap.put("owhNo", owhNo);
            rList.add(returnMap);

            ProdOwh chkpovo = prodOwhRepo.findByCustNoAndOwhNoAndUsedYn(custNo,owhNo, "N");
            if(chkpovo != null){
                chkpovo.setUsedYn("Y");
                prodOwhRepo.save(chkpovo);
            }

            ProdStk chkpsvo = prodStkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,whNo, prodNo, "Y");
            if(chkpsvo != null){
                Float stkQty = chkpsvo.getStkQty() - Float.parseFloat(el.get("owhQty").toString());
                chkpsvo.setStkQty(stkQty);

                chkpsvo.setModDt(DateUtils.getCurrentDateTime());
                chkpsvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                chkpsvo.setModIp(paraMap.get("ipaddr").toString());

                prodStkRepo.save(chkpsvo);

            }
        }

        return rList;
    }

    @Override
    public List<Map<String, Object>> getPalets(Map<String, Object> paraMap) {
        String tag = "sysService.getPalets => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getPalets(paraMap);
    }


}
