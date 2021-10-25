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
            Long fileext= Long.valueOf(env.getProperty("base_file_ext_cd"));
            fileEntity.setFileLen(multipartFile.getSize());

            CodeInfo codeEntity = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(fileext,fileExtNm.toUpperCase(Locale.ROOT),"Y");
            fileEntity.setFileTp(codeEntity.getCodeNo());
            log.info(tag + " fileEntity = " + fileEntity);

            FileInfo chkvo = fileRepository.findByCustNoAndSaveFileNmAndUsedYn(custNo,fileEntity.getSaveFileNm(),"Y"); //파일정보저장후 생성된 파일번호를 추출하기 위해서 사용중.
            if (chkvo != null) {
                fileEntity.setFileNo(chkvo.getFileNo());
            }
            afterFileVo = fileRepository.save(fileEntity); //uploaded된 파일 정보 저장

            buf.setLength(0);
            buf.append(afterFileVo.getRegId())
                    .append(File.separator)
                    .append(afterFileVo.getFileNo())
                    .append(File.separator);
//                    .append(afterFileVo.getOrgFileNm());
            fileEntity.setAccUrl(buf.toString());
            fileEntity.setFileNo(afterFileVo.getFileNo());
            fileRepository.save(fileEntity); //uploaded된 파일 정보 저장

        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(tag + "after file No = " + afterFileVo.getFileNo());//kill
        return afterFileVo.getFileNo();
    }

    @Override
    public String getFileInfo(Long fileNo) {
        StringBuffer buf = new StringBuffer();
        FileInfo fileEntity = fileRepository.getOne(fileNo);
        Long custNo = fileEntity.getCustNo();
        String fileRoot = env.getProperty("file.root.path");
        buf.setLength(0);
        buf.append(fileRoot)
                .append(fileEntity.getRegId()).append(File.separator)
                .append(fileEntity.getFileNo()).append(File.separator)
                .append(fileEntity.getSaveFileNm()); // org_file_nm 읽으면 파일명이 달라서 실행안됨...
        return buf.toString();
    }

    @Override
    public FileInfo getFileVo(Long custNo, Long fileNo) {
        return fileRepository.findByCustNoAndFileNoAndUsedYn(custNo,fileNo ,"Y");
    }

    @Override
    public void revivalFileUsed(Map<String, Object> map) {
        fileMapper.revivalFileUsed(map); //uploaded된 파일 정보 저장
    }

    @Override
    public void dropFile(String absFile) {
        File file = new File (absFile);
        if( file.exists() ){
            if(file.delete()){
                System.out.println("파일삭제 성공");
            }
            else{
                System.out.println("파일삭제 실패");
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
        //String imgOriginalPath= "C:/test/test.jpg";          // 원본 이미지 파일명
        //String imgTargetPath= "C:/test/test_resize.jpg";      // 새 이미지 파일명
        int newWidth  = 0;                                    // 변경 할 넓이
        int newHeight = 0;                                    // 변경 할 높이
        String mainPosition = "X";                              // W:넓이중심, H:높이중심, X:설정한 수치로(비율무시)

        Image image;
        int imageWidth;
        int imageHeight;
        double ratio;
        int w;
        int h;
        try {

            newWidth  = Integer.parseInt(env.getProperty("img.prod.width"));
            newHeight = Integer.parseInt(env.getProperty("img.prod.height"));
            image = ImageIO.read(new File(imgOriginalPath)); // 원본 이미지 가져오기

            // 원본 이미지 사이즈 가져오기
            imageWidth = image.getWidth(null);
            imageHeight = image.getHeight(null);

            if(mainPosition.equals("W")){    // 넓이기준
                ratio = (double)newWidth/(double)imageWidth;
                w = (int)(imageWidth * ratio);
                h = (int)(imageHeight * ratio);

            }
            else if(mainPosition.equals("H")){ // 높이기준
                ratio = (double)newHeight/(double)imageHeight;
                w = (int)(imageWidth * ratio);
                h = (int)(imageHeight * ratio);

            }
            else{ //설정값 (비율무시)
                w = newWidth;
                h = newHeight;
            }

            // 이미지 리사이즈
            // Image.SCALE_DEFAULT : 기본 이미지 스케일링 알고리즘 사용
            // Image.SCALE_FAST    : 이미지 부드러움보다 속도 우선
            // Image.SCALE_REPLICATE : ReplicateScaleFilter 클래스로 구체화 된 이미지 크기 조절 알고리즘
            // Image.SCALE_SMOOTH  : 속도보다 이미지 부드러움을 우선
            // Image.SCALE_AREA_AVERAGING  : 평균 알고리즘 사용
            Image resizeImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);

            // 새 이미지  저장하기
            BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics g = newImage.getGraphics();
            g.drawImage(resizeImage, 0, 0, null);
            g.dispose();

            //ImageIO.write(newImage, imgFormat, new File(imgTargetPath));

            this.dropFile(imgOriginalPath); //원본파일 삭제하고
            ImageIO.write(newImage, imgFormat, new File(imgOriginalPath)); //크기가 변경된 이미지로 교체

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
