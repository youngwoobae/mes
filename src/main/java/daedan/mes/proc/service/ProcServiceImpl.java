package daedan.mes.proc.service;

import daedan.mes.code.domain.CcpType;
import daedan.mes.code.domain.CodeInfo;
import daedan.mes.code.mapper.CodeMapper;
import daedan.mes.code.repository.CodeRepository;
import daedan.mes.code.service.CodeService;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.proc.domain.ProcBrnch;
import daedan.mes.proc.domain.ProcInfo;
import daedan.mes.proc.domain.ProcMp;
import daedan.mes.proc.mapper.ProcMapper;
import daedan.mes.proc.repository.ProcBrnchRepository;
import daedan.mes.proc.repository.ProcInfoRepository;
import daedan.mes.proc.repository.ProcMpRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*공정관리*/
@Service("procService")
public class ProcServiceImpl implements ProcService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private ProcInfoRepository procRepo;

    @Autowired
    private CodeRepository codeRepo;

    @Autowired
    private ProcBrnchRepository procBrnchRepo;

    @Autowired
    private ProcMpRepository procMpRepo;

    @Autowired
    private ProcMapper mapper;

    @Autowired
    private CodeMapper codeMapper;

    @Override
    public List<Map<String, Object>> getProcList(Map<String, Object> paraMap) {
        String tag = "vsvc.ProcService.getProcList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProcList(paraMap);
    }
    @Override
    public int getProcListCount(Map<String, Object> paraMap) {
        return mapper.getProcListCount(paraMap);
    }

    @Override
    public Map<String, Object> getProcInfo(Map<String, Object> paraMap) {
        String tag = "ProcService.getProcInfo => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProcInfo(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProcBrnchList(Map<String, Object> paraMap) {
        String tag = "ProcService.getProcBrnchList ==> ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProcBrnchList(paraMap);
    }
    @Override
    public int getProcBrnchListCount(Map<String, Object> paraMap) {
        String tag = "ProcService.getProcBrnchListCount ==> ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProcBrnchListCount(paraMap);
    }
    @Override
    @Transactional
    public void saveProcWork(Map<String, Object> paraMap) {
        String tag = "vsvc.ProcService.saveProcWork => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        //paraMap = {procBrnchNo=43, procCd=721, procSeq=1, brnchNo=1251, maxMakeQty=0, needDtVal=1, nextStepVal=0, procGrpCd=1901, ccpTp=NONE, userId=5, ipaddr=127.0.0.1}
        Long brnchNo = Long.parseLong(paraMap.get("brnchNo").toString());
        Long procCd = 0L;
        try{
            procCd = Long.parseLong(paraMap.get("procCd").toString());
        }catch(NullPointerException ne){
            procCd = 0L;
        }

        String procNm = paraMap.get("procNm").toString();
        int procSeq = Integer.parseInt(paraMap.get("procSeq").toString());

        CodeInfo cdvo = codeRepo.findByCodeNoAndUsedYn(procCd,"Y");
        if (cdvo == null) {
            cdvo = new CodeInfo();

            cdvo.setCodeNo(0L);
            cdvo.setParCodeNo(Long.parseLong(env.getProperty("code_base_proc")));
            cdvo.setCodeNm(procNm);
            cdvo.setCodeBrief(procNm);
            cdvo.setCodeAlais(procNm);
            cdvo.setCodeSeq(procSeq);
            cdvo.setCcpTp(CcpType.valueOf(paraMap.get("ccpTp").toString()));
            cdvo.setUsedYn("Y");
            cdvo.setModableYn("N");
            cdvo.setCcpLmtStdNo("N");
            cdvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            cdvo.setRegIp(paraMap.get("ipaddr").toString());
            cdvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            cdvo.setModDt(DateUtils.getCurrentBaseDateTime());
            cdvo.setModIp(paraMap.get("ipaddr").toString());
            cdvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            cdvo.setCustNo(custNo);
            cdvo = codeRepo.save(cdvo);

            procCd = cdvo.getCodeNo();
        }
        else {
            cdvo.setCodeNm(procNm);
            cdvo.setCodeBrief(procNm);
            cdvo.setCodeAlais(procNm);
            cdvo.setCodeSeq(procSeq);
            cdvo.setCcpTp(CcpType.valueOf(paraMap.get("ccpTp").toString()));
            cdvo.setModDt(DateUtils.getCurrentBaseDateTime());
            cdvo.setModIp(paraMap.get("ipaddr").toString());
            cdvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            cdvo.setCustNo(custNo);
            codeRepo.save(cdvo);
        }
        //코드순서 재조정 (입력된 코드순서 이하 코드를 몽땅 입력된 코드번호 + 1씩 증가시켜 재설정)
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code_base_proc")));
        paraMap.put("codeSeq",procSeq);
        List<Map<String,Object>> ds = this.getCodeListByParAndSeqOver(paraMap);
        int idx = -1;
        while(++idx < ds.size()) {
            Map<String,Object> dsMap = ds.get(idx);
            cdvo = codeRepo.findByCodeNoAndUsedYn(Long.parseLong(dsMap.get("codeNo").toString()),"Y");
            if (cdvo != null) {
                cdvo.setCodeSeq(procSeq + idx);
                cdvo.setCustNo(custNo);
                codeRepo.save(cdvo);
            }
        }
        /*프로세스정보 저장 : 프로세스 테이블이 필요한 이유 : 코드정보에서 포함하는 못하는 필드를 사용하기 위함 (작업별 하루최대 생산량등..)*/
        ProcInfo pivo = procRepo.findByCustNoAndProcCdAndUsedYn(custNo,procCd,"Y");
        if (pivo == null) {
            pivo = new ProcInfo();
            pivo.setUsedMpLvlb(0);
            pivo.setUsedMpLvlt(0);
            pivo.setUsedMpLvlm(0);
        }
        pivo.setProcCd(procCd);
        pivo.setProcSeq(procSeq);
        pivo.setCcpNo(0L);
        pivo.setMaxMakeQty(0);
        pivo.setNeedHm(0);
        pivo.setProcUnit(84L);
        pivo.setUsedYn(cdvo.getUsedYn());
        pivo.setModDt(cdvo.getModDt());
        pivo.setModId(cdvo.getModId());
        pivo.setModIp(cdvo.getModIp());
        pivo.setRegDt(cdvo.getRegDt());
        pivo.setRegId(cdvo.getRegId());
        pivo.setRegIp(cdvo.getRegIp());
        pivo.setFileNo(0L);
        pivo.setCustNo(custNo);
        procRepo.save(pivo);

        /*프로세스 출력순서를 공정작업코드 출력순서와 동기화 */
        idx = -1;
        while(++idx < ds.size()) {
            Map<String,Object> dsMap = ds.get(idx);
            pivo = procRepo.findByCustNoAndProcCdAndUsedYn(custNo,Long.parseLong(dsMap.get("codeNo").toString()),"Y");
            if (pivo != null) {
                pivo.setProcSeq(procSeq + idx);
                pivo.setCustNo(custNo);
                procRepo.save(pivo);
            }
        }

        Long procBrnchNo= 0L;
        ProcBrnch pbvo = new ProcBrnch();
        pbvo.setProcCd(procCd);
        pbvo.setNeedDtVal(Integer.parseInt(paraMap.get("needDtVal").toString()));
        pbvo.setNextStepVal(Integer.parseInt(paraMap.get("nextStepVal").toString()));
        pbvo.setBrnchNo(Long.parseLong(paraMap.get("brnchNo").toString()));
        try {
            pbvo.setCcpTp(paraMap.get("ccpTp").toString());
        }
        catch (NullPointerException ne) {
            pbvo.setCcpTp("NONE");
        }
        pbvo.setProcGrpCd(Long.parseLong(paraMap.get("procGrpCd").toString()));
        pbvo.setMaxMakeQty(Long.parseLong(paraMap.get("maxMakeQty").toString()));

        //onetoone관련 join 이므로 코드정보 추출시 이하 2줄 사용해야 함.
        //pbvo.setCodeInfo(codeRepo.findByCodeNoAndUsedYn(Long.parseLong(el.get("proc_cd").toString()),"Y"));
        //pbvo.setProcSeq(pbvo.getCodeInfo().getCodeSeq());

        pbvo.setProcSeq(cdvo.getCodeSeq());
        pbvo.setModDt(DateUtils.getCurrentBaseDateTime());
        pbvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        pbvo.setModIp(paraMap.get("ipaddr").toString());
        pbvo.setUsedYn("Y");
        try {
            procBrnchNo = Long.parseLong(paraMap.get("procBrnchNo").toString());
        }
        catch (NullPointerException ne) {
            procBrnchNo = 0L;
        }
        /*공정분류정보 저장*/
        ProcBrnch chkvo = procBrnchRepo.findByCustNoAndProcBrnchNoAndUsedYn(custNo,procBrnchNo,"Y");
        if (chkvo != null) {
            pbvo.setProcBrnchNo(chkvo.getProcBrnchNo());
            pbvo.setRegDt(chkvo.getRegDt());
            pbvo.setRegId(chkvo.getRegId());
            pbvo.setRegIp(chkvo.getRegIp());
        }
        else {
            pbvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            pbvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            pbvo.setRegIp(paraMap.get("ipaddr").toString());
        }
        pbvo.setProcCd(procCd);
        pbvo.setCustNo(custNo);
        procBrnchRepo.save(pbvo);

        /*공정분류 출력순서를 공정작업코드 출력순서와 동기화 */
        idx = -1;
        while(++idx < ds.size()) {
            Map<String,Object> dsMap = ds.get(idx);
            chkvo = procBrnchRepo.findByCustNoAndBrnchNoAndProcCdAndUsedYn(custNo,pbvo.getBrnchNo(),Long.parseLong(dsMap.get("codeNo").toString()),"Y");
            if (chkvo != null ) {
                pbvo.setProcCd(procCd);
                pbvo.setProcSeq(procSeq + idx);
                pbvo.setCustNo(custNo);
                procBrnchRepo.save(pbvo);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getProcMemberList(Map<String, Object> paraMap) {
        String tag = "vsvc.ProcService.getProcMemberList ==> ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProcMemberList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getCodeListByParAndSeqOver(Map<String, Object> paraMap) {
        String tag = "vsvc.ProcService.getCodeListByParAndSeqOver ==> ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getCodeListByParAndSeqOver(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProcInfoList(Map<String, Object> paraMap) {
        String tag = "vsvc.ProcService.getProcInfoList ==> ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProcInfoList(paraMap);
    }

    @Override
    public int getProcInfoListCount(Map<String, Object> paraMap) {
        return mapper.getProcInfoListCount(paraMap);
    }

    @Override
    @Transactional
    public void saveProcInfo(Map<String, Object> paraMap) {
        String tag = "vsvc.ProcService.saveProcInfo ==> ";
        log.info(tag + "paraMap = " + paraMap.toString());
        /*
        Gson gson = new Gson();
        ProcInfo pivo = gson.fromJson((JsonElement) paraMap, ProcInfo.class);
        ProcInfo chkvo = procRepo.findByProcCdAndUsedYn(pivo.getProcCd(), "Y");
        pivo.setModDt(DateUtils.getCurrentDate());
        pivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        pivo.setModIp(paraMap.get("ipaddr").toString());

        if (chkvo != null) {
            pivo.setProcCd(chkvo.getProcCd());
        }
        else {
            pivo.setProcCd(Long.parseLong(paraMap.get("proc_cd").toString()));
            pivo.setRegDt(DateUtils.getCurrentDate());
            pivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            pivo.setRegIp(paraMap.get("ipaddr").toString());
        }
        procRepo.save(pivo);
         */
    }

    @Override
    @Transactional
    public void saveProcMpList(Map<String, Object> paraMap) {
        String tag = "vsvc.ProcService.saveProcMpList => ";
        log.info(tag + " paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        ProcMp vo = null;
        Long worker = 0L;
        Long procGrpCd = Long.parseLong(paraMap.get("proc_grp_cd").toString());
        // 저장시에 proc_mp => used_yn 을 모드 N 으로 변경 그리고 체크된 리스트만 다시 Y변경 해당하는 id 없으면 생성
        mapper.getUpdateUsedyn(procGrpCd);

        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("userList");
        for (Map<String,Object> el : ds) {
            vo = new ProcMp();

            worker = Long.parseLong(el.get("user_id").toString());
            ProcMp chkvo = procMpRepo.findByProcGrpCdAndUserId(procGrpCd, worker);
            if (chkvo != null) {
                chkvo.setUsedYn("Y");
                procMpRepo.save(chkvo);
            }else {
                vo.setProcGrpNo(0L);
                vo.setProcGrpCd(procGrpCd);
                vo.setUserId(worker);
                vo.setUsedYn("Y");
                vo.setCustNo(custNo);
                procMpRepo.save(vo);
            }
        }
    }

    @Override
    @Transactional
    public void saveProcBrnchList(Map<String, Object> paraMap) {
        String tag = "vsvc.ProcService.saveProcBrnchList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        ProcBrnch pbvo = null;
        Long brnchNo = Long.parseLong(paraMap.get("brnch_no").toString());
        Long procBrnchNo = 0L;
        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("procList");
        List<Map<String,Object>> ts = (List<Map<String, Object>>) paraMap.get("totalList");

        for (Map<String,Object> el : ts) {
            ProcBrnch chkvo = procBrnchRepo.findByCustNoAndBrnchNoAndProcCdAndUsedYn(custNo,brnchNo, Long.parseLong(el.get("proc_cd").toString()),"Y");
            if (chkvo != null) {
                chkvo.setUsedYn("N");
                chkvo.setCustNo(custNo);
                procBrnchRepo.save(chkvo);
            }
        }

        for (Map<String,Object> el : ds) {
            pbvo = new ProcBrnch();
            pbvo.setBrnchNo(brnchNo);
            try {
                pbvo.setProcGrpCd(Long.parseLong(el.get("proc_grp_cd").toString()));
            }
            catch (NullPointerException ne) {
                pbvo.setProcGrpCd(0L);
            }

            try {
                pbvo.setMaxMakeQty(Long.parseLong(el.get("max_make_qty").toString()));
            }
            catch (NullPointerException ne) {
                pbvo.setMaxMakeQty(0L);
            }

            try {
                pbvo.setNeedDtVal(Integer.parseInt(el.get("need_dt_val").toString()));
            }
            catch (NullPointerException ne) {
                pbvo.setNeedDtVal(1);
            }
            try {
                pbvo.setNextStepVal(Integer.parseInt(el.get("next_step_val").toString()));
            }
            catch (NullPointerException ne) {
                pbvo.setNextStepVal(0);
            }

            try{
                pbvo.setProcSeq(Integer.parseInt(el.get("proc_seq").toString()));
            }catch(NullPointerException ne){
                pbvo.setProcSeq(Integer.parseInt(pbvo.getProcCd().toString()));
            }

            //onetoone관련 join 이므로 코드정보 추출시 이하 2줄 사용해야 함.
//            pbvo.setCodeInfo(codeRepo.findByCodeNoAndUsedYn(Long.parseLong(el.get("proc_cd").toString()),"Y"));
//            pbvo.setProcSeq(pbvo.getCodeInfo().getCodeSeq());

            pbvo.setProcCd(Long.parseLong(el.get("proc_cd").toString()));


            pbvo.setModDt(DateUtils.getCurrentDate());
            pbvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            pbvo.setModIp(paraMap.get("ipaddr").toString());
            pbvo.setUsedYn("Y");

            try {
                procBrnchNo = Long.parseLong(el.get("proc_brnch_no").toString());
            }
            catch (NullPointerException ne) {
                procBrnchNo = 0L;
            }
            ProcBrnch chkvo = procBrnchRepo.findByCustNoAndProcBrnchNoAndUsedYn(custNo,procBrnchNo,"N");
            if (chkvo != null) {
                pbvo.setProcBrnchNo(chkvo.getProcBrnchNo());
                pbvo.setRegDt(chkvo.getRegDt());
                pbvo.setRegId(chkvo.getRegId());
                pbvo.setRegIp(chkvo.getRegIp());
            }
            else {
                pbvo.setProcBrnchNo(0L);
                pbvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                pbvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                pbvo.setRegIp(paraMap.get("ipaddr").toString());
            }
            pbvo.setCustNo(custNo);
            procBrnchRepo.save(pbvo);

        }
    }

    @Override
    public List<Map<String, Object>> getProcStdList(Map<String, Object> paraMap) {
        String tag = "vsvc.ProcService.getProcStdList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProcStdList(paraMap);
    }

    @Override
    public int getProcStdListCount(Map<String, Object> paraMap) {
        return mapper.getProcStdListCount(paraMap);
    }

    @Override
    public void saveProcStd(Map<String, Object> paraMap){
        String tag = "vsvc.ProcService.saveProcStd => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        String exData = "";

        ProcInfo infovo = new ProcInfo();
        infovo.setUsedYn("Y");

        infovo.setMaxMakeQty(Integer.parseInt(paraMap.get("maxMakeQty").toString()));
        infovo.setProcUnit(Long.parseLong(paraMap.get("procUnit").toString()));
        infovo.setProcSeq(Integer.parseInt(paraMap.get("procSeq").toString()));
        try {
            infovo.setUsedMpLvlt(Integer.parseInt(paraMap.get("usedMpLvlt").toString()));
        }
        catch (NullPointerException ne) {
            infovo.setUsedMpLvlt(0);
        }
        try {
            infovo.setUsedMpLvlm(Integer.parseInt(paraMap.get("usedMpLvlm").toString()));
        }
        catch (NullPointerException ne) {
            infovo.setUsedMpLvlm(0);
        }
        try {
            infovo.setUsedMpLvlb(Integer.parseInt(paraMap.get("usedMpLvlb").toString()));
        }
        catch (NullPointerException ne) {
            infovo.setUsedMpLvlb(0);
        }
        infovo.setModDt(DateUtils.getCurrentDate()); //
        infovo.setModId(Long.parseLong(paraMap.get("userId").toString())); //
        infovo.setModIp(paraMap.get("ipaddr").toString()); //
        infovo.setProcCtnt(paraMap.get("procCtnt").toString());
        try {
            infovo.setFileNo(Long.parseLong(paraMap.get("fileNo").toString()));
        }
        catch (NullPointerException ne) {
            infovo.setFileNo(0L);
        }
        try {
            infovo.setNeedHm(Integer.parseInt(paraMap.get("needHm").toString()));
        }
        catch (NullPointerException ne) {
            infovo.setNeedHm(0);
        }
        infovo.setProcCd(Long.parseLong(paraMap.get("procCd").toString()));

        ProcInfo chkvo = procRepo.findByCustNoAndProcCdAndUsedYn(custNo,infovo.getProcCd(), infovo.getUsedYn());
        if (chkvo != null) {
            infovo.setProcCd(chkvo.getProcCd());
            infovo.setRegDt(DateUtils.getCurrentDate());
            infovo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            infovo.setRegIp(paraMap.get("ipaddr").toString()); //
        }
        else {
            infovo.setProcCd(0L);
            infovo.setRegDt(chkvo.getRegDt()); //
            infovo.setRegId(chkvo.getRegId()); //
            infovo.setRegIp(chkvo.getRegIp()); //
        }
        infovo.setCustNo(custNo);
        procRepo.save(infovo);

        CodeInfo codeInfo = codeRepo.findByCodeNoAndUsedYn(Long.parseLong(paraMap.get("procCd").toString()),"Y");
        codeInfo.setCodeNm(paraMap.get("procNm").toString());
        codeInfo.setCustNo(custNo);
        codeRepo.save(codeInfo);
    }
    @Override
    public void dropProcStd(Map<String, Object> paraMap){
        String tag = "vsvc.ProcService.dropProcStd => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        ProcInfo chkvo = procRepo.findByCustNoAndProcCdAndUsedYn(custNo,Long.parseLong(paraMap.get("procCd").toString()),"Y");
        if(chkvo != null){
            chkvo.setUsedYn("N");
            chkvo.setModDt(DateUtils.getCurrentDate());
            chkvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            chkvo.setModIp(paraMap.get("ipaddr").toString()); //
            procRepo.save(chkvo);
        }
    }

    @Override
    public List<Map<String, Object>> getPopProcList(Map<String, Object> paraMap){
        String tag = "vsvc.ProcService.getPopProcList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getPopProcList(paraMap);
    }

    @Override
    public int getPopProcListCount(Map<String, Object> paraMap){
        return mapper.getPopProcListCount(paraMap);
    }

    @Override
    public Map<String, Object> getProcBrnchInfo(Map<String, Object> paraMap) {
        String tag = "vsvc.ProcService.getProcBrnchInfo => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        ProcBrnch vo =  procBrnchRepo.findByCustNoAndProcBrnchNoAndUsedYn(custNo,Long.parseLong(paraMap.get("procBrnchNo").toString()),"Y");
        Map<String,Object> rmap = (vo != null) ? StringUtil.voToMap(vo) : null;
        if (rmap != null) {
            CodeInfo cdvo = codeRepo.findByCodeNoAndUsedYn(vo.getProcCd(),"Y");
            if (cdvo != null) {
                rmap.put("procNm",cdvo.getCodeNm());
                rmap.put("procSeq",cdvo.getCodeSeq());
            }
        }
         return rmap;
    }
}

