package daedan.mes.file.service;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.code.domain.CodeInfo;
import daedan.mes.code.repository.CodeRepository;
import daedan.mes.code.service.CodeService;
import daedan.mes.file.domain.FileInfo;
import daedan.mes.file.mapper.FileMapper;
import daedan.mes.file.repository.FileRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.hibernate.procedure.internal.Util.copy;

@Service("fileService")
public class FileServiceImpl implements FileService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    FileRepository fileRepository;


    @Autowired
    private CodeRepository codeRepo;

    @Override
    public Long saveFile(FileInfo fileEntity) {
        String tag = "FileService.saveFile.";
        Long custNo = fileEntity.getCustNo();
        MultipartFile multipartFile = fileEntity.getMultipartFile();
        String originalfileName = multipartFile.getOriginalFilename();
        int extpos = originalfileName.lastIndexOf(".");
        StringBuffer buf = new StringBuffer();
        buf.append(UUID.randomUUID());
        String fileExtNm = originalfileName.substring(extpos + 1).toUpperCase();
        //log.info(tag + "orgFileNm name = " + originalfileName);
        if (extpos > 0) {
            buf.append(".").append(fileExtNm);
        }
        fileEntity.setOrgFileNm(originalfileName);
        fileEntity.setSaveFileNm(buf.toString());

        fileEntity.setAccUrl(buf.toString());
        log.info(tag + "orgFileNm  = " + fileEntity.getOrgFileNm());
        log.info(tag + "saveFileNm  = " + fileEntity.getSaveFileNm());
        FileInfo afterFileVo = null;
        try {
            log.info("base_file_ext_cd = "+env.getProperty("base_file_ext_cd"));
            Long fileExt= Long.valueOf(env.getProperty("base_file_ext_cd"));
            fileEntity.setFileLen(multipartFile.getSize());

            CodeInfo codeEntity = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(fileExt,fileExtNm.toUpperCase(Locale.ROOT),"Y");
            fileEntity.setFileTp(codeEntity.getCodeNo());
            log.info(tag + " fileEntity = " + fileEntity);

            FileInfo chkvo = fileRepository.findByCustNoAndSaveFileNmAndUsedYn(custNo,fileEntity.getSaveFileNm(),"Y"); //????????????????????? ????????? ??????????????? ???????????? ????????? ?????????.
            if (chkvo != null) {
                fileEntity.setFileNo(chkvo.getFileNo());
            }
            afterFileVo = fileRepository.save(fileEntity); //uploaded??? ?????? ?????? ??????

            buf.setLength(0);
            buf.append(afterFileVo.getRegId())
                    .append(File.separator)
                    .append(afterFileVo.getFileNo())
                    .append(File.separator);
//                    .append(afterFileVo.getOrgFileNm());
            fileEntity.setAccUrl(buf.toString());
            fileEntity.setFileNo(afterFileVo.getFileNo());
            fileRepository.save(fileEntity); //uploaded??? ?????? ?????? ??????

        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(tag + "after file No = " + afterFileVo.getFileNo());//kill
        return afterFileVo.getFileNo();
    }

    @Override
    public String getFileInfo(String fileRoot, Long fileNo) {
        StringBuffer buf = new StringBuffer();
        FileInfo fileEntity = fileRepository.getOne(fileNo);

        buf.setLength(0);
        buf.append(fileRoot)
                .append(fileEntity.getRegId()).append(File.separator)
                .append(fileEntity.getFileNo()).append(File.separator)
                .append(fileEntity.getSaveFileNm()); // org_file_nm ????????? ???????????? ????????? ????????????...
        return buf.toString();
    }

    @Override
    public FileInfo getFileVo(Long custNo, Long fileNo) {
        return fileRepository.findByCustNoAndFileNoAndUsedYn(custNo,fileNo ,"Y");
    }

    @Override
    public void revivalFileUsed(Map<String, Object> map) {
        fileMapper.revivalFileUsed(map); //uploaded??? ?????? ?????? ??????
    }

    @Override
    public void dropFile(String absFile) {
        File file = new File (absFile);
        if( file.exists() ){
            if(file.delete()){
                System.out.println("???????????? ??????");
            }
            else{
                System.out.println("???????????? ??????");
            }
        }
    }

    @Override
    public String getApndFileUrl(Map<String, Object> paraMap) {
        StringBuffer buf = new StringBuffer();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        try {
            Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
            FileInfo fi = fileRepository.findByCustNoAndFileNoAndUsedYn(custNo,fileNo,"Y");

            if (fi != null) {
                buf.append(env.getProperty("base_file_url")).append(fi.getAccUrl());
            }
            log.info("getApndFileUrl = " + buf.toString());//kill
        }
        catch (NullPointerException ne) {
        }

        return buf.toString();
    }

    @Override
    public String getImageUrl(Map<String, Object> paraMap) {
        StringBuffer buf = new StringBuffer();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        FileInfo fi = (FileInfo) fileRepository.findByCustNoAndFileNoAndUsedYn(custNo,fileNo,"Y");

        if (fi != null) {
            buf.append(env.getProperty("base_img_path"))
                    .append(fi.getRegId()).append("/")
                    .append(fi.getFileNo()).append("/")
                    .append(fi.getSaveFileNm());
        }
        log.info("getImageUrl = " + buf.toString());//kill

        return buf.toString();
    }

    private String getFileExt(String filename) {
        String fileExt = "";
        try {
            int extpos = filename.indexOf(".");
            fileExt = filename.substring(extpos + 1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return fileExt;

    }

    private void resetImageSize(String imgOriginalPath ,String imgFormat) {
        //String imgOriginalPath= "C:/test/test.jpg";          // ?????? ????????? ?????????
        //String imgTargetPath= "C:/test/test_resize.jpg";      // ??? ????????? ?????????
        int newWidth  = 0;                                    // ?????? ??? ??????
        int newHeight = 0;                                    // ?????? ??? ??????
        String mainPosition = "X";                              // W:????????????, H:????????????, X:????????? ?????????(????????????)

        Image image;
        int imageWidth;
        int imageHeight;
        double ratio;
        int w;
        int h;
        try {

            newWidth  = Integer.parseInt(env.getProperty("img.prod.width"));
            newHeight = Integer.parseInt(env.getProperty("img.prod.height"));
            image = ImageIO.read(new File(imgOriginalPath)); // ?????? ????????? ????????????

            // ?????? ????????? ????????? ????????????
            imageWidth = image.getWidth(null);
            imageHeight = image.getHeight(null);

            if(mainPosition.equals("W")){    // ????????????
                ratio = (double)newWidth/(double)imageWidth;
                w = (int)(imageWidth * ratio);
                h = (int)(imageHeight * ratio);

            }
            else if(mainPosition.equals("H")){ // ????????????
                ratio = (double)newHeight/(double)imageHeight;
                w = (int)(imageWidth * ratio);
                h = (int)(imageHeight * ratio);

            }
            else{ //????????? (????????????)
                w = newWidth;
                h = newHeight;
            }

            // ????????? ????????????
            // Image.SCALE_DEFAULT : ?????? ????????? ???????????? ???????????? ??????
            // Image.SCALE_FAST    : ????????? ?????????????????? ?????? ??????
            // Image.SCALE_REPLICATE : ReplicateScaleFilter ???????????? ????????? ??? ????????? ?????? ?????? ????????????
            // Image.SCALE_SMOOTH  : ???????????? ????????? ??????????????? ??????
            // Image.SCALE_AREA_AVERAGING  : ?????? ???????????? ??????
            Image resizeImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);

            // ??? ?????????  ????????????
            BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics g = newImage.getGraphics();
            g.drawImage(resizeImage, 0, 0, null);
            g.dispose();

            //ImageIO.write(newImage, imgFormat, new File(imgTargetPath));

            this.dropFile(imgOriginalPath); //???????????? ????????????
            ImageIO.write(newImage, imgFormat, new File(imgOriginalPath)); //????????? ????????? ???????????? ??????

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
