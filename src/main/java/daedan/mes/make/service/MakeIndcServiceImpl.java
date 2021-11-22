package daedan.mes.make.service;


import daedan.mes.bord.mapper.BordMapper;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.equip.repository.OperMastRepository;
import daedan.mes.file.domain.FileInfo;
import daedan.mes.file.repository.FileRepository;
import daedan.mes.io.domain.MatrIwh;
import daedan.mes.io.domain.MatrOwh;
import daedan.mes.io.domain.ProdIwh;
import daedan.mes.io.repository.ProdIwhRepository;
import daedan.mes.io.service.IoService;
import daedan.mes.io.repository.MatrIwhRepository;
import daedan.mes.io.repository.MatrOwhRepository;
import daedan.mes.make.domain.*;
import daedan.mes.make.mapper.MakeIndcMapper;
import daedan.mes.make.repository.*;
import daedan.mes.ord.domain.OrdInfo;
import daedan.mes.ord.domain.OrdProd;
import daedan.mes.ord.mapper.OrdMapper;
import daedan.mes.ord.repository.OrdProdRepository;
import daedan.mes.ord.repository.OrdRepository;
import daedan.mes.proc.domain.ProcBrnch;
import daedan.mes.proc.repository.ProcBrnchRepository;
import daedan.mes.proc.repository.ProcInfoRepository;
import daedan.mes.proc.service.ProcService;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.mapper.ProdMapper;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.prod.service.ProdService;
import daedan.mes.purs.domain.PursInfo;
import daedan.mes.purs.domain.PursMatr;
import daedan.mes.purs.repository.PursInfoRepository;
import daedan.mes.purs.repository.PursMatrRepository;
import daedan.mes.purs.service.PursService;
import daedan.mes.stock.domain.MatrPos;
import daedan.mes.stock.domain.MatrStk;
import daedan.mes.stock.domain.ProdStk;
import daedan.mes.stock.repository.MatrPosRepository;
import daedan.mes.stock.repository.MatrStkRepository;
import daedan.mes.stock.repository.ProdStkRepository;
import daedan.mes.stock.service.StockService;
import daedan.mes.user.domain.UserInfo;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.jdbc.Null;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang.StringUtils.*;

