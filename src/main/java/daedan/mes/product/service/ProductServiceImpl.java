package daedan.mes.product.service;

import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.product.domain.ProductIndc;
import daedan.mes.product.domain.ProductPlan;
import daedan.mes.product.mapper.ProductMapper;
import daedan.mes.product.repository.ProductIndcRepository;
import daedan.mes.product.repository.ProductPlanRepositry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("productService")
public class ProductServiceImpl implements ProductService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private ProductPlanRepositry productPlanRepo;

    @Autowired
    private ProductIndcRepository productIndcRepo;

    @Override
    @Transactional
    public void saveProductPlan(Map<String, Object> paraMap) {
        String tag = "ProductService.saveProductPlan => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        /*
          { cmpyNo=495424
           , prodNo=495428
           , userId=14
           , custNo=14
           , ipaddr=127.0.0.1
           , DateList=[2021-12-12, 2021-12-13, 2021-12-14, 2021-12-15, 2021-12-16, 2021-12-17, 2021-12-18, 2021-12-19, 2021-12-20, 2021-12-21, 2021-12-22]
           , ordRecList=[499498, 499499, 0, 0, 0, 0, 0, 0, 0, 0, 0],
           , planQtyList=[300, 200, 0, 0, 0, 0, 0, 0, 0, 0, 0]
         }
         */
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        Long cmpyNo = Long.parseLong(paraMap.get("cmpyNo").toString());
        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        String ipaddr = paraMap.get("ipaddr").toString();
        List<Long> dsOrdRecNo = (List<Long>) paraMap.get("ordRecList");
        List<String> dsDateList = (List<String>) paraMap.get("dateList");
        List<Integer> dsQtyList = (List<Integer>) paraMap.get("planQtyList");

        for (int ix = 0; ix < dsDateList.size(); ix++) {
            int planQty = Integer.parseInt(String.valueOf(dsQtyList.get(ix)));
            if (planQty == 0) continue;
            ProductPlan mpvo = new ProductPlan();

            mpvo.setCustNo(custNo);
            mpvo.setCmpyNo(cmpyNo);
            mpvo.setProdNo(prodNo);
            mpvo.setOrdRecvNo(Long.parseLong(String.valueOf(dsOrdRecNo.get(ix))));
            mpvo.setPlanQty(planQty);
            mpvo.setUsedYn("Y");
            try {
                Date shd = sdf.parse(dsDateList.get(ix));
                mpvo.setPlanUt(shd.getTime() / 1000);
            } catch (ParseException e) {
                e.printStackTrace();
                break;
            }
            mpvo.setStatCd(Long.parseLong(env.getProperty("code.make_plan_plan")));
            mpvo.setModDt(DateUtils.getCurrentBaseDateTime());
            mpvo.setModId(userId);
            mpvo.setModIp(ipaddr);
            ProductPlan chkvo = productPlanRepo.findByCustNoAndPlanUtAndProdNoAndUsedYn(custNo, mpvo.getPlanUt(), prodNo, "Y");
            if (chkvo != null) {
                mpvo.setProductPlanNo(chkvo.getProductPlanNo());
                mpvo.setOrdRecvNo(chkvo.getOrdRecvNo());
                mpvo.setRegDt(chkvo.getRegDt());
                mpvo.setRegId(chkvo.getRegId());
                mpvo.setRegIp(chkvo.getRegIp());
            } else {
                mpvo.setProductPlanNo(0L);
                mpvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                mpvo.setRegId(userId);
                mpvo.setRegIp(ipaddr);
            }
            productPlanRepo.save(mpvo);
        }
    }


    /*수신받은 주문정보로 간략 생산지시 정보 생성*/
    @Override
    public void makeProductPlan(Map<String, Object> paraMap) {
        String tag = "ProductService.makeProductPlan=> ";
        log.info(tag + "paraMap =  " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String ipaddr = paraMap.get("ipaddr").toString();

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        /*
          { cmpyNo=495424
           , prodNo=495428
           , userId=14
           , custNo=14
           , ipaddr=127.0.0.1
           , DateList=[2021-12-12, 2021-12-13, 2021-12-14, 2021-12-15, 2021-12-16, 2021-12-17, 2021-12-18, 2021-12-19, 2021-12-20, 2021-12-21, 2021-12-22]
           , ordRecList=[499498, 499499, 0, 0, 0, 0, 0, 0, 0, 0, 0],
           , planQtyList=[300, 200, 0, 0, 0, 0, 0, 0, 0, 0, 0]
         }
         */
        Long cmpyNo = Long.parseLong(paraMap.get("cmpyNo").toString());
        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        List<Long> dsOrdRecNo = (List<Long>) paraMap.get("ordRecList");
        List<String> dsDateList = (List<String>) paraMap.get("dateList");
        List<Integer> dsQtyList = (List<Integer>) paraMap.get("planQtyList");

        for (int ix = 0; ix < dsDateList.size(); ix++) {
            int planQty = Integer.parseInt(String.valueOf(dsQtyList.get(ix)));
            if (planQty == 0) continue;
            ProductPlan mpvo = new ProductPlan();

            mpvo.setCustNo(custNo);
            mpvo.setCmpyNo(cmpyNo);
            mpvo.setProdNo(prodNo);
            mpvo.setOrdRecvNo(Long.parseLong(String.valueOf(dsOrdRecNo.get(ix))));
            mpvo.setPlanQty(planQty);
            mpvo.setUsedYn("Y");
            try {
                Date shd = sdf.parse(dsDateList.get(ix));
                mpvo.setPlanUt(shd.getTime() / 1000);
            } catch (ParseException e) {
                e.printStackTrace();
                break;
            }
            mpvo.setStatCd(Long.parseLong(env.getProperty("code.make_plan_plan")));
            mpvo.setModDt(DateUtils.getCurrentBaseDateTime());
            mpvo.setModId(userId);
            mpvo.setModIp(ipaddr);
            ProductPlan chkvo = productPlanRepo.findByCustNoAndPlanUtAndProdNoAndUsedYn(custNo, mpvo.getPlanUt(), prodNo, "Y");
            if (chkvo != null) {
                mpvo.setProductPlanNo(chkvo.getProductPlanNo());
                mpvo.setOrdRecvNo(chkvo.getOrdRecvNo());
                mpvo.setRegDt(chkvo.getRegDt());
                mpvo.setRegId(chkvo.getRegId());
                mpvo.setRegIp(chkvo.getRegIp());
            } else {
                mpvo.setProductPlanNo(0L);
                mpvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                mpvo.setRegId(userId);
                mpvo.setRegIp(ipaddr);
            }
            productPlanRepo.save(mpvo);
        }
    }

    @Override
    public List<Map<String, Object>> getProductPlanList(Map<String, Object> paraMap) {
        String tag = " ProductService.getProductPlanList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProductPlanList(paraMap);
    }

    @Override
    public Map<String, Object> getProductPlanInfo(Map<String, Object> paraMap) {
        String tag = " ProductService.getProductPlanInfo => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long prodcustPlanNo = Long.parseLong(paraMap.get("prodcutPlanN").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        ProductIndc vo = productPlanRepo.findByCustNoAndProductPlanNoAndUsedYn(custNo,prodcustPlanNo,"Y");
        return StringUtil.voToMap(vo);
    }

    @Override
    public List<Map<String, Object>> getProductIndcList(Map<String, Object> paraMap) {
        String tag = " ProductService.getProductIndcList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProductIndcList(paraMap);
    }

    @Override
    public void saveProductIndc(Map<String, Object> paraMap) {
        String tag = "ProductService.saveProductIndc => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        /*
        paraMap = {  cmpyNo=495424
                   , prodNo=495428
                   , indcQty=200
                   , productPlanNo=525107
                   , workFrDt=2021-12-01T15:00:00.000Z
                   , workToDt=2021-12-02T15:00:00.000Z
                   , indcCont=<p>테스트 중 입니다.</p>
                   , userId=14
                   , custNo=14
                   , ipaddr=127.0.0.1
                   }
         */
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        Long cmpyNo = Long.parseLong(paraMap.get("cmpyNo").toString());
        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        String ipaddr = paraMap.get("ipaddr").toString();

        ProductIndc pivo = new ProductIndc();

        pivo.setCustNo(custNo);
        pivo.setProductPlanNo(Long.parseLong(paraMap.get("productPlanNo").toString()));
        pivo.setCmpyNo(cmpyNo);
        pivo.setProdNo(prodNo);
        pivo.setMakeFrUt(DateUtils.getDateToTimeStamp(paraMap.get("workFrDt").toString().substring(0,10)));
        pivo.setMakeToUt(DateUtils.getDateToTimeStamp(paraMap.get("workToDt").toString().substring(0,10)));
        pivo.setUsedYn("Y");
        pivo.setCustNo(custNo);
        pivo.setModDt(DateUtils.getCurrentBaseDateTime());
        pivo.setModIp(ipaddr);
        pivo.setModId(userId);
        pivo.setIndcQty(Float.valueOf(paraMap.get("indcQty").toString()));
        try {
            pivo.setProductPlanNo(Long.parseLong(paraMap.get("productPlanNo").toString()));
        }
        catch(NullPointerException ne) {
            pivo.setProductIndcNo(0L);
        }
        pivo.setStatCd(Long.parseLong(env.getProperty("code.workst.proc")));
        ProductIndc chkvo = productIndcRepo.findByCustNoAndMakeFrUtAndProdNoAndUsedYn(custNo, pivo.getMakeFrUt(), prodNo, "Y");
        if (chkvo != null) {
            pivo.setProductIndcNo(chkvo.getProductIndcNo());
            pivo.setRegDt(chkvo.getRegDt());
            pivo.setRegId(chkvo.getRegId());
            pivo.setRegIp(chkvo.getRegIp());
        } else {
            pivo.setProductIndcNo(0L);
            pivo.setRegDt(DateUtils.getCurrentBaseDateTime());
            pivo.setRegId(userId);
            pivo.setRegIp(ipaddr);
        }
        productIndcRepo.save(pivo);
    }

}
