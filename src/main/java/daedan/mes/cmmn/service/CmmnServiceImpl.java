package daedan.mes.cmmn.service;

import daedan.mes.cmmn.mapper.CmmnMapper;
import daedan.mes.common.service.util.KISA_SEED_CBC;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.user.domain.CustInfo;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.user.repository.CustInfoRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service("CmmnService")
public class CmmnServiceImpl implements CmmnService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private CmmnMapper mapper;

    @Autowired
    private CustInfoRepository custInfoRepo;

    @Override
    public List<Map<String, Object>> getCalDateList(Map<String, Object> paraMap) {
        return mapper.getCalDateList(paraMap);
    }


    @Override
    public Date getIntervalDate(Map<String, Object> paraMap) {
        return mapper.getIntervalDate(paraMap);
    }

    @Override
    public byte[] encryptStr(Long custNo,String plainText) {
        String tag = "CmmnService.encryptStr => ";
        byte[] encryptedMessage = null;
        byte[] encArray = null;
        CustInfo custvo = custInfoRepo.findByCustNo(custNo);
        if (custvo != null) {
            byte[] pbszUserKey = custvo.getPbszUserKey().getBytes(StandardCharsets.UTF_8);
            byte[] pbszIv = custvo.getPbszIv().getBytes(StandardCharsets.UTF_8);
            encryptedMessage = KISA_SEED_CBC.SEED_CBC_Encrypt(pbszUserKey, pbszIv, plainText.getBytes(), 0, plainText.getBytes().length);
           // log.info("암호화된 데이터1 => " + new String(encryptedMessage));
            //log.info("암호화된 데이터2 => " + encryptedMessage.toString());

            Base64.Encoder encoder = Base64.getEncoder();
            encArray = encoder.encode(encryptedMessage);
            try {
                System.out.println(new String(encArray, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return encArray;
    }

    @Override
    public String decryptStr(Long custNo, byte[] encStr) throws UnsupportedEncodingException {
        String result = "";
        CustInfo custvo = custInfoRepo.findByCustNo(custNo);

        if (encStr != null){
            String tag = "CmmnService.decryptStr => ";
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] enc = decoder.decode(encStr);

            if (custvo != null) {
                byte[] pbszUserKey = custvo.getPbszUserKey().getBytes(StandardCharsets.UTF_8);
                byte[] pbszIv = custvo.getPbszIv().getBytes(StandardCharsets.UTF_8);
                byte[] decBytes = KISA_SEED_CBC.SEED_CBC_Decrypt(pbszUserKey, pbszIv, enc, 0, enc.length);
                result = new String(decBytes);
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> getAuthUserMenuInfo(Map<String, Object> paraMap) {
        String tag = "cmmnService.getAuthUserMenuInfo => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getAuthUserMenuInfo(paraMap);
    }

    @Override
    public String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1)
            return "MSIE";
        else if (header.indexOf("Chrome") > -1)
            return "Chrome";
        else if (header.indexOf("Opera") > -1)
            return "Opera";
        return "Firefox";
    }

    @Override
    public String getDisposition(String filename, String browser)
            throws UnsupportedEncodingException {
        String dispositionPrefix = "attachment;filename=";
        String encodedFilename = null;
        if (browser.equals("MSIE")) {
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Firefox")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Opera")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < filename.length(); i++) {
                char c = filename.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        }
        return dispositionPrefix + encodedFilename;
    }

//    @Override
//    public String getBlobToBin(String filename, String browser){
//        Blob blob = rs.getBlob("SomeDatabaseField");
//
//        int blobLength = (int) blob.length();
//        byte[] blobAsBytes = blob.getBytes(1, blobLength);
//
////release the blob and free up memory. (since JDBC 4.0)
//        blob.free();
//    }

    @Override
    public byte[] getFileToBin(String filePath){
        File file = new File(filePath);
        byte[] fileData = new byte[(int) file.length()];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            in.read(fileData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileData;

    }


    @Override
    public Map<String, Object> getMakeHstrPreVal(Map<String, Object> paraMap) {
        String tag = "cmmnService.getMakeHstrPreVal => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getMakeHstrPreVal(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboWh(Map<String, Object> paraMap) {
        String tag = "cmmnService.getComboWh => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getComboWh(paraMap);
    }

    @Override
    /*
        주의 : 월요일 = 1, 일요일 = 7로 나옴.
     */
    public int getWeekIdx(Map<String, Object> paraMap) {
        String tag = "cmmnService.getWeekIdx => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getWeekIdx(paraMap);
    }

    @Override
    public int getWeekIdxByUnixTime(long iValue) {
        return mapper.getWeekIdxByUnixTime(iValue);
    }

}
