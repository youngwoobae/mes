package daedan.mes.moniter.service;

import daedan.mes.make.mapper.MakeIndcMapper;
import daedan.mes.moniter.mapper.MoniterMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("moniterService")
public class MoniterServiceImpl implements MoniterService{
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private MoniterMapper mapper;

    @Override
    public List<Map<String, Object>> getComboEquip(Map<String, Object> paraMap) {
        String tag = "MoniterService.getComboEquipList => ";
        paraMap.put("custNo", Long.parseLong(env.getProperty("cust_no")));
        paraMap.put("svcTp", "DIRECT_RECV");
        log.info(tag +  "params = " + paraMap.toString());
        return mapper.getComboEquip(paraMap);
    }

    @Override
    public List<Map<String, Object>> getEquipUsedHstr(Map<String, Object> paraMap) {
        String tag = "MoniterService.getEquipUsedHstr => ";
        log.info(tag + "paraMap = " + paraMap.toString() );
        return mapper.getEquipUsedHstr(paraMap);
    }


    @Override
    public int getEquipUsedHstrCount(Map<String, Object> paraMap) {
        return mapper.getEquipUsedHstrCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getTotalStatusList(Map<String, Object> paraMap) {
        String tag = "vsvc.MoniterService.getTotalStatusList => ";
        log.info(tag + "paraMap = " + paraMap.toString() );
        return mapper.getTotalStatusList(paraMap);
    }

    @Override
    public int getTotalStatusListCount(Map<String, Object> paraMap) {
        return mapper.getTotalStatusListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboOrderCmpy(Map<String, Object> paraMap) {
        String tag = "moniterService.getComboOrderCmpy =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getComboOrderCmpy(paraMap);
    }

    @Override
    public List<Map<String, Object>> getCcpHeatList(Map<String, Object> paraMap) {
        String tag = "moniterService.getCcpHeatList =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getCcpHeatList(paraMap);
    }
    @Override
    public int getCcpHeatListCount(Map<String, Object> paraMap) {
        return mapper.getCcpHeatListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getEquipProdMove(Map<String, Object> paraMap) {
        String tag = "moniterService.getEquipProdMove =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getEquipProdMove(paraMap);
    }

    @Override
    public List<Map<String, Object>> getCcpTexts(Map<String, Object> paraMap) {
        String tag = "moniterService.getCcpTexts =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getCccpTexts(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdIoFileList(Map<String, Object> paraMap) {
        String tag = "moniterService.getProdIoFileList =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getProdIoFileList(paraMap);
    }

    @Override
    public int getProdIoFileListCount(Map<String, Object> paraMap) {
        String tag = "moniterService.getProdIoFileListCount =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getProdIoFileListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getOrdHstList(Map<String, Object> paraMap) {
        String tag = "moniterService.getOrdHstList =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getOrdHstList(paraMap);
    }

    @Override
    public int getOrdHstListCount(Map<String, Object> paraMap) {
        String tag = "moniterService.getOrdHstListCount =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getOrdHstListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrPursHstList(Map<String, Object> paraMap) {
        String tag = "moniterService.getMatrPursHstList =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getMatrPursHstList(paraMap);
    }

    @Override
    public int getMatrPursHstListCount(Map<String, Object> paraMap) {
        String tag = "moniterService.getMatrPursHstListCount =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getMatrPursHstListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrIwhHist(Map<String, Object> paraMap) {
        String tag = "moniterService.getMatrIwhHist =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getMatrIwhHist(paraMap);
    }



    @Override
    public int getMatrIwhHistCount(Map<String, Object> paraMap) {
        String tag = "moniterService.getMatrIwhHistCount =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getMatrIwhHistCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrIwhHstList(Map<String, Object> paraMap) {
        String tag = "moniterService.getMatrIwhHstList =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getMatrIwhHstList(paraMap);
    }

    @Override
    public int getMatrIwhHstListCount(Map<String, Object> paraMap) {
        return mapper.getMatrIwhHstListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrOwhHstList(Map<String, Object> paraMap) {
        String tag = "moniterService.getMatrOwhHstList =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getMatrOwhHstList(paraMap);
    }

    @Override
    public int getMatrOwhHstListCount(Map<String, Object> paraMap) {
        return mapper.getMatrOwhHstListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdIwhHstList(Map<String, Object> paraMap) {
        String tag = "moniterService.getProdIwhHstList =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getProdIwhHstList(paraMap);
    }

    @Override
    public int getProdIwhHstListCount(Map<String, Object> paraMap) {
        return mapper.getProdIwhHstListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdOwhHstList(Map<String, Object> paraMap) {
        String tag = "moniterService.getProdOwhHstList =>";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getProdOwhHstList(paraMap);
    }

    @Override
    public int getProdOwhHstListCount(Map<String, Object> paraMap) {
        return mapper.getProdOwhHstListCount(paraMap);
    }


}
