package daedan.mes.prod.service;

import daedan.mes.ccp.domain.HeatLmtInfo;
import daedan.mes.ccp.repository.HeatLmtInfoRepository;
import daedan.mes.cmpy.domain.CmpyInfo;
import daedan.mes.cmpy.repository.CmpyRepository;
import daedan.mes.code.domain.CodeInfo;
import daedan.mes.code.repository.CodeRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.file.service.FileService;
import daedan.mes.io.domain.MatrIwh;
import daedan.mes.io.domain.MatrOwh;
import daedan.mes.io.domain.ProdIwh;
import daedan.mes.io.domain.ProdOwh;
import daedan.mes.io.repository.MatrIwhRepository;
import daedan.mes.io.repository.MatrOwhRepository;
import daedan.mes.io.repository.ProdIwhRepository;
import daedan.mes.io.repository.ProdOwhRepository;
import daedan.mes.make.domain.MakeIndc;
import daedan.mes.make.domain.MakeIndcMatr;
import daedan.mes.make.mapper.MakeIndcMapper;
import daedan.mes.make.repository.MakeIndcMatrRepository;
import daedan.mes.make.repository.MakeIndcRepository;
import daedan.mes.make.service.ExcelMakeService;
import daedan.mes.make.service.MakeIndcService;
import daedan.mes.matr.domain.MatrCmpy;
import daedan.mes.matr.domain.MatrInfo;
import daedan.mes.matr.domain.MatrFormat;
import daedan.mes.matr.repository.MatrCmpyRepository;
import daedan.mes.matr.repository.MatrRepository;
import daedan.mes.ord.domain.OrdInfo;
import daedan.mes.ord.domain.OrdProd;
import daedan.mes.ord.repository.OrdProdRepository;
import daedan.mes.ord.repository.OrdRepository;
import daedan.mes.prod.domain.*;
import daedan.mes.prod.mapper.ProdMapper;
import daedan.mes.prod.repository.ProdAttrRepository;
import daedan.mes.prod.repository.ProdBomRepository;
import daedan.mes.prod.repository.ProdBrnchRepository;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.purs.domain.PursInfo;
import daedan.mes.purs.domain.PursMatr;
import daedan.mes.purs.repository.PursInfoRepository;
import daedan.mes.purs.repository.PursMatrRepository;
import daedan.mes.purs.service.PursService;
import daedan.mes.stock.domain.MatrStk;
import daedan.mes.stock.domain.ProdStk;
import daedan.mes.stock.domain.WhInfo;
import daedan.mes.stock.repository.MatrStkRepository;
import daedan.mes.stock.repository.ProdStkRepository;
import daedan.mes.stock.repository.WhInfoRepository;
import daedan.mes.stock.service.StockService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.jdbc.Null;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("prodService")
public class ProdServiceImpl implements  ProdService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;
    @Autowired
    private MatrIwhRepository imr;
    @Autowired
    private MakeIndcMapper inmapper;
    @Autowired
    private PursService pursService;
    @Autowired
    private MatrOwhRepository omr;
    @Autowired
    private ProdRepository prodRepository;

    @Autowired
    private ProdAttrRepository prodAttrRepo;


    @Autowired
    private HeatLmtInfoRepository heatLmtInfoRepo;


    @Autowired
    private ProdOwhRepository owhRepo;
    @Autowired
    private MatrStkRepository matStkRepo;
    @Autowired
    private WhInfoRepository whinfoRepo;
    @Autowired
    private ProdIwhRepository iwhRepo;
    @Autowired
    private ProdStkRepository stkRepo;
    @Autowired
    private ProdBomRepository bomRepo;

    @Autowired
    private CmpyRepository cmpyRepo;
    @Autowired
    private ProdBrnchRepository pbr;
    @Autowired
    private OrdRepository ordRepo;
    @Autowired
    private MatrRepository matrRepo;
    @Autowired
    private MakeIndcRepository makeIndcRepo;
    @Autowired
    private OrdProdRepository ordprRepo;
    @Autowired
    private PursMatrRepository pmr;
    @Autowired
    private PursInfoRepository pir;
    @Autowired
    private MatrCmpyRepository matrCmpyRepo;

    @Autowired
    private ProdMapper mapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ExcelMakeService excelMakeService;

    @Autowired
    private MakeIndcMatrRepository makeIndcMatrRepo;

    @Autowired
    private MakeIndcService indcService;

    @Autowired
    private ProdService prodService;

    @Override
    public List<Map<String, Object>> getProdList(Map<String, Object> paraMap) {
        return mapper.getProdList(paraMap);
    }

    @Override
    public int getProdListCount(Map<String, Object> paraMap) {
        return mapper.getProdListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdExcelList(Map<String, Object> paraMap) {
        return mapper.getProdExcelList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getBomExcelList(Map<String, Object> paraMap) {
        return mapper.getBomExcelList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdByPrefixList(Map<String, Object> paraMap) {
        List<Map<String, Object>> ds = this.getProdList(paraMap);
        paraMap.put("pagNo", 1);
        paraMap.put("pagSz", Integer.parseInt(env.getProperty("max_page_sz")));
        List<Map<String, Object>> findDs = new ArrayList<Map<String, Object>>();
        boolean isfind = false;
        for (Map<String, Object> el : ds) {
            String prodNm = el.get("prod_nm").toString();
            String findPrefix = paraMap.get("findSz").toString();
            isfind = StringUtil.matchString(prodNm, findPrefix);
            if (isfind) {
                findDs.add(el);
            }
        }
        return findDs;
    }

    @Override
    public void dropProdInfo(Map<String, Object> paraMap) {
        String tag = "prodService.dropProdInfo => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        ProdInfo vo = prodRepository.findByCustNoAndProdNoAndUsedYn(custNo,prodNo,"Y");
        if (vo != null) {
            vo.setUsedYn("N");
            vo.setModDt(DateUtils.getCurrentBaseDateTime());
            vo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            vo.setModIp(paraMap.get("ipaddr").toString());
        }

    }

    @Override
    public List<Map<String, Object>> getProdBomList(Map<String, Object> paraMap) {
        String tag = "vsvc.ProdService.prodBomList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProdBomList(paraMap);
    }

    @Override
    public int getProdBomListCount(Map<String, Object> paraMap) {
        return mapper.getProdBomListCount(paraMap);
    }


    @Override
    public List<Map<String, Object>> getHdfdProdBomList(Map<String, Object> paraMap) {
        String tag = "vsvc.ProdService.prodBomList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getHdfdProdBomList(paraMap);
    }
    @Override
    public int getHdfdProdBomListCount(Map<String, Object> paraMap) {
        return mapper.getHdfdProdBomListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getHdfdProdBomListByIndc(Map<String, Object> paraMap) {
        String tag = "vsvc.ProdService.prodBomList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getHdfdProdBomListByIndc(paraMap);
    }

    @Override
    public List<Map<String, Object>> getSfProdBomListByIndc(Map<String, Object> paraMap) {
        String tag = "vsvc.ProdService.prodBomList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getSfProdBomListByIndc(paraMap);
    }


    @Override
    public List<Map<String, Object>> getProdBomListByIndc(Map<String, Object> paraMap) {
        String tag = "vsvc.ProdService.prodBomList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProdBomListByIndc(paraMap);
    }

    @Override
    public List<Map<String, Object>> getAbleOnProdBomList(Map<String, Object> paraMap) {
        return mapper.getAbleOnProdBomList(paraMap);
    }

    @Override
    public int getAbleOnProdBomListCount(Map<String, Object> paraMap) {
        return mapper.getAbleOnProdBomListCount(paraMap);
    }

    @Override
    public Map<String, Object> getProdInfo(Map<String, Object> paraMap) {
        String tag = "vsvc.ProdService.getProdInfo => ";
        log.info(tag + "paraMap = " + paraMap.toString());

        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        ProdInfo pivo = prodRepository.findByCustNoAndProdNoAndUsedYn(custNo,prodNo, "Y");
        if (pivo != null) {
            if (pivo.getCcpTp() != Long.parseLong(env.getProperty("code.ccp_tp.insp"))) { //살균공정을 포함한 경우
                HeatLmtInfo hlvo = heatLmtInfoRepo.findByHeatTpAndUsedYn(pivo.getHeatTp(), "Y");
                if (hlvo != null) {
                    pivo.setHeatLmtInfo(hlvo);
                }
                ProdAttr pavo = prodAttrRepo.findByCustNoAndProdNoAndUsedYn(custNo,pivo.getProdNo(), "Y");
                if (pavo != null) {
                    pivo.setProdAttr(pavo);
                }
                //StringBuffer buf = new StringBuffer();
                //buf.append(env.getProperty("file.root.path")).append("prod/").append(pivo.getProdNo()).append(".png");
                //log.info(tag + "bar_code_url = " + rmap.get("bar_code_url"));
            }
        }
        Long cmpyTp = Long.parseLong(env.getProperty("code.cmpytp.sale"));
        Map<String, Object> rmap = StringUtil.voToMap(pivo);
        if(pivo.getCmpyNo() != null){
            CmpyInfo cmpy = cmpyRepo.findByCustNoAndCmpyTpAndCmpyNoAndUsedYn(custNo,cmpyTp,pivo.getCmpyNo() ,"Y");
            if (cmpy != null){
                rmap.put("cmpyNm",cmpy.getCmpyNm());
            }
        }
        log.info("rmap => " + rmap);
        return rmap;
    }

    @Override
    @Transactional
    public Map<String, Object> saveProd(Map<String, Object> paraMap) {
        String tag = "ProdService.saveProd => ";
        Long prodNo = 0L;
        Long cmpyNo = 0L;
        Long fileNo = 0L;
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        StringBuffer buf = new StringBuffer();
        try {
            prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        } catch (NullPointerException ne) {
            prodNo = 0L;
        }
        try {
            fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        } catch (NullPointerException ne) {
            fileNo = 0L;
        }
        //SOL Addon by KMJ At  21.10.19 - 판매유형(OEM,ODM) 구분
        try {
            cmpyNo = Long.parseLong(paraMap.get("cmpyNo").toString());
        } catch (NullPointerException ne) {
            cmpyNo = 0L;
        }
        //EOL Addon by KMJ At  21.10.19 - 판매유형(OEM,ODM) 구분
        ProdInfo prodInfo = new ProdInfo();
        prodInfo.setCustNo(custNo);
        prodInfo.setProdNo(prodNo);
        prodInfo.setProdNm(paraMap.get("prodNm").toString());
        prodInfo.setProdTp(Long.parseLong(paraMap.get("prodTp").toString())); //AddOn By KMJ At 21.10.19 --판매구분(OEM,ODM)
        prodInfo.setCmpyNo(cmpyNo); //AddOn By KMJ At 21.10.19 --OEM업체번호
        prodInfo.setCustNo(Long.parseLong(paraMap.get("custNo").toString())); //AddOn By KMJ At 21.10.21
        try {
            prodInfo.setBomLvl(Long.parseLong(paraMap.get("bomLvl").toString()));
        }
        catch (NullPointerException ne) {
            prodInfo.setBomLvl(1L);
        }
        try {
            prodInfo.setErpProdNm(paraMap.get("erpProdNm").toString());
        } catch (NullPointerException ne) {

        }
        try {
            prodInfo.setMess(Float.parseFloat(paraMap.get("mess").toString()));
        }
        catch (NullPointerException ne) {
            prodInfo.setMess(1f);
        }
        try {
            prodInfo.setProdShape(Long.parseLong(paraMap.get("prodShape").toString()));
        }
        catch (NullPointerException ne) {
            prodInfo.setProdShape(0L);
        }

        Float spga = 1F;
        try {
            spga = Float.parseFloat(paraMap.get("spga").toString());
        } catch (NullPointerException ne) {
            spga = 1f;
        }

        prodInfo.setSpga(spga);
        Float vol = 1f;
        try {
            vol = (Float.parseFloat(paraMap.get("mess").toString()) * spga) / 1000;
        }
        catch (NullPointerException ne) {
            vol = 1f;
        }

        try {
            prodInfo.setVol(vol);
        } catch (NullPointerException ne) {
            prodInfo.setVol(prodInfo.getMess());
        }

        try {
            prodInfo.setHeatTp(Long.parseLong(paraMap.get("heatTp").toString()));
        } catch (NullPointerException ne) {
            prodInfo.setHeatTp(0L);
        }

        try {
            prodInfo.setSz(paraMap.get("sz").toString());
        } catch (NullPointerException ne) {
            prodInfo.setSz(Float.toString(prodInfo.getMess()));
        }

        //영양제과의 경우, 공정 없음.
        try {
            prodInfo.setBrnchNo(Long.parseLong(paraMap.get("brnchNo").toString()));
        } catch (NullPointerException ne) {
            prodInfo.setBrnchNo(1252L);
        }
        try {
            prodInfo.setSaveTmpr(Long.parseLong(paraMap.get("saveTmpr").toString()));
        }
        catch (NullPointerException ne) {
            prodInfo.setSaveTmpr(Long.parseLong(env.getProperty("code.savetmpr.room")));
        }
        try {
            prodInfo.setMngrUnit(Long.parseLong(paraMap.get("mngrUnit").toString()));
        }
        catch (NullPointerException ne) {
            prodInfo.setSaveTmpr(Long.parseLong(env.getProperty("code.base.mngrbase_qty")));
        }
        try {
            prodInfo.setCcpTp(Long.parseLong(paraMap.get("ccpTp").toString()));
        }
        catch (NullPointerException ne) {
            prodInfo.setCcpTp(0L);
        }
        try {
            prodInfo.setSaleUnit(Long.parseLong(paraMap.get("saleUnit").toString()));
        } catch (NullPointerException ne) {
            prodInfo.setSaleUnit(Long.parseLong(env.getProperty("code.base.sale_unit_ea")));
        }
        try {
            prodInfo.setQtyPerPkg(Integer.parseInt(paraMap.get("qtyPerPkg").toString()));
        }
        catch (NullPointerException ne) {
            prodInfo.setQtyPerPkg(1);
        }
        try {
            prodInfo.setModlNm((String) paraMap.get("modlNm"));
        } catch (NullPointerException ne) {

        }
        try {
            prodInfo.setProdCode((String) paraMap.get("prodCode"));
        } catch (NullPointerException ne) {

        }
        try {
            prodInfo.setValidTerm(Integer.parseInt(paraMap.get("validTerm").toString()));
        } catch (NullPointerException ne) {

        }

        try {
            prodInfo.setProdCont(paraMap.get("prodCont").toString());
        } catch (NullPointerException ne) {

        }

        try {
            prodInfo.setMinPh(Float.parseFloat(paraMap.get("minPh").toString()));
        } catch (NullPointerException ne) {
            prodInfo.setMinPh(7F);
        }

        try {
            prodInfo.setMaxPh(Float.parseFloat(paraMap.get("maxPh").toString()));
        } catch (NullPointerException ne) {
            prodInfo.setMaxPh(7F);
        }


        try {
            prodInfo.setFileNo(fileNo);
        } catch (NullPointerException ne) {
            prodInfo.setFileNo(0L);
        }
        prodInfo.setModIp((String) paraMap.get("ipaddr"));
        prodInfo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        prodInfo.setModDt(prodInfo.getModDt());
        prodInfo.setUsedYn("Y");

        if (prodInfo.getProdNo() == 0L) {
            prodInfo.setRegIp(prodInfo.getModIp());
            prodInfo.setRegId(prodInfo.getModId());
            prodInfo.setRegDt(prodInfo.getModDt());
        }

        ProdAttr pavo = this.saveProdAttr(paraMap);

        prodInfo.setProdAttr(pavo);

        HeatLmtInfo hlivo = this.saveProdHeatLmtInfo(paraMap);
        prodInfo.setHeatLmtInfo(hlivo);

        prodInfo = prodRepository.save(prodInfo);


        /* 바코드이미지생성 Remarked By KMJ AT 21.10.21
        Map<String, Object> bmap = new HashMap<String, Object>();
        bmap.put("codeNo", prodInfo.getProdNo());
        bmap.put("savePath", "prod/");
        stockService.makeBarCode(bmap);
        */
        if (fileNo > 0L) {
            Map<String, Object> hmap = new HashMap<String, Object>();
            hmap.put("fileNo", paraMap.get("fileNo"));
            hmap.put("custNo", custNo);
            fileService.revivalFileUsed(hmap);
        }
        return StringUtil.voToMap(prodInfo);
    }

    @Transactional
    public ProdAttr saveProdAttr(Map<String, Object> paraMap) {
        ProdAttr pavo = new ProdAttr();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        pavo.setCustNo(custNo);
        pavo.setUsedYn("Y");
        try {
            pavo.setProdType(Long.parseLong(paraMap.get("attrTp").toString()));
        } catch (NullPointerException ne) {
            pavo.setProdType(2902L);
        }

        try {
            pavo.setChroma(Float.parseFloat(paraMap.get("chroma").toString()));
        } catch (NullPointerException ne) {
            pavo.setChroma(0F);
        }

        try {
            pavo.setMaxBrix(Float.parseFloat(paraMap.get("maxBrix").toString()));
        } catch (NullPointerException ne) {
            pavo.setMaxBrix(0F);
        }

        try {
            pavo.setMinBrix(Float.parseFloat(paraMap.get("minBrix").toString()));
        } catch (NullPointerException ne) {
            pavo.setMinBrix(0F);
        }

        try {
            pavo.setHowOld(Long.parseLong(paraMap.get("howOld").toString()));
        } catch (NullPointerException ne) {
            pavo.setHowOld(0L);
        }

        try {
            pavo.setJinseno(Float.parseFloat(paraMap.get("jinseno").toString()));
        } catch (NullPointerException ne) {
            pavo.setJinseno(0F);
        }

        try {
            pavo.setSolid(Float.parseFloat(paraMap.get("solid").toString()));
        } catch (NullPointerException ne) {
            pavo.setSolid(0F);
        }

        try {
            pavo.setMaxVisco(Float.parseFloat(paraMap.get("maxVisco").toString()));
        } catch (NullPointerException ne) {

        }

        try {
            pavo.setMinVisco(Float.parseFloat(paraMap.get("minVisco").toString()));
        } catch (NullPointerException ne) {

        }

        try {
            pavo.setUsedTp(Long.parseLong(paraMap.get("usedTp").toString()));
        } catch (NullPointerException ne) {
            pavo.setUsedTp(0L);
        }
        pavo.setCustNo(Long.parseLong(paraMap.get("custNo").toString())); //AddOn By KMJ At 21.10.21
        try {
        ProdAttr chkpavo = prodAttrRepo.findByCustNoAndProdNoAndUsedYn(custNo,Long.parseLong(paraMap.get("prodNo").toString()), "Y");
            pavo.setProdAttrNo(chkpavo.getProdAttrNo());
            pavo.setProdNo(chkpavo.getProdNo());
        }
        catch (NullPointerException ne){
            try {
                pavo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString()));
            }
            catch (NullPointerException ne2) {
                pavo.setProdNo(0L);
            }
            pavo.setProdAttrNo(0L);
        }
        pavo = prodAttrRepo.save(pavo);

        return pavo;
    }

    @Transactional
    public HeatLmtInfo saveProdHeatLmtInfo(Map<String, Object> paraMap) {
        HeatLmtInfo hlivo = new HeatLmtInfo();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        try {
            hlivo.setMinHeatTime(Float.parseFloat(paraMap.get("minHeatTime").toString()));
        } catch (NullPointerException ne) {
            hlivo.setMinHeatTime(0F);
        }

        try {
            hlivo.setMaxHeatTime(Float.parseFloat(paraMap.get("maxHeatTime").toString()));
        } catch (NullPointerException ne) {
            hlivo.setMaxHeatTime(0F);
        }

        try {
            hlivo.setMinHeat(Float.parseFloat(paraMap.get("minHeat").toString()));
        } catch (NullPointerException ne) {
            hlivo.setMinHeat(0F);
        }
        try {
            hlivo.setMaxHeat(Float.parseFloat(paraMap.get("maxHeat").toString()));
        } catch (NullPointerException ne) {
            hlivo.setMaxHeat(0F);
        }
        hlivo.setCustNo(custNo);
        hlivo.setUsedYn("Y");

        HeatLmtInfo chkhli = heatLmtInfoRepo.findByHeatTpAndUsedYn(Long.parseLong(paraMap.get("heatTp").toString()), "Y");
        if (chkhli != null) {
            hlivo.setLmtNo(chkhli.getLmtNo());
            hlivo.setHeatTp(chkhli.getHeatTp());

            hlivo.setModDt(DateUtils.getCurrentBaseDateTime());
            hlivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            hlivo.setModIp(paraMap.get("ipaddr").toString());

        } else {
            hlivo.setLmtNo(0L);
            hlivo.setHeatTp(Long.parseLong(paraMap.get("heatTp").toString()));

            hlivo.setRegDt(DateUtils.getCurrentBaseDateTime());
            hlivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            hlivo.setRegIp(paraMap.get("ipaddr").toString());

        }

        hlivo = heatLmtInfoRepo.save(hlivo);

        return hlivo;
    }


    @Override
    @Transactional
    public void apndProdBom(Map<String, Object> paraMap) {
        Map<String, Object> mapProd = (Map<String, Object>) paraMap.get("prodInfo");
        List<Map<String, Object>> matrList = (List<Map<String, Object>>) paraMap.get("matrList");
        ProdBom prodBom = null;
        Long custNo = Long.parseLong(mapProd.get("custNo").toString());
        Map<String, Object> matrMap = new LinkedHashMap<String, Object>();
        Long prodNo = Long.parseLong(mapProd.get("prodNo").toString());
        for (int idx = 0; idx < matrList.size(); idx++) {
            prodBom = new ProdBom();
            matrMap = matrList.get(idx);
            prodBom.setProdNo(prodNo);
            prodBom.setMatrNo(Long.parseLong(matrMap.get("matrNo").toString()));
            prodBom.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            prodBom.setRegIp((String) paraMap.get("ipaddr"));
            prodBom.setModId(Long.parseLong(paraMap.get("userId").toString()));
            prodBom.setModIp((String) paraMap.get("ipaddr"));
            prodBom.setRegDt(DateUtils.getCurrentDate());
            prodBom.setModDt(DateUtils.getCurrentDate());
            prodBom.setCustNo(Long.parseLong(paraMap.get("custNo").toString())); //AddOn By KMJ At 21.10.21
            prodBom.setUsedYn("Y");
            ProdBom chkvo = bomRepo.findByCustNoAndProdNoAndMatrNoAndUsedYn(custNo,prodBom.getProdNo(), prodBom.getMatrNo(), "Y");
            if (chkvo != null) {
                prodBom.setBomNo(chkvo.getBomNo());
            }
            bomRepo.save(prodBom);
        }
    }

    //BOM 복사하기
    @Override
    @Transactional
    public void copyProdBom(Map<String, Object> paraMap) {
        //복사해야하는 제품번호는 prod_no , 복사할 제품번호는 prodNo(copyProdNo)
        List<Map<String, Object>> prodBomList = mapper.getProdBom(paraMap);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        ProdBom prodBom = null;

        Map<String, Object> matrMap = new LinkedHashMap<String, Object>();

        for (int idx = 0; idx < prodBomList.size(); idx++) {
            prodBom = new ProdBom();
            matrMap = prodBomList.get(idx);
            prodBom.setProdNo(Long.parseLong(paraMap.get("prod_no").toString()));
            prodBom.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            prodBom.setRegIp((String) paraMap.get("ipaddr"));
            prodBom.setRegDt(DateUtils.getCurrentDate());

            prodBom.setMatrNo(Long.parseLong(matrMap.get("matr_no").toString()));
            prodBom.setPursYn(matrMap.get("purs_yn").toString());
            prodBom.setConsistRt(Float.parseFloat(matrMap.get("consist_rt").toString()));
            prodBom.setBomLvl(Long.parseLong(matrMap.get("bom_lvl").toString()));
            prodBom.setCustNo(Long.parseLong(paraMap.get("custNo").toString())); //AddOn By KMJ At 21.10.21
            prodBom.setUsedYn("Y");
            ProdBom chkvo = bomRepo.findByCustNoAndProdNoAndMatrNoAndUsedYn(custNo, prodBom.getProdNo(), prodBom.getMatrNo(), "Y");
            if (chkvo != null) {
                prodBom.setBomNo(chkvo.getBomNo());
            }
            bomRepo.save(prodBom);
        }
    }

    /*직접입력하는 경우*/
    @Override
    @Transactional
    public void saveProdBomByInput(Map<String, Object> paraMap) {
        String tag = "prodService.saveProdBomByInput =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        ProdBom bomvo = new ProdBom();
        bomvo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString()));
        bomvo.setMatrNo(Long.parseLong(paraMap.get("matrNo").toString()));
        bomvo.setConsistRt(Float.parseFloat(paraMap.get("consistRt").toString()));
        bomvo.setPursYn(paraMap.get("pursYn").toString());
        bomvo.setBomLvl(Long.parseLong(paraMap.get("bomLvl").toString()));
        bomvo.setModDt(DateUtils.getCurrentDate());
        bomvo.setModIp(paraMap.get("ipaddr").toString());
        bomvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        bomvo.setCustNo(Long.parseLong(paraMap.get("custNo").toString())); //AddOn By KMJ At 21.10.21
        bomvo.setUsedYn("Y");
        bomvo.setCustNo(custNo);
        ProdBom chkvo = bomRepo.findByCustNoAndProdNoAndMatrNoAndUsedYn(custNo, bomvo.getProdNo(), bomvo.getMatrNo(), "Y");
        if (chkvo != null) {
            bomvo.setBomNo(chkvo.getBomNo());
            bomvo.setRegDt(chkvo.getRegDt());
            bomvo.setRegId(chkvo.getRegId());
            bomvo.setRegIp(chkvo.getRegIp());
        } else {
            bomvo.setBomNo(0L);
        }
        bomRepo.save(bomvo);
    }

    /*엑셀로 업로딩하는 경우 */
    @Override
    @Transactional
    public void saveProdBom(Map<String, Object> paraMap) throws Exception {
        String tag = "ProdService.saveProdBom => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        StringBuffer buf = new StringBuffer();


        buf.setLength(0);
        buf.append(paraMap.get("fileRoot").toString())
           .append(File.separator)
           .append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);

        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        int rowindex = 0;
        int prodRowindex = Integer.parseInt(env.getProperty("excel_prod_bom_row_no"));    //품명이 배치된 행
        int startMatrRowindex = Integer.parseInt(env.getProperty("excel_fr_bom_row_no")); //자재명이 시작되는 행
        int endMatrRowindex = Integer.parseInt(env.getProperty("excel_to_bom_row_no")); //자재명이 종료되는 행
        int ProdFirstindex = Integer.parseInt(env.getProperty("excel_prod_row_first")); // 열의 처음
        int ProdLastindex = Integer.parseInt(env.getProperty("excel_prod_row_last")); // 열의 마지막
        int prodSvindex = Integer.parseInt(env.getProperty("excel_Sv_row_no")); // 서브 품명

        XSSFSheet sheet = workbook.getSheetAt(0); //첫번째쉬트
        int rows = sheet.getPhysicalNumberOfRows();
        XSSFRow rowProdMain = sheet.getRow(prodRowindex); //메인상품명을 가지고 있는 행
        XSSFRow rowProdNm = sheet.getRow(prodSvindex); //서브상품명을 가지고 있는 행
        XSSFRow row = sheet.getRow(rowindex);

        String prodNm = "";
        String mainProdNm = "";
        String svProdNm = "";
        String matrNm = "";
        String sveProdNm = "";
        String secProdNm = "";
        Float ConsistRt = 0F;
        Long matrNo = 0L;
        int idx = 0;
        int multiply = 100;


        for (int columnindex = ProdFirstindex; columnindex <= ProdLastindex; columnindex++) {
            ++idx;
            mainProdNm = rowProdMain.getCell(columnindex).getStringCellValue();
            sveProdNm = rowProdNm.getCell(columnindex).getStringCellValue();
            if (!StringUtil.isEmpty(mainProdNm)) {
                svProdNm = mainProdNm;
            }
            if (sveProdNm != null) {
                secProdNm = sveProdNm;
            }
            prodNm = this.getProdNm(sheet, prodRowindex, columnindex, svProdNm, secProdNm);
            for (rowindex = startMatrRowindex; rowindex <= endMatrRowindex; rowindex++) {
                row = sheet.getRow(rowindex);
                if (row == null) continue;
                matrNm = row.getCell(0).getStringCellValue();
                ConsistRt = (float) row.getCell(idx).getNumericCellValue();
                if (ConsistRt == 0F) continue;
                ProdBom bomvo = new ProdBom();
                bomvo.setConsistRt(ConsistRt * multiply);
                MatrInfo chkvo = matrRepo.findByCustNoAndMatrNmAndUsedYn(custNo,matrNm, "Y");
                if (chkvo != null) {
                    matrNo = chkvo.getMatrNo();
                }

                ProdBom bomchkvo = bomRepo.findByCustNoAndProdNoAndMatrNoAndUsedYn(custNo, bomvo.getProdNo(), bomvo.getMatrNo(), "Y");
                if (bomchkvo != null) {
                    bomvo.setBomNo(bomchkvo.getBomNo());
                }

                ProdInfo chkInfo = prodRepository.findByCustNoAndProdNmAndUsedYn(custNo, prodNm, "Y");
                if (chkInfo != null) {
                    bomvo.setProdNo(chkInfo.getProdNo());
                }
                bomvo.setMatrNo(matrNo);
                bomvo.setModDt(DateUtils.getCurrentDate());
                bomvo.setModId(0L);
                bomvo.setRegIp((String) paraMap.get("ipaddr"));
                bomvo.setRegDt(bomvo.getModDt());
                bomvo.setRegId(0L);
                bomvo.setCustNo(Long.parseLong(paraMap.get("custNo").toString()));
                bomvo.setUsedYn("Y");
                bomvo.setCustNo(custNo);
                bomRepo.save(bomvo);
            }
        }
    }

    @Override
    @Transactional
    public void dropProdBom(Map<String, Object> paraMap) {
        String tag = "prodService.dropProdBom => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long bomNo = Long.parseLong(paraMap.get("bomNo").toString());
        Map<String, Object> map = new HashMap<String, Object>();
        ProdBom prodBom = bomRepo.findByCustNoAndBomNoAndUsedYn(custNo, bomNo, "Y");
        if (prodBom != null) {
            bomRepo.delete(prodBom);
        }
    }


    @Override
    public void makeExcel(List<Map<String, Object>> paraMap) throws Exception {
        String tag = "ProdService.makeExcel ==> ";
        Map<String,Object> bomchkmap = new HashMap<String,Object>();
        Float indcWgt = Float.parseFloat(paraMap.get(0).get("indcWgt").toString());
        Long prodNo = Long.parseLong(paraMap.get(0).get("prodNo").toString());
        Long indcNo = Long.parseLong(paraMap.get(0).get("indcNo").toString());
        Long custNo = Long.parseLong(paraMap.get(0).get("custNo").toString());
        bomchkmap.put("prodNo",prodNo);
        bomchkmap.put("custNo",custNo);
        bomchkmap.put("pageNo", 0);
        bomchkmap.put("pageSz", 100);
        bomchkmap.put("indcQty", indcWgt); //소요량 산출용

        List<Map<String,Object>> ds = mapper.getSfProdBomList(bomchkmap);


        log.info(tag + "3.생산지시 상품기준 소요자재정보 관리 (지시상품기준 BOM 리스트갯수)===> " + ds.size());
        int idx = 0;
        for (Map<String, Object> el : ds) {
            ++idx;
            el.put("indc_no",indcNo);
            el.put("cust_no",custNo);
            log.info("작업지시에 해당하는 원료 목록 : "+el);
            this.saveMakeIndcMatr(el);
        }

        Map<String,Object> Map = new HashMap<String,Object>();
        Map.put("prodNo", prodNo);
        Map.put("pageNo", 0);
        Map.put("pageSz", 100);
        Map.put("indcNo", indcNo);
        Map.put("make_dt", paraMap.get(0).get("date"));
        indcService.autoSaveIndcMp(Map);

        this.savePursInfoSeoul(Map);


    }



    private void saveMakeIndcMatr(Map<String,Object> el) {
        String tag = "MakeIndcService.saveMakeIndcMatr => ";
        Long matrNo = Long.parseLong(el.get("matrNo").toString());
        MakeIndcMatr matrvo = new MakeIndcMatr();
        Long indcNo = Long.parseLong(el.get("indcNo").toString());
        Long custNo = Long.parseLong(el.get("custNo").toString());
        matrvo.setIndcNo(indcNo);
        matrvo.setMatrNo(matrNo);
        matrvo.setCustNo(custNo);
        matrvo.setNeedQty(Float.parseFloat(el.get("needQty").toString()));
        matrvo.setUsedYn("Y");
        matrvo.setMatrSts("N");
        matrvo.setCustNo(Long.parseLong(el.get("custNo").toString())); //AddOn By KMJ At 21.10.21
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
            try{
                matrvo.setRegId(Long.parseLong(el.get("userId").toString()));
            }catch (NullPointerException en){
                matrvo.setRegId(0L);
            }

            matrvo.setRegDt(DateUtils.getCurrentBaseDateTime());

            try{
                matrvo.setRegIp(el.get("ipaddr").toString());
            }catch (NullPointerException en){
                matrvo.setRegIp("127.0.0.1");
            }
        }
        makeIndcMatrRepo.save(matrvo);

    }
    // 하담푸드 작업지시 생성 입니다

    @Override
    public void ProdIndcByExcel(HashMap<String, Object> paraMap) throws Exception {
        String tag = "ProdService.ProdIndcByExcel => ";
        StringBuffer buf = new StringBuffer();
        String fileRoot = paraMap.get("fileRoot").toString();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int rowindex = 0;
        int startMatrRowindex = Integer.parseInt(env.getProperty("excel_fr_matrbom_row_no"));
        int endMatrRowindex = Integer.parseInt(env.getProperty("excel_to_matrbom_row_no"));
        Float mess = 0f;

        for (int i = 0; i < 23; ++i) {
            XSSFSheet sheet = workbook.getSheetAt(i); //첫번째쉬트
            int rows = sheet.getPhysicalNumberOfRows();
            //작업지시 생성
            String prodNm = "";
            Float indcQty = 0F;
            Float indcStk = 0F;
            Float sumQty = 0F;
            Long prodNo = 0L;
            Long makeUnit = 0L;
            Long statCd = 0L;


            XSSFRow row = sheet.getRow(rowindex);

            for (rowindex = startMatrRowindex; rowindex <= endMatrRowindex; rowindex++) {
                row = sheet.getRow(rowindex);
                if (row == null) continue;
                prodNm = row.getCell(1).getStringCellValue();
                prodNm = prodNm.replaceAll("\\p{Z}", "");


                indcQty = (float) row.getCell(3).getNumericCellValue();


                row = sheet.getRow(3);
                Date date = row.getCell(4).getDateCellValue();

                makeUnit = Long.parseLong(env.getProperty("code.base.sale_unit_g"));
                statCd = Long.parseLong(env.getProperty("code.matrst.purs"));


                ProdInfo chkInfo = prodRepository.findByCustNoAndProdNmAndUsedYn(custNo,prodNm, "Y");
                if (chkInfo != null) {
                    prodNo = chkInfo.getProdNo();
                    mess = chkInfo.getMess();
                }
                sumQty = (indcQty * mess) / 1000;

                paraMap.put("makeUnit", makeUnit); // 단위
                paraMap.put("statCd", statCd); // 작업상태
                paraMap.put("date", date); // 일자
                paraMap.put("prodNo", prodNo); // 제품 번호
                paraMap.put("indcQty", sumQty); // kg으로 환산.
                paraMap.put("iwhQty", indcQty);
                paraMap.put("custNo", custNo);

                excelMakeService.hadamfoodsaveMakeProc(paraMap);
            }
        }
    }


    @Override
    public void hadamfoodmakeExcel(List<Map<String, Object>> paraMap) throws Exception {
        String tag = "ProdService.hadamfoodmakeExcel => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get(0).get("custNo").toString());
        Map<String, Object> Map = new HashMap<String, Object>();

        Float indcQty = Float.parseFloat(paraMap.get(0).get("indcQty").toString());
        Float consist = 0F;
        Float svindcQty = 0.01F;
        Long matrNo = 0L;

        MakeIndcMatr mivo = new MakeIndcMatr();
        Long prodNo = Long.parseLong(paraMap.get(0).get("prodNo").toString());
        Long indcNo = Long.parseLong(paraMap.get(0).get("indcNo").toString());


        Map.put("prodNo", prodNo);
        Map.put("pageNo", 0);
        Map.put("pageSz", 100);

        List<Map<String, Object>> ds = this.getProdBomList(Map);
        for (Map<String, Object> el : ds) {
            consist = Float.parseFloat(el.get("consist_rt").toString());
            matrNo = Long.parseLong(el.get("matr_no").toString());
            mivo.setNeedQty(indcQty * (consist * svindcQty));
            mivo.setIndcNo(indcNo);
            mivo.setMatrNo(matrNo);
            try {
                mivo.setRegId(0L);
            } catch (NullPointerException ne) {
            }
            try {
                mivo.setRegDt(DateUtils.getCurrentDate());
            } catch (NullPointerException ne) {
            }
            try {
                mivo.setModId(0L);
            } catch (NullPointerException ne) {
            }
            try {
                mivo.setModDt(DateUtils.getCurrentDate());
            } catch (NullPointerException ne) {
            }
            mivo.setUsedYn("Y");
            mivo.setCustNo(custNo);
            makeIndcMatrRepo.save(mivo);

        }

    }


    @Override
    public Map<String, Object> getMonthlyOrderQty(Map<String, Object> paraMap) {
        return mapper.getMonthlyOrderQty(paraMap);
    }

    @Override
    public void dropProd(Map<String, Object> paraMap) {
        String tag = "prodService.dropProd => ";
        mapper.dropProd(paraMap);
    }

    @Override
    public void makeProdByExcel(HashMap<String, Object> paraMap) throws Exception {
        String tag = "prodService.makeProdByExcel => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        StringBuffer buf = new StringBuffer();
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int rowindex = 0;
        int basePubsUnitCd = 0;
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        log.info(tag + "excel처리행수 = " + rows);
        String cmpyNm = "";

        String brnchNm = "";
        int weight = 0;
        ProdInfo ckvo = null;
        for (rowindex = 0; rowindex < rows; rowindex++) {
            if (rowindex <= 2) continue; //헤더정보 skip
            ProdInfo pivo = new ProdInfo();

            XSSFRow row = sheet.getRow(rowindex);
            if (row == null) continue;
            //int cells=row.getPhysicalNumberOfCells();
            //for(columnindex=0; columnindex<=cells; columnindex++) {
            //matrInfo.setMatrNo(0L);
            //XSSFCell cell=row.getCell(0);

            //log.info(tag + " rowindex = " + rowindex);//kill
            pivo.setHeatTp(0L);
            pivo.setProdNm(row.getCell(0).getStringCellValue());
            pivo.setReptProdNo(row.getCell(1).getStringCellValue());
            brnchNm = row.getCell(2).getStringCellValue();
            //분류코드 설정 시작
            ProdBrnch pbvo = pbr.findByCustNoAndBrnchNmAndUsedYn(custNo,brnchNm, "Y");
            pivo.setProdBrnch((pbvo == null) ? 0L : pbvo.getBrnchNo());
            //분류코드 설정 끝

            weight = (int) row.getCell(3).getNumericCellValue();
            pivo.setUsedYn("Y");
            buf.setLength(0);
            buf.append(Integer.toString(weight)).append("g");
            pivo.setSz(buf.toString());
            ckvo = prodRepository.findByCustNoAndBrnchNoAndProdNmAndUsedYn(custNo, pivo.getBrnchNo(), pivo.getProdNm(), "Y");
            if (ckvo != null) {
                pivo.setProdNo(ckvo.getProdNo());
                pivo.setModDt(pivo.getModDt());
            } else {
                pivo.setProdNo(0L);
                pivo.setRegDt(pivo.getRegDt());
            }
            pivo.setCustNo(custNo);
            pivo = prodRepository.save(pivo);
            //여기서 BOM 구성할 것.
        }

    }

    @Override
    public Map<String, Object> getProdBomInfo(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        //ProdBom vo =  bomRepo.findByBomNoAndUsedYn(Long.parseLong(custNo, paraMap.get("bomNo").toString()),"Y");
        //return StringUtil.voToMap(vo);
        return mapper.getProdBomInfo(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdCmpyList(Map<String, Object> paraMap) {
        return mapper.getProdCmpyList(paraMap);
    }

    @Override
    public int getProdCmpyListCount(Map<String, Object> paraMap) {
        return mapper.getProdCmpyListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdStkList(Map<String, Object> paraMap) {
        return mapper.getProdStkList(paraMap);
    }

    @Override
    public int getProdStkListCount(Map<String, Object> paraMap) {
        return mapper.getProdStkListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdOrdList(Map<String, Object> paraMap) {
        return mapper.getProdOrdList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getAnaprodList(Map<String, Object> paraMap) {
        return mapper.getAnaprodList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboProcBrnch(Map<String, Object> paraMap) {
        return mapper.getComboProcBrnch(paraMap);
    }

    @Override
    public void dropOrdProd(Map<String, Object> paraMap) {
        mapper.dropOrdProd(paraMap);
    }

    @Override
    public Object getKioProdOwhList(Map<String, Object> paraMap) {
        return null;
    }

    @Override
    public int getKioProdOwhListCount(Map<String, Object> paraMap) {
        return 0;
    }


    @Override
    public int getProdOrdListCount(Map<String, Object> paraMap) {
        return mapper.getProdOrdListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdStatList(Map<String, Object> paraMap) {
        return mapper.getProdStatList(paraMap);
    }

    @Override
    public int getProdStatListCount(Map<String, Object> paraMap) {
        return mapper.getProdStatListCount(paraMap);
    }

    public String getProdNm(XSSFSheet sheet, int rowindex, int colindex, String mainProdNm, String sveProdNm) {
        StringBuffer buf = new StringBuffer();

        buf.setLength(0);
        buf.append(mainProdNm);
        buf.append("\n").append(sveProdNm);
        return buf.toString().replaceAll("(\r|\n|\r\n|\n\r)", " ");
    }

    @Override
    public Float getIndcQty(XSSFSheet sheet, int MatrRowindex, int columnindex, Float svIndcQty) {
        return svIndcQty;
    }

    @Override
    public void matchBrnch(Map<String, Object> paraMap) {
        ProdInfo pivo = new ProdInfo();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        ProdBom prodBom = null;
        List<Map<String, Object>> ds = (ArrayList<Map<String, Object>>) paraMap.get("testArr");
        for (Map<String, Object> el : ds) {
            String prodNm = el.get("prod_nm").toString();
            prodNm = prodNm.replaceAll("\\p{Z}", "");
            ProdInfo chkvo = prodRepository.findByCustNoAndProdNmAndUsedYn(custNo,prodNm, "Y");
            if (chkvo != null) {
                pivo.setBrnchNo(Long.parseLong(el.get("brnch_no").toString()));
                pivo.setProdNm(prodNm);
                pivo.setProdNo(chkvo.getProdNo());
                pivo.setFileNo(chkvo.getFileNo());
                pivo.setModId(chkvo.getModId());
                pivo.setModIp(chkvo.getModIp());
                pivo.setModDt(chkvo.getModDt());
                pivo.setModlNm(chkvo.getModlNm());
                pivo.setProdBrnch(chkvo.getProdBrnch());
                pivo.setQtyPerPkg(chkvo.getQtyPerPkg());
                pivo.setUsedYn(chkvo.getUsedYn());
                pivo.setSaleUnit(chkvo.getSaleUnit());
                pivo.setUnitAmt(chkvo.getUnitAmt());
                pivo.setSaveTmpr(chkvo.getSaveTmpr());
                pivo.setMess(chkvo.getMess());
                pivo.setHeatTp(chkvo.getHeatTp());
                pivo.setRegId(chkvo.getRegId());
                pivo.setRegIp(chkvo.getRegIp());
                pivo.setRegDt(chkvo.getRegDt());
                pivo.setCustNo(custNo);
                pivo.setProdCode(chkvo.getProdCode());
            }
            prodRepository.save(pivo);
        }


    }


    // 대동 고려삼 작업지시 엑셀
    @Override
    public void daedongIndcByExcel(HashMap<String, Object> paraMap) throws Exception {
        String tag = "ProdService.DaedongIndcByExcel => ";
        StringBuffer buf = new StringBuffer();
        String fileRoot = paraMap.get("fileRoot").toString();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int rowindex = 0;
        int startMatrRowindex = Integer.parseInt(env.getProperty("excel_fr_no"));
        int endMatrRowindex = Integer.parseInt(env.getProperty("excel_to_no"));


        XSSFSheet sheet = workbook.getSheetAt(0); //첫번째쉬트
        int rows = sheet.getPhysicalNumberOfRows();
        //작업지시 생성
        String prodCd = "";
        int indcQty = 0;
        Long brnchNo = 0L;
        Long prodNo = 0L;
        String cont = "";
        String cmpy = "";
        Float mess = 0F;


        for (rowindex = startMatrRowindex; rowindex <= endMatrRowindex; rowindex++) {
            XSSFRow row = sheet.getRow(rowindex);
            if (row == null) continue;
            prodCd = row.getCell(6).getStringCellValue();  // 지시 제품명(코드)
            prodCd = prodCd.replaceAll("\\p{Z}", ""); // 띄어쓰기 제거

            indcQty = (int) row.getCell(12).getNumericCellValue(); //지시수량

            Date date = row.getCell(2).getDateCellValue(); // 지시일자

//            mess = (float) row.getCell(12).getNumericCellValue(); // 단위

            cont = row.getCell(3).getStringCellValue(); // cont 지시 내용

            cmpy = row.getCell(1).getStringCellValue(); //매출 거래처 명

//            makeUnit = Long.parseLong(env.getProperty("code.base.sale_unit_g").toString());

//            statCd = Long.parseLong(env.getProperty("code.matrst.purs"));
//            try {
//                QtyPerPkg = row.getCell(11).getNumericCellValue();
//            } catch (IllegalStateException en) {
//                QtyPerPkg = 1;
//            }

//            magrbase = row.getCell(13).getNumericCellValue();


            ProdInfo chkInfo = prodRepository.findByCustNoAndProdCodeAndUsedYn(custNo,prodCd, "Y");
            if (chkInfo != null) {
                prodNo = chkInfo.getProdNo();
                mess = chkInfo.getMess();
                brnchNo = chkInfo.getBrnchNo();
            }

            Float indcWgt = (indcQty * mess) / 1000;

            paraMap.put("brnchNo", brnchNo);
            paraMap.put("indcWgt", indcWgt);
            paraMap.put("date", date); // 일자
            paraMap.put("prodNo", prodNo); // 제품 번호
            paraMap.put("indcQty", indcQty); // 지시 수량
            paraMap.put("indcCont", cont); // cont 지시 내용
            paraMap.put("cmpy", cmpy);//매출 거래처 명
            paraMap.put("custNo",custNo);

            excelMakeService.DaedongsaveMakeProc(paraMap);
        }

    }

    @Override
    public void DaedongmakeExcel(List<Map<String, Object>> paraMap) throws Exception {
        String tag = "ProdService.DaedongmakeExcel => ";
        log.info(tag + "paraMap = " + paraMap.toString());


        Map<String, Object> Map = new HashMap<String, Object>();


        Float consist = 0F;
        Float svindcQty = 0.01F;
        Long matrNo = 0L;
        Float indcQty = 0F;

        Long custNo = Long.parseLong(paraMap.get(0).get("custNo").toString());
        Long prodNo = Long.parseLong(paraMap.get(0).get("prodNo").toString());
        Long indcNo = Long.parseLong(paraMap.get(0).get("indcNo").toString());


        Map.put("prodNo", prodNo);
        Map.put("pageNo", 0);
        Map.put("pageSz", 100);
        Map.put("indcNo", indcNo);
        Map.put("date", paraMap.get(0).get("date"));

        List<Map<String, Object>> ds = this.getProdBom(Map);

        for (Map<String, Object> el : ds) {
            MakeIndcMatr mivo = new MakeIndcMatr();
            indcQty = Float.parseFloat(paraMap.get(0).get("indcQty").toString());
            consist = Float.parseFloat(el.get("consist_rt").toString());
            matrNo = Long.parseLong(el.get("matr_no").toString());

            MakeIndcMatr chkvo = makeIndcMatrRepo.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo,indcNo, matrNo, "Y");
            if (chkvo != null) {
                mivo.setNeedQty(chkvo.getNeedQty());
                mivo.setMatrNo(chkvo.getMatrNo());
                mivo.setIndcNo(chkvo.getIndcNo());
                mivo.setModDt(DateUtils.getCurrentDate());
                mivo.setModId(0L);
                mivo.setModIp("127.0.0.1");
            } else {
                mivo.setNeedQty(indcQty * (consist * svindcQty));
                mivo.setIndcNo(indcNo);
                mivo.setMatrNo(matrNo);
                mivo.setRegDt(DateUtils.getCurrentDate());
                mivo.setRegId(0L);
                mivo.setRegIp("127.0.0.1");
            }
            mivo.setUsedYn("Y");
            mivo.setIndcMatrNo(custNo);
            makeIndcMatrRepo.save(mivo);

        }
        /*투입인력 자동 생성*/
        indcService.autoSaveIndcMp(Map);

        this.savePursInfo(Map);
    }

    @Override
    public void saveReqMatr(Map<String, Object> paraMap) {

    }


    /*생산지시기반 작업에 필요한 소요자재 구매요청*/
    private PursInfo savePursInfo(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.saveReqMatr==>";
        Long confirmPurs = 0L;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = inmapper.chkReqPurs(paraMap); //재고부족분이 있는 자재가 존재하는지 점검

        if (ds.size() <= 0) return null;

        PursInfo pivo = new PursInfo();

        pivo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        pivo.setPursDt((Date) paraMap.get("date"));
        pivo.setDlvReqDt((Date) paraMap.get("date"));

        confirmPurs = Long.parseLong(env.getProperty("purs.sts.ing")); //입고로 변경
        pivo.setPursSts(confirmPurs);
        pivo.setDlvDt((Date) paraMap.get("date"));
        pivo.setUsedYn("Y");
        pivo.setModDt(DateUtils.getCurrentDate());
        pivo.setModId(0L);
        pivo.setModIp("127.0.0.1");
        pivo.setRegDt(DateUtils.getCurrentDate());
        pivo.setRegId(0L);
        pivo.setRegIp("127.0.0.1");
        pivo.setCustNo(custNo);

        Long indcNo = Long.parseLong(paraMap.get("indcNo").toString());

        MakeIndc vhkvo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,indcNo, "Y");
        if (vhkvo != null) {
            vhkvo.getOrdNo();
        }
        pivo.setOrdNo(vhkvo.getOrdNo());

        pivo = pir.save(pivo); //구매요청 기본정보 저장


        PursMatr pmvo = null;
        for (Map<String, Object> el : ds) {
            if (Float.parseFloat(el.get("req_purs_qty").toString()) < 0) {
                continue; //소요자재 vs 재고량 비교, 재고부족이 아닌경우 skip
                //주의사항 : 자재입고시점에 작업지시가 이루어진 자료가 있는 경우 바로 해당 작업지번호로 직출고처리해야야 함
                //        그렇지 않은 경우 작업처리용으로 입고된 자료가 재고로 잡혀 자재입고 대상에서 제외되는 경우가 발생함.
            }
            pmvo = new PursMatr();
            pmvo.setPursNo(pivo.getPursNo());
            pmvo.setMatrNo(Long.parseLong(el.get("matr_no").toString()));
            pmvo.setCmpyNo(0L);//구매거래처는 입고 검수 시점에 MatrIwh.cmptyNo와 함꼐 설정해야 함.
            pmvo.setPursQty(Float.valueOf(String.valueOf(el.get("req_purs_qty"))));
            pmvo.setPursUnit(Long.parseLong(el.get("purs_unit").toString()));
            pmvo.setUsedYn("Y");
            pmvo.setCustNo(custNo);
            pmvo.setWhNo(2L);
            PursMatr chkmvo = pmr.findByCustNoAndPursNoAndMatrNoAndUsedYn(custNo,pmvo.getPursNo(), pmvo.getMatrNo(), "Y");
            if (chkmvo != null) {
                pmvo.setPursMatrNo(chkmvo.getPursMatrNo());
                pmvo.setModDt(DateUtils.getCurrentDateTime());
                pmvo.setModId(pivo.getModId());
                pmvo.setModIp(pivo.getModIp());
            } else {
                pmvo.setPursMatrNo(0L);
                pmvo.setRegDt(DateUtils.getCurrentDateTime());
                pmvo.setRegId(pivo.getRegId());
                pmvo.setRegIp(pivo.getRegIp());
                pmvo.setModDt(DateUtils.getCurrentDateTime());
                pmvo.setModId(pivo.getModId());
                pmvo.setModIp(pivo.getModIp());
            }
            chkmvo = pmr.save(pmvo);
        }
        /*Remarked By KMJ At 21.10.24
        Map<String, Object> bmap = new HashMap<String, Object>();
        bmap.put("codeNo", pivo.getPursNo());
        bmap.put("savePath", "purs/");
        stockService.makeBarCode(bmap);
        */
        this.saveMatrIwh(pivo);
        return pivo;

    }

    private void saveMatrIwh(PursInfo pivo) {
        String tag = "MakeIndcService.svaematrIwh => ";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long custNo = pivo.getCustNo();
        Map<String, Object> pmap = new HashMap<String, Object>();
        pmap.put("pursNo", pivo.getPursNo());
        pmap.put("pageNo", 0);
        pmap.put("pageSz", 1000);
        pmap.put("custNo",custNo);
        List<Map<String, Object>> ds = pursService.getPursMatrList(pmap);
        for (Map<String, Object> el : ds) {
            MatrIwh mivo = new MatrIwh();
            mivo.setCustNo(custNo);
            mivo.setPursNo(Long.valueOf(el.get("pursNo").toString()));
            mivo.setPursMatrNo(Long.valueOf(el.get("pursMatrNo").toString()));
            mivo.setCmpyNo(0L); //입고거래처는 입고 검수 시점에 설정해야 함.
            Long matrNo = Long.valueOf(el.get("matrNo").toString());
            mivo.setMatrNo(matrNo);
            try {
                mivo.setIwhDt(sdf.parse(el.get("pursDt").toString())); //입고일자를 구매일자로 자동 설
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mivo.setIwhQty(Float.valueOf(String.valueOf(el.get("pursQty"))));
            mivo.setWhNo(2L);//입고창고번호는 입고 검수 시점에 설정 해야 함,
            mivo.setUsedYn("Y");
            mivo.setCustNo(custNo);
            MatrIwh chkMatrIwh = imr.findByCustNoAndPursNoAndMatrNoAndUsedYn(custNo,mivo.getPursNo(), mivo.getMatrNo(), "Y");
            if (chkMatrIwh != null) {
                mivo.setIwhNo(chkMatrIwh.getIwhNo());
            }
            try {
                mivo.setRetnQty(Float.valueOf(String.valueOf(el.get("retnQty"))));
            } catch (NullPointerException ne) {
                mivo.setRetnQty(Float.valueOf(0));
            } catch (NumberFormatException ne) {
                mivo.setRetnQty(Float.valueOf(0));
            }
            imr.save(mivo);
            /*Remarked By KMJ At 21.10.24
            Map<String, Object> ibmap = new HashMap<String, Object>();
            ibmap.put("codeNo", mivo.getIwhNo());
            ibmap.put("savePath", "matr/iwh/");
            stockService.makeBarCode(ibmap);
            */

            //자재 현황
            MatrStk stkvo = new MatrStk();
            stkvo.setUsedYn("Y");
            stkvo.setCustNo(custNo);
            MatrStk mrchk = matStkRepo.findByCustNoAndMatrNoAndUsedYn(custNo,matrNo, "Y");
            if (mrchk != null) {
                stkvo.setMatrStkNo(mrchk.getMatrStkNo());
                stkvo.setMatrNo(mrchk.getMatrNo());
                stkvo.setWhNo(mrchk.getWhNo());
                stkvo.setStkQty(0F);
                stkvo.setModDt(DateUtils.getCurrentDate());
                stkvo.setModId(0L);
                stkvo.setModIp("127.0.0.1");
            } else {
                stkvo.setStkQty(0F);
                stkvo.setMatrNo(matrNo);
                stkvo.setRegDt(DateUtils.getCurrentDateTime());
                stkvo.setRegId(0L);
                stkvo.setRegIp("127.0.0.1");
                stkvo.setWhNo(2L);
            }

            matStkRepo.save(stkvo);
            Long indcNo = pivo.getIndcNo();
            MatrOwh owhvo = new MatrOwh();
            owhvo.setIndcNo(indcNo);
            owhvo.setMatrNo(matrNo); //소요자재번호
            owhvo.setModDt(DateUtils.getCurrentDate());
            owhvo.setModId(0L);
            owhvo.setModIp("127.0.0.1");

            MatrOwh chkvo = omr.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo, owhvo.getIndcNo(), owhvo.getMatrNo(), "Y");
            if (chkvo != null) {
                owhvo.setOwhNo(chkvo.getOwhNo()); //관리번호
                owhvo.setWhNo(chkvo.getWhNo());//창고번호
            } else {
                owhvo.setWhNo(2L);//창고번호 -> 실제출고시점에 설정됨
                owhvo.setOwhQty(Float.valueOf(String.valueOf(el.get("pursQty")))); //출고수량-> matr_stk - stkQty 입력 후 삭제 예정
                owhvo.setRegDt(DateUtils.getCurrentDateTime());
                owhvo.setRegId(0L);
                owhvo.setRegIp("127.0.0.1");
            }
            Float owhReqQty = Float.valueOf(String.valueOf(el.get("pursQty")));
            owhvo.setOwhReqQty(owhReqQty); //요청수량
            try {
                owhvo.setOwhReqDt(sdf.parse(el.get("pursDt").toString())); //출고요청일자
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                owhvo.setOwhDt(sdf.parse(el.get("pursDt").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            owhvo.setOwhUnit(Long.parseLong(el.get("makeUnit").toString()));//요청단위
            owhvo.setUsedYn("Y");
            owhvo.setCustNo(custNo);
            owhvo = omr.save(owhvo);
            /* Remarked By KMJ At 21.10.24
            Map<String, Object> bmap = new HashMap<String, Object>();
            bmap.put("codeNo", owhvo.getOwhNo());
            bmap.put("savePath", "matr/owh/");
            stockService.makeBarCode(bmap);
            */
        }

    }

    /*제품 출고 처리 (Excel) */
    @Override
    public List<Map<String, Object>> prodExcelOwh(HashMap<String, Object> paraMap) throws Exception {
        String tag = "ProdService.prodExcelOwh => ";
        String fileRoot = paraMap.get("fileRoot").toString();
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        String filePath = fileService.getFileInfo(fileRoot,fileNo);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        FileInputStream file = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int rowindex = 0;
        int startMatrRowindex = Integer.parseInt(env.getProperty("excel_owh_fr"));
        int endMatrRowindex = Integer.parseInt(env.getProperty("excel_owh_to"));


        XSSFSheet sheet = workbook.getSheetAt(0); //첫번째쉬트
        int rows = sheet.getPhysicalNumberOfRows();
        //작업지시 생성
        String erProdNm = "";
        Float owhQty = 0F;
        Long prodNo = 0L;
        String telNo = "";
        String cmpy = "";
        String owhWhNm = "";
        Long cmpyNo = 0L;
        Long whNo = 0L;
        Date owdate = new Date();
        Float stkQty = 0F;
        Float mistkQty = 0F;
        int idx = 0;
        List<Map<String, Object>> list = new ArrayList<>();

        XSSFRow row = sheet.getRow(rowindex);
        for (rowindex = startMatrRowindex; rowindex <= endMatrRowindex; rowindex++) {
            ++idx;
            Map<String, Object> Map = new HashMap<String, Object>();
            row = sheet.getRow(rowindex);
            if (row == null) continue;
            ProdOwh owh = new ProdOwh();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = row.getCell(0).getStringCellValue(); // 출고일자
            date = date.replaceAll("\\p{Z}[-][0-9]", "");
            date = date.replaceAll("[/]", "-");
            if (date.contains("계") == true) {
                break;
            }

            erProdNm = row.getCell(1).getStringCellValue();  // 지시 제품명
            erProdNm = erProdNm.replaceAll("\\p{Z}", ""); // 띄어쓰기 제거
            ProdInfo pochk = prodRepository.findByCustNoAndErpProdNmAndUsedYn(custNo,erProdNm, "Y");
            if (pochk != null) {
                prodNo = pochk.getProdNo();
                owh.setProdNo(prodNo);
            } else {
                Map.put("erpNm", erProdNm); // 열
                Map.put("idx", idx);
            }

            owdate = sdf.parse(date);
            owh.setOwhDt(owdate);
            owh.setOwhReqDt(owdate);

            owhQty = (float) row.getCell(2).getNumericCellValue(); // 출고 수량
            owh.setOwhQty(owhQty);
            owh.setOwhReqQty(owhQty);
            owh.setOwhUnit(Long.parseLong(env.getProperty("code.base.sale_unit_ea")));

            owh.setRegDt(DateUtils.getCurrentDateTime());
            owh.setRegId(0L);
            owh.setRegIp("127.0.0.1");
            owh.setModDt(DateUtils.getCurrentDateTime());
            owh.setModId(0L);
            owh.setModIp("127.0.0.1");
            owh.setUsedYn("Y");

            cmpy = row.getCell(4).getStringCellValue(); // 출고 회사
            cmpy = cmpy.replaceAll("\\p{Z}", "");

            owhWhNm = row.getCell(3).getStringCellValue(); // 출고 창고
            owhWhNm = owhWhNm.replaceAll("\\p{Z}", "");
            WhInfo whchk = whinfoRepo.findByCustNoAndWhNmAndUsedYn(custNo,owhWhNm, "Y");
            if (whchk != null) {
                whNo = whchk.getWhNo();
                owh.setWhNo(whNo);
            } else {
                Map.put("whNm", owhWhNm); // 열
                Map.put("idx", idx);
            }
            Long cmpyTp = Long.parseLong(env.getProperty("code.cmpytp.sale"));
            CmpyInfo cmchk = cmpyRepo.findByCustNoAndCmpyTpAndCmpyNmAndUsedYn(custNo,cmpyTp,cmpy, "Y");
            if (cmchk != null) {
                if (cmchk.getMngrGbnCd() == 21) {
                    cmpyNo = cmchk.getCmpyNo();
                    owh.setCmpyNo(cmpyNo);
                    telNo = row.getCell(5).getStringCellValue(); // 회사번호
                    cmchk.setTelNo(telNo);
                    cmpyRepo.save(cmchk);
                } else {
                    Map.put("mngrNm", cmpy); // 열
                    Map.put("idx", idx);
                }
            } else {
                Map.put("cmpyNm", cmpy); // 열
                Map.put("idx", idx);
            }


            ProdStk stkvo = new ProdStk();
            ProdStk stkchk = stkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,whNo, prodNo, "Y");
            if (stkchk != null) {
                stkvo.setProdNo(0L);
                stkQty = stkchk.getStkQty();
                mistkQty = stkQty - owhQty;
                stkvo.setStkQty(mistkQty);
                stkvo.setRegDt(DateUtils.getCurrentDateTime());
                stkvo.setRegId(0L);
                stkvo.setRegIp("127.0.0.1");
                stkvo.setWhNo(whNo);
                stkvo.setStkDt(owdate);
                stkvo.setStatTrfDt(DateUtils.getCurrentDateTime());
            } else {
                stkvo.setProdNo(prodNo);
                stkvo.setModDt(DateUtils.getCurrentDateTime());
                stkvo.setModId(0L);
                stkvo.setModIp("127.0.0.1");
                stkvo.setWhNo(whNo);
                stkvo.setStkQty(0F);
                stkvo.setStkDt(owdate);
                stkvo.setStatTrfDt(DateUtils.getCurrentDateTime());
            }
            stkvo.setCustNo(custNo);
            stkvo.setUsedYn("Y");


            OrdInfo ordvo = new OrdInfo();
            ordvo.setOrdSts(Long.parseLong(env.getProperty("ord_status.complete"))); //스택
            ordvo.setOrdGbn(Long.parseLong(env.getProperty("code_byExcel"))); //주문형태
            ordvo.setUsedYn("Y");
            ordvo.setOrdDt(owdate);
            ordvo.setDlvDt(owdate);
            ordvo.setDlvReqDt(owdate);
            ordvo.setOrdTp(Long.parseLong(env.getProperty("ord_oem_cd")));
            ordvo.setPlcNo(0L);
            ordvo.setModDt(DateUtils.getCurrentDateTime());
            ordvo.setModId(0L);
            ordvo.setModIp("127.0.0.1");
            ordvo.setCmpyNo(cmpyNo);
            ordvo.setOrdNm(erProdNm);
            ordvo.setRegDt(DateUtils.getCurrentDateTime());
            ordvo.setRegId(0L);
            ordvo.setRegIp("127.0.0.1");


            OrdProd opvo = new OrdProd();
            opvo.setQtyPerPkg(1);
            opvo.setUsedYn("Y");
            opvo.setCustNo(custNo);
            opvo.setSaleUnit(Long.parseLong(env.getProperty("code.base.sale_unit_ea")));

            OrdProd orchk = ordprRepo.findByCustNoAndOrdNoAndProdNoAndDlvDtAndUsedYn(custNo,ordvo.getOrdNo(), prodNo, owdate, "Y");
            if (orchk != null) {
                opvo.setOrdNo(orchk.getOrdNo());
                opvo.setProdNo(orchk.getProdNo());
                Float ordQty = orchk.getOrdQty();
                Float sumQty = ordQty + owhQty;
                opvo.setOrdQty(sumQty);
                opvo.setModDt(DateUtils.getCurrentDateTime());
                opvo.setModId(0L);
                opvo.setModIp("127.0.0.1");
            } else {
                opvo.setOrdNo(ordvo.getOrdNo());
                opvo.setProdNo(prodNo);
                opvo.setOrdQty(owhQty);
                opvo.setRegDt(DateUtils.getCurrentDateTime());
                opvo.setRegId(0L);
                opvo.setRegIp("127.0.0.1");
                opvo.setDlvDt(owdate);
            }

            if (pochk != null) {
                if (whchk != null) {
                    if (cmchk != null) {
                        if (cmchk.getMngrGbnCd() == 21) {
                            stkRepo.save(stkvo);
                            ordRepo.save(ordvo);
                            ordprRepo.save(opvo);
                            owh.setOrdNo(ordvo.getOrdNo());
                            owhRepo.save(owh);
                        }
                    } else {
                        continue;
                    }
                }
            }
            list.add(Map);
        }

        return list;

    }

    @Override
    public List<Map<String, Object>> getSendInfoToElecticTagUrlList(Map<String, Object> paraMap) {
        return null;
    }

    @Override
    public int getSendInfoToElecticTagUrlListCount(Map<String, Object> paraMap) {
        return 0;
    }


    /*제품 입고 처리 (Excel) */
    @Override
    public List<Map<String, Object>> stsProdExcelIwh(HashMap<String, Object> paraMap) throws Exception {
        String tag = "ProdService.prodExcelOwh => ";
        String fileRoot = paraMap.get("fileRoot").toString();
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        String filePath = fileService.getFileInfo(fileRoot,fileNo);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());

        FileInputStream file = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int rowindex = 0;
        int startMatrRowindex = Integer.parseInt(env.getProperty("excel_owh_fr"));
        int endMatrRowindex = Integer.parseInt(env.getProperty("excel_owh_to"));


        XSSFSheet sheet = workbook.getSheetAt(0); //첫번째쉬트
        int rows = sheet.getPhysicalNumberOfRows();
        //작업지시 생성
        String erProdNm = "";
        Float iwhQty = 0F;
        Long prodNo = 0L;
        String iwhWhNm = "";
        Long whNo = 0L;
        Date iwdate = new Date();
        Float stkQty = 0F;
        int idx = 0;
        List<Map<String, Object>> list = new ArrayList<>();

        XSSFRow row = sheet.getRow(rowindex);
        for (rowindex = startMatrRowindex; rowindex <= endMatrRowindex; rowindex++) {
            ++idx;
            Map<String, Object> Map = new HashMap<String, Object>();
            row = sheet.getRow(rowindex);
            if (row == null) continue;

            ProdIwh iwh = new ProdIwh();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = row.getCell(0).getStringCellValue(); // 입고일자
            date = date.replaceAll("\\p{Z}[-][0-9]", "");
            date = date.replaceAll("[/]", "-");
            if (date.contains("계") == true) {
                break;
            }

            erProdNm = row.getCell(1).getStringCellValue();  // 지시 제품명
            erProdNm = erProdNm.replaceAll("\\p{Z}", ""); // 띄어쓰기 제거
            ProdInfo pochk = prodRepository.findByCustNoAndErpProdNmAndUsedYn(custNo,erProdNm, "Y");
            if (pochk != null) {
                prodNo = pochk.getProdNo();
                iwh.setProdNo(prodNo);
            } else {
                Map.put("erpNm", erProdNm); // 열
                Map.put("idx", idx);
            }

            iwdate = sdf.parse(date);
            iwh.setIwhDt(iwdate);

            iwhQty = (float) row.getCell(2).getNumericCellValue(); // 출고 수량
            iwh.setIwhQty(iwhQty);

            iwh.setRegDt(DateUtils.getCurrentDateTime());
            iwh.setRegId(0L);
            iwh.setRegIp("127.0.0.1");
            iwh.setModDt(DateUtils.getCurrentDateTime());
            iwh.setModId(0L);
            iwh.setModIp("127.0.0.1");
            iwh.setIndcRsltNo(0L);
            iwh.setUsedYn("Y");
            iwh.setCustNo(custNo);
            iwhWhNm = row.getCell(3).getStringCellValue(); // 입고 창고
            iwhWhNm = iwhWhNm.replaceAll("\\p{Z}", "");
            WhInfo whchk = whinfoRepo.findByCustNoAndWhNmAndUsedYn(custNo,iwhWhNm, "Y");
            if (whchk != null) {
                whNo = whchk.getWhNo();
                iwh.setWhNo(whNo);
            } else {
                Map.put("whNm", iwhWhNm); // 열
                Map.put("idx", idx);
            }


            ProdStk stkvo = new ProdStk();
            ProdStk stkchk = stkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,whNo, prodNo, "Y");
            if (stkchk != null) {
                stkvo.setProdNo(0L);
                stkQty = stkchk.getStkQty();
                stkQty += iwhQty;
                stkvo.setStkQty(stkQty);
                stkvo.setRegDt(DateUtils.getCurrentDateTime());
                stkvo.setRegId(0L);
                stkvo.setRegIp("127.0.0.1");
                stkvo.setWhNo(whNo);
                stkvo.setStkDt(iwdate);
                stkvo.setStatTrfDt(DateUtils.getCurrentDateTime());
            } else {
                stkvo.setProdNo(prodNo);
                stkvo.setModDt(DateUtils.getCurrentDateTime());
                stkvo.setModId(0L);
                stkvo.setModIp("127.0.0.1");
                stkvo.setWhNo(whNo);
                stkvo.setStkQty(iwhQty);
                stkvo.setStkDt(iwdate);
                stkvo.setStatTrfDt(DateUtils.getCurrentDateTime());
            }
            stkvo.setUsedYn("Y");
            stkvo.setCustNo(custNo);
            if (pochk != null) {
                if (whchk != null) {
                    stkRepo.save(stkvo);
                    iwhRepo.save(iwh);
                }

            }

            list.add(Map);
        }
        return list;
    }

    /*자재 입고 처리 (Excel) */
    @Override
    public List<Map<String, Object>> stsMatrExcelIwh(HashMap<String, Object> paraMap) throws Exception {
        String tag = "ProdService.stsMatrExcelIwh => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        String filePath = fileService.getFileInfo(fileRoot,fileNo);
        FileInputStream file = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int rowindex = 0;
        int startMatrRowindex = Integer.parseInt(env.getProperty("excel_owh_fr"));
        int endMatrRowindex = Integer.parseInt(env.getProperty("excel_owh_to"));


        XSSFSheet sheet = workbook.getSheetAt(0); //첫번째쉬트
        int rows = sheet.getPhysicalNumberOfRows();
        //작업지시 생성
        String erMatrNm = "";
        Float iwhQty = 0F;
        Long matrNo = 0L;
        Long cmpyNo = 0L;
        String telNo = "";
        String iwhWhNm = "";
        String cmpy = "";
        Long whNo = 0L;
        Date iwdate = new Date();
        Float stkQty = 0F;
        int idx = 0;
        List<Map<String, Object>> list = new ArrayList<>();

        XSSFRow row = sheet.getRow(rowindex);
        for (rowindex = startMatrRowindex; rowindex <= endMatrRowindex; rowindex++) {
            ++idx;
            Map<String, Object> Map = new HashMap<String, Object>();
            row = sheet.getRow(rowindex);
            if (row == null) continue;

            MatrIwh iwh = new MatrIwh();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = row.getCell(0).getStringCellValue(); // 입고일자
            date = date.replaceAll("\\p{Z}[-][0-9]", "");
            date = date.replaceAll("[/]", "-");
            if (date.contains("계") == true) {
                break;
            }

            erMatrNm = row.getCell(1).getStringCellValue();  // 지시 제품명
            erMatrNm = erMatrNm.replaceAll("\\p{Z}", ""); // 띄어쓰기 제거

            MatrInfo machk = matrRepo.findByCustNoAndMatrNmAndUsedYn(custNo,erMatrNm, "Y");
            if (machk != null) {
                matrNo = machk.getMatrNo();
                iwh.setMatrNo(matrNo);
            } else {
                Map.put("erpNm", erMatrNm); // 열
                Map.put("idx", idx);
            }

            iwdate = sdf.parse(date);
            iwh.setIwhDt(iwdate);

            iwhQty = (float) row.getCell(2).getNumericCellValue(); // 출고 수량
            iwh.setIwhQty(iwhQty);

            iwh.setRegDt(DateUtils.getCurrentDateTime());
            iwh.setRegId(0L);
            iwh.setRegIp("127.0.0.1");
            iwh.setModDt(DateUtils.getCurrentDateTime());
            iwh.setModId(0L);
            iwh.setModIp("127.0.0.1");
            iwh.setPursNo(0L);
            iwh.setPursMatrNo(0L);
            iwh.setUsedYn("Y");

            iwhWhNm = row.getCell(3).getStringCellValue(); // 입고 창고
            iwhWhNm = iwhWhNm.replaceAll("\\p{Z}", "");
            WhInfo whchk = whinfoRepo.findByCustNoAndWhNmAndUsedYn(custNo,iwhWhNm, "Y");
            if (whchk != null) {
                whNo = whchk.getWhNo();
                iwh.setWhNo(whNo);
            } else {
                Map.put("whNm", iwhWhNm); // 열
                Map.put("idx", idx);
            }

            cmpy = row.getCell(4).getStringCellValue(); // 출고 회사
            cmpy = cmpy.replaceAll("\\p{Z}", "");
            Long cmpyTp = Long.parseLong(env.getProperty("code.cmpytp.purs"));
            CmpyInfo cmchk = cmpyRepo.findByCustNoAndCmpyTpAndCmpyNmAndUsedYn(custNo,cmpyTp,cmpy, "Y");
            if (cmchk != null) {
                if (cmchk.getMngrGbnCd() == 20) {
                    cmpyNo = cmchk.getCmpyNo();
                    iwh.setCmpyNo(cmpyNo);
                    telNo = row.getCell(5).getStringCellValue(); // 회사번호
                    cmchk.setTelNo(telNo);
                    cmpyRepo.save(cmchk);
                } else {
                    Map.put("custNo",custNo);
                    Map.put("mngrNm", cmpy); // 열
                    Map.put("idx", idx);
                }
            } else {
                Map.put("custNo",custNo);
                Map.put("cmpyNm", cmpy); // 열
                Map.put("idx", idx);
            }


            MatrStk stkvo = new MatrStk();
            MatrStk stkchk = matStkRepo.findByCustNoAndMatrNoAndUsedYn(custNo,matrNo, "Y");
            if (stkchk != null) {
                stkvo.setMatrNo(0L);
                stkQty = stkchk.getStkQty();
                stkQty += iwhQty;
                stkvo.setStkQty(stkQty);
                stkvo.setRegDt(DateUtils.getCurrentDateTime());
                stkvo.setRegId(0L);
                stkvo.setRegIp("127.0.0.1");
                stkvo.setWhNo(whNo);
                stkvo.setStatTrfDt(iwdate);
                stkvo.setStatTrfDt(DateUtils.getCurrentDateTime());
            } else {
                stkvo.setMatrNo(matrNo);
                stkvo.setModDt(DateUtils.getCurrentDateTime());
                stkvo.setModId(0L);
                stkvo.setModIp("127.0.0.1");
                stkvo.setWhNo(whNo);
                stkvo.setStkQty(iwhQty);
                stkvo.setStatTrfDt(iwdate);
                stkvo.setStatTrfDt(DateUtils.getCurrentDateTime());
            }
            stkvo.setUsedYn("Y");
            stkvo.setCustNo(custNo);
            if (machk != null) {
                if (whchk != null) {
                    if (cmchk != null) {
                        if (cmchk.getMngrGbnCd() == 21) {
                            matStkRepo.save(stkvo);
                            imr.save(iwh);
                        }
                    } else {
                        continue;
                    }
                }
            }

            list.add(Map);
        }
        return list;
    }

    /*자제 출고 처리 (Excel) */
    @Override
    public List<Map<String, Object>> stsMatrExcelOwh(HashMap<String, Object> paraMap) throws Exception {
        String tag = "ProdService.stsMatrExcelIwh => ";
        String fileRoot = paraMap.get("fileRoot").toString();
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        String filePath = fileService.getFileInfo(fileRoot,fileNo);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        FileInputStream file = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int rowindex = 0;
        int startMatrRowindex = Integer.parseInt(env.getProperty("excel_owh_fr"));
        int endMatrRowindex = Integer.parseInt(env.getProperty("excel_owh_to"));


        XSSFSheet sheet = workbook.getSheetAt(0); //첫번째쉬트
        int rows = sheet.getPhysicalNumberOfRows();
        //작업지시 생성
        String erMatrNm = "";
        Float owhQty = 0F;
        Long matrNo = 0L;
        Long cmpyNo = 0L;
        String telNo = "";
        String owhWhNm = "";
        String cmpy = "";
        Long whNo = 0L;
        Date owdate = new Date();
        Float stkQty = 0F;
        int idx = 0;
        List<Map<String, Object>> list = new ArrayList<>();

        XSSFRow row = sheet.getRow(rowindex);
        for (rowindex = startMatrRowindex; rowindex <= endMatrRowindex; rowindex++) {
            ++idx;
            Map<String, Object> Map = new HashMap<String, Object>();
            row = sheet.getRow(rowindex);
            if (row == null) continue;

            MatrOwh owh = new MatrOwh();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = row.getCell(0).getStringCellValue(); // 입고일자
            date = date.replaceAll("\\p{Z}[-][0-9]", "");
            date = date.replaceAll("[/]", "-");
            if (date.contains("계") == true) {
                break;
            }

            erMatrNm = row.getCell(1).getStringCellValue();  // 지시 제품명
            erMatrNm = erMatrNm.replaceAll("\\p{Z}", ""); // 띄어쓰기 제거

            MatrInfo machk = matrRepo.findByCustNoAndMatrNmAndUsedYn(custNo,erMatrNm, "Y");
            if (machk != null) {
                matrNo = machk.getMatrNo();
                owh.setMatrNo(matrNo);
            } else {
                Map.put("custNo",custNo);
                Map.put("erpNm", erMatrNm); // 열
                Map.put("idx", idx);
            }

            owdate = sdf.parse(date);
            owh.setOwhDt(owdate);

            owhQty = (float) row.getCell(2).getNumericCellValue(); // 출고 수량
            owh.setOwhQty(owhQty);

            owh.setRegDt(DateUtils.getCurrentDateTime());
            owh.setRegId(0L);
            owh.setRegIp("127.0.0.1");
            owh.setModDt(DateUtils.getCurrentDateTime());
            owh.setModId(0L);
            owh.setModIp("127.0.0.1");
            owh.setOwhReqQty(owhQty);
            owh.setIndcNo(0L);
            owh.setOwhUnit(Long.parseLong(env.getProperty("code.base.sale_unit_Kg")));
            owh.setOwhReqDt(owdate);
            owh.setUsedYn("Y");
            owh.setCustNo(custNo);

            owhWhNm = row.getCell(3).getStringCellValue(); // 입고 창고
            owhWhNm = owhWhNm.replaceAll("\\p{Z}", "");
            WhInfo whchk = whinfoRepo.findByCustNoAndWhNmAndUsedYn(custNo,owhWhNm, "Y");
            if (whchk != null) {
                whNo = whchk.getWhNo();
                owh.setWhNo(whNo);
            } else {
                Map.put("custNo",custNo);
                Map.put("whNm", owhWhNm); // 열
                Map.put("idx", idx);
            }

            MatrStk stkvo = new MatrStk();
            MatrStk stkchk = matStkRepo.findByCustNoAndMatrNoAndUsedYn(custNo,matrNo, "Y");
            if (stkchk != null) {
                stkvo.setMatrNo(0L);
                stkQty = stkchk.getStkQty();
                stkQty -= owhQty;
                stkvo.setStkQty(stkQty);
                stkvo.setRegDt(DateUtils.getCurrentDateTime());
                stkvo.setRegId(0L);
                stkvo.setRegIp("127.0.0.1");
                stkvo.setWhNo(whNo);
                stkvo.setStatTrfDt(owdate);
                stkvo.setStatTrfDt(DateUtils.getCurrentDateTime());
            } else {
                stkvo.setMatrNo(matrNo);
                stkvo.setModDt(DateUtils.getCurrentDateTime());
                stkvo.setModId(0L);
                stkvo.setModIp("127.0.0.1");
                stkvo.setWhNo(whNo);
                stkvo.setStkQty(0F);
                stkvo.setStatTrfDt(owdate);
                stkvo.setStatTrfDt(DateUtils.getCurrentDateTime());
            }
            stkvo.setUsedYn("Y");
            stkvo.setCustNo(custNo);

            if (machk != null) {
                if (whchk != null) {
                    matStkRepo.save(stkvo);
                    omr.save(owh);
                }
            }
            list.add(Map);
        }
        return list;
    }

    /*서울 식품 생산지시기반 작업에 필요한 소요자재 구매요청*/
    private PursInfo savePursInfoSeoul(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.saveReqMatr==>";

        Long indcNo = Long.parseLong(paraMap.get("indcNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = inmapper.chkReqPurs(paraMap); //재고부족분이 있는 자재가 존재하는지 점검
        if (ds.size() <= 0) return null;

        PursInfo pivo = new PursInfo();

        pivo.setIndcNo(indcNo);
        pivo.setPursDt((Date) paraMap.get("makeDt"));
        pivo.setDlvReqDt((Date) paraMap.get("makeDt"));
        pivo.setPursSts(Long.parseLong(env.getProperty("purs.sts.blind")));
        pivo.setDlvDt((Date) paraMap.get("makeDt"));
        pivo.setUsedYn("Y");
        pivo.setModDt(DateUtils.getCurrentDateTime());
        try{
            pivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        }catch (NullPointerException en){
            pivo.setModId(0L);
        }

        pivo.setModIp("127.0.0.1");
        pivo.setRegDt(DateUtils.getCurrentDateTime());
        try{
            pivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        }catch (NullPointerException en){
            pivo.setRegId(0L);
        }
        pivo.setRegIp("127.0.0.1");
        pivo.setCustNo(custNo);

        MakeIndc vhkvo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,indcNo, "Y");
        if (vhkvo != null) {
            vhkvo.getOrdNo();
        }
        pivo.setOrdNo(vhkvo.getOrdNo());

        pivo = pir.save(pivo); //구매요청 기본정보 저장


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
            pmvo.setPursQty(Float.valueOf(String.valueOf(el.get("reqPursQty"))));
            pmvo.setPursUnit(Long.parseLong(el.get("pursUnit").toString()));
            pmvo.setUsedYn("Y");
            PursMatr chkmvo = pmr.findByCustNoAndPursNoAndMatrNoAndUsedYn(custNo,pmvo.getPursNo(), pmvo.getMatrNo(), "Y");
            if (chkmvo != null) {
                pmvo.setPursMatrNo(chkmvo.getPursMatrNo());
                pmvo.setModDt(DateUtils.getCurrentDateTime());
                pmvo.setModId(pivo.getModId());
                pmvo.setModIp(pivo.getModIp());
            } else {
                pmvo.setPursMatrNo(0L);
                pmvo.setRegDt(DateUtils.getCurrentDateTime());
                pmvo.setRegId(pivo.getRegId());
                pmvo.setRegIp(pivo.getRegIp());
                pmvo.setModDt(DateUtils.getCurrentDateTime());
                pmvo.setModId(pivo.getModId());
                pmvo.setModIp(pivo.getModIp());
            }
            pmvo.setCustNo(custNo);
            chkmvo = pmr.save(pmvo);
        }

//        Map<String, Object> bmap = new HashMap<String, Object>();
//        bmap.put("codeNo", pivo.getPursNo());
//        bmap.put("savePath", "purs/");
//        stockService.makeBarCode(bmap);

        this.saveMatrIwhSeoul(pivo);
        return pivo;

    }

    private void saveMatrIwhSeoul(PursInfo pivo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long custNo = pivo.getCustNo();
        Map<String, Object> pmap = new HashMap<String, Object>();
        pmap.put("pursNo", pivo.getPursNo());
        pmap.put("custNo", custNo);
        pmap.put("pageNo", 0);
        pmap.put("pageSz", 1000);


        List<Map<String, Object>> ds = pursService.getPursMatrList(pmap);
        for (Map<String, Object> el : ds) {
            MatrIwh mivo = new MatrIwh();
            mivo.setPursNo(Long.valueOf(el.get("pursNo").toString()));
            mivo.setPursMatrNo(Long.valueOf(el.get("pursMatrNo").toString()));
            mivo.setCmpyNo(699693L); //입고거래처는 입고 검수 시점에 설정해야 함.
            Long matrNo = Long.valueOf(el.get("matrNo").toString());
            mivo.setMatrNo(matrNo);
            try {
                mivo.setIwhDt(sdf.parse(el.get("pursDt").toString())); //입고일자를 구매일자로 자동 설
            } catch (NullPointerException en) {
                mivo.setIwhDt(pivo.getDlvDt());
            }
            catch (ParseException e) {
                e.printStackTrace();
            }

            mivo.setIwhQty(Float.valueOf(String.valueOf(el.get("pursQty"))));
            mivo.setWhNo(3932L);//입고창고번호는 입고 검수 시점에 설정 해야 함,
            mivo.setUsedYn("N");
            MatrIwh chkMatrIwh = imr.findByCustNoAndPursNoAndMatrNoAndUsedYn(custNo,mivo.getPursNo(), mivo.getMatrNo(), "Y");
            if (chkMatrIwh != null) {
                mivo.setIwhNo(chkMatrIwh.getIwhNo());
            }
            try {
                mivo.setRetnQty(Float.valueOf(String.valueOf(el.get("retnQty"))));
            } catch (NullPointerException ne) {
                mivo.setRetnQty(Float.valueOf(0));
            } catch (NumberFormatException ne) {
                mivo.setRetnQty(Float.valueOf(0));
            }
            MatrIwh iwhvo = imr.findByCustNoAndPursNoAndMatrNoAndWhNoAndUsedYn(custNo,mivo.getPursNo(),mivo.getMatrNo(),mivo.getWhNo(),"Y");
            if(iwhvo != null){
                mivo.setIwhNo(iwhvo.getIwhNo());
                mivo.setModDt(DateUtils.getCurrentDateTime());
                try{
                    mivo.setModId(pivo.getModId());
                }catch (NullPointerException en){
                    mivo.setModId(0L);
                }
                mivo.setModIp("127.0.0.1");
            }else {
                mivo.setRegDt(DateUtils.getCurrentDateTime());
                try{
                    mivo.setRegId(pivo.getRegId());
                }catch (NullPointerException en){
                    mivo.setRegId(0L);
                }

                mivo.setRegIp("127.0.0.1");
            }
            mivo.setCustNo(custNo);
            imr.save(mivo);

//            Map<String, Object> ibmap = new HashMap<String, Object>();
//            ibmap.put("codeNo", mivo.getIwhNo());
//            ibmap.put("savePath", "matr/iwh/");
//            stockService.makeBarCode(ibmap);

            //자재 현황
            MatrStk stkvo = new MatrStk();
            stkvo.setUsedYn("Y");

            MatrStk mrchk = matStkRepo.findByCustNoAndMatrNoAndUsedYn(custNo,matrNo, "Y");
            if (mrchk != null) {
                stkvo.setMatrStkNo(mrchk.getMatrStkNo());
                stkvo.setMatrNo(mrchk.getMatrNo());
                stkvo.setWhNo(mrchk.getWhNo());
                stkvo.setStkQty(0F);
                stkvo.setModDt(DateUtils.getCurrentDateTime());
                try{
                    mivo.setModId(pivo.getModId());
                }catch (NullPointerException en){
                    mivo.setModId(0L);
                }
                mivo.setModIp("127.0.0.1");
            } else {
                stkvo.setStkQty(0F);
                stkvo.setMatrNo(matrNo);
                stkvo.setRegDt(DateUtils.getCurrentDateTime());
                try{
                    stkvo.setRegId(pivo.getRegId());
                }catch (NullPointerException en){
                    stkvo.setRegId(0L);
                }
                stkvo.setRegIp("127.0.0.1");
                stkvo.setWhNo(3932L);
            }
            stkvo.setCustNo(custNo);
            matStkRepo.save(stkvo);

            Long indcNo = pivo.getIndcNo();
            MatrOwh owhvo = new MatrOwh();
            owhvo.setIndcNo(indcNo);
            owhvo.setMatrNo(matrNo); //소요자재번호
            owhvo.setModDt(DateUtils.getCurrentDateTime());
            owhvo.setModId(0L);
            owhvo.setModIp("127.0.0.1");

            MatrOwh chkvo = omr.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo,owhvo.getIndcNo(), owhvo.getMatrNo(), "Y");
            if (chkvo != null) {
                owhvo.setOwhNo(chkvo.getOwhNo()); //관리번호
                owhvo.setWhNo(chkvo.getWhNo());//창고번호
                owhvo.setModDt(DateUtils.getCurrentDateTime());
                try{
                    owhvo.setModId(pivo.getModId());
                }catch (NullPointerException en){
                    owhvo.setModId(0L);
                }
                owhvo.setModIp("127.0.0.1");
            } else {
                owhvo.setWhNo(3932L);//창고번호 -> 실제출고시점에 설정됨
                owhvo.setOwhQty(Float.valueOf(String.valueOf(el.get("pursQty")))); //출고수량-> matr_stk - stkQty 입력 후 삭제 예정
                owhvo.setRegDt(DateUtils.getCurrentDateTime());
                try{
                    owhvo.setRegId(pivo.getModId());
                }catch (NullPointerException en){
                    owhvo.setRegId(0L);
                }
                owhvo.setRegIp("127.0.0.1");
            }
            Float owhReqQty = Float.valueOf(String.valueOf(el.get("pursQty")));
            owhvo.setOwhReqQty(owhReqQty); //요청수량
            try {
                owhvo.setOwhReqDt(sdf.parse(el.get("pursDt").toString())); //출고요청일자
            } catch (NullPointerException en) {
                owhvo.setOwhReqDt(pivo.getDlvDt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                owhvo.setOwhDt(sdf.parse(el.get("pursDt").toString()));
            }catch (NullPointerException en) {
                owhvo.setOwhReqDt(pivo.getDlvDt());
            }catch (ParseException e) {
                e.printStackTrace();
            }
            owhvo.setOwhUnit(Long.parseLong(el.get("makeUnit").toString()));//요청단위
            owhvo.setUsedYn("N");
            owhvo.setCustNo(custNo);
            omr.save(owhvo);

//            Map<String, Object> bmap = new HashMap<String, Object>();
//            bmap.put("codeNo", owhvo.getOwhNo());
//            bmap.put("savePath", "matr/owh/");
//            stockService.makeBarCode(bmap);

        }

    }

    @Override
    public List<Map<String, Object>> getProdBom(Map<String, Object> paraMap) {
        String tag = "vsvc.ProdService.prodBomList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProdBom(paraMap);
    }

    @Override
    public int getProdBomCount(Map<String, Object> paraMap) {
        return mapper.getListProdBomCount(paraMap);
    }

    @Override
    public Map<String, Object> getProdStkInfo(Map<String, Object> paraMap) {
        String tag = "vsvc.ProdService.getProdStkInfo => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProdStkInfo(paraMap);
    }



    // 일단 보류 OJT
    public long prodInfoExcel(HashMap<String, Object> paraMap) throws Exception {
        String tag = "ProdService.prodIndcExcel => ";
        StringBuffer buf = new StringBuffer();
        String fileRoot = paraMap.get("fileRoot").toString();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        String prodNm = "";
        String matrNm = "";
        String exData = "";
        String prodcd = "";
        Long brnchNo = 0L;
        Float spga = 0F;

        Float mess = 0f;
        double magrbase = 0;

        XSSFSheet sheet = workbook.getSheetAt(0); //첫번째쉬트
        int rows = sheet.getPhysicalNumberOfRows();
        int rowindex = 0;
        log.info(tag + "전체 excel처리 행수 = " + rows);
        XSSFRow row = sheet.getRow(rowindex);

//        원자재 등록
        for (rowindex = 0; rowindex <= rows; rowindex++) {
            row = sheet.getRow(rowindex);
            if (row == null) continue;
            try {
                matrNm = row.getCell(0).getStringCellValue();
                matrNm = matrNm.replaceAll("\\p{Z}", "");
                if (prodNm.contains("합계") == true) {
                    break;
                }
            } catch (NullPointerException ne) {
                log.info(tag + "행번(" + rowindex + ") : 자재명 없음.... skip....");
                continue;
            }

//            MatrInfo matrInfo = new MatrInfo();
////            prodcd = row.getCell(0).getStringCellValue(); // 코드번호
////            matrInfo.setItemCd(prodcd);
//            matrInfo.setMatrNm(matrNm);
//            matrInfo.setSaveTmpr(Long.parseLong(env.getProperty("code.savetmpr.room"))); //보관온도
//            matrInfo.setValidTerm(12); //유효기간-12개월
//            matrInfo.setPursUnit(Long.parseLong(env.getProperty("code.purs_unit.kg"))); //구매단위(KG)
//            matrInfo.setMatrTp(Long.parseLong(env.getProperty("code.matrtp.matr"))); // 타입 / 원자재,부자재
//            matrInfo.setBrnchNo(1L);
//            matrInfo.setUsedYn("Y");
//            matrInfo.setVol(0F);
//            matrInfo.setValidTerm(12);
//            matrInfo.setVol(1000F);
//            matrInfo.setMess(1000F);
//            matrInfo.setMngrBase(Long.parseLong(env.getProperty("code.base.mngrbase_mess")));
//            matrInfo.setSpge(1F);
//
//            MatrInfo chkMatr = matrRepo.findByMatrNmAndUsedYn(matrNm, "Y");
//            if(chkMatr != null ){
//                matrInfo.setMatrNo(chkMatr.getMatrNo());
//                matrInfo.setRegIp(chkMatr.getRegIp());
//                matrInfo.setRegId(chkMatr.getRegId());
//                matrInfo.setRegDt(chkMatr.getRegDt());
//            }else{
//                matrInfo.setMatrNo(0L);
//                matrInfo.setModIp("127.0.0.1");
//                matrInfo.setModId(2L);
//                matrInfo.setModDt(DateUtils.getCurrentDate());
//            }
//            matrRepo.save(matrInfo);

            MatrCmpy cmvo = new MatrCmpy();

            cmvo.setCmpyNo(598977L);
            cmvo.setDefaultYn("N");
            cmvo.setUsedYn("Y");

            MatrInfo chkMatr = matrRepo.findByCustNoAndMatrNmAndUsedYn(custNo,matrNm, "Y");
            if (chkMatr != null) {
                cmvo.setMatrNo(chkMatr.getMatrNo());
                cmvo.setRegIp(chkMatr.getRegIp());
                cmvo.setRegId(chkMatr.getRegId());
                cmvo.setRegDt(chkMatr.getRegDt());
            } else {
                cmvo.setMatrNo(0L);
                cmvo.setModIp("127.0.0.1");
                cmvo.setModId(2L);
                cmvo.setModDt(DateUtils.getCurrentDate());
            }
            cmvo.setCustNo(custNo);
            matrCmpyRepo.save(cmvo);

//            상품 등록
//
//            try {
//                prodNm = row.getCell(1).getStringCellValue();
//                prodNm = prodNm.replaceAll("\\p{Z}", "");
//                if (prodNm.contains("합계") == true) {
//                    break;
//                }
//            } catch (NullPointerException ne) {
//                log.info(tag + "행번(" + rowindex + ") : 제품명 없음.... skip....");
//                continue;
//            }
//            ProdInfo prodInfo = new ProdInfo();
//            prodcd = row.getCell(0).getStringCellValue(); // 코드번호
//            if(prodcd == null){continue;}
//            prodInfo.setProdCode(prodcd);
//            prodInfo.setErpProdNm(prodNm);
//            double qtypkg = row.getCell(6).getNumericCellValue();
//            prodInfo.setQtyPerPkg((int)qtypkg); //세트구성수량(기본값설정)
//            prodInfo.setFileNo(0L);//상품이미지(기본값설정)
//            prodInfo.setUsedYn("Y");//사용구분(기본값설정)
//            prodInfo.setProdBrnch(1L); //분류(기본값설정)
//            prodInfo.setSaveTmpr(Long.parseLong(env.getProperty("code.savetmpr.room"))); //보관온도
//            prodInfo.setBrnchNo(1251L); // 공정별 분류
//            prodInfo.setBrix(0F);
//            prodInfo.setSpga(1F); // 비중 비율
//            prodInfo.setHeatTp(0L);
//
//            Float vol = (float) row.getCell(4).getNumericCellValue();
//            exData = row.getCell(5).getStringCellValue();
//            Long prodShape = Long.parseLong(env.getProperty("code.base.saleunit"));
//            CodeInfo mgvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(prodShape, exData.replace(" ", ""), "Y");
//            if (mgvo != null) {
//                Long prodUnit = mgvo.getCodeNo();
//                prodInfo.setSaleUnit(prodUnit); // 단위
//
//                if(prodUnit == Long.parseLong(env.getProperty("code.base.sale_unit_Kg"))){ // KG을 G으로 환산
//                    prodInfo.setVol(vol*1000);
//                    prodInfo.setMess(vol*1000);
//                }else {
//                    prodInfo.setVol(vol);
//                    prodInfo.setMess(vol);
//                }if(prodUnit == Long.parseLong(env.getProperty("code.base.sale_unit_ml"))){
//                    prodInfo.setMngrBase(Long.parseLong(env.getProperty("code.base.mngrbase_mess")));
//                }else {
//                    prodInfo.setMngrBase(Long.parseLong(env.getProperty("code.base.mngrbase_imp")));
//                }
//
//            }
//
//            ProdInfo chkInfo = prodRepo.findByProdNmAndUsedYn(prodNm, "Y");
//            if(chkInfo != null ){
//                prodInfo.setProdNo(chkInfo.getProdNo());
//                prodInfo.setRegIp(chkInfo.getRegIp());
//                prodInfo.setRegId(chkInfo.getRegId());
//                prodInfo.setRegDt(chkInfo.getRegDt());
//                prodInfo.setProdNm(chkInfo.getProdNm());
//            }else{
//                prodInfo.setProdNm(prodNm);
//                prodInfo.setModIp("127.0.0.1");
//                prodInfo.setModId(2L);
//                prodInfo.setModDt(DateUtils.getCurrentDate());
//            }
//
//            prodRepo.save(prodInfo);
//
        }
        return 0;
    }

//    @Override
//    public void updateBomPursYn(Map<String, Object> paraMap){
//        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
//        List<Map<String, Object>> matrList = (List<Map<String, Object>>) paraMap.get("matrList");
//        for(Map<String, Object> el : matrList){
//            Long matrNo = Long.parseLong(el.get("matr_no").toString());
//            ProdBom chkvo = bomRepo.findByProdNoAndMatrNoAndUsedYn(prodNo, matrNo, "Y");
//            if(chkvo != null){
//                chkvo.setPursYn(el.get("purs_yn").toString());
//                bomRepo.save(chkvo);
//            }
//        }
//    }
}












