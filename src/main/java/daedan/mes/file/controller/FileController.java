package daedan.mes.file.controller;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.file.domain.FileInfo;
import daedan.mes.file.repository.FileRepository;
import daedan.mes.file.service.FileService;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;

@RestController
@RequestMapping("/api/daedan/mes/file")
public class FileController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fileRepo;

    @Autowired
    private CmmnService cmmnService;


    @PostMapping(value="/upload")
    public Result file(@RequestPart("file") MultipartFile multipartFile , HttpServletRequest request , HttpSession session) throws IOException {

        String tag = "FileController/upload => ";
        log.info(tag);

        Map<String,Object> rmap = new HashMap<String,Object>();
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        Long fileNo = 0L;
        try {
            fileNo = Long.parseLong(request.getParameter("fileNo").toString());
        }
        catch (NullPointerException ne) {
            fileNo = 0L;
        }
        Long userId = Long.parseLong(request.getParameter("userId").toString());
        FileInfo fileEntity = new FileInfo();
        /*
        Long fileNo
        Long saveSeq
        usedYn
        tmpYn
        MultipartFile multipartFile
        Long userId
        String ipaddr
        */
        fileEntity = new FileInfo (
                  fileNo
                , 1L
                ,  uvo.getCustInfo().getCustNo()
                ,"Y"
                ,"N"
                , multipartFile
                , userId
                , NetworkUtil.getClientIp(request)
        );
        rmap.put("fileNo",fileService.saveFile(fileEntity));
        log.info(tag + "receive rmap = " + rmap.toString());
        result.setData(rmap);

        fileEntity = fileRepo.findByCustNoAndFileNoAndUsedYn(custNo,Long.parseLong(rmap.get("fileNo").toString()),"Y");
        StringBuffer buf = new StringBuffer();
        String fileRoot = env.getProperty("file.root.path");
        buf.setLength(0);
        buf.append(fileRoot)
           .append(userId).append(File.separator)
           .append(fileEntity.getFileNo()).append(File.separator)
           .append(fileEntity.getSaveFileNm());
        File targetFile = null;
        try {
            InputStream fileStream = multipartFile.getInputStream();
            targetFile = new File(buf.toString());
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);
            e.printStackTrace();
        }
        return result;

    }
    @PostMapping(value="/getImage")
    public Result getImage(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "FileController.getImage=>";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        String imageUrl = fileService.getApndFileUrl(paraMap);
        log.info(tag + "imageUrl = " + imageUrl);
        result.setData(imageUrl);
        return result;
    }

    @PostMapping("/downloadFile")
    public void downloadFile(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        StringBuffer buf = new StringBuffer();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        FileInfo filevo = fileService.getFileVo(custNo,fileNo);
        buf.append(env.getProperty("file.root.path"))
                .append(File.separator)
                .append(filevo.getAccUrl())
                .append(filevo.getSaveFileNm());

        File file = new File(buf.toString());
        byte[] buffer = new byte[(int) file.length()];
        BufferedInputStream bis = null;
        OutputStream os = null;

        FileInputStream in = null;


        log.info(file.exists());
        try {
            if (file.exists()) {
                String browser = cmmnService.getBrowser(request);
                response.setHeader("Content-Disposition", cmmnService.getDisposition(filevo.getOrgFileNm(), browser));
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setHeader("Content-Type", "application/octet-stream;charset=UTF-8");
                response.setHeader("Content-Length", String.valueOf(file.length()));
                response.setHeader("Pragma", "no-cache;");
                response.setHeader("Expires", "-1;");
                os = response.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file));
                while(bis.read(buffer) != -1){
                    os.write(buffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/downloadImage")
    public Result downloadImage(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "fildController.downloadImage => ";
        StringBuffer buf = new StringBuffer();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        FileInfo filevo = fileService.getFileVo(custNo,fileNo);
        buf.append(env.getProperty("file.root.path"))
                      .append(filevo.getAccUrl())
                      .append(filevo.getSaveFileNm());
        log.info(tag + "downLoadPath = " + buf.toString());
        Result result = Result.successInstance();
        byte[] readBytes = cmmnService.getFileToBin(buf.toString());
        result.setData(readBytes);
        return result;
    }

}