@Service("indcService")
public class MakeIndcServiceImpl implements MakeIndcService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private MakeIndcMapper mapper;

    @Autowired
    private MakeIndcRepository makeIndcRepo;

    @Autowired
    private OrdMapper ordMapper;

    @Autowired
    private MakeMpRepository makeMpRepo;

    @Autowired
    private MakeWorkPlanRepository makeWorkPlanRepo;

    @Autowired
    private ProdIwhRepository prodIwhRepo;

    @Autowired
    private ProcBrnchRepository procBrnchRepo;

    @Autowired
    private MakeIndcRsltRepository mir;

    @Autowired
    private OrdRepository ordRepository;

    @Autowired
    private OrdProdRepository ordProdRepository;

    @Autowired
    private MatrStkRepository matrStkRepo;

    @Autowired
    private PursService pursService;

    @Autowired
    private IoService ioService;

    @Autowired
    private PursInfoRepository pursRepo;

    @Autowired
    private PursMatrRepository pmr;
    @Autowired
    private PursInfoRepository pir;

    @Autowired
    private MatrOwhRepository omr;

    @Autowired
    private MatrIwhRepository imr;

    @Autowired
    private ProdRepository prodRepo;

    @Autowired
    private MatrPosRepository matrPosRepo;

    @Autowired
    private MakeIndcMatrRepository makeIndcMatrRepo;


    @Autowired
    private ProdStkRepository prodStkRepo;

    @Autowired
    private ProdService prodService;

    @Autowired
    private FileRepository fr;

    @Autowired
    private StockService stockService;


    //시작 날짜 -- make_indc 날짜 생성을 위한 전역 변수
    private Date frDt;

    //종료 날짜 -- make_indc 날짜 생성을 위한 전역 변수
    private Date toDt;

    @Override
    public List<Map<String, Object>> getMakeIndcList(Map<String, Object> paraMap) {
        return mapper.getMakeIndcList(paraMap);
    }

    @Override
    public int getMakeIndcListCount(Map<String, Object> paraMap) {
        return mapper.getMakeIndcListCount(paraMap);
    }

    @Override
    public Map<String, Object>  getMakeIndcInfo(Map<String, Object> paraMap) {

        StringBuffer buf = new StringBuffer();
        Map<String,Object> rmap = null;
        rmap = mapper.getMakeIndcInfo(paraMap);
        try {
            buf.append(env.getProperty("base_file_url")).append("make/").append(rmap.get("indcNo")).append(".png");
            rmap.put("barCodeUrl", buf.toString());
            log.info("getMakeIndcInfo.barCodeUrl = " + rmap.get("barCodeUrl"));
        }
        catch (NullPointerException ne) {
            rmap = new HashMap<String,Object>();
            buf.setLength(0);
            buf.append(env.getProperty("base_file_url")).append("make/default.png");
            rmap.put("barCodeUrl", buf.toString());
        }
        return rmap;
    }

    @Override
    public Map<String, Object> getMakeIndcRsltInfo(Map<String, Object> paraMap) {
        return mapper.getMakeIndcRsltInfo(paraMap);
    }

    @Override
    public List<Map<String, Object>> getFaultList(Map<String, Object> paraMap) {
        return mapper.getFaultList(paraMap);

    }

    @Override
    public List<Map<String, Object>> getMakePlanList(Map<String, Object> paraMap) {
        return mapper.getMakePlanList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakePlanProdList(Map<String, Object> paraMap) {
        return mapper.getMakePlanProdList(paraMap);
    }

    @Override
    public int getMakePlanProdListCount(Map<String, Object> paraMap) {
        return mapper.getMakePlanProdListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboOrdrCmpyList(Map<String, Object> paraMap) {
        return mapper.getComboOrdrCmpyList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboProdProcList(Map<String, Object> paraMap) {
        return mapper.getComboProdProcList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakePlanInfo(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.getMakePlanInfo => ";
        List<Map<String, Object>> ds = mapper.getMakePlanInfo(paraMap);
        /* Remarked By KMJ At 21.10.25 --사용중지
        for(Map<String,Object> el : ds){
            StringBuffer buf = new StringBuffer();
            buf.append(env.getProperty("img.root.path")).append("prod/").append(el.get("prodNo")).append(".png");
            el.put("barCodeUrl", buf.toString());
        }
         */
        return ds;
    }

    @Override
    public List<Map<String, Object>> getMakeIndcRsltList(Map<String, Object> paraMap) {
        return mapper.getMakeIndcRsltList(paraMap);
    }

    @Override
    public int getMakeIndcRsltListCount(Map<String, Object> paraMap) {
        return mapper.getMakeIndcRsltListCount(paraMap);
    }


    /*작업현황 캘린더에서 드래그&드롭으로 변경된 작업지시 일자 변경*/
    @Override
    @Transactional
    public void resetMakeTermByDragDrop(Map<String, Object> paraMap) {
        String tag = "makeIndcService.resetMakeTermByDragDrop ==> ";
        paraMap.put("ipaddr",paraMap.get("ipaddr"));
        log.info(tag + "paraMap = " + paraMap.toString());
        mapper.resetMakeTermByDragDrop(paraMap);
    }


    /*
        생산품목에 기반한 공정목록 추출후 추출된 공정 갯수 많큼
        savemakeIndc 프로세스를 호출함.
            > 생산지시정보 생성 (make_indc)
            > 본작업 지시인 경우 해당 상품의 bom을 기반으로 소요자재 추출 (
                - 생산자재 생성(make_indc_matr)
                - 구매정보 생성 (savePursInfo) 호출
                    >  purs_info & purs_matr 생성
                      - 재고 부족분이 존재하는지 점검 (mapper.chkReqPurs) : (생산요청 자재수량 - 재고수량)
                      - 재고부족분이 있는 경우
	                    . 구매정보 생성(purs_mast & purs_matr)
	               > 자재단위중량(mess)을 기준하여 적재위치 기초종보 생성(matr_pos)
	            - 구매자재(purs_matr)을 기반으로 자재입고대상 자료 생성 (matr_iwh)
     */

    @Transactional
    @Override
    public void saveMakeIndcFullByPlan(Map<String, Object> paraMap){
        String tag = "makeIndcService.saveMakeIndcFullByPlan == > " ;
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String, Object> prodMap = new HashMap<>();
        prodMap.put("ordNo",paraMap.get("ordNo"));
        prodMap.put("prodNo",paraMap.get("prodNo"));
        prodMap.put("custNo",paraMap.get("custNo"));
        List<Map<String,Object>> passMap = mapper.getOrdProdInfo(prodMap);

        for(Map<String, Object> el : passMap){
            //el.put("ord_no",paraMap.get("ordNo"));
            el.put("indc_dt", paraMap.get("indcDt"));
            el.put("indc_qty", paraMap.get("indcQty"));
            el.put("cust_no",paraMap.get("custNo"));
            el.put("make_fr_dt", paraMap.get("makeFrDt"));
            el.put("make_to_dt", paraMap.get("makeToDt"));
            el.put("indc_sts", paraMap.get("indcSts"));
            el.put("type",paraMap.get("type"));
            el.put("user_id", paraMap.get("userId"));
            el.put("ipaddr", paraMap.get("ipaddr"));
            this.saveMakeIndcFull(el);
        }

        OrdInfo chkoivo = ordRepository.findByCustNoAndOrdNoAndUsedYn(custNo,Long.parseLong(paraMap.get("ordNo").toString()), "Y");
        if(chkoivo != null){
            chkoivo.setOrdSts(Long.parseLong(env.getProperty("ord_status_makeIndc")));
            ordRepository.save(chkoivo);
        }

    }

    @Transactional
    @Override
    public Long saveMakeIndcFull(Map<String, Object> paraMap) { //상품정보에 설정된 제조공정 전체를 생성
        String tag = "makeIndcService.saveMakeIndcFull ==> ";
        log.info(tag + "paraMap = " + paraMap.toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> procMap = new HashMap<String,Object>();
        procMap.put("prodNo",Long.parseLong(paraMap.get("prodNo").toString()));
        procMap.put("custNo",paraMap.get("custNo"));

        Long parIndcNo = 0L;
        Long svParIndcNo = 0L;
        Long chkZeroParent = 0L;
        Long ordNo = 0L;
        boolean isCreate = false;

        log.info("작업지시 추가로 인한 ordInfo, ordProd 생성 시작" + paraMap.get("type"));
        if(paraMap.get("type") == null){
            paraMap.put("type","newMake");
        }
        if(paraMap.get("type").toString().equals("newMake")){
            ordNo = this.makeOrderByIndc(paraMap);
            log.info("신규생성된 주문번호 : "+ ordNo);
        } else {
            ordNo = Long.parseLong(paraMap.get("ordNo").toString());
        }
        try {
            chkZeroParent = Long.parseLong(paraMap.get("parIndcNo").toString());
        }
        catch (NullPointerException ne) {
            chkZeroParent = 0L; //신규로 등록한 경우만 해당됨.
        }
        try{
            if(paraMap.get("type").toString().equals("rowClick")){ //공정전체를 생성하지 않고 클릭된 공정만 생성
                return saveMakeIndc(paraMap);
            }
        }catch(NullPointerException ne){

        }

        int idx = 0;
        List<Map<String,Object>> ds =  mapper.getProdProcList(procMap); //작업지시제품의 제조공정목록 추가
        for (Map<String, Object> el : ds) { //상품에 설정된 제조공정별 처리작업목록 (예:해동,염지,열처리,금속검출,내포장,완제품입고)
            paraMap.put("matr_req_able_yn", (idx == 0) ? "Y" : "N");
            paraMap.put("par_indc_no", (idx == 0) ? 0L : svParIndcNo);
            paraMap.put("proc_cd", Long.parseLong(el.get("value").toString())); //value = > 작업공정번호(721,732..etc)
            paraMap.put("brnch_no",Long.parseLong(el.get("brnchno").toString())); //상품제조분류번호(1253... etc)
            //paraMap.put("custNo",paraMap.get("custNo")); Remarked by KMJ AT 21.10.20 --이미들어와 있음.
            /*작업지시 추가 버튼 클릭 시, 입력된 중량 값으로 소요량을 계산하도록 변경. --Remarked By KMJ 사용중지(21.10.20)
            try{
                paraMap.put("indcWgt", Float.parseFloat(el.get("indcWgt").toString()));
            }catch (NullPointerException ne){
            }
            */
            if(ordNo != null){
                paraMap.put("ord_no",ordNo); //작업지시 추가 버튼으로 생성된 주문번호.
            }

            if (chkZeroParent != 0) { //child를 가지고 있는 작업지시인 경우
                if (!isCreate) {
                    this.saveMakeIndc(paraMap);
                    break;
                }
            }
            parIndcNo = this.saveMakeIndc(paraMap);

            if (idx == 0) {
                svParIndcNo = parIndcNo;
            }
            ++idx;
        }

        if(Integer.parseInt(paraMap.get("custNo").toString()) == 8) { //영양제과
            Map<String, Object> indcRslt = new HashMap<>();
            indcRslt.put("ord_no", ordNo);
            indcRslt.put("prod_no", paraMap.get("prodNo"));
            indcRslt.put("indc_no", parIndcNo);
            indcRslt.put("make_dt", paraMap.get("makeDt"));
            indcRslt.put("proc_cd", 0);
            indcRslt.put("make_wgt", paraMap.get("indcQty"));
            indcRslt.put("proc_unit_nm", "EA");
            paraMap.put("indc_rslt", indcRslt);
            log.info("paraMap = > "+ paraMap );
            this.saveIndcRslt(paraMap);
        }

        return svParIndcNo;
    }



    @Transactional
    @Override
    public Long saveMakeIndc(Map<String, Object> paraMap) {
        String tag = "makeIndcService.saveMakeIndc ==> ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());

        MakeIndc mivo = this.saveMakeIndcInfo(paraMap); //생산지시정보 생성
        Long cmpyNo = 0L; //AddOn By KMJ AT 21.09.10 09:52
        Long parIndcNo = 0L;

        if (mivo.getParIndcNo() == 0L ) {
           parIndcNo = mivo.getIndcNo();
        }
        if (mivo.getProdNo() == 0L) {
            log.info(tag + "생산지시 상품 불필요 공정(생산제품에 공통 적용등) 이므로 이하 프로세스 생략");
            return 0L;
        }
        log.info(tag + "0.본작업지시여부 = " + paraMap.get("matrReqAbleYn"));
        log.info(tag + "1.본작업지시인경우 소요자재 점검후 자재 입출고 요청자료 자동 생성 하고 고도화 시점에 담당자에게 푸시 발송해야 함");
        if (paraMap.get("matrReqAbleYn").toString().equals("Y")) {
            log.info(tag + "2.BOM추출용 생산지시 상품번호 ===> " + mivo.getProdNo());

            Map<String,Object> bomchkmap = new HashMap<String,Object>();
            bomchkmap.put("prodNo", mivo.getProdNo());

            //camel 혼용으로 인한 ord_no 구분 추가, ord_no에 null이 글자로 들어가는 경우있음
            /* SOL Remarked By KMJ AT 21.09.10 08:58
            if(paraMap.get("ord_no") == null){
                bomchkmap.put("ordNo", paraMap.get("ordNo"));
            }else{
                bomchkmap.put("ordNo", paraMap.get("ord_no"));
            }
            EOL Addon By KMJ AT 21.09.10 08:58 */

            /* SOL Addon By KMJ AT 21.09.10 08:58 */
            Long ordNo = 0L;
            try {
                ordNo = Long.parseLong(paraMap.get("ordNo").toString());

                Map<String,Object> tmpMap = new HashMap<String,Object>();
                tmpMap.put("custNo",custNo);
                tmpMap.put("ordNo",ordNo);
                OrdInfo ordvo = new OrdInfo();
                tmpMap = ordMapper.findByCustNoAndOrdNoAndUsedYn(tmpMap);
                ordvo = (OrdInfo) StringUtil.mapToVo(tmpMap,ordvo);
                //OrdInfo ordvo = ordRepository.findByCustNoAndOrdNoAndUsedYn(custNo,ordNo,"Y");  //사입기능 구현으로 인하여 추가됨
                if (ordvo != null) {
                    cmpyNo = ordvo.getCmpyNo();
                }
            }
            catch (NullPointerException ne) {
                ordNo = 0L;
            }
            /* EOL Addon By KMJ AT 21.09.10 08:58 */

            bomchkmap.put("cmpyNo" ,cmpyNo); //AddOn By KMJ AT 21.09.10 09:53 : 사입기능 구현으로 인한 추가
            bomchkmap.put("ordNo" ,ordNo);
            bomchkmap.put("custNo", custNo);
            bomchkmap.put("pageNo", 0);
            bomchkmap.put("pageSz", 100);
            bomchkmap.put("checkPursYn", "checking");
            bomchkmap.put("indcQty", mivo.getIndcQty()); //소요량 산출용
            List<Map<String,Object>> ds = null;

            /* SOL Addon By KMJ AT 21.09.10 08:58 */
            ds = prodService.getProdBomListByIndc(bomchkmap);
            /* EOL Addon By KMJ AT 21.09.10 08:58 */

            /* SOL Remarked By KMJ AT 21.09.10 08:58
            if(paraMap.get("cust_no").equals(3) ){ //하담푸드
                 ds = prodService.getHdfdProdBomListByIndc(bomchkmap);
            }else if(paraMap.get("cust_no").equals(2)){ //서울식품
                 ds = prodService.getSfProdBomListByIndc(bomchkmap);
            }else {
                 ds = prodService.getProdBomListByIndc(bomchkmap);
            }
            EOL Remarked By KMJ AT 21.09.10 08:58 */

            log.info(tag + "3.생산지시 상품기준 소요자재정보 관리 (지시상품기준 BOM 리스트갯수)===> " + ds);
            int idx = 0;
            for (Map<String, Object> el : ds) {
                ++idx;
                el.put("indc_no", mivo.getIndcNo());
                el.put("fill_yield",mivo.getFillYield());
                el.put("userId", paraMap.get("userId"));
                el.put("ipaddr", paraMap.get("ipaddr"));
                el.put("cust_no",custNo);
                log.info("작업지시에 해당하는 원료 목록 : "+el);
                //작업지시 추가 버튼 클릭 시, 지시중량 값을 받아서 넘겨주어야 함.
                this.saveMakeIndcMatr(el);
            }

            boolean chk = true;
            MakeIndc inchk = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,parIndcNo ,"Y");
            if(inchk != null){
                Map<String,Object> datas = new HashMap<String,Object>();
                datas.put("custNo",custNo);
                datas.put("indcNo",parIndcNo);
                List<Map<String, Object>> dl = mapper.chkStkByIndc(datas); // 재고 파악
                for (Map<String, Object> es : dl) {
                    if (Float.parseFloat(es.get("reqPursQty").toString()) < 0) { //구매수량 < 필요수량인 경우
                        chk = false;
                        break;
                    }
                }
                inchk.setIndcSts( (!chk) ?  2401L : 2402L );
                inchk.setCustNo(custNo);
                makeIndcRepo.save(inchk);
            }

            try {
                log.info("4.작업지시기반 구매정보(purs_info) , 구매자재(purs_matr) 및 자재위치(matr_pos) 정보 생성 / 작업지시번호 = " + mivo.getIndcNo());
                paraMap.put("indcNo", mivo.getIndcNo());
                paraMap.put("custNo",custNo);
                this.savePursInfo(paraMap);
            }
            catch (NullPointerException ne) {
                ne.printStackTrace();
            }
        }

        return mivo.getIndcNo();
    }

    /*작업지시 추가 버튼을 클릭 후 저장 클릭 시, 주문정보 생성.*/
    private Long makeOrderByIndc(Map<String, Object> paraMap){
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());

        log.info("계획생산의 주문정보 추가 => " +paraMap);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        OrdInfo oivo = new OrdInfo();
        Long ordNo = 0L;

        try{
            ordNo = Long.parseLong(paraMap.get("ordNo").toString());
        }catch(NullPointerException ne){
            ordNo = 0L;
        }

        OrdInfo chkvo = ordRepository.findByCustNoAndOrdNoAndUsedYn(custNo,ordNo, "Y");
        Map<String,Object> tmpMap = new HashMap<String,Object>();
        tmpMap.put("custNo",custNo);
        tmpMap.put("ordNo",ordNo);
//        OrdInfo chkvo = new OrdInfo();
//        tmpMap = ordMapper.findByCustNoAndOrdNoAndUsedYn(tmpMap);
//        chkvo = (OrdInfo) StringUtil.mapToVo(tmpMap,chkvo);
        if(chkvo != null){
            oivo.setOrdNo(chkvo.getOrdNo());
        }else{
            oivo.setOrdNo(0L);
        }

        oivo.setOrdNm(paraMap.get("prodNm").toString());
        oivo.setOrdSts(Long.parseLong(env.getProperty("ord_status_acpt")));
        oivo.setOrdTp(0L);
        oivo.setPlcNo(0L);
        oivo.setUsedYn("Y");

        try{
            oivo.setOrdDt(sdf.parse(paraMap.get("makeToDt").toString()));
        }catch(ParseException e){
            e.printStackTrace();
        }catch(NullPointerException ne){
            oivo.setOrdDt(DateUtils.getCurrentDate());
        }

        try{
            oivo.setDlvReqDt(sdf.parse(paraMap.get("makeFrDt").toString()));
        }catch(ParseException e){
            e.printStackTrace();
        }

        try{
            oivo.setCmpyNo(Long.parseLong(paraMap.get("cmpyNo").toString()));
        }catch(NullPointerException ne){
            oivo.setCmpyNo(0L);
        }

        oivo.setRegDt(DateUtils.getCurrentDate());
        oivo.setRegIp(paraMap.get("ipaddr").toString());
        oivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        oivo.setCustNo(custNo);
        oivo = ordRepository.save(oivo);

        OrdProd opvo = new OrdProd();
        OrdProd chkopvo = ordProdRepository.findByCustNoAndOrdNoAndProdNoAndUsedYn(custNo,oivo.getOrdNo(), Long.parseLong(paraMap.get("prodNo").toString()), "Y");
        if(chkopvo != null){
            opvo.setOrdNo(chkopvo.getOrdNo());
            opvo.setOrdProdNo(chkopvo.getOrdProdNo());
        }else{
            opvo.setOrdProdNo(0L);
            opvo.setOrdNo(oivo.getOrdNo());
        }

        opvo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString()));
        opvo.setQtyPerPkg(1);
        opvo.setUsedYn("Y");
        /*SOL AddOn By KMJ At 21.09.04*/
        try{
            opvo.setPursNo(Long.parseLong(paraMap.get("pursNo").toString()));
        }catch(NullPointerException ne){
           opvo.setPursNo(0L);
        }
        /*EOL AddOn By KMJ At 21.09.04*/

        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        float val = 0f;
        ProdInfo chk = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,prodNo,"Y");
        if(chk != null){
            //하담푸드의 경우 vol과 mess 구분 X
            try{
                if(custNo == 3 || custNo == 2){
                    val = chk.getVol() * 0.001F;
                }
                else{
                    val = chk.getVol();
                }
            }catch(NullPointerException e){
                val = chk.getVol();
            }
        }

        Float ordQty = 0F;
        if(custNo == 6){
                Float indcWgt = Float.parseFloat(paraMap.get("indcWgt").toString());
                ordQty = ((indcWgt/val));
        }else {
            try{
                Float indcWgt = Float.parseFloat(paraMap.get("indcWgt").toString());
                ordQty = indcWgt;
            }
            catch(NullPointerException ne){
                Float indcQty = Float.parseFloat(paraMap.get("indcQty").toString());
                ordQty = indcQty;
            }
        }

        opvo.setOrdQty(ordQty);

        try{
            opvo.setSaleUnit(Long.parseLong(paraMap.get("makeUnit").toString()));
        }catch(NullPointerException ne){
            opvo.setSaleUnit(Long.parseLong(env.getProperty("code.base.sale_unit_g")));
        }

        opvo.setRegDt(DateUtils.getCurrentDate());
        opvo.setRegIp(paraMap.get("ipaddr").toString());
        opvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        opvo.setCustNo(custNo);
        ordProdRepository.save(opvo);
        return oivo.getOrdNo();
    }

    @Override
    public List<Map<String,Object>> getProdIndcNo(Map<String,Object> paraMap){
        return mapper.getProdIndcNo(paraMap);
    }

    @Override
    public Float getCurrentProdStock(Map<String, Object> paraMap) {
        return mapper.getCurrentProdStock(paraMap);
    }

    private MakeIndc saveMakeIndcInfo(Map<String,Object> paraMap) {
        String tag = "MakeIndcService.saveMakeIndcInfo=> ";
        log.info(tag + "paraMap =  " + paraMap.toString());
        Long procCd = 0L;
        Long brnchNo = 0L;
        Float indcQty = 0F;
        Float indcWgt = 0F;
        int needValDt = 0;
        int nextvalDt = 0;
        Date tomkdt = null;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Date frd;

        MakeIndc mivo = new MakeIndc();
        mivo.setUsedYn("Y");

        try {
            mivo.setParIndcNo(Long.parseLong(paraMap.get("parIndcNo").toString()));
        }
        catch (NullPointerException ne) {
            mivo.setParIndcNo(0L);
        }
        try {
            //indcNo를 가져오지 못한상태로 공정별로 루틴을 돌면서 NullPointer 발생되면서 새로운 작어지시가 계속 만들어지는 현상 발생됨.(2021.04.26 : KMJ)
            mivo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        }
        catch (NullPointerException ne) {
            mivo.setIndcNo(0L);
            mivo.setRegDt(DateUtils.getCurrentDate());
            mivo.setRegId(mivo.getModId());
            mivo.setRegIp(mivo.getModIp());
        }


        try{
            mivo.setIndcTp(Long.parseLong(paraMap.get("indcTp").toString()));
        }catch(NullPointerException ne){

        }
        try{
            mivo.setIndcSts(Long.parseLong(paraMap.get("indcSts").toString()));
        }catch (NullPointerException en){
            mivo.setIndcSts(Long.parseLong(env.getProperty("code.base.indcsts")));
        }


        procCd = Long.parseLong(paraMap.get("procCd").toString()); //공정코드목록
        mivo.setProcCd(procCd);
        if (mivo.getParIndcNo() == 0L) {
            MakeIndc chkvo = makeIndcRepo.findByCustNoAndParIndcNoAndIndcNoAndProcCdAndUsedYn(custNo,mivo.getParIndcNo(), mivo.getIndcNo(), mivo.getProcCd(), "Y");
            if (chkvo != null ) {
                if (chkvo.getParIndcNo() > 0L ) {
                    log.info(tag + "하위작업 일괄생성자료 존재함.===> ");
                    log.info(tag + "부모작업지시번호 = " + mivo.getParIndcNo());
                    log.info(tag + "작업지시번호 = " + mivo.getIndcNo());
                    log.info(tag + "제조공번호 = " + mivo.getProcCd());
                    log.info(tag + "이하 프로세스 생략함.===> ");
                    return chkvo;
                }
            }
        }
        /*충진수율 : 칭량시점에 가열공정중 수분증발등으로 인하여 줄어드는 중량을 감안하여 지지중량보다 크게 칭량하는 경우 계산되는 수율 (충진중량 / 지시중량) : 일반적으로 100% 넘을 것으로 판단됨.*/
        try{
            mivo.setFillYield(Float.parseFloat(paraMap.get("fillYield").toString()));
        }catch(NullPointerException ne){
            mivo.setFillYield(0F);
        }
        /*관리자 조정수율-결과에서만 입력됨
        try{
            mivo.setCtlFillYield(Float.parseFloat(paraMap.get("ctlFillYield").toString()));
        }catch(NullPointerException ne){
            mivo.setCtlFillYield(mivo.getFillYield());
        }
        */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long prodNo = 0L;
        Float mess = 0F;
        float val = 0f;

        try {
            mivo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString())); //품번
        }
        catch (NullPointerException ne) {
            mivo.setProdNo(0L); //품번
        }

        try {
            mivo.setOrdNo(Long.parseLong(paraMap.get("ordNo").toString())); //작업지시 추가 버튼 클릭으로 생성하는 주문번호
        }
        catch (NullPointerException ne) {
                mivo.setOrdNo(0L); //연관주문번호
        }

        prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        ProdInfo chk = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,prodNo,"Y");

        if(chk != null){
            //하담푸드의 경우 vol과 mess 구분 X
            //try{ //Remarked By KMJ At 21.10.25
                //if(custNo == 3 || custNo == 2){ //Remarked By KMJ At 21.10.25
                //    val = chk.getVol() * 0.001F; //Remarked By KMJ At 21.10.25
                //} //Remarked By KMJ At 21.10.25
                //else{ //Remarked By KMJ At 21.10.25
                    val = chk.getVol();
                    mess = chk.getMess();
                //} //Remarked By KMJ At 21.10.25
            //}catch(NullPointerException e){ //Remarked By KMJ At 21.10.25
            //   val = chk.getVol(); //Remarked By KMJ At 21.10.25
            //} //Remarked By KMJ At 21.10.25
        }

        try{
            indcQty = Float.parseFloat(paraMap.get("indcQty").toString());
        }catch(NullPointerException ne){
            indcQty = 0F;
        }

        try{
            indcWgt = Float.parseFloat(paraMap.get("indcWgt").toString());
        }catch (NullPointerException ne){
            indcWgt = 0F;
        }

        if(indcQty == 0){

            mivo.setIndcWgt(indcWgt);
            if(custNo == 2){
                mivo.setIndcQty((indcWgt));
            }else{
                mivo.setIndcQty(indcWgt / val);
            }

        }else{
            String type = paraMap.get("type").toString();
            if(type.equals("rowClick")){
                mivo.setIndcQty(indcQty);
                mivo.setIndcWgt(indcWgt);//생산지시중량
            }else{
                mivo.setIndcQty(indcWgt / val);
                mivo.setIndcWgt(indcWgt);//생산지시중량
            }

        }

        try {
            mivo.setMakeUnit(Long.parseLong(paraMap.get("makeUnit").toString())); //생산지시단위
        }
        catch (NullPointerException ne) {
            mivo.setMakeUnit(0L);
        }
        try {
            mivo.setIndcDt(sdf.parse(paraMap.get("indcDt").toString().substring(0,10)));
        } catch (ParseException e) {
            e.printStackTrace();
        }catch (NullPointerException ne) {
            mivo.setIndcDt(DateUtils.getCurrentDateTime());
        }
        try {
            try{
                brnchNo = Long.parseLong(paraMap.get("brnchNo").toString());
            }catch(NullPointerException ne){
                brnchNo = Long.parseLong(paraMap.get("brnchNo").toString());
            }

            ProcBrnch brnvo = procBrnchRepo.findByCustNoAndBrnchNoAndProcCdAndUsedYn(custNo,brnchNo, procCd, "Y");
            if (brnvo != null) {
                needValDt = brnvo.getNeedDtVal();
                nextvalDt = brnvo.getNextStepVal();
            }

            /*공정별 작업일자 계산( 추후에 더 추가할 필요있음)*/
            frd = sdf.parse(paraMap.get("indcDt").toString().substring(0,10));


            tomkdt =sdf.parse(paraMap.get("makeToDt").toString().substring(0,10));

            //작업지시의 최상단 리스트에는 생산계획에서 지정한 작업시작 종료일을 넣고,  이후 공정엔 계산된 값을 넣도록 변경 - 21.07.18
            if(procCd == 721){
                Calendar cal = Calendar.getInstance();
                cal.setTime(frd);
                cal.add(Calendar.DATE, needValDt-1);
                toDt = cal.getTime();

                mivo.setMakeFrDt(frd);
                mivo.setMakeToDt(tomkdt);
            }else{
                mivo.setMakeFrDt(toDt);

                Calendar cal = Calendar.getInstance();
                cal.setTime(toDt);
                cal.add(Calendar.DATE, needValDt+nextvalDt-1);
                toDt = cal.getTime();

                mivo.setMakeToDt(toDt);
            }


//            if(procCd == 721) {
//                if (nextvalDt > 0) {
//                    Calendar cal = Calendar.getInstance();
//                    cal.setTime(frd);
//                    cal.add(Calendar.DATE, needValDt+nextvalDt-1);
//                    toDt = cal.getTime();
//                    mivo.setMakeFrDt(frd);
//                    mivo.setMakeToDt(toDt);
//                } else{
//                    Calendar cal = Calendar.getInstance();
//                    cal.setTime(frd);
//                    cal.add(Calendar.DATE, needValDt-1);
//                    toDt = cal.getTime();
//                    mivo.setMakeFrDt(frd);
//                    mivo.setMakeToDt(toDt);
//                }
//            }else {
//                if (needValDt == 1) {
//                    Calendar cale = Calendar.getInstance();
//                    cale.setTime(frd);
//                    cale.add(Calendar.DATE, needValDt + nextvalDt-1);
//                    toDt = cale.getTime();
//                    mivo.setMakeFrDt(tomkdt);
//                    mivo.setMakeToDt(toDt);
//                }
//                if(nextvalDt > 0) {
//                    Calendar nextCal = Calendar.getInstance();
//                    nextCal.setTime(tomkdt);
//                    nextCal.add(Calendar.DATE, needValDt+nextvalDt-1);
//                    toDt = nextCal.getTime();
//                    mivo.setMakeFrDt(tomkdt);
//                    mivo.setMakeToDt(toDt);
//                }
//            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }catch (NullPointerException ne){
            try {
                mivo.setMakeFrDt(sdf.parse(paraMap.get("makeFrDt").toString()));//생산시작일
                mivo.setMakeToDt(sdf.parse(paraMap.get("makeToDt").toString()));//생산종료일
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
//            log.info(tag + "설정된 공정코드로 하루최대 생산량 추출....");
//            Map<String, Object> chkmap = new HashMap<String,Object>();
//            chkmap.put("procCd",paraMap.get("procCd"));
//            Map<String,Object> procmap = procService.getProcInfo(chkmap);
//            int maxMakeQty = 0;
//            try {
//                maxMakeQty = Integer.parseInt(procmap.get("max_makeQty").toString());
//            }
//            catch (NullPointerException ne) {
//                maxMakeQty = 0;
//            }
//            chkmap.put("procNo", mivo.getProdNo());
//           //Map<String,Object> prodmap = prodService.getProdInfo(chkmap);
//
//            int dateTerm = DateUtils.getProgressDays(paraMap.get("make_fr_dt").toString(), paraMap.get("make_to_dt").toString());
//            dateTerm++;
//            mivo.setMaxMakeQty((float) (maxMakeQty * dateTerm));
//            log.info(tag + " maxMakeQty = " + mivo.getMaxMakeQty());
//            mivo.setMaxMakeWgt(mivo.getMaxMakeQty() * val );
        mivo.setMaxMakeWgt(indcWgt);
        mivo.setMaxMakeQty(indcQty);

        try {
            mivo.setStatCd(Long.parseLong(paraMap.get("statCd").toString()));
        }
        catch (NullPointerException ne) {
            Map<String,Object> scm = new HashMap<String,Object>();
            scm.put("makeFrDt", mivo.getMakeFrDt());
            scm.put("makeToDt", mivo.getMakeToDt());
            mivo.setStatCd(mapper.getMakeIndcStatus(scm)); //시작일및 종료일을 기반으로 작업상태(대기,진행,완료 설정)
        }
        try {
            mivo.setFaultRt(Float.parseFloat(paraMap.get("faultRt").toString()));
        }
        catch (NullPointerException ne) {
            mivo.setFaultRt(0f);
        }
        try {
            mivo.setOperRt(Float.parseFloat(paraMap.get("operRt").toString()));
        }
        catch (NullPointerException ne) {
            mivo.setOperRt(0f);
        }

        try {
            mivo.setFillYield(Float.parseFloat(paraMap.get("fillYield").toString()));
        }
        catch (NullPointerException ne) {
            mivo.setFillYield(0f);
        }
        try {
            mivo.setCtlFillYield(Float.parseFloat(paraMap.get("ctlFillYield").toString()));
        }
        catch (NullPointerException ne) {
            mivo.setCtlFillYield(mivo.getFillYield());
        }
        try {
            mivo.setRealYield(Float.parseFloat(paraMap.get("realYield").toString()));
        }
        catch (NullPointerException ne) {
            mivo.setRealYield(0f);
        }

        try{
            mivo.setModDt(mivo.getModDt());
        }catch (NullPointerException e){

        }
        try{
            mivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        }catch (NullPointerException e){

        }
        try{
            mivo.setModIp(paraMap.get("ipaddr").toString());
        }catch (NullPointerException e){

        }
        try {
            mivo.setIndcCont(paraMap.get("indcCont").toString());
        }
        catch (NullPointerException ne) {
        }


        MakeIndc chkvo = makeIndcRepo.findByCustNoAndIndcNoAndProcCdAndUsedYn(custNo,mivo.getIndcNo(), mivo.getProcCd(), "Y");
        if (chkvo != null) {
            mivo.setIndcNo(chkvo.getIndcNo());
            if (mivo.getCtlFillYield()== 0f) {
                mivo.setCtlFillYield(chkvo.getCtlFillYield());
            }
            if (mivo.getOperRt() == 0f) {
                mivo.setOperRt(chkvo.getOperRt());
            }
            if (mivo.getFaultRt() == 0f) {
                mivo.setFaultRt(chkvo.getFaultRt());
            }
            if (mivo.getRealYield() == 0f) {
                mivo.setRealYield(chkvo.getRealYield());
            }
            mivo.setRegId(chkvo.getRegId());
            mivo.setRegIp(chkvo.getRegIp());
            mivo.setRegDt(chkvo.getRegDt());
        }
        else {
            mivo.setIndcNo(0L);
            mivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            mivo.setRegIp(paraMap.get("ipaddr").toString());
            mivo.setRegDt(DateUtils.getCurrentBaseDateTime());
            try {
                mivo.setFaultRt(Float.parseFloat(paraMap.get("faultRt").toString())); //불량율
            }
            catch (NullPointerException ne) {

            }
            try {
                mivo.setFillYield(Float.parseFloat(paraMap.get("fillYield").toString())); //충진수율
            }
            catch (NullPointerException ne) {

            }
            try {
                mivo.setCtlFillYield(Float.parseFloat(paraMap.get("ctlFillYield").toString())); //관지자조정수율
            }
            catch (NullPointerException ne) {

            }
            try {
                mivo.setRealYield(chkvo.getRealYield()); //운영수율
            }
            catch (NullPointerException ne) {
                MakeIndcRslt rvo = mir.findByCustNoAndIndcNoAndUsedYn(custNo,mivo.getIndcNo(),"Y");
                if (rvo != null) {
                    mivo.setRealYield(rvo.getMakeWgt() / mivo.getIndcQty());
                }
            }
            //SOL AddOn By KMJ At 21.08.06 : 생산완료 구분 필드 처리
            try {
                mivo.setClosYn(paraMap.get("closYn").toString());
            }
            catch (NullPointerException ne) {
                mivo.setClosYn("N");
            }
            //EOL AddOn By KMJ At 21.08.06 : 생산완료 구분 필드 처리
        }
        mivo.setCustNo(custNo);
        mivo = makeIndcRepo.save(mivo); //생산지시 기본정보
        log.info("저장완료 "+ mivo +"지시중량은 : " + mivo.getIndcWgt());


//        if (chkvo == null) {
//            log.info(tag + "생산지시관리번호 바코드 이미지 생성===> ");
//            Map<String, Object> bmap = new HashMap<String, Object>();
//            bmap.put("codeNo", mivo.getIndcNo());
//            bmap.put("savePath", "make/");
//            stockService.makeBarCode(bmap);
//        }
        return mivo;
    }

    private void saveMakeIndcMatr(Map<String,Object> el) {
        String tag = "MakeIndcService.saveMakeIndcMatr => ";
        Long matrNo = Long.parseLong(el.get("matrNo").toString());
        Long custNo = Long.parseLong(el.get("custNo").toString());
        MakeIndcMatr matrvo = new MakeIndcMatr();
        Long indcNo = Long.parseLong(el.get("indcNo").toString());

        matrvo.setIndcNo(indcNo);
        matrvo.setMatrNo(matrNo);
        matrvo.setCustNo(custNo);
        // getProdBomList 에서 needQty를 계산해서 가져오기 때문에 수정 --21.07.17 KTH --
//        float consistRt = Float.parseFloat(el.get("consist_rt").toString());
//        float indcWgt   = Float.parseFloat(el.get("indc_wgt").toString());
//        log.info(tag + "소요량 계산.자재번호 = " + matrvo.getMatrNo() + ": 자재명 = " + el.get("matr_nm").toString() );
//        log.info(tag + "소요량 계산.구성비율 = " + consistRt + " : 생사지시중량 = " + indcWgt );
//        matrvo.setNeedQty ((float) ( ((consistRt * 0.01) * indcWgt )) );

        matrvo.setNeedQty(Float.parseFloat(el.get("needQty").toString()));
        matrvo.setUsedYn("Y");
        matrvo.setMatrSts("N");
        // 추후 수정해줘야 합니다. 21/08/24
        matrvo.setTakeYn("N");

        try{
            matrvo.setModId(Long.parseLong(el.get("userId").toString()));
        }catch (NullPointerException e){

        } try{
            matrvo.setModDt(DateUtils.getCurrentBaseDateTime());
        }catch (NullPointerException e){

        } try{
            matrvo.setModIp(el.get("ipaddr").toString());
        }catch (NullPointerException e){
        }

        MakeIndcMatr chkvo = makeIndcMatrRepo.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo,matrvo.getIndcNo(), matrvo.getMatrNo(), "Y");
        if (chkvo != null) {
            matrvo.setIndcMatrNo(chkvo.getIndcMatrNo());
        } else {
            matrvo.setIndcMatrNo(0L);
            matrvo.setRegId(Long.parseLong(el.get("userid").toString()));
            matrvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            matrvo.setRegIp(el.get("ipaddr").toString());
        }
        matrvo.setCustNo(custNo);
        makeIndcMatrRepo.save(matrvo);
        log.info(tag + "생산지시 상품기준 소요자재정보 관리 (지시상품기준 BOM 추출) 끝.===> ");
        log.info(tag + "생산지시 소요자재정보 쟈재 출고위처정보 설정 해야 함..===> ");
    }

    @Override
    public void dropMakeIndc(Map<String, Object> paraMap) {
        Long indcNo = Long.parseLong(paraMap.get("indcNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MakeIndc vo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,indcNo,"Y");
        if(vo != null){
            log.info("1. 해당 작업지시 삭제");
            vo.setUsedYn("N");
            makeIndcRepo.save(vo);

            log.info("2. 작업지시 삭제에 따른 작업지시원료 정보 삭제");
            mapper.dropMakeIndcMatr(indcNo);

            log.info("3. 원료 출고가 있는경우");
            List<Map<String,Object>>  ds =  mapper.dropSumMatrOwh(indcNo);
            for (Map<String,Object> el : ds) {
                Long matrNo = Long.parseLong(el.get("matrNo").toString());
                Float stkQty = Float.parseFloat(el.get("owhQty").toString());
                MatrStk skvo = matrStkRepo.findByCustNoAndMatrNoAndUsedYn(custNo,matrNo,"Y");
                if(skvo != null){
                    skvo.setMatrNo(skvo.getMatrNo());
                    skvo.setMatrStkNo(skvo.getMatrStkNo());
                    skvo.setWhNo(skvo.getWhNo());
                    skvo.setStkQty(skvo.getStkQty() + stkQty);
                    skvo.setCustNo(custNo);
                    matrStkRepo.save(skvo);
                }
            }
            //mapper.dropMatrOwh(indcNo); // usedYn = N

            MakeIndc mivoForDrop = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,indcNo,"Y");
            if (mivoForDrop != null) {
                mivoForDrop.setUsedYn("N");
                mivoForDrop.setModDt(DateUtils.getCurrentBaseDateTime());
                mivoForDrop.setModId(Long.parseLong(paraMap.get("userId").toString()));
                mivoForDrop.setModIp(paraMap.get("ipaddr").toString());
                makeIndcRepo.save(mivoForDrop);
            }
            Long pursNo = 0L;
            PursInfo pivo = pursRepo.findByCustNoAndIndcNoAndUsedYn(custNo,indcNo, "Y");
            if(pivo != null){
                pursNo = pivo.getPursNo();
                //원료구매(purs_info)의 상태가 입고이거나 부분입고인 경우, 적재수량에서 입고수량만큼 감소.
                log.info("4. 원료 입고가 있는 경우");
                if(pivo.getPursSts() == Long.parseLong(env.getProperty("purs.sts.end")) || pivo.getPursSts() == Long.parseLong(env.getProperty("purs.sts.ing")) ){
                    pivo.setPursSts(Long.parseLong(env.getProperty("purs.sts.insp")));
                    pursRepo.save(pivo);

                    List<Map<String, Object>> iwhList = mapper.getDropMatrIwhList(pursNo);

                    Float iwhQty = 0F;
                    Long matrNo = 0L;
                    Long whNo = 0L;
                    for(Map<String, Object> el : iwhList){
                        iwhQty = Float.parseFloat(el.get("iwhQty").toString());
                        matrNo = Long.parseLong(el.get("matrNo").toString());
                        whNo = Long.parseLong(el.get("whNo").toString());

                        //입고된 원료 원복.
                        MatrStk msvo = matrStkRepo.findByCustNoAndWhNoAndMatrNoAndUsedYn(custNo,whNo, matrNo, "Y");
                        if(msvo != null){
                            Float qty = Float.parseFloat(msvo.getStkQty().toString()) - iwhQty;
                            msvo.setStkQty(qty);
                            msvo.setCustNo(custNo);
                            matrStkRepo.save(msvo);
                        }
                    }

                    //입고이력 삭제.
                    mapper.dropIwhList(pursNo);
                }
                //원료구매정보(purs_info)의 상태가 검수인 경우, 사용여부 N으로 변경
                else{
                    log.info("5. 원료 입고 하지 않은 경우");
                    pivo.setUsedYn("N");
                    pivo.setCustNo(custNo);
                    pivo = pursRepo.save(pivo);

                    //원료구매(purs_matr)의 사용여부 N으로 변경
                    PursInfo pivoForDrop = pursRepo.findByCustNoAndPursNoAndUsedYn(custNo,pivo.getPursNo(),"Y");
                    if (pivoForDrop != null) {
                        pivoForDrop.setUsedYn("N");
                        pivoForDrop.setModDt(DateUtils.getCurrentBaseDateTime());
                        pivoForDrop.setModId(Long.parseLong(paraMap.get("userId").toString()));
                        pivoForDrop.setModIp(paraMap.get("ipaddr").toString());
                        pivo.setCustNo(custNo);
                        pursRepo.save(pivoForDrop);
                    }
                    //mapper.dropPursMatr(pursNo);
                }
            }
        }
    }


    @Override
    public List<Map<String, Object>> getComboWorkDay(Map<String, Object> paraMap) {

        Map<String,Object> infoMap = mapper.getMakeIndcInfo(paraMap);
        List<Date> ds = null;
        List<Map<String,Object>> comboList = new ArrayList<Map<String,Object>>();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();

        try {
            Date fr = sdFormat.parse(infoMap.get("makeFrDt").toString());
            cal.setTime(fr);
            //cal.add(Calendar.DATE ,  +1);

            Date to = sdFormat.parse(infoMap.get("makeToDt").toString());



            cal.setTime(to);
            //cal.add(Calendar.DATE ,  +1);
            ds = DateUtils.getBetweenDates(fr, to);

            for (Date el : ds) {
                Map<String,Object> dateMap = new HashMap<String,Object>();
                dateMap.put("value",sdFormat.format(el));
                dateMap.put("text",sdFormat.format(el));
                comboList.add(dateMap);
            }
        }
        catch (NullPointerException ne) {

        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return comboList;
    }

    @Override
    public void saveMakeIndcMp(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String,Object>> ds = (List<Map<String,Object>>) paraMap.get("users");
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date workDt = null;
        for (Map<String,Object> el : ds) {
            el.put("indcNo", paraMap.get("indcNo"));

            MakeIndcMp mpvo = new MakeIndcMp();
            try {
                workDt = sdFormat.parse(paraMap.get("workDt").toString());
                cal.setTime(workDt);
                cal.add(Calendar.DATE, +1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mpvo.setUserId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setMpUsedDt(workDt);
            MakeIndcMp chkvo = makeMpRepo.findByCustNoAndUserIdAndMpUsedDtAndUsedYn(custNo,mpvo.getUserId(), mpvo.getMpUsedDt(),"Y");
            if (chkvo != null) continue;


            mpvo.setTotWorkHm(0);
            mpvo.setRegulWorkHm(0);//정규시간
            mpvo.setOverWorkHm(0); //잔업시간
            mpvo.setExchgWorkHm(0);
            mpvo.setFrHm("0000");
            mpvo.setToHm("0000");
            mpvo.setSpotNo(0L);
            mpvo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
            mpvo.setRegDt(mpvo.getModDt());
            mpvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setRegIp(paraMap.get("ipaddr").toString());
            mpvo.setModDt(mpvo.getModDt());
            mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setModIp(paraMap.get("ipaddr").toString());
            mpvo.setUsedYn("Y");
            mpvo.setIndcMpNo(0L);
            mpvo.setCustNo(custNo);
            makeMpRepo.save(mpvo);
        }
    }

    @Override
    public List<Map<String, Object>> getMakeIndcMpList(Map<String, Object> paraMap) {
        String tag  = "vsvc.MakeIndcService.getMakeIndcMpList => ";
        log.info(tag + "params = " + paraMap.toString());
        return mapper.getMakeIndcMpList(paraMap);
    }

    @Override
    public int getMakeIndcMpListCount(Map<String, Object> paraMap) {
        return mapper.getMakeIndcMpListCount(paraMap);
    }

    @Override
    public Map<String, Object> getMakeIndcMpInfo(Map<String, Object> paraMap) {
        return mapper.getMakeIndcMpInfo(paraMap);
    }

    @Override
    public Map<String, Object> getTimeString(Map<String, Object> paraMap) {

        return mapper.getTimeString(paraMap);
    }

    @Override
    public List<Map<String, Object>> getReqMatrList(Map<String, Object> paraMap) {
        return mapper.getReqMatrList(paraMap);
    }

    @Override
    public int getReqMatrListCount(Map<String, Object> paraMap) {
        return mapper.getReqMatrListCount(paraMap);
    }

    /*생산지지에 기반한 소요자재 출고요청*/
    @Override
    public void saveReqMatr(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.saveReqMatr==>";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        /* Remarked At 21.03.05 : 출고처리 시점에 matr_pos테이블에서 matrNo,whNo로 카운트하여 재고조정 해야 함*/
        log.info(tag + "작업지시 상품의 BOM에 근거한 출고요청 시작.지시번호(indcNo) = " + paraMap.get("indcNo"));
        List<Map<String,Object>> ds = mapper.getReqMatrList(paraMap); //작업지시 상품의 BOM에 근거한 출고요청용 소요자재 목록 추출
        Long indcNo = Long.parseLong(paraMap.get("indcNo").toString());
        for (Map<String, Object> el : ds) {
            MatrOwh owhvo = new MatrOwh();
            owhvo.setIndcNo(indcNo);
            owhvo.setMatrNo(Long.parseLong(el.get("matrNo").toString())); //소요자재번호
            owhvo.setModDt(DateUtils.getCurrentBaseDateTime());
            owhvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            owhvo.setModIp(paraMap.get("ipaddr").toString());

            MatrOwh chkvo = omr.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo,owhvo.getIndcNo(), owhvo.getMatrNo(), "Y");
            if (chkvo != null) {
                owhvo.setOwhNo(chkvo.getOwhNo()); //관리번호
                owhvo.setWhNo(chkvo.getWhNo());//창고번호
            } else {
                owhvo.setWhNo(0L);//창고번호 -> 실제출고시점에 설정됨
                owhvo.setOwhQty(Float.valueOf(0)); //출고수량-> 실제출고시점에 설정됨
                owhvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                owhvo.setRegId(Long.valueOf(paraMap.get("userId").toString()));
                owhvo.setRegIp(paraMap.get("ipaddr").toString());
            }
            owhvo.setOwhReqQty(Float.valueOf(el.get("needQty").toString())); //요청수량
            owhvo.setOwhReqDt(DateUtils.getCurrentBaseDateTime()); //출고요청일자
            owhvo.setOwhUnit(Long.parseLong(el.get("makeUnit").toString()));//요청단위
            owhvo.setUsedYn("Y");
            owhvo.setCustNo(custNo);
            owhvo = omr.save(owhvo);
            /*Remarked By KMJ At 21.10.26
            Map<String,Object> bmap = new HashMap<String,Object>();
            bmap.put("codeNo",owhvo.getOwhNo());
            bmap.put("savePath","matr/owh/");
            stockService.makeBarCode(bmap);
            log.info(tag + "바코드이미지 생성 끝........");
             */
        }
        log.info(tag + "출고요청 끝......");
    }

    @Override
    public Map<String, Object> getMakeMainProc(Map<String, Object> paraMap) {
        return mapper.getMakeMainProc(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakeStatList(Map<String, Object> paraMap) {
        return mapper.getMakeStatList(paraMap);
    }

    @Override
    public int getMakeStatListCount(Map<String, Object> paraMap) {
        return mapper.getMakeStatListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakeStatMoreList(Map<String, Object> paraMap) {
        return mapper.getMakeStatMoreList(paraMap);
    }

    @Override
    public int getMakeStatMoreListCount(Map<String, Object> paraMap) {
        return mapper.getMakeStatMoreListCount(paraMap);
    }




    @Override
    public List<Map<String, Object>> getMakeProcList(Map<String, Object> paraMap) {
        return mapper.getMakeProcList(paraMap);
    }



    /*상품별 제조공정 이외 별도 공정을 추가하는 경우 처리됨*/
    @Override
    public List<Map<String, Object>> getMakeExtraProc(Map<String, Object> paraMap) {
        return mapper.getMakeExtraProc(paraMap);
    }

    /*생산지시기반 작업에 필요한 소요자재 구매요청*/
    private PursInfo  savePursInfo(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.saveReqMatr==>";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long confirmPurs = 0L;
        /*이하 두줄 remarked 된 이유 : 작업지시 제품의 bom을 기반으로 구매요청뙨 자재가 미출고된 상태에서 다시 동일 자제를
        사용하는 상품 작업지시가 이루어진 경우 미출고된 자료가 재고로 인식되어 구매요청이 이루어 지지 않은 경우가 발생함.
        이하 4줄 추가 다시 복구함:21.02.26
        */
        log.info(tag + "1.생산지시 번호로 기존에 생성된 구매정보 삭제.생산지시번호 ===> " + paraMap.get("indcNo"));
        //mapper.initTempMatrPos(paraMap);    //생산지시번호를 기반으로 자재재고위치정보 초기화
        //mapper.initPursMatr(paraMap);       //생산지시번호를 기반으로 구매정보 초기화
        // mapper.initPursInfo(paraMap);       //생산지시번호를 기반으로 구매자재 초기화

        List<Map<String, Object>> ds = mapper.chkReqPurs(paraMap); //재고부족분이 있는 자재가 존재하는지 점검
        if (ds.size() <= 0) return null;

        log.info(tag + "paraMap = " + paraMap.toString());
        PursInfo pivo = new PursInfo();

        log.info(tag + "2.  구매요청처리 시작.......");
        pivo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        pivo.setPursDt(DateUtils.getCurrentBaseDateTime());
        pivo.setDlvReqDt(DateUtils.getCurrentBaseDateTime());
        if (env.getProperty("purs_proc_yn").equals("Y")) { //구매품의를 사용하는 업체
            confirmPurs = Long.parseLong(env.getProperty("purs.sts.insp")); //검수대기
        } else {
            confirmPurs = Long.parseLong(env.getProperty("purs.sts.end")); //구매완료
        }
        pivo.setPursSts(confirmPurs);
        try {
            pivo.setOrdNo(Long.parseLong(paraMap.get("ord_no").toString()));
        }catch (NullPointerException en){
            pivo.setOrdNo(0L);
        }
        pivo.setDlvDt(DateUtils.getCurrentDateTime());
        pivo.setUsedYn("Y");
        pivo.setModIp(paraMap.get("ipaddr").toString());
        pivo.setModDt(DateUtils.getCurrentDateTime());
        pivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        pivo.setRegIp(paraMap.get("ipaddr").toString());
        pivo.setRegDt(pivo.getRegDt());
        pivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        pivo.setCustNo(custNo);
        pivo = pir.save(pivo); //구매요청 기본정보 저장
        log.info(tag + " pirSave complete.........");

        log.info(tag + "구매요청 자재 생성 시작.지시번호 = " + paraMap.get("indcNo"));
        log.info(tag + "구매요청 자재 생성 시작.발주번호 = " + pivo.getPursNo());

        PursMatr pmvo = null;
        for (Map<String, Object> el : ds) {
            if (Float.parseFloat(el.get("reqPursQty").toString()) < 0) {
                continue; //소요자재 vs 재고량 비교, 재고부족이 아닌경우 skip
                //주의사항 : 자재입고시점에 작업지시가 이루어진 자료가 있는 경우 바로 해당 작업지번호로 직출고처리해야야 함
                //        그렇지 않은 경우 작업처리용으로 입고된 자료가 재고로 잡혀 자재입고 대상에서 제외되는 경우가 발생함.
            }
            pmvo = new PursMatr();
            pmvo.setPursNo(pivo.getPursNo());
            pmvo.setMatrNo(Long.parseLong(el.get("matrNo").toString()));
            pmvo.setCmpyNo(0L);//구매거래처는 입고 검수 시점에 MatrIwh.cmptyNo와 함꼐 설정해야 함.
            pmvo.setPursQty(Float.valueOf(el.get("reqPursQty").toString()));
            pmvo.setPursUnit(Long.parseLong(el.get("pursUnit").toString()));
            pmvo.setUsedYn("Y");
            PursMatr chkmvo = pmr.findByCustNoAndPursNoAndMatrNoAndUsedYn(custNo,pmvo.getPursNo(), pmvo.getMatrNo(), "Y");
            if (chkmvo != null) {
                pmvo.setPursMatrNo(chkmvo.getPursMatrNo());
                pmvo.setModDt(DateUtils.getCurrentBaseDateTime());
                pmvo.setModId(pivo.getModId());
                pmvo.setModIp(pivo.getModIp());
            } else {
                pmvo.setPursMatrNo(0L);
                pmvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                pmvo.setRegId(pivo.getRegId());
                pmvo.setRegIp(pivo.getRegIp());
                pmvo.setModDt(DateUtils.getCurrentBaseDateTime());
                pmvo.setModId(pivo.getModId());
                pmvo.setModIp(pivo.getModIp());
            }
            pmvo.setCustNo(custNo);
            chkmvo = pmr.save(pmvo);
            /* 입고시점에 생성되므로 여기서는 생략 (2021.03.23)
            log.info(tag + "구매자재 입고대상 자재 위치정보 생성.자재번호 = " + chkmvo.getPursMatrNo() + " : " + chkmvo.getMatrNo()); //수량이 아니고 중량임에 주의할 것.
            int vol = Integer.parseInt(el.get("vol").toString());//단위용량
            float pursQty = chkmvo.getPursQty(); //구매중량

             Map<String,Object> ckmap = new HashMap<String,Object>();
             ckmap.put("matrNo",pmvo.getMatrNo());
            log.info("aaaaaaaaaaaaa = " + ckmap.toString());
             int posCount = mapper.getPosCount(ckmap);

            int storCnt = (int) (pursQty / vol); //구매수량=구매중량/단위용량
            for (int idx = posCount; idx <= posCount + storCnt; idx++) {
                paraMap.put("pursNo", chkmvo.getPursNo());
                paraMap.put("pursMatrNo", chkmvo.getPursMatrNo());
                paraMap.put("matrNo", chkmvo.getMatrNo());
                paraMap.put("iwhQty", 1f);
                paraMap.put("iwhSeq", idx);
                this.saveMatrPos(paraMap);
                pursQty -= vol;
            }
            if (pursQty > 0) {
                log.info(tag + "구매자재 입고대상 잔여자재중량이 단위중량 이하인경우 처리.자재번호 = " + chkmvo.getPursMatrNo() + " : " + chkmvo.getMatrNo()); //수량이 아니고 중량임에 주의할 것.의할 것.
                paraMap.put("pursNo", chkmvo.getPursNo());
                paraMap.put("pursMatrNo", chkmvo.getPursMatrNo());
                paraMap.put("matrNo", chkmvo.getMatrNo());
                paraMap.put("iwhQty", pursQty);
                paraMap.put("iwhSeq", storCnt + 1);
                this.saveMatrPos(paraMap);
            }
            */
        }
        log.info(tag + "구매요청 자재 생성 끝/ 생성건수 = " + ds.size());
        log.info(tag + "구매정보 바코드이미지 생성 시작.구매번호(pursNo) = " + pivo.getPursNo());

        /*Remarked By KMJ At 21.10.25
        Map<String,Object> bmap = new HashMap<String,Object>();
        bmap.put("codeNo",pivo.getPursNo());
        bmap.put("savePath","purs/");
        stockService.makeBarCode(bmap);
        log.info(tag + "구매정보 바코드이미지 생성 끝........");
        log.info(tag + "구매요청처리 끝.......");
        */

        return pivo;
    }
    private  void saveMatrIwh(PursInfo pivo) {
        String tag = "MakeIndcService.svaematrIwh => ";
        log.info(tag + "상품의 BOM에 근거하여 구매요청된 자료를 자동으로 자재입고 자료로 생성 시작.구매번호(pursNo) = " + pivo.getPursNo());
        Long custNo = pivo.getCustNo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,Object> pmap = new HashMap<String,Object>();
        pmap.put("pursNo", pivo.getPursNo());
        pmap.put("pageNo",0);
        pmap.put("custNo",custNo);
        pmap.put("pageSz",1000);


        List<Map<String,Object>> ds = pursService.getPursMatrList(pmap);
        log.info(tag + "자재입고대상건 수 = " + ds.size());
        for (Map<String,Object> el : ds) {
            log.info(tag + "자재입고 처리.pursMatrNo =  " +el.get("pursMatrNo").toString());
            MatrIwh mivo = new MatrIwh();
            mivo.setPursNo(Long.valueOf(el.get("pursNo").toString()));
            mivo.setPursMatrNo(Long.valueOf(el.get("pursMatrNo").toString()));
            mivo.setCmpyNo(0L); //입고거래처는 입고 검수 시점에 설정해야 함.
            mivo.setMatrNo(Long.valueOf(el.get("matrNo").toString()));
            try {
                mivo.setIwhDt(sdf.parse(el.get("pursDt").toString())); //입고일자를 구매일자로 자동 설
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mivo.setIwhQty(Float.valueOf(el.get("pursQty").toString()));
            mivo.setWhNo(0L);//입고창고번호는 입고 검수 시점에 설정 해야 함,
            mivo.setUsedYn("Y");
            MatrIwh chkMatrIwh = imr.findByCustNoAndPursNoAndMatrNoAndUsedYn(custNo,mivo.getPursNo(), mivo.getMatrNo(),"Y");
            if (chkMatrIwh != null) {
                mivo.setIwhNo(chkMatrIwh.getIwhNo());
            }
            try {
                mivo.setRetnQty(Float.valueOf(String.valueOf(el.get("retnQty"))));
            } catch (NullPointerException ne) {
                mivo.setRetnQty(Float.valueOf(0));
            }
            catch(NumberFormatException ne) {
                mivo.setRetnQty(Float.valueOf(0));
            }
            mivo.setCustNo(custNo);
            imr.save(mivo);
            /*Remarked by KMJ At 21.10.25
            log.info(tag + "바코드 이미지 생성....");
            Map<String,Object> ibmap = new HashMap<String,Object>();
            ibmap.put("codeNo",mivo.getIwhNo());
            ibmap.put("savePath","matr/iwh/");
            stockService.makeBarCode(ibmap);
             */
        }
    }


    //창고레이아웃 형성.
    private void saveMatrPos(Map<String, Object> paraMap) {

        String tag = "MakeIndcService.saveMatrPos=>";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MatrPos mpvo = new MatrPos();
        mpvo.setMatrStat(Long.parseLong(env.getProperty("code.stk_iwh_stat")));
        mpvo.setMatrNo(Long.parseLong(paraMap.get("matrNo").toString()));
        mpvo.setIwhSeq(Integer.parseInt(paraMap.get("iwhSeq").toString()));
        mpvo.setWhNo(0L);
        mpvo.setColIdx(0);
        mpvo.setRowIdx(0);
        mpvo.setStairIdx(0);

        MatrPos chkmp = matrPosRepo.findByCustNoAndMatrNoAndWhNoAndStairIdxAndColIdxAndRowIdxAndIwhSeq(custNo,mpvo.getMatrNo(), mpvo.getWhNo(), mpvo.getStairIdx(), mpvo.getColIdx(), mpvo.getRowIdx(), mpvo.getIwhSeq());
        if(chkmp != null){ //기존 레이아웃이 형성되어 있을 때
            mpvo.setMatrPosNo(chkmp.getMatrPosNo());
            mpvo.setIwhSeq(chkmp.getIwhSeq());
            mpvo.setWhNo(chkmp.getWhNo());
            mpvo.setStairIdx(chkmp.getStairIdx());
            mpvo.setRowIdx(chkmp.getRowIdx());
            mpvo.setColIdx(chkmp.getColIdx());
            mpvo.setRegIp(chkmp.getRegIp());
            mpvo.setRegId(chkmp.getRegId());
            mpvo.setRegDt(chkmp.getRegDt());

            mpvo.setModDt(DateUtils.getCurrentBaseDateTime());
            mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setModIp(paraMap.get("ipaddr").toString());

            mpvo.setMatrQty(chkmp.getMatrQty());
            mpvo.setUsedYn("Y");
        }
        else{//신규 형성
            mpvo.setIwhSeq(Integer.parseInt(paraMap.get("iwhSeq").toString()));
            mpvo.setMatrQty(0f);
            mpvo.setUsedYn("Y");
            mpvo.setModDt(DateUtils.getCurrentBaseDateTime());
            mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setModIp(paraMap.get("ipaddr").toString());
            mpvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            mpvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setRegIp(paraMap.get("ipaddr").toString());
            mpvo.setMatrPosNo(0L);
        }
        mpvo.setCustNo(custNo);
        matrPosRepo.save(mpvo);
    }


    @Override
    public List<Map<String, Object>> getExportPlanList(Map<String, Object> paraMap) {
        return mapper.getExportPlanList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakeDailyReportList(Map<String, Object> paraMap) {
        return mapper.getMakeDailyReportList(paraMap);
    }

    @Override
    public int getMakeDailyReportListCount(Map<String, Object> paraMap) {
        return mapper.getMakeDailyReportListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboProcList(Map<String, Object> paraMap) {
        return mapper.getComboProcList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakeStatusList(Map<String, Object> paraMap) {
        return mapper.getMakeStatusList(paraMap);
    }
    @Override
    public int getMakeStatusListCount(Map<String, Object> paraMap) {
        return mapper.getMakeStatusListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getWorkerList(Map<String, Object> paraMap) {
        return mapper.getWorkerList(paraMap);
    }
    @Transactional //AddOn By KMJ AT 21.08.05 19:50
    @Override
    public void saveIndcRslt(Map<String, Object> passMap) {
        String tag = "MakeIndcService.saveIndcRslt => ";
        Long custNo = Long.parseLong(passMap.get("custNo").toString());
        MakeIndcRslt mirvo = new MakeIndcRslt();
        Map<String, Object> paraMap = (Map<String, Object>) passMap.get("indcRslt");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = paraMap.get("make_dt").toString().substring(0, 10);
            mirvo.setMakeDt(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //저장 및 추가 버튼 클릭 시 indcNo 구분할 것
        mirvo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        MakeIndc mivo = null;
        ProdInfo pivo = null;
        Float makeQty = 0F;
        Float mess = 0F;

        try {
            mirvo.setAdjMakeWgt(Float.parseFloat(paraMap.get("adjMakeWgt").toString()));
        } catch (NullPointerException e) {
            mirvo.setAdjMakeWgt(0F);
        }

        mivo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,mirvo.getIndcNo(), "Y");
        if (mivo != null) {
            pivo = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,mivo.getProdNo(), "Y");
            if (pivo != null) {
                mess = pivo.getMess();
            }
            try {
                makeQty = Float.parseFloat(paraMap.get("makeQty").toString());
                if(custNo==6){
                    mirvo.setMakeQty(makeQty * pivo.getQtyPerPkg());
                }else{
                    mirvo.setMakeQty(makeQty);
                }
            } catch (NullPointerException ne) {
            }
            try {
                //log.info(tag + "생산중량 계산 시작.proc_unit_nm =  " + paraMap.get("proc_unit_nm"));
                mirvo.setMakeWgt(makeQty);
            } catch (NullPointerException ne) {
            }
            try {
                mirvo.setWgtQty(Long.parseLong(paraMap.get("wgtQty").toString())); //중량미달
            } catch (NullPointerException ne) {
                mirvo.setWgtQty(0L);
            }
            try {
                mirvo.setSznQty(Long.parseLong(paraMap.get("sznQty").toString())); //배합불량
            } catch (NullPointerException ne) {
                mirvo.setSznQty(0L);
            }
            try {
                mirvo.setMetalQty(Long.parseLong(paraMap.get("metalQty").toString())); //금속검출
            } catch (NullPointerException ne) {
                mirvo.setMetalQty(0L);
            }
            try {
                mirvo.setPackQty(Long.parseLong(paraMap.get("packQty").toString())); //패키지수량
            } catch (NullPointerException ne) {
                mirvo.setPackQty(0L);
            }

            try {
                mirvo.setCtlFillYield(Float.parseFloat(paraMap.get("ctlFillYield").toString()));
            } catch (NullPointerException ne) {

            }


            mirvo.setModDt(DateUtils.getCurrentBaseDateTime());
            mirvo.setModId(Long.parseLong(passMap.get("userId").toString()));
            mirvo.setModIp(passMap.get("ipaddr").toString());
            mirvo.setUsedYn("Y");
            MakeIndcRslt chkvo = mir.findByCustNoAndIndcNoAndMakeDtAndUsedYn(custNo,mirvo.getIndcNo(), mirvo.getMakeDt(), "Y");
            if (chkvo != null) {
                mirvo.setIndcRsltNo(chkvo.getIndcRsltNo());
                mirvo.setRegId(chkvo.getRegId());
                mirvo.setRegIp(chkvo.getRegIp());
                mirvo.setRegDt(chkvo.getRegDt());
            } else {
                mirvo.setIndcRsltNo(0L);
                mirvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                mirvo.setRegId(Long.parseLong(passMap.get("userId").toString()));
                mirvo.setRegIp(passMap.get("ipaddr").toString());
            }
            log.info(tag + "1.1 작업결과 저정직전...");
            mirvo.setCustNo(custNo);
            mirvo = mir.save(mirvo);
            log.info(tag + "1.2 작업결과 저정직후...");


            if (mivo != null) {
                log.info(tag + "관리자조정 수율 및 운영수율 계산시작");
                mivo.setRealYield(mirvo.getMakeWgt() / mivo.getIndcQty());
                try {
                    mivo.setCtlFillYield(Float.parseFloat(paraMap.get("ctlFillYield").toString()));
                } catch (NullPointerException ne) {
                    mivo.setCtlFillYield(mivo.getCtlFillYield());
                }
                mivo.setRealYield((mirvo.getMakeWgt() / mivo.getIndcQty()) * 100);
                log.info(tag + "관리자조정 수율 및 운영수율 계산종료.운영수율.생산중량 = " + mirvo.getMakeWgt());
                log.info(tag + "관리자조정 수율 및 운영수율 계산종료.운영수율.지시중량 = " + mivo.getIndcQty());
                log.info(tag + "관리자조정 수율 및 운영수율 계산종료.운영수율 = " + mivo.getRealYield());
                log.info(tag + "관리자조정 수율 및 운영수율 계산종료.관리자조정수율 = " + mivo.getCtlFillYield());
                log.info(tag + "불량률 계산시작");
                float faultQty = 0f;
                if (mivo != null && pivo != null) {
                    faultQty = (float) (mirvo.getMetalQty() + mirvo.getPackQty() + mirvo.getSznQty() + mirvo.getWgtQty());
                    float indcQty = (mivo.getIndcQty() / pivo.getMess());
                    mivo.setFaultRt((faultQty / indcQty) * 100);
                    log.info(tag + "불량률 계산종료.불량율 = " + mivo.getFaultRt());
                }
                //해당 작업지시의 완제품입고 클릭 후, 제일 첫번째공정의 indc_sts 2404로 변경
                int procCd = Integer.parseInt(passMap.get("procCd").toString());
                if (procCd == 999) {
                    Long parIndcNo = Long.parseLong(paraMap.get("parIndcNo").toString()); //완제품입고의 parIndcNo 가져오기.
                    MakeIndc chkmivo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,parIndcNo, "Y");
                    if (chkmivo != null) {
                        //SOL AddOn By KMJ At 21.08.05 23:00
                        if (paraMap.get("clos_yn").toString().equals("Y")) { //생산완료
                            chkmivo.setIndcSts(Long.parseLong(env.getProperty("code.base.makeEnd")));
                        }
                        //EOL AddOn By KMJ At 21.08.05 23:00
                    }
                    makeIndcRepo.save(chkmivo);
                } else { //AddOn by KMJ At 21.08.05 19:50 --완제품입고가 아닌 경우만 처리하기 위함.
                    mivo.setCustNo(custNo);
                    makeIndcRepo.save(mivo);
                }
            }

            int procCd = Integer.parseInt(passMap.get("procCd").toString());
            if (procCd != 999) return;


            Long whNo = Long.parseLong(paraMap.get("whNo").toString());
            if (whNo == 0) return; //KMJ At 21.08.05 19:50 - 생산지시에서 결과 입력시 창고번호가 무조건 0 이므로 이하 프로세스는 절대 처리되지 않을 것으로 보임.

            log.info(tag + "2.1 제품 입고이력 생성...");
            passMap.put("indcRsltNo", mirvo.getIndcRsltNo());
            //ioService.saveProdIwhFromIndc(passMap); JPA Transction 문제로 함수사용자 않고 직접 처리

            ProdIwh iwhvo = new ProdIwh();
            Map<String, Object> rmap = new HashMap<String, Object>();

            iwhvo.setIndcRsltNo(mirvo.getIndcRsltNo());
            iwhvo.setProdNo(Long.parseLong(passMap.get("prodNo").toString()));
            iwhvo.setWhNo(Long.parseLong(passMap.get("whNo").toString()));
            iwhvo.setUsedYn("Y");
            iwhvo.setIwhDt(DateUtils.getCurrentBaseDateTime());
            iwhvo.setIwhQty(mirvo.getMakeQty());
            iwhvo.setModDt(DateUtils.getCurrentBaseDateTime());
            iwhvo.setModId(Long.parseLong(passMap.get("userId").toString()));
            iwhvo.setModIp(passMap.get("ipaddr").toString());

            ProdIwh iwhchkvo = prodIwhRepo.findByCustNoAndIndcRsltNoAndUsedYn(custNo,iwhvo.getIndcRsltNo(), iwhvo.getUsedYn());
            if (iwhchkvo != null) {
                iwhvo.setIwhNo(iwhchkvo.getIwhNo());
                iwhvo.setRegDt(iwhchkvo.getRegDt());
                iwhvo.setRegId(iwhchkvo.getRegId());
                iwhvo.setRegIp(iwhchkvo.getRegIp());
            } else {
                iwhvo.setIwhNo(0L);
                iwhvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                iwhvo.setRegId(Long.parseLong(passMap.get("userId").toString()));
                iwhvo.setRegIp(passMap.get("ipaddr").toString());
            }
            iwhvo.setCustNo(custNo);
            prodIwhRepo.save(iwhvo);

            log.info(tag + "3/2 상품재고 reset ....");
            ProdStk stkvo = new ProdStk();
            stkvo.setProdNo(iwhvo.getProdNo());
            stkvo.setUsedYn("Y");
            stkvo.setWhNo(iwhvo.getWhNo());
            Map<String, Object> stkmap = new HashMap<String, Object>();

            stkmap.put("prodNo", iwhvo.getProdNo());
            stkmap.put("whNo", iwhvo.getWhNo());
            stkvo.setStkQty(mapper.getCurrentProdStock(stkmap));
            stkvo.setStatTrfDt(DateUtils.getCurrentBaseDateTime());
            stkvo.setModId(iwhvo.getModId());
            stkvo.setModIp(iwhvo.getModIp());
            stkvo.setModDt(iwhvo.getModDt());
            stkvo.setStkDt(DateUtils.getCurrentBaseDateTime());
            ProdStk chkstk = prodStkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,stkvo.getWhNo(), stkvo.getProdNo(), "Y");
            if (chkstk != null) {
                stkvo.setStkNo(chkstk.getStkNo());
                stkvo.setRegDt(chkstk.getRegDt());
                stkvo.setRegId(chkstk.getRegId());
                stkvo.setRegIp(chkstk.getRegIp());
            } else {
                stkvo.setStkNo(0L);
                stkvo.setRegId(iwhvo.getModId());
                stkvo.setRegIp(iwhvo.getModIp());
                stkvo.setRegDt(iwhvo.getModDt());
            }
            stkvo.setCustNo(custNo);
            prodStkRepo.save(stkvo);
        }
    }

    @Transactional
    @Override
    public void dropIndcRslt(Map<String, Object> paraMap) {
        mapper.dropIndcRslt(paraMap);
    }
    @Override
    public List<Map<String, Object>> getMpUsedList(Map<String, Object> paraMap) {
        return mapper.getMpUsedList(paraMap);
    }

    @Override
    public int getMpUsedListCount(Map<String, Object> paraMap) {
        return  mapper.getMpUsedListCount(paraMap);
    }
    @Override
    public int getMaxMakeCapacityPerDay(Map<String, Object> paraMap) {
        return  mapper.getMaxMakeCapacityPerDay(paraMap);
    }

    @Override
    public void getMpDropInfo(Map<String, Object> paraMap) {
        mapper.getMpDropInfo(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMpUsedDetlList(Map<String, Object> paraMap) {
        return mapper.getMpUsedDetlList(paraMap);
    }

    @Override
    public int getMpUsedDetlListCount(Map<String, Object> paraMap) {
        return mapper.getMpUsedDetlListCount(paraMap);
    }

    //@Transactional
    @Override
    public void saveMpInfo(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.saveMpInfo => ";
        Map<String, Object> passMap = (Map<String, Object>) paraMap.get("mpi");
        //paraMap : {mpi={indcNo=151239, indcMpNo=null, procCd=722, mpUsedDt=2021-04-01T00:00:00.000Z, frHm=0700, toHm=1700, regulWorkHm=600, over_work_hm=0, exchg_work_hm=0, totWorkHm=600},
        //               workerList=[{workerId=28053, worker_nm=박금별}, {workerId=28038, worker_nm=조상범}, {workerId=2, worker_nm=김종석}]
        //               , userId=4} , request : org.apache.catalina.connector.RequestFacade@57bd70c0 ,


        List<Map<String,Object>> dsWorker =(List<Map<String,Object>>) paraMap.get("workerList");
        log.info(tag + "투입인력 카운트 = " + dsWorker.size());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        for (Map<String,Object> el : dsWorker) {
            MakeIndcMp mpInfo = new MakeIndcMp();
            mpInfo.setUserId(Long.parseLong(el.get("workerId").toString()));
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = passMap.get("mpUsedDt").toString().substring(0,10);
                mpInfo.setMpUsedDt(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
                break;
            }

            //mpInfo.setIndcNo(Long.parseLong(passMap.get("indcNo").toString()));

            //chkIndcInfo = 작업공정을 변경한 경우 해당 작업공정이 부모공정에 존재하는지 점검후 변경된공정을 검색된 부모공정에 추가.
            passMap.put("userId", paraMap.get("userId"));
            passMap.put("ipaddr", paraMap.get("ipaddr"));

            //log.info(tag + "점검전 수신 작업번호 = " + passMap.get("indcNo").toString());
            //Long rcvIndcNo = Long.parseLong(passMap.get("indcNo").toString());
            try {
                mpInfo.setIndcMpNo(Long.parseLong(passMap.get("indcMpNo").toString()));
            }
            catch (NullPointerException ne) {
                mpInfo.setIndcMpNo(0L);
            }
            mpInfo.setIndcNo(Long.parseLong(passMap.get("indcNo").toString()));
            //mpInfo.setIndcNo(this.chkIndcInfo(passMap));
            //log.info(tag + "점검후 수신 작업번호 = " + mpInfo.getIndcNo());
            //if (rcvIndcNo != mpInfo.getIndcNo()) {
            //    try {
            //        makeMpRepo.deleteById(indcMpNo);
            //    }
            //   catch(EmptyResultDataAccessException ee) {
            //    }
            //}
            mpInfo.setFrHm(passMap.get("frHm").toString());//정규시간
            mpInfo.setToHm(passMap.get("toHm").toString());//정규시간
            mpInfo.setRegulWorkHm(Integer.parseInt(passMap.get("regulWorkHm").toString()));//정규시간
            mpInfo.setOverWorkHm(Integer.parseInt(passMap.get("over_work_hm").toString())); //잔업시간
            mpInfo.setExchgWorkHm(Integer.parseInt(passMap.get("exchg_work_hm").toString())); //환산잔업시간
            mpInfo.setTotWorkHm(Integer.parseInt(passMap.get("totWorkHm").toString())); //전잔업시간

            mpInfo.setUsedYn("Y");
            mpInfo.setSpotNo(0L);
            //MakeIndcMp chkvo = makeMpRepo.findByIndcNoAndUserIdAndUsedYn(mpInfo.getIndcNo(), mpInfo.getUserId(), "Y");
            MakeIndcMp chkvo = makeMpRepo.findByCustNoAndIndcMpNoAndUsedYn(custNo,mpInfo.getIndcMpNo(), "Y");
            if (chkvo != null) {
                mpInfo.setRegIp(chkvo.getRegIp());
                mpInfo.setRegDt(chkvo.getRegDt());
                mpInfo.setRegDt(chkvo.getRegDt());

                mpInfo.setModDt(mpInfo.getModDt());
                mpInfo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                mpInfo.setModIp(paraMap.get("ipaddr").toString());
            } else {
                mpInfo.setRegDt(DateUtils.getCurrentBaseDateTime());
                mpInfo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                mpInfo.setRegIp(paraMap.get("ipaddr").toString());
            }
            mpInfo.setCustNo(custNo);
            makeMpRepo.save(mpInfo);
        }
    }
    private Long chkIndcInfo(Map<String, Object> paraMap) {
        Long retVal = 0L;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MakeIndc indcvo = new MakeIndc();
        indcvo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        indcvo.setProcCd(Long.parseLong(paraMap.get("procCd").toString()));
        MakeIndc orgvo =  makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,indcvo.getIndcNo(),"Y");
        if (orgvo != null) {
            MakeIndc chkvo =  makeIndcRepo.findByCustNoAndIndcNoAndProcCdAndUsedYn(custNo,indcvo.getIndcNo(),indcvo.getProcCd(),"Y");
            if (chkvo != null) {
                retVal = chkvo.getIndcNo();
            }
            else { //투입의 작업공정 변경시 변경된 공정의 작업지시가 없는 경우임.
                MakeIndc newIndcVo = new MakeIndc();
                newIndcVo.setIndcNo(0L);
                newIndcVo.setIndcCont(orgvo.getIndcCont());
                newIndcVo.setIndcQty(orgvo.getIndcQty());
                newIndcVo.setProcCd(indcvo.getProcCd());
                newIndcVo.setBuf(orgvo.getBuf());
                newIndcVo.setMakeFrDt(orgvo.getMakeFrDt());
                newIndcVo.setMakeToDt(orgvo.getMakeToDt());
                newIndcVo.setIdxNo(orgvo.getIdxNo());
                newIndcVo.setMakeUnit(orgvo.getMakeUnit());
                newIndcVo.setMaxMakeQty(orgvo.getMaxMakeQty());
                newIndcVo.setOrdNo(orgvo.getOrdNo());
                newIndcVo.setParIndcNo(orgvo.getParIndcNo());
                newIndcVo.setProdNo(orgvo.getProdNo());
                newIndcVo.setStatCd(orgvo.getStatCd());
                newIndcVo.setUsedYn("Y");
                newIndcVo.setModDt(DateUtils.getCurrentBaseDateTime());
                newIndcVo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                newIndcVo.setModIp(paraMap.get("ipaddr").toString());
                newIndcVo.setRegDt(DateUtils.getCurrentBaseDateTime());
                newIndcVo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                newIndcVo.setRegIp(paraMap.get("ipaddr").toString());
                chkvo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,newIndcVo.getIndcNo(), "Y");
                if (chkvo == null) {
                    newIndcVo.setIndcNo(0L);
                    newIndcVo.setCustNo(custNo);
                    newIndcVo = makeIndcRepo.save(newIndcVo);
                }
                retVal = newIndcVo.getIndcNo();
            }
        }
        return retVal;

    }

    @Transactional
    public void loadMpByExcel(HashMap<String, Object> paraMap) throws Exception {
        String tag = "MatrService.loadRawMatByExcel => ";
        StringBuffer buf = new StringBuffer();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        FileInfo fileEntity = fr.findByCustNoAndFileNoAndUsedYn(custNo,fileNo,"Y");

        buf.setLength(0);
        buf.append(fileRoot)
                .append(fileEntity.getRegId()).append(File.separator)
                .append(fileNo).append(File.separator)
                .append(fileEntity.getSaveFileNm());

        String absFilePath = buf.toString();

        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int rowindex = 0;
        XSSFSheet sheet = workbook.getSheetAt(0);

        int rows = sheet.getPhysicalNumberOfRows();
        log.info(tag + "excel처리행수 = " + rows);

        for (rowindex = 0; rowindex < rows - 1; rowindex++) {
            if (rowindex <= 1) continue; //헤더정보 skip
            UserInfo userInfo = new UserInfo();
            MakeIndcMp mpInfo = new MakeIndcMp();
            FileInfo fileInfo = new FileInfo();

            XSSFRow row = sheet.getRow(rowindex);
            if (row == null) continue;

            int cells = row.getPhysicalNumberOfCells();
            mpInfo.setUserId(0L);
            log.info(tag + " rowindex = " + rowindex);//kill

            String userNm = row.getCell(2).getStringCellValue();
            //userInfo = ur.findByUserNm(userNm);

            Date Date = row.getCell(3).getDateCellValue();
            String frhm = substring(row.getCell(5).getStringCellValue() , 0,2) +substring(row.getCell(5).getStringCellValue() , 3,5);
            String tohm = substring(row.getCell(6).getStringCellValue() , 0,2) +substring(row.getCell(6).getStringCellValue() , 3,5);


            Long starthh = Long.valueOf(substring(row.getCell(5).getStringCellValue() , 0,2));
            Long startmm = Long.valueOf(substring(row.getCell(5).getStringCellValue() , 3,5));
            Long endhh = Long.valueOf(substring(row.getCell(6).getStringCellValue() , 0,2));
            Long endmm = Long.valueOf(substring(row.getCell(6).getStringCellValue() , 3,5));

            Long Starthm = ((starthh*60)+startmm);
            Long Endhm = ((endhh*60)+endmm);


            // 7시이전출근 7시로맞춤
            if(Starthm < 420){
                Starthm = Long.valueOf(420);
            }
            Long Tot =  Endhm - Starthm;
            log.info(" 일한시간 = " + Tot);

            try {
                userInfo.getUserId();
                mpInfo.setUserId(userInfo.getUserId());
                mpInfo.setMpUsedDt(Date);
                mpInfo.setFrHm(frhm);
                mpInfo.setToHm(String.valueOf(Endhm));

                if (Tot >= 570){ //정규시간  9시 30분
                    mpInfo.setRegulWorkHm(570);
                    log.info("570분 정규시간 이상이면 570 강제 삽입" + mpInfo.getRegulWorkHm());
                }else{
                    mpInfo.setRegulWorkHm(Integer.parseInt(String.valueOf(Tot)));
                    log.info("아닐경우 그냥삽임" + mpInfo.getRegulWorkHm());
                }
                if (Tot > 570){ //환산시간 570분이상일경우계산
                    mpInfo.setExchgWorkHm(Integer.parseInt(String.valueOf(((Tot - 570)*15)/10)));

                    log.info("570분이사이면 환산시간"+mpInfo.getExchgWorkHm());
                }else{         // 아닐경우 0값
                    mpInfo.setExchgWorkHm(Integer.parseInt(String.valueOf(0)));

                    log.info("570분이하이면 0삽입"+mpInfo.getExchgWorkHm());
                    mpInfo.setTotWorkHm(Integer.parseInt(String.valueOf(Tot)));
                    log.info("그냥 최종 시간" + mpInfo.getTotWorkHm());
                }

                if(Tot > 570) { //잔업시간 위와같음
                    mpInfo.setOverWorkHm(Integer.parseInt(String.valueOf(Tot - 570)));
                    log.info("그냥 570분 이상이면 잔업시간" + mpInfo.getOverWorkHm());
                }else{
                    mpInfo.setOverWorkHm(Integer.parseInt(String.valueOf(0)));
                    log.info("아니면 0" + mpInfo.getOverWorkHm());
                }
                mpInfo.setUsedYn("Y");

                mpInfo.setTotWorkHm(Integer.parseInt(String.valueOf(570+Integer.parseInt(String.valueOf(((Tot-570)*15)/10)))));
                log.info("그냥 최종 시간" + mpInfo.getTotWorkHm());
                log.info("머냐이거"+mpInfo.getTotWorkHm());
            }
            catch (NullPointerException e) {
                mpInfo.setSpotNo(0L);
                mpInfo.setIndcMpNo(0L);
                mpInfo.setIndcNo(0L);
            }
            mpInfo.setCustNo(custNo);
            makeMpRepo.save(mpInfo);
        }
    }
    /*공정별 일 최대 생산량 : 가동율 계산시 사용됨*/
    @Override
    public Map<String, Object> getMaxMakeCapacity(Map<String, Object> paraMap) {
        return mapper.getMaxMakeCapacity(paraMap);
    }

    @Override
    public List<Map<String, Object>> getOperProdList(Map<String, Object> paraMap) {
        return mapper.getOperProdList(paraMap);
    }

    @Override
    public Map<String, Object> getStartProc(Map<String, Object> paraMap) {
        return mapper.getStartProc(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMoniterItemList(Map<String, Object> paraMap) {
        return mapper.getMoniterItemList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getManPowerSummaryList(Map<String, Object> paraMap) {
        return mapper.getManPowerSummaryList(paraMap);
    }

    @Override
    public int getManPowerSummaryListCount(Map<String, Object> paraMap) {
        return mapper.getManPowerSummaryListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getManPowerList(Map<String, Object> paraMap) {
        return mapper.getManPowerList(paraMap);
    }

    @Override
    public int getManPowerListCount(Map<String, Object> paraMap) {
        return mapper.getManPowerListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getWgtchkSummaryList(Map<String, Object> paraMap) {
        return mapper.getWgtchkSummaryList(paraMap);
    }

    @Override
    public int getWgtchkSummaryListCount(Map<String, Object> paraMap) {
        return mapper.getWgtchkSummaryListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getWgtchkDiaryList(Map<String, Object> paraMap) {
        return mapper.getWgtchkDiaryList(paraMap);
    }

    @Override
    public int getWgtchkDiaryListCount(Map<String, Object> paraMap) {
        return mapper.getWgtchkDiaryListCount(paraMap);
    }

    /*투입인력 자동생성
      파라미터 (공정코드가 999인 indcNo)
     */
    @Override
    public void autoSaveIndcMp(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.autoSaveMp =?";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String, Object> procMap = new HashMap<String, Object>();
        procMap.put("indcNo",Long.parseLong(paraMap.get("indcNo").toString()));
        List<Map<String,Object>> ds = mapper.getForMpMakerIndcList(procMap);// Excel로 작업지시 내릴때 사용
        int idx = -1;
        Long procCd = 0L;
        MakeIndcMp mpvo = new MakeIndcMp();
        Map<String,Object> mpmap = new HashMap<String,Object>();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        while(++idx < ds.size()) {
            try {
                mpmap = ds.get(idx);
                mpvo.setIndcNo(Long.parseLong(mpmap.get("indcNo").toString()));
                mpvo.setUserId(Long.parseLong(mpmap.get("userId").toString()));
                mpvo.setExchgWorkHm(0);
                mpvo.setOverWorkHm(0);
                mpvo.setFrHm(mpmap.get("frHm").toString());
                mpvo.setToHm(mpmap.get("toHm").toString());

                mpvo.setUsedYn("Y");
                mpvo.setRegulWorkHm(Integer.parseInt(mpmap.get("regulWorkHm").toString()));
                mpvo.setTotWorkHm(Integer.parseInt(mpmap.get("totWorkHm").toString()));
                try{
                    mpvo.setMpUsedDt(sdf.parse(mpmap.get("makeDt").toString()));
                }catch (NullPointerException en ) {
                    mpvo.setMpUsedDt((Date)paraMap.get("makeDt"));
                }


                mpvo.setModDt(DateUtils.getCurrentBaseDateTime());
                try {
                    mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                }catch (NullPointerException en){
                    mpvo.setModId(2L);
                }
                try{
                    mpvo.setModIp(paraMap.get("ipaddr").toString());
                }catch (NullPointerException en){
                    mpvo.setModIp("127.0.0.1");
                }

                MakeIndcMp chkvo = makeMpRepo.findByCustNoAndIndcNoAndUserIdAndUsedYn(custNo,mpvo.getIndcNo(), mpvo.getUserId(), "Y");
                if (chkvo != null) {
                    mpvo.setIndcMpNo(chkvo.getIndcMpNo());
                    mpvo.setRegDt(chkvo.getRegDt());
                    mpvo.setRegId(chkvo.getRegId());
                    mpvo.setRegIp(chkvo.getRegIp());

                } else {
                    mpvo.setIndcMpNo(0L);
                    mpvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                    try {
                        mpvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                    }catch (NullPointerException en){
                        mpvo.setRegId(2L);
                    }
                    try{
                        mpvo.setRegIp(paraMap.get("ipaddr").toString());
                    }catch (NullPointerException en){
                        mpvo.setRegIp("127.0.0.1");
                    }
                }
                mpvo.setCustNo(custNo);
                 makeMpRepo.save(mpvo);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Map<String, Object>> getMakeStatusReport(Map<String, Object> paraMap) {
        return mapper.getMakeStatusReport(paraMap);
    }

    @Override
    public int getMakeStatusReportCount(Map<String, Object> paraMap) {
        return mapper.getMakeStatusReportCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> makeIndcProc(Map<String, Object> paraMap) {
        return mapper.getmakeIndcProc(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakeIndcPrintList(Map<String, Object> paraMap){
        return mapper.getMakeIndcPrintList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakeIndcBfPrintList(Map<String, Object> paraMap){
        List<Map<String, Object>> printList = (List<Map<String, Object>>) paraMap.get("bomList");
        List<Map<String, Object>> rList = new ArrayList<>();
        for(Map<String,Object> el : printList){
            String prodNm = el.get("matrNm").toString();

            Map<String, Object> indcChk = new HashMap<>();
            indcChk.put("prodNm", prodNm);
            indcChk.put("bomLvl", 2);

            rList = mapper.getMakeIndcBfPrintList(indcChk);
            if(rList.size() > 0){
                return rList;
            }

        }
        return rList;
    }

    @Override
    public List<Map<String, Object>> getProcCtntList(Map<String, Object> paraMap){
        return mapper.getProcCtntList(paraMap);
    }

    @Override
    public void resetIndcSts(Map<String, Object> paraMap){
        mapper.resetIndcSts(paraMap);
    }

    @Override
    public List<Map<String, Object>> getNeedProdBomList(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.getNeedProdBomList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        /*SOL AddOn By KMJ AT 21.09.02 : 예상수율로 소요량 예축하기 위해 사용됨 : 대동고려삼 협의 결과임.*/
        float ctlFillYield = Float.parseFloat(paraMap.get("ctlFillYield").toString());
        ctlFillYield *= 0.01; //백분율(%) 이므로 0.1을 곱합
        paraMap.put("ctlFillYield", ctlFillYield);
        /*EOL AddOn By KMJ AT 21.09.02*/
        return mapper.getNeedProdBomList(paraMap);
    }

    //작업지시 - 자재입고 완료여부 확인
    @Override
    public List<Map<String, Object>> getMatchIndcList(Map<String, Object> paraMap){
        List<Map<String,Object>> list = new ArrayList<>();
        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("indcList");

        for (Map<String,Object> el : ds) {
            Map<String, Object> passMap = new HashMap<String,Object>();
            passMap.put("indcNo", el.get("indcNo"));

            Map<String, Object> yn = mapper.getMatchIndcList(passMap);
            log.info("yn : " + yn);
            if(yn == null){
                passMap.put("stkSts", "N");
                passMap.put("matrIwhDt", "");
            }
            else{
                log.info("yn : " + yn);
                passMap.put("stkSts", yn.get("stkSts"));
                try{
                    passMap.put("matrIwhDt", yn.get("matrIwhDt").toString().substring(0, 10));
                }catch(NullPointerException ne){
                    passMap.put("matrIwhDt" , "");
                }

            }


            list.add(passMap);
        }
        log.info("list는 " + list);
        return list;
    }

    @Override
    public List<Map<String, Object>> getIndcListByProc(Map<String, Object> paraMap){
        return mapper.getIndcListByProc(paraMap);
    }

    @Override
    public int getFaultListCount(Map<String, Object> paraMap) {
        return mapper.getFaultListCount(paraMap);
    }


    @Override
    public List<Map<String, Object>> getIndcSaltList(Map<String, Object> paraMap){
        return mapper.getIndcSaltList(paraMap);
    }

    @Override
    public int getIndcSaltListCount(Map<String, Object> paraMap){
        return mapper.getIndcSaltListCount(paraMap);
    }

    @Override
    public void saveIndcSaltList(Map<String, Object> paraMap){
        Long prodNo = 0L;
        Long indcNo = 0L;
        Float val = 0F;
        MakeIndcRslt mirvo = new MakeIndcRslt();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        paraMap.put("searchTp", "first");
        Map<String,Object> saltMap = mapper.getIndcListBySalt(paraMap);
        if(Float.parseFloat(saltMap.get("parIndcNo").toString()) == 0F){
            indcNo = Long.parseLong(saltMap.get("indcNo").toString());
        }
        else{
            indcNo = Long.parseLong(saltMap.get("parIndcNo").toString());
        }


        Long procCd = Long.parseLong(paraMap.get("procCd").toString());

        prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        ProdInfo chk = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,prodNo,"Y");
        if(chk != null){

            //하담푸드의 경우 vol과 mess 구분 X
            try{
                if(custNo == 3){
                    val = chk.getSpga();
                }
                else{
                    val = chk.getVol();
                }
            }catch(NullPointerException e){
                val = chk.getVol();
            }
        }

        MakeIndc michkvo = makeIndcRepo.findByCustNoAndIndcNoAndProcCdAndUsedYn(custNo,indcNo, procCd, "Y");
        if(michkvo != null){
            indcNo = michkvo.getIndcNo();
        }

        MakeIndcRslt chkvo = mir.findByCustNoAndIndcNoAndUsedYn(custNo,indcNo, "Y");
        if(chkvo != null){
            chkvo.setModDt(DateUtils.getCurrentBaseDateTime());
            chkvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            chkvo.setModIp(paraMap.get("ipaddr").toString());
            chkvo.setMakeQty(Float.parseFloat(paraMap.get("makeQty").toString()));
            chkvo.setMakeWgt(Float.parseFloat(paraMap.get("makeQty").toString()) * val);//생산지시중량
            chkvo.setCustNo(custNo);
            mir.save(chkvo);
        }
        else{
            mirvo.setIndcRsltNo(0L);
            mirvo.setIndcNo(indcNo);
            mirvo.setAdjMakeQty(0F);
            mirvo.setAdjMakeWgt(0F);

            mirvo.setMakeQty(Float.parseFloat(paraMap.get("makeQty").toString()));
            mirvo.setMakeWgt(Float.parseFloat(paraMap.get("makeQty").toString()) * val);//생산지시중량
            mirvo.setMetalQty(0L);
            mirvo.setPackQty(0L);
            mirvo.setSznQty(0L);
            mirvo.setWgtQty(0L);

            mirvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            mirvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            mirvo.setRegIp(paraMap.get("ipaddr").toString());
            mirvo.setUsedYn("Y");
            mirvo.setCustNo(custNo);
            mir.save(mirvo);
        }
    }

    @Transactional //AddOn By KMJ AT 21.08.05 19:50
    @Override
    public void saveIndcPrintText(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.saveIndcRslt => ";
        Long indcNo = Long.parseLong(paraMap.get("indcNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MakeIndc michkvo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,indcNo, "Y");
        try{
            michkvo.setIndcCont(paraMap.get("indcCont").toString());
        }catch(NullPointerException e){
            michkvo.setIndcCont(null);
        }
        michkvo.setCustNo(custNo);
        makeIndcRepo.save(michkvo);

    }

    @SneakyThrows
    @Transactional
    @Override
    public void planSave(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        MakeWorkPlan mwpvo = new MakeWorkPlan();

        MakeWorkPlan chkvo = makeWorkPlanRepo.findByCustNoAndPlanDtAndBrnchNoAndUsedYn(custNo, sdf.parse(paraMap.get("planDt").toString()), Long.parseLong(paraMap.get("brnchNo").toString()), "Y");

        if (chkvo != null) {
            mwpvo.setBrnchNo(chkvo.getBrnchNo());
            mwpvo.setCustNo(chkvo.getCustNo());
            mwpvo.setPlanDt(chkvo.getPlanDt());
            mwpvo.setPlanNo(chkvo.getPlanNo());
            mwpvo.setUsedYn("Y");

            mwpvo.setTextArea(paraMap.get("textArea").toString());
        } else {
            mwpvo.setBrnchNo(Long.parseLong(paraMap.get("brnchNo").toString()));
            mwpvo.setCustNo(custNo);
            mwpvo.setPlanDt(sdf.parse(paraMap.get("planDt").toString()));
            mwpvo.setPlanNo(0L);
            mwpvo.setUsedYn("Y");

            mwpvo.setTextArea(paraMap.get("textArea").toString());
        }


        makeWorkPlanRepo.save(mwpvo);
    }
}
