package daedan.mes.ord.service;

import daedan.mes.cmpy.domain.CmpyInfo;
import daedan.mes.cmpy.repository.CmpyRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.matr.domain.MatrInfo;
import daedan.mes.matr.service.MatrService;
import daedan.mes.ord.domain.OrdInfo;
import daedan.mes.ord.domain.OrdProd;
import daedan.mes.ord.domain.OrdProdCart;
import daedan.mes.ord.mapper.OrdMapper;
import daedan.mes.ord.repository.OrdProdCartRepository;
import daedan.mes.ord.repository.OrdProdRepository;
import daedan.mes.ord.repository.OrdRepository;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.prod.service.ProdService;
import daedan.mes.purs.domain.PursMatr;
import daedan.mes.purs.repository.PursInfoRepository;
import daedan.mes.purs.service.PursService;
import daedan.mes.stock.service.StockService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("ordService")
public class OrdServiceImpl implements  OrdService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private OrdRepository ordRepo;

    @Autowired
    private OrdProdRepository ordprod;

    @Autowired
    private OrdProdRepository ordProdRepo;

    @Autowired
    private OrdProdRepository opRepository;

    @Autowired
    private ProdRepository prodRepo;

    @Autowired
    private CmpyRepository cmpyRepo;

    @Autowired
    private OrdProdCartRepository opcRepository;

    @Autowired
    private OrdService ordService;

    @Autowired
    private PursService pursService;

    @Autowired
    private MatrService matrService;

    @Autowired
    private ProdService prodService;

    @Autowired
    private OrdMapper mapper;


    @Override
    public void initOrdCart(Map<String, Object> paraMap) {
        String tag = "OrdServiceImpl.initOrdCart";
        long userId = (int) paraMap.get("userId");
        log.info(tag + "userId = " + userId);
        ordService.dropOrdProdCart(paraMap);
    }

    @Override
    @Transactional
    public void addOnProdToCart(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = (ArrayList<Map<String, Object>>) paraMap.get("prods");
        for (Map<String, Object> el : ds) {
            OrdProdCart opcvo = new OrdProdCart();
            Map<String, Object> pmap = new HashMap<String, Object>();
            pmap.put("prodNo", el.get("prod_no"));
            Map<String, Object> prodMap = prodService.getProdInfo(pmap);
            opcvo.setOrdNo(Long.parseLong(paraMap.get("ord_no").toString()));
            opcvo.setProdNo(Long.parseLong(el.get("prod_no").toString()));
            opcvo.setUserId(Long.parseLong(paraMap.get("userId").toString()));
            opcvo.setOrdQty(1F);
            opcvo.setOrdSz((String) prodMap.get("sz"));
            opcvo.setOrdModlNm((String) prodMap.get("modl_nm"));
            opcvo.setDlvQty(0);
            opcvo.setSaleUnit(Long.parseLong(prodMap.get("sale_unit").toString()));
            opcvo.setQtyPerPkg(Integer.parseInt(prodMap.get("qty_per_pkg").toString()));
            OrdProdCart chkvo = opcRepository.findByCustNoAndUserIdAndProdNo(custNo,Long.parseLong(paraMap.get("userId").toString()), opcvo.getProdNo());
            if (chkvo != null) {
                opcvo.setCartNo(chkvo.getCartNo());
            }
            opcRepository.save(opcvo);
        }
    }

    @Override
    @Transactional
    public void moveOrdProdToCart(Map<String, Object> paraMap) {
        String tag = "OrdServiceImpl.moveOrdProdToCart => ";
        long userId = (int) paraMap.get("userId");
        mapper.dropOrdProdCart(paraMap);
        List<Map<String, Object>> ds = mapper.getOrdProdList(paraMap);
        int idx = -1;
        Map<String, Object> dsMap = null;

        while (++idx < ds.size()) {
            dsMap = ds.get(idx);
            OrdProdCart opcvo = new OrdProdCart();
            opcvo.setOrdNo(Long.parseLong(dsMap.get("ord_no").toString()));
            opcvo.setProdNo(Long.parseLong(dsMap.get("prod_no").toString()));
            opcvo.setUserId(Long.parseLong(paraMap.get("userId").toString()));
            opcvo.setOrdQty(Float.parseFloat(dsMap.get("ord_qty").toString()));
            opcvo.setOrdSz((String) dsMap.get("ord_sz"));
            opcvo.setOrdModlNm((String) dsMap.get("ord_modl_nm"));
            opcvo.setDlvQty(Integer.parseInt(dsMap.get("dlv_qty").toString()));
            opcvo.setSaleUnit(Long.parseLong(dsMap.get("sale_unit").toString()));
            opcvo.setQtyPerPkg(Integer.parseInt(dsMap.get("qty_per_pkg").toString()));
            opcRepository.save(opcvo);
        }
    }


    @Override
    public List<Map<String, Object>> getOrdList(Map<String, Object> paraMap) {
        return mapper.getOrdList(paraMap);
    }

    @Override
    public int getOrdListCount(Map<String, Object> paraMap) {

        return mapper.getOrdListCount(paraMap);
    }

    @Override
    public Map<String, Object> getOrdInfo(Map<String, Object> paraMap) {
        String tag = "OrdServiceImpl.getOrdInfo => ";
        long ordNo = (int) paraMap.get("ordNo");
        log.info(tag + "ordNo = " + ordNo);
        Map<String, Object> rmap = mapper.getOrdInfo(paraMap);
        if (rmap != null) {
            StringBuffer buf = new StringBuffer();
            buf.append(env.getProperty("img.root.path")).append("ord/").append(rmap.get("ord_no")).append(".png");
            rmap.put("ord_bar_code_url", buf.toString());
        }
        try {
            log.info(tag + "ord_bar_code_url => " + rmap.get("ord_bar_code_url"));
        } catch (NullPointerException ne) {

        }
        return rmap;
    }

    @Override
    public Map<String, Object> getOemOrdInfo(Map<String, Object> paraMap) {
        String tag = "OrdServiceImpl.getOemOrdInfo => ";
        long ordNo = (int) paraMap.get("ordNo");
        log.info(tag + "ordNo = " + ordNo);
        return mapper.getOemOrdInfo(paraMap);
    }

    @Override
    public Map<String, Object> getPrjOrdInfo(Map<String, Object> paraMap) {
        String tag = "OrdServiceImpl.getPrjOrdInfo => ";
        long ordNo = (int) paraMap.get("ordNo");
        log.info(tag + "ordNo = " + ordNo);
        return mapper.getPrjOrdInfo(paraMap);
    }

    @Override
    public List<Map<String, Object>> getOrdProdList(Map<String, Object> paraMap) {
        String tag = "vscv.ordService.getOrdProdList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getOrdProdList(paraMap);
    }

    @Override
    public int getOrdProdListCount(Map<String, Object> paraMap) {
        return mapper.getOrdProdListCount(paraMap);

    }

    @Override
    public List<Map<String, Object>> getOrdProdCartList(Map<String, Object> paraMap) {
        return mapper.getOrdProdCartList(paraMap);
    }

    @Override
    public int getOrdProdCartListCount(Map<String, Object> paraMap) {
        return mapper.getOrdProdCartListCount(paraMap);

    }

    @Transactional
    @Override
    public void apndOrdProd(Map<String, Object> paraMap) {
        String tag = "vsvc.ordService.apndOrdProd => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> prodList = (List<Map<String, Object>>) paraMap.get("prodList");
        Map<String, Object> prodMap = null;
        Map<String, Object> prMap = new HashMap<String, Object>();
        Long ordNo = Long.parseLong(paraMap.get("ordNo").toString());


        for (Map<String, Object> el : prodList) {
            OrdProd vo = new OrdProd();
            vo.setOrdNo(ordNo);
            vo.setProdNo(Long.parseLong(el.get("prodNo").toString()));
            vo.setModDt(DateUtils.getCurrentDate());
            vo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            vo.setModIp(paraMap.get("ipaddr").toString());

            int qtyParPkg = Integer.parseInt(el.get("qtyPerPkg").toString());

            OrdProd chkvo = ordProdRepo.findByCustNoAndOrdNoAndProdNoAndUsedYn(custNo,vo.getOrdNo(), vo.getProdNo(), "Y");
            if (chkvo != null) {
                vo.setOrdQty(chkvo.getOrdQty());
                vo.setOrdSz(chkvo.getOrdSz());
                vo.setOrdModlNm(chkvo.getOrdModlNm());
                vo.setSaleUnit(chkvo.getSaleUnit());
                vo.setQtyPerPkg(chkvo.getQtyPerPkg());
            } else {
                prMap.put("prodNo", Long.parseLong(el.get("prodNo").toString()));
                try {
                    vo.setOrdQty(Float.parseFloat(el.get("ordQty").toString()));
                } catch (NullPointerException ne) {
                    vo.setOrdQty(0F);
                }
                vo.setOrdSz((String) el.get("sz"));
                vo.setSaleUnit(Long.parseLong(el.get("saleUnit").toString()));
                vo.setQtyPerPkg(qtyParPkg);

                /*getProdInfo에서 나오는 리스트가 해당 창고 2개 이상일 경우, 문제 발생 - 21.07.10 - 풀지 말것.*/
//                prodMap = prodService.getProdInfo(prMap);
//                if (prodMap != null) {
//                    vo.setOrdModlNm((String) prodMap.get("modl_nm"));
//                }
                vo.setRegDt(DateUtils.getCurrentDate());
                vo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                vo.setRegIp(paraMap.get("ipaddr").toString());
            }
            vo.setUsedYn("Y");
            vo.setCustNo(custNo);
            ordProdRepo.save(vo); //주문상품 저장
        }
    }


    @Transactional
    @Override
    public void apndOrdProdToCart(Map<String, Object> paraMap) {
        String tag = "OrdService.apndOrdProdToCart => ";
        List<Map<String, Object>> prodList = (List<Map<String, Object>>) paraMap.get("prods");
        Map<String, Object> prodMap = null;
        Map<String, Object> prMap = new HashMap<String, Object>();
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        Long ordNo = Long.parseLong(paraMap.get("ordNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());

        for (Map<String, Object> el : prodList) {
            OrdProdCart vo = new OrdProdCart();
            vo.setOrdNo(ordNo);
            vo.setProdNo(Long.parseLong(el.get("prod_no").toString()));
            vo.setUserId(userId.longValue());
            OrdProdCart chkvo = opcRepository.findByCustNoAndUserIdAndProdNo(custNo,vo.getUserId(), vo.getProdNo());
            if (chkvo != null) {
                vo.setOrdQty(chkvo.getOrdQty());
                vo.setOrdSz(chkvo.getOrdSz());
                vo.setOrdModlNm(chkvo.getOrdModlNm());
                vo.setSaleUnit(chkvo.getSaleUnit());
                vo.setQtyPerPkg(chkvo.getQtyPerPkg());
            } else {
                prMap.put("prodNo", Long.parseLong(el.get("prod_no").toString()));
                try {
                    vo.setOrdQty(Float.parseFloat(el.get("ord_qty").toString()));
                } catch (NullPointerException ne) {
                    vo.setOrdQty(0F);
                }
                vo.setOrdSz((String) el.get("sz"));
                vo.setSaleUnit(Long.parseLong(el.get("sale_unit").toString()));
                vo.setQtyPerPkg(Integer.parseInt(el.get("qty_per_pkg").toString()));

                prodMap = prodService.getProdInfo(prMap);
                if (prodMap != null) {
                    vo.setOrdModlNm((String) prodMap.get("modl_nm"));
                }
            }
            OrdProdCart chkcartvo = opcRepository.findByCustNoAndUserIdAndOrdNoAndProdNo(custNo,userId, vo.getOrdNo(), vo.getProdNo());
            if (chkvo != null) {
                vo.setCartNo(chkcartvo.getCartNo());
            }
            opcRepository.save(vo); //주문상품카트에 저장
        }
    }

    @Transactional
    @Override
    public void saveOrdProdCart(Map<String, Object> paraMap) { //사용중
        String tag = "OrdService.saveOrdProdCart => ";

        Map<String, Object> prodMap = null;
        Map<String, Object> prMap = new HashMap<String, Object>();
        OrdProdCart vo = new OrdProdCart();
        log.info(tag + "1. 주문상품카드 update");
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        vo.setOrdNo(Long.parseLong(paraMap.get("ord_no").toString()));
        vo.setProdNo(Long.parseLong(paraMap.get("prod_no").toString()));
        vo.setUserId(userId.longValue());

        try {
            vo.setOrdQty(Float.parseFloat(paraMap.get("ord_qty").toString()));
        } catch (NullPointerException ne) {
            vo.setOrdQty(0F);
        }
        vo.setOrdSz((String) paraMap.get("ord_sz"));
        vo.setSaleUnit(Long.parseLong(paraMap.get("sale_unit").toString()));
        vo.setQtyPerPkg(Integer.parseInt(paraMap.get("qty_per_pkg").toString()));

        prMap.put("prodNo", vo.getProdNo());
        prodMap = prodService.getProdInfo(prMap);
        if (prodMap != null) {
            vo.setOrdModlNm((String) prodMap.get("modl_nm"));
        }

        OrdProdCart chkvo = opcRepository.findByCustNoAndUserIdAndOrdNoAndProdNo(custNo,userId, vo.getOrdNo(), vo.getProdNo());
        if (chkvo != null) {
            vo.setCartNo(chkvo.getCartNo());
        }
        opcRepository.save(vo); //주문상품카트에 저장
        log.info(tag + "2. 주문상품수량 update");

        OrdProd opvo = new OrdProd();
        opvo.setUsedYn("Y");
        opvo.setOrdNo(vo.getOrdNo());
        opvo.setProdNo(vo.getProdNo());
        opvo.setOrdSz(vo.getOrdSz());
        opvo.setOrdQty(vo.getOrdQty());
        opvo.setQtyPerPkg(vo.getQtyPerPkg());
        opvo.setSaleUnit(vo.getSaleUnit());
        try {
            opvo.setOrdModlNm(vo.getOrdModlNm());
        } catch (NullPointerException ne) {

        }
        try {
            opvo.setOrdProdNo(Long.parseLong(paraMap.get("ord_prod_no").toString()));
        } catch (NullPointerException ne) {
            opvo.setOrdProdNo(0L);
            opvo.setRegId(userId);
            opvo.setRegIp(paraMap.get("ipaddr").toString());
            opvo.setRegDt(DateUtils.getCurrentDate());
        }
        opvo.setModId(userId);
        opvo.setModIp(paraMap.get("ipaddr").toString());
        opvo.setModDt(DateUtils.getCurrentDate());
        ordProdRepo.save(opvo);
        //mapper.updateOrdProdQty(StringUtil.voToMap(vo));
    }

    @Transactional
    @Override
    public void saveOrdProd(Map<String, Object> paraMap) {
        String tag = "OrdService.saveOrdProd => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String, Object> prodMap = null;
        Map<String, Object> prMap = new HashMap<String, Object>();
        OrdProd vo = new OrdProd();
        log.info(tag + "1. 주문상품 update");
        vo.setOrdNo(Long.parseLong(paraMap.get("ordNo").toString()));
        vo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString()));
        try{
            vo.setOrdSz((String) paraMap.get("ordSz"));
        }catch (NullPointerException ne){
            vo.setOrdSz(null);
        }

        try {
            vo.setSaleUnit(Long.parseLong(paraMap.get("saleUnit").toString()));
        }
        catch (NullPointerException ne) {
            vo.setSaleUnit(Long.parseLong(env.getProperty("code.saleunit_default")));
        }
        try {
            vo.setQtyPerPkg(Integer.parseInt(paraMap.get("qtyPerPkg").toString()));
        }
        catch (NullPointerException ne) {
            vo.setQtyPerPkg(1);
        }

        vo.setUsedYn("Y");
        prMap.put("prodNo", vo.getProdNo());
        int qtyParPkg = 0;

        ProdInfo prodvo = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,vo.getProdNo(), "Y");
        if (prodvo != null) {
            qtyParPkg = prodvo.getQtyPerPkg();
        }else{
            log.error(tag + "상품정보 검색실패 !!!!");
            throw new RuntimeException();
        }
        prodMap = StringUtil.voToMap(prodvo);
        //prodMap = prodService.getProdInfo(prMap);
        if (prodMap != null) {
            vo.setOrdModlNm((String) prodMap.get("modlNm"));
        }

        try {
            Float ordQty =  Float.parseFloat(paraMap.get("ordQty").toString());
            vo.setOrdQty(ordQty * qtyParPkg);
        } catch (NullPointerException ne) {
            vo.setOrdQty(0F);
        }

        vo.setModDt(DateUtils.getCurrentDate());
        vo.setModIp(paraMap.get("ipaddr").toString());
        vo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        OrdProd chkvo = ordProdRepo.findByCustNoAndOrdNoAndProdNoAndUsedYn(custNo,vo.getOrdNo(), vo.getProdNo(), "Y");
        if (chkvo != null) {
            vo.setOrdProdNo(chkvo.getOrdProdNo());
            vo.setRegDt(chkvo.getRegDt());
            vo.setRegId(chkvo.getRegId());
            vo.setRegIp(chkvo.getRegIp());
        } else {
            vo.setOrdProdNo(0L);
            vo.setRegDt(DateUtils.getCurrentDate());
            vo.setRegIp(paraMap.get("ipaddr").toString());
            vo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        }
        log.info(tag + "2. 주문상품수량 update.ordProdNo = " + vo.getOrdProdNo());
        log.info(tag + "2. 주문상품수량 update.ordQty = " + vo.getOrdQty());
        vo.setCustNo(custNo);
        ordProdRepo.save(vo); //주문상품 저장

    }

    @Transactional
    @Override
    public void dropOrdProdCarts(Map<String, Object> paraMap) { //일괄선택삭제(사용중지)
        String tag = "OrdService.dropOrdProdCart => ";
        List<Map<String, Object>> prodList = (List<Map<String, Object>>) paraMap.get("prodList");
        Map<String, Object> prMap = new HashMap<String, Object>();

        for (Map<String, Object> el : prodList) {
            prMap.put("prodNo", el.get("prod_no"));
            prMap.put("userId", paraMap.get("userId"));
            mapper.dropOrdProdCart(prMap);
        }
    }

    @Override
    @Transactional
    public void dropOrdProds(Map<String, Object> paraMap) {
        String tag = "OrdService.dropOrdProds => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long ordProdNo = Long.parseLong(paraMap.get("ordProdNo").toString());
        OrdProd chkvo = opRepository.findByCustNoAndOrdProdNoAndUsedYn(custNo,ordProdNo,"Y");
        if(chkvo != null){
            chkvo.setUsedYn("N");
            opRepository.save(chkvo);
        }
    }

    @Override
    @Transactional
    public void resetOrdStatus(Map<String, Object> prodMap) {
        mapper.resetOrdStatus(prodMap);
    }

    @Transactional
    @Override
    public void dropOrdProdCart(Map<String, Object> paraMap) { //개별 삭제
        String tag = "OrdService.dropOrdProdCart => ";

        Map<String, Object> infoMap = null;
        try {
            infoMap = (Map<String, Object>) paraMap.get("prodInfo");
            infoMap.put("prodNo", infoMap.get("prod_no"));
        } catch (NullPointerException ne) {
            infoMap = new HashMap<String, Object>();
        }
        infoMap.put("userId", paraMap.get("userId"));

        mapper.dropOrdProdCart(infoMap);
    }

    @Transactional
    @Override
    public void saveOrd(Map<String, Object> paraMap) {
        String tag = "OrdService.saveOrd => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String, Object> mapInfo = (Map<String, Object>) paraMap.get("ordInfo");
        //List<Map<String,Object>> listProd  = (List<Map<String,Object>>) paraMap.get("prodList");
        long userId = (int) paraMap.get("userId");
        String ipaddr = (String) paraMap.get("ipaddr");
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

        String ordDlvReqDt = (String) mapInfo.get("dlvReqDt").toString().substring(0, 10);
        Long acptOrdStat = Long.parseLong(env.getProperty("ord_status_acpt")); //주문접수
        OrdInfo ordInfo = new OrdInfo();
        try {
            ordInfo.setOrdNo(Long.parseLong(mapInfo.get("ordNo").toString()));
        } catch (NullPointerException ne) { //신규주문처리
            ordInfo.setOrdNo(0L);
        }

        ordInfo.setCmpyNo(Long.parseLong(mapInfo.get("cmpyNo").toString()));
        ordInfo.setPlcNo(Long.parseLong(mapInfo.get("plcNo").toString()));

        ordInfo.setOrdNm(mapInfo.get("ordNm").toString());
        try {
            ordInfo.setOrdDt(transFormat.parse((String) mapInfo.get("ordDt").toString().substring(0, 10)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            ordInfo.setDlvReqDt(transFormat.parse(ordDlvReqDt)); //납품의뢰일자
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ordInfo.setOrdTp(Long.parseLong(mapInfo.get("ordTp").toString())); //주문유형(OEM,ODM)
        try {
            ordInfo.setOrdSts(Long.parseLong(mapInfo.get("ordSts").toString())); //주문상태
        } catch (NullPointerException ne) {
            ordInfo.setOrdSts(acptOrdStat);
        }
        try {
            ordInfo.setOrdPath(Long.parseLong(mapInfo.get("ordPath").toString())); //주문경로-수주인경우 설정됨.
        } catch (NullPointerException ne) {
            ordInfo.setOrdPath(0L);
        }
        try {
            ordInfo.setTakeNo((String) mapInfo.get("takeNo"));  //인수증번호.
        } catch (NullPointerException ne) {
        }
        try {
            ordInfo.setTrkCmpyNo(Long.parseLong(mapInfo.get("trkCmpyNo").toString()));
        }catch(NullPointerException ne){

        }

        ordInfo.setUsedYn("Y");
        ordInfo.setModId(userId);
        ordInfo.setModIp(ipaddr);
        ordInfo.setModDt(DateUtils.getCurrentBaseDateTime());
        OrdInfo chkInfo = ordRepo.findByCustNoAndOrdNoAndUsedYn(custNo,ordInfo.getOrdNo(), "Y");
        if (chkInfo != null) {
            ordInfo.setOrdNo(chkInfo.getOrdNo());
            ordInfo.setRegId(chkInfo.getRegId());
            ordInfo.setRegIp(chkInfo.getRegIp());
            ordInfo.setRegDt(chkInfo.getRegDt());
            ordInfo.setOrdSts(chkInfo.getOrdSts());
        } else {

            ordInfo.setRegId(userId);
            ordInfo.setRegIp(ipaddr);
            ordInfo.setRegDt(DateUtils.getCurrentBaseDateTime());
        }
        ordInfo.setCustNo(custNo);
        ordInfo = ordRepo.save(ordInfo);
        paraMap.put("ordNo", ordInfo.getOrdNo());

        List<Map<String, Object>> ds = this.getOrdProdList(paraMap);
        for (Map<String, Object> el : ds) {
            OrdProd opvo = new OrdProd();
            opvo.setOrdNo(ordInfo.getOrdNo());
            opvo.setProdNo(Long.parseLong(el.get("prodNo").toString()));
            try {
                opvo.setOrdQty(Float.parseFloat(el.get("ordQty").toString()));
            }
            catch (NumberFormatException ne) {
                opvo.setOrdQty(3F); //시연중 긴급처리 //kmj
            }

            try {
                opvo.setOrdSz(el.get("ordSz").toString());
            }
            catch (NullPointerException ne) {

            }

            opvo.setQtyPerPkg(Integer.parseInt(el.get("qtyPerPkg").toString()));
            opvo.setSaleUnit(Long.parseLong(el.get("saleUnit").toString()));
            opvo.setModDt(DateUtils.getCurrentDateTime());
            opvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            opvo.setModIp(paraMap.get("ipaddr").toString());
            opvo.setUsedYn("Y");
            OrdProd chkvo = opRepository.findByCustNoAndOrdNoAndProdNoAndUsedYn(custNo,opvo.getOrdNo(), opvo.getProdNo(), "Y");
            if (chkvo != null) {
                opvo.setOrdProdNo(chkvo.getOrdProdNo());
            } else {
                opvo.setRegDt(DateUtils.getCurrentDateTime());
                opvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                opvo.setRegIp(paraMap.get("ipaddr").toString());
            }
            opvo.setCustNo(custNo);
            opRepository.save(opvo);
        }
        /*
        if (acptOrdStat == ordInfo.getOrdSts() ) { //주문접수상태인 경우
           log.info(tag + "긴급구매의뢰 처리.......");

            int idx = -1;
            Map<String, Object> matrMap = null; //긴급구매의뢰 대상 원자재처리용
            Map<String,Object> pmap = new HashMap<String,Object>();
            Map<String,Object> drdmap = new HashMap<String,Object>();

            pmap.put("ordSts",ordInfo.getOrdSts());
            pmap.put("ordNo",ordInfo.getOrdNo());
            List<Map<String, Object>> matrds = mapper.getOwhWaitMatrList(pmap); //긴급구매자재 목록 추출 (안전재고를 감안한 자재 재고수량을 점검)
            if (matrds.size() > 0) {
                //긴급구매마스타 생성
                PursInfo pivo = new PursInfo();
                pivo.setPursNo(0L);
                pivo.setPursSts(Long.parseLong(env.getProperty("purs_sts_req")));
                pivo.setPursDt(pivo.getRegDt());
                pivo.setUsedYn("Y");

                //주문자료의 납품요청일을 기준하여 구매원부자재의 입고요청일을 설정 ;
                drdmap.put("ordNo",ordInfo.getOrdNo());

                Calendar cal = Calendar.getInstance();
                cal.setTime(ordInfo.getDlvReqDt());
                cal.add(Calendar.DATE, Integer.parseInt(env.getProperty("req_ipgo_dates")));
                pivo.setDlvReqDt(cal.getTime());

                //등록(수정)자 정보 설정
                pivo.setModId(ordInfo.getModId());
                pivo.setModIp(ordInfo.getModIp());
                pivo.setModDt(ordInfo.getModDt());
                pivo.setRegId(ordInfo.getRegId());
                pivo.setRegIp(ordInfo.getRegIp());
                pivo.setRegDt(ordInfo.getRegDt());

                pmap = pursService.savePursInfoByVo(pivo);
                pivo.setPursNo(Long.parseLong(pmap.get("pursNo").toString()));

                //주문기본정보에 상기프로세스에서 추출된 구매번호 설정
                pmap.put("ordNo",ordInfo.getOrdNo());
                pmap.put("modId",ordInfo.getModId());
                pmap.put("modIp",ordInfo.getModIp());
                ordService.resetPursNo(pmap);

                //긴급구매자재 생성
                idx = -1;
                while(++idx < matrds.size()) {
                    matrMap = matrds.get(idx);
                    PursMatr pmvo = new PursMatr();
                    pmvo.setPursNo(pivo.getPursNo());
                    pmvo.setMatrNo(Long.parseLong(matrMap.get("matr_no").toString()));
                    pmvo.setPursQty((Float) matrMap.get("owh_wait_qty"));
                    //구매금액 및 구매단위 설정
                    MatrInfo mivo = matrService.getMatrInfoByJPA(pmvo.getMatrNo());
                    pmvo.setPursAmt(mivo.getPursUnitPrc() == null ? 0L : (long) (mivo.getPursUnitPrc() * pmvo.getPursQty()));
                    pmvo.setPursUnit(mivo.getPursUnit() == null ? 0L : mivo.getPursUnit() );
                    pmvo.setUsedYn("Y");
                    pmvo.setModId(ordInfo.getModId());
                    pmvo.setModIp(ordInfo.getModIp());
                    pmvo.setModDt(ordInfo.getModDt());
                    pmvo.setRegId(ordInfo.getRegId());
                    pmvo.setRegIp(ordInfo.getRegIp());
                    pmvo.setRegDt(ordInfo.getRegDt());
                    pursService.savePursMatrByVo(pmvo);
                }
            }
        }
        else {
            log.info(tag + "출고대기 자재 설정 SKIP../주문정보 상태값 = "  + ordInfo.getOrdSts());
        }
        */
        /*바코드이미지생성 Remarked By KMJ At 21.10.20
        Map<String, Object> bmap = new HashMap<String, Object>();
        bmap.put("codeNo", ordInfo.getOrdNo());
        bmap.put("savePath", "ord/");
        stockService.makeBarCode(bmap);
        */
        //Map<String,Object> cleanMap = new HashMap<String,Object>();
        //cleanMap.put("userId",userId);
        //mapper.dropOrdProdCart(cleanMap);
    }

    @Transactional
    @Override
    public void updateOrd(Map<String, Object> paraMap) {
        String tag = "OrdService.updateOrd => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String, Object> mapInfo = (Map<String, Object>) paraMap.get("ordInfo");
        List<Map<String, Object>> listProd = (List<Map<String, Object>>) paraMap.get("prodList");
        long userId = (int) paraMap.get("userId");
        String ipaddr = (String) paraMap.get("ipaddr");
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        Properties properties = new Properties();
        Long acptOrdStat = 0L;

        acptOrdStat = Long.parseLong(properties.getProperty("ord_status_acpt")); //주문접수

        String ordDlvReqDt = (String) mapInfo.get("dlvReqDt").toString().substring(0, 10);
        OrdInfo ordInfo = new OrdInfo();
        ordInfo.setOrdNo(Long.parseLong(mapInfo.get("ordNo").toString()));
        ordInfo.setOrdNm(mapInfo.get("ordNm").toString());
        ordInfo.setCmpyNo(Long.parseLong(mapInfo.get("cmpyNo").toString())); //주문거래처
        //ordInfo.setPursNo(Long.parseLong(mapInfo.get("purs_no").toString())); Remarked By KMJ AT 21.09.04 12:17
        try {
            ordInfo.setOrdDt(transFormat.parse((String) mapInfo.get("ordDt").toString().substring(0, 10)));
            ordInfo.setDlvReqDt(transFormat.parse(ordDlvReqDt)); //납의뢰일자
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ordInfo.setOrdTp(Long.parseLong(mapInfo.get("ordTp").toString())); //주문유형(oem주문,자제영업주문)
        ordInfo.setOrdSts(Long.parseLong(mapInfo.get("ordSts").toString())); //주문상태
        try {
            ordInfo.setOrdPath(Long.parseLong(mapInfo.get("ordPath").toString())); //주문경로-수주인경우 설정됨.
        } catch (NullPointerException ne) {
            ordInfo.setOrdPath(0L);
        }
        try {
            ordInfo.setTakeNo((String) mapInfo.get("takeNo"));  //인수증번호.
        } catch (NullPointerException ne) {
        }
        ordInfo.setUsedYn("Y");
        ordInfo.setModId(userId);
        ordInfo.setModIp(ipaddr);
        ordInfo.setModDt(ordInfo.getModDt());
        ordInfo.setCustNo(custNo);
        ordInfo = ordRepo.save(ordInfo);

        //주문상품저장
        Long prodNo = 0L;
        Long pursNo = Long.parseLong(mapInfo.get("pursNo").toString());
        List<Long> prodNos = new ArrayList<Long>();
        for (Map<String, Object> el : listProd) {
            prodNo = Long.parseLong(el.get("prodNo").toString());
            el.put("userId", userId);
            el.put("ipaddr", ipaddr);
            prodNos.add(prodNo);

            OrdProd ordProd = new OrdProd();
            ordProd.setOrdNo(ordInfo.getOrdNo());
            ordProd.setProdNo(prodNo);
            ordProd.setOrdQty(Float.parseFloat(el.get("ordQty").toString()));
            ordProd.setSaleUnit(Long.parseLong(el.get("saleUnit").toString())); //판매단위
            ordProd.setQtyPerPkg(Integer.parseInt(el.get("qtyPerPkg").toString())); //포장당 인입수량
            ordProd.setOrdSz((String) el.get("ordSz")); //주문규격
            ordProd.setOrdModlNm((String) el.get("ordModlNm")); //주문모델명
            ordProd.setUsedYn("Y");

            ordProd.setRegId(userId);
            ordProd.setRegIp(ipaddr);
            ordProd.setRegDt(ordProd.getRegDt());
            ordProd.setModId(userId);
            ordProd.setModIp(ipaddr);
            ordProd.setModDt(ordProd.getModDt());
            /*SOL AddOn By KMJ AT 21.09.04 12:17*/
            try {
                ordProd.setPursNo(Long.parseLong(mapInfo.get("pursNo").toString()));
            }
            catch (NullPointerException ne) {
                ordProd.setPursNo(0L);
            }
            /*EOL AddOn By KMJ AT 21.09.04 12:17*/
            OrdProd chkProd = opRepository.findByCustNoAndOrdNoAndProdNoAndUsedYn(custNo,ordProd.getOrdNo(), ordProd.getProdNo(), "Y");
            ordProd.setOrdProdNo((chkProd == null) ? 0L : chkProd.getOrdProdNo());
            ordProd.setCustNo(custNo);
            opRepository.save(ordProd);
        }
        //ArrayList에 존재하지 않은 상품 삭제(초기화)
        Map<String, Object> dropmap = new HashMap<String, Object>();
        dropmap.put("ordNo", ordInfo.getOrdNo());
        //dropmap.put("prodList",prodNos);
        //mapper.renewalOrdProd(dropmap);

        if (Long.toString(acptOrdStat).equals(Long.toString(ordInfo.getOrdSts()))) { //주문접수상태인 경우
            mapper.initPursProd(dropmap);

            int idx = -1;
            Map<String, Object> matrMap = null; //긴급구매의뢰 대상 원자재처리용
            Map<String, Object> pmap = new HashMap<String, Object>();
            Map<String, Object> drdmap = new HashMap<String, Object>();

            pmap.put("ordSts", ordInfo.getOrdSts());
            pmap.put("ordNo", ordInfo.getOrdNo());
            //pmap.put("pursNo", ordInfo.getPursNo()); //Remarked By KMJ At 21.09.04 12:20
            List<Map<String, Object>> matrds = mapper.getOwhWaitMatrList(pmap); //긴급구매자재 목록 추출 (안전재고를 감안한 자재 재고수량을 점검)
            if (matrds.size() > 0) {
                idx = -1;
                while (++idx < matrds.size()) {
                    matrMap = matrds.get(idx);
                    PursMatr pmvo = new PursMatr();
                    //pmvo.setPursNo(ordProd.getPursNo()); Remarked By KMJ AT 21.09.04 12:20
                    pmvo.setPursNo(pursNo); //AddOn By KMJ AT 21.09.04 12:20
                    pmvo.setMatrNo(Long.parseLong(matrMap.get("matrNo").toString()));
                    pmvo.setPursQty((Float) matrMap.get("owhWaitQty"));
                    //구매금액 및 구매단위 설정
                    MatrInfo mivo = matrService.getMatrInfoByJPA(custNo,pmvo.getMatrNo());
                    pmvo.setPursAmt(mivo.getPursUnitPrc() == null ? 0L : (long) (mivo.getPursUnitPrc() * pmvo.getPursQty()));
                    pmvo.setPursUnit(mivo.getPursUnit() == null ? 0L : mivo.getPursUnit());
                    pmvo.setUsedYn("Y");
                    pmvo.setModId(ordInfo.getModId());
                    pmvo.setModIp(ordInfo.getModIp());
                    pmvo.setModDt(ordInfo.getModDt());
                    pmvo.setRegId(ordInfo.getRegId());
                    pmvo.setRegIp(ordInfo.getRegIp());
                    pmvo.setRegDt(ordInfo.getRegDt());
                    pmvo.setCustNo(custNo);
                    pursService.savePursMatrByVo(pmvo);
                }
            }
        } else {
            log.info(tag + "출고대기 자재 설정 SKIP../주문정보 상태값 = " + ordInfo.getOrdSts());
        }
        /* Remarked By KMJ At 21.10.10 --cart사용하지 안음.
        Map<String, Object> cleanMap = new HashMap<String, Object>();
        cleanMap.put("userId", userId);
        mapper.dropOrdProdCart(cleanMap);
         */
    }

    @Transactional
    @Override
    public void dropOrdInfo(Map<String, Object> paraMap) {
        String tag = "vsvc.OrdService.dropOrdInfo => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        OrdInfo chkvo = ordRepo.findByCustNoAndOrdNoAndUsedYn(custNo,Long.parseLong(paraMap.get("ordNo").toString()), "Y");
        if (chkvo != null) {
            chkvo.setUsedYn("N");
            chkvo.setModDt(DateUtils.getCurrentBaseDateTime());
            chkvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            chkvo.setModIp(paraMap.get("ipaddr").toString());
            log.info(tag + "1. 주문정보 사용중지처리");
            ordRepo.save(chkvo);
            /*SOL AddOn By KMJ At 21.04.04 12:20
                  생산계획에서 구매필요여부 검출Query에서 발견된 논리적 오류를 제거하기 위해 OrdProd에 PursNo가 생성됨으로서
                  연관된 로직이 변경됨
            */
            Map<String,Object> pursMap = new HashMap<String,Object>();
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Map<String,Object> oParam = new HashMap<String,Object>();
            oParam.put("ordDt",sdf.format(chkvo.getOrdDt()));
            oParam.put("pageNo",0);
            oParam.put("pageSz",1000);
            List<Map<String,Object>>  dsOrd = mapper.getOrdList(oParam);
            int idx = -1;
            Long svPursNo = 0L;
            while(++idx < dsOrd.size()) {
                Long pursNo = Long.parseLong(dsOrd.get(0).get("pursNo").toString());
                if (pursNo == svPursNo) continue;
                log.info(tag + "2.구매정보삭제용 구매번호(pursNo) = " + pursNo);
                pursMap.put("pursNo", pursNo);
                pursMap.put("ipaddr",chkvo.getModIp());
                pursMap.put("userId",chkvo.getModId());
                log.info(tag + "2. 주문정보 연관 구매정보 사용중지처리");
                pursService.dropPursInfo(pursMap);
                svPursNo = pursNo;
            }
            /*EOL AddOn By KMJ At 21.04.04 12:20*/
        }
        //mapper.dropOrdInfo(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboDlvPlc(Map<String, Object> paraMap) {
        String tag = "vsvc.OrdService.getComboDlvPlc => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getComboDlvPlc(paraMap);
    }

    @Override
    public void resetPursNo(Map<String, Object> paraMap) {
        mapper.resetPursNo(paraMap);
    }

    @Override
    public void initOrdProdCart(Map<String, Object> paraMap) {
        mapper.initOrdProdCart(paraMap);
        mapper.createOrdrProdCart(paraMap);
    }

    @Override
    public List<Map<String, Object>> getOrdProdBomList(Map<String, Object> paraMap) {
        return mapper.getOrdProdBomList(paraMap);
    }

    @Override
    public int getOrdProdBomListCount(Map<String, Object> paraMap) {
        return mapper.getOrdProdBomListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getOrdProdCartBomList(Map<String, Object> paraMap) {
        return mapper.getOrdProdCartBomList(paraMap);
    }

    @Override
    public int getOrdProdCartBomListCount(Map<String, Object> paraMap) {
        return mapper.getOrdProdCartBomListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getExOrdProdList(Map<String, Object> paraMap) {
        String tag = "OrdServic.getExOrdProdList =>";
        log.info(tag + "paraMap = " + paraMap);
        return mapper.getExOrdProdList(paraMap);
    }

    @Override
    public Map<String, Object> getOrdProdInfo(Map<String, Object> paraMap) {
        return mapper.getOrdProdInfo(paraMap);
    }

    @Override
    public int getExOrdProdListCount(Map<String, Object> paraMap) {
        return mapper.getExOrdProdListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboDlvPlcList(Map<String, Object> paraMap) {
        return mapper.getComboDlvPlcList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getFirstHalfPerformList(Map<String, Object> paraMap) {
        String tag = "OrdService.getFirstHalfPerformList => ";
        log.info(tag + "pataMap = " + paraMap.toString());
        return mapper.getFirstHalfPerformList(paraMap);
    }

    @Override
    public int getFirstHalfPerformListCount(Map<String, Object> paraMap) {
        return mapper.getFirstHalfPerformListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getSecondHalfPerformList(Map<String, Object> paraMap) {
        String tag = "OrdService.getSecondHalfPerformList => ";
        log.info(tag + "pataMap = " + paraMap.toString());
        return mapper.getSecondHalfPerformList(paraMap);
    }

    @Override
    public int getSecondHalfPerformListCount(Map<String, Object> paraMap) {
        return mapper.getSecondHalfPerformListCount(paraMap);
    }

    @Override
    @Transactional
    public void ordIndfoByExcel(Map<String, Object> paraMap) throws Exception {
        String tag = "OrdService.ordIndfoByExcel => ";
        StringBuffer buf = new StringBuffer();
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int rowindex = 0;


        String cmpyNm = "";
        Date dlRregdt = new Date();
        Date ordDt = new Date();
        String ordNm = "";


        XSSFSheet sheet = workbook.getSheetAt(0); //첫번째쉬트
        int rows = sheet.getPhysicalNumberOfRows();


        for (rowindex = 0; rowindex < 249; rowindex++) {
            XSSFRow row = sheet.getRow(rowindex);
            if (row == null) continue;
            OrdInfo oivo = new OrdInfo();
            cmpyNm = row.getCell(1).getStringCellValue(); //거래처 명
            cmpyNm = cmpyNm.replaceAll("\\p{Z}", "");

            dlRregdt = row.getCell(4).getDateCellValue(); // 납기 일자
            ordDt = row.getCell(0).getDateCellValue(); //지시일자
            ordNm = row.getCell(2).getStringCellValue(); //제품명
            ordNm = ordNm.replaceAll("\\p{Z}", "");// 제품명 띄어쓰기 제거

            oivo.setOrdSts(Long.parseLong(env.getProperty("ord_status_acpt"))); // 주문대기 입력
            oivo.setOrdTp(Long.parseLong(env.getProperty("code.ordtype.project"))); // OEM 등록
            oivo.setDlvReqDt(dlRregdt);
            oivo.setOrdDt(ordDt);
            oivo.setOrdNm(ordNm);
            oivo.setPlcNo(6466L); //대책없는코드임
            oivo.setUsedYn("Y");

            Long mngrgbnSale = Long.parseLong(env.getProperty("code.mngrgbn.sale"));
            CmpyInfo cmvo = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNmAndUsedYn(custNo,mngrgbnSale,cmpyNm, "Y");
            if (cmvo != null) {
                oivo.setCmpyNo(cmvo.getCmpyNo());
            }
            oivo.setCustNo(custNo);
            ordRepo.save(oivo);
            paraMap.put("ordNo", oivo.getOrdNo());

            OrdProd orvo = new OrdProd();
            ProdInfo prvo = prodRepo.findByCustNoAndProdNmAndUsedYn(custNo,ordNm, "Y");
            if (prvo != null) {
                orvo.setProdNo(prvo.getProdNo());
            }
            orvo.setOrdNo(Long.parseLong(paraMap.get("ordNo").toString()));
            orvo.setQtyPerPkg(0);
            orvo.setOrdQty(0F);
            orvo.setModId(0L);
            orvo.setModIp("127.0.0.1");
            orvo.setModDt(DateUtils.getCurrentDateTime());
            orvo.setRegDt(DateUtils.getCurrentDateTime());
            orvo.setRegId(0L);
            orvo.setRegIp("127.0.0.1");
            orvo.setSaleUnit(Long.parseLong(env.getProperty("code.base.sale_unit_g")));
            orvo.setUsedYn("Y");
            orvo.setCustNo(custNo);
            ordprod.save(orvo);

        }
    }

    @Override
    public List<Map<String, Object>> getProcStockList(Map<String, Object> paraMap) {
        return mapper.getProcStockList(paraMap);
    }

    @Override
    public int getProcStockListCount(Map<String, Object> paraMap) {
        return mapper.getProcStockListCount(paraMap);
    }

    @Override
    public void chkSubMatr(Map<String,Object> paraMap){
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        OrdInfo chkvo = ordRepo.findByCustNoAndOrdNoAndUsedYn(custNo,Long.parseLong(paraMap.get("ordNo").toString()), "Y");
        if(chkvo != null){
            chkvo.setSubmatrExistYn("Y");
            ordRepo.save(chkvo);
        }
    }

    @Override
    public void saveMakeAbleYn(Map<String, Object> paraMap){
        Long ordProdNo = Long.parseLong(paraMap.get("ordProdNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        OrdProd chkvo = ordProdRepo.findByCustNoAndOrdProdNoAndUsedYn(custNo,ordProdNo, "Y");
        if(chkvo != null){
            chkvo.setMakeAbleYn("Y");
            ordProdRepo.save(chkvo);
        }
    }

    @Override
    public void updateDlvReqDt(Map<String, Object> paraMap){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        OrdInfo chkvo = ordRepo.findByCustNoAndOrdNoAndUsedYn(custNo,Long.parseLong(paraMap.get("ord_no").toString()), "Y");
        if(chkvo != null){
            try{
                chkvo.setDlvReqDt(sdf.parse(paraMap.get("dlvReqDt").toString()));
            }catch(ParseException e){

            }
            ordRepo.save(chkvo);
        }
    }

    @Override
    public List<Map<String, Object>> geOwhDateList(Map<String, Object> paraMap) {
        return mapper.geOwhDateList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getOrderBookList(Map<String ,Object> paraMap){
        return mapper.getOrderBookList(paraMap);
    }
}