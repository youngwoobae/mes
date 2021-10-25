package daedan.mes.haccp.common.datamgr.rawdata;

import daedan.mes.haccp.common.datamgr.DataMgrVO;
import daedan.mes.haccp.common.datamgr.errdata.ErrDataController;
import daedan.mes.haccp.common.datamgr.errdata.ErrDataMapper;
import daedan.mes.haccp.common.datamgr.io.SocketIoMapper;
import daedan.mes.haccp.common.datamgr.utils.BeanUtils;
import daedan.mes.haccp.common.error_handle.CustomErrorHandler;
import daedan.mes.haccp.project.equip_ctrl.heater.HeaterMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RawDataService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    RawDataMapper rawDataMapper;

    @Autowired
    ErrDataMapper errDataMapper;

    @Autowired
    HeaterMapper heatMapper;

    @Autowired
    SocketIoMapper socketIoMapper;

    //DataMgrVO dataMgrVO;

    private SimpleDateFormat mSimpleDateFormat = null;
    private Date currentTime = null;
    private String mTime = null;

    /**
     * 수신된 패킷데이터를 Queue에 뽑아내 분해하여 map에 저장
     *
     * return void
     * throws SQLException
     * throws InterruptedException
     * throws ParserConfigurationException
     * throws IOException
     * throws SAXException
     * throws TransformerException
     */

    public void packetProcess() {
        try {
             DataMgrVO dataMgrVO = (DataMgrVO) BeanUtils.getBean("dataMgrVO");
            //if (null != dataMgrVO && dataMgrVO.packetDataMgrQ.size() > 0) {
            if (dataMgrVO.packetDataMgrQ.size() > 0) {
                dataMgrVO = (DataMgrVO) BeanUtils.getBean("dataMgrVO");
                Object[] obj = dataMgrVO.packetDataMgrQ.poll();
                ChannelHandlerContext ctx = (ChannelHandlerContext) obj[0];
                ByteBuf packetMsg = (ByteBuf) obj[1];

                InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                InetAddress inetaddress = socketAddress.getAddress();
                String ipAddress = inetaddress.getHostAddress(); // IP address of client

                SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
                Date currentDate = new Date();
                String measTime = currentDateFormat.format(currentDate);

                Float tempF = null;
                Float pressF = null;
                Integer speedInt = null;
                Integer mdYnInt = null;
                Integer mdKindInt = null;
                int functionCode = 0;
                int length = 0;
                String mCode = null;
                String verfCode = null;

                if (null != packetMsg) {
                    ByteBuf byteBufMessage = packetMsg;

                    int packetSize = byteBufMessage.readableBytes();

                    StringBuilder sb = new StringBuilder();

                    for (int idx = 0; idx < packetSize; idx++) {
                        byte b = byteBufMessage.getByte(idx);
                        sb.append(String.format("%02x ", b & 0xff));
                    }
                    log.info("client ip : " + ipAddress + " 수신된 값(hex) : " + sb.toString());

                    byte[] mCodeArr = new byte[4];

                    // 수신받은 데이터 중 mcode, futioncode, length 구함
                    for (int idx = 0; idx < byteBufMessage.readableBytes(); idx++) {
                        if (0 <= idx && 3 >= idx) {
                            mCodeArr[idx] = byteBufMessage.getByte(idx);
                        } else if (4 == idx) {
                            functionCode = byteBufMessage.getByte(4);
                            functionCode -=48; //AddOn By KMJ At 21.10.08

                        } else if (5 == idx) {
                            length = byteBufMessage.getByte(5);
                            length -=48; //AddOn By KMJ At 21.10.08
                        }
                    }
                    mCode = new String(mCodeArr);

                    Map<String, Object> paramMap = new HashMap<String, Object>();

                    log.info("수신된 mCode  : " + mCode );
                    log.info("수신된 functionCode : " + functionCode );

                    paramMap.put("mCode", mCode);
                    paramMap.put("functionCode", functionCode);

                    if (socketIoMapper == null) { //AddOn By KMJ AT 21.10.08
                        socketIoMapper = (SocketIoMapper) BeanUtils.getBean("socketIoMapper");
                    }
                    Map<String, Object> rstVerfYn = socketIoMapper.selectVerfInfo(paramMap);
                    String stdYn = rstVerfYn.get("stdYn").toString();

                    // function code에 정의된 자릿수와 맞지 않거나, 패킷 정보와 DB에 등록된 한계기준 정보랑 일치하지 않을경우 처리하지 않음
                    switch (functionCode) {
                        // 설비 event 설정
                        case 0:
                            if (7 != packetSize) {
                                throw new RuntimeException();
                            }
                            int eventYn = byteBufMessage.getByte(6);
                            paramMap.put("eventYn", eventYn);  //이벤트발생여부
                            updateRecCcpEvent(paramMap);
                            return;
                        // 온도
                        case 1:
                            if (10 != packetSize || "N".equals(stdYn)) {
                                throw new RuntimeException();
                            }
                            tempF = getTempF(byteBufMessage); //수신된온도
                            break;
                        // 온도, 압력
                        case 2:
                            if (14 != packetSize || "N".equals(stdYn)) {
                                throw new RuntimeException();
                            }
                            tempF = getTempF(byteBufMessage); //수신된 온도
                            pressF = getPressF(byteBufMessage); //수신된 압력
                            break;
                        // 온도, 속도
                        case 3:
                            if (12 != packetSize || "N".equals(stdYn)) {
                                throw new RuntimeException();
                            }
                            tempF = getTempF(byteBufMessage);   //수신된 온도
                            speedInt = getSpeed(byteBufMessage); //수신된 속도
                            break;
                        // 금속 여부
                        case 4:
                            if (7 != packetSize || "N".equals(stdYn)) {
                                throw new RuntimeException();
                            }
                            mdYnInt = getMetalDetection(byteBufMessage, 4); //수신된 금속
                            break;
                        // 금속 여부(테스트)
                        case 5:
                            if (8 != packetSize || "N".equals(stdYn)) {
                                throw new RuntimeException();
                            }
                            mdKindInt = getMetalDetectionKind(byteBufMessage);   //수신된 금속 종류
                            mdYnInt = getMetalDetection(byteBufMessage, 5); //수신된 금속
                            break;
                    }
                }

                log.info("수신된 온도 : " + tempF);
                log.info("수신된 압력 : " + pressF);
                log.info("수신된 속도 : " + speedInt);
                log.info("수신된 금속 종류 : " + mdKindInt);
                log.info("수신된 금속 : " + mdYnInt);

                // 설비에 설정된 정보들을 조회
                if (socketIoMapper == null) { //AddOn By KMJ AT 21.10.08
                    socketIoMapper = (SocketIoMapper) BeanUtils.getBean("socketIoMapper");
                }
                List<Map<String, Object>> equipInfoParamList = socketIoMapper.selectEquipInfo(mCode);

                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("mCode", mCode);
                paramMap.put("regDate", measTime.substring(0, 10));

                // 설비의 마지막 SEQ + 1 조회
                if (rawDataMapper == null) { //AddOn By KMJ AT 21.10.08
                    rawDataMapper = (RawDataMapper) BeanUtils.getBean("rawDataMapper");
                }
                Map<String, Object> maxSeqMap = rawDataMapper.selectRawDataMaxSeq(paramMap);

                if (null != equipInfoParamList && equipInfoParamList.size() > 0) {

                    for (int i = 0; i < equipInfoParamList.size(); i++) {

                        equipInfoParamList.get(i).put("temper_meas_val", tempF);
                        equipInfoParamList.get(i).put("speed_meas_val", speedInt);
                        equipInfoParamList.get(i).put("pressure_meas_val", pressF);
                        equipInfoParamList.get(i).put("md_yn_meas_val", mdYnInt);
                        equipInfoParamList.get(i).put("md_kind", mdKindInt);
                        equipInfoParamList.get(i).put("meas_time", measTime);
                        equipInfoParamList.get(i).put("reg_date", measTime.substring(0, 10));
                        equipInfoParamList.get(i).put("seq", maxSeqMap.get("seq"));

                        // 금속검출은 한계 기준 테이블에 아이템 코드가 등록되지 않으므로 04번으로 하드코딩
                        if ("C0030".equals(equipInfoParamList.get(i).get("lCode").toString())) {
                            equipInfoParamList.get(i).put("lmt_item_code", "04");
                        }
                    }
                    // 데이터 축척(DB저장, 파일저장)
                    rawDataWrite(equipInfoParamList);
                    // 이탈검증
                    ErrDataController errDataController = new ErrDataController();
                    errDataController.errDataStartVerify(equipInfoParamList);
                }
                Thread.sleep(1000);
            }
        } catch (IllegalStateException | SQLException e) {
            log.error("Occurs RawDataService.packetProcess.IllegalStateException or SQLException !!!!!");
            CustomErrorHandler.handle(getClass().getSimpleName(), e);
        } catch (InterruptedException e) {
            log.error("Occurs RawDataService.packetProcess.InterruptedException !!!!!");
            CustomErrorHandler.handle(getClass().getSimpleName(), e);
        } catch (ParserConfigurationException e) {
            log.error("Occurs RawDataService.packetProcess.ParserConfigurationException !!!!!");
            CustomErrorHandler.handle(getClass().getSimpleName(), e);
        } catch (TransformerException e) {
            log.error("Occurs RawDataService.packetProcess.TransformerException !!!!!");
            CustomErrorHandler.handle(getClass().getSimpleName(), e);
        //} catch (SAXException e) {
        //    CustomErrorHandler.handle(getClass().getSimpleName(), e);
        } catch (IOException e) {
            log.error("Occurs RawDataService.packetProcess.IOException !!!!!");
            CustomErrorHandler.handle(getClass().getSimpleName(), e);
        } catch (RuntimeException e) {
            log.error("Occurs RawDataService.packetProcess.RuntimeException !!!!!");
            CustomErrorHandler.handle(getClass().getSimpleName(), e);
        }
    }

    /**
     * 축적할 데이터가 담긴 map을 db저장, 파일저장 함수에 호출
     *
     * param paramMap
     * return void
     * throws SQLException
     * throws InterruptedException
     * throws ParserConfigurationException
     * throws IOException
     * throws SAXException
     * throws TransformerException
     */
    public void rawDataWrite(List<Map<String, Object>> paramList) throws SQLException, InterruptedException,
            ParserConfigurationException, TransformerException, IOException {

        if (null != paramList && paramList.size() > 0) {
            // raw 데이터 db 저장
            rawDataDBWrite(paramList);
            // raw 데이터 file 저장
            rawDataFileWrite(paramList);
        }

    }

    /**
     * 서버에 수신된 데이터를 축적 테이블에 저장
     *
     * param paramMap
     * return void
     * throws SQLException
     */
    public void rawDataDBWrite(List<Map<String, Object>> paramList) throws SQLException {
        if (null != paramList && paramList.size() > 0) {
            for (int idx = 0; idx < paramList.size(); idx++) {
                rawDataMapper.insertRawdata(paramList.get(idx));
            }
        }
    }

    /**
     * 서버에 수신된 데이터를 축적 파일에 저장
     *
     * param paramMap
     * return void
     * throws ParserConfigurationException
     * throws IOException
     * throws SAXException
     * throws TransformerException
     */
    public void rawDataFileWrite(List<Map<String, Object>> paramList)
            throws InterruptedException, ParserConfigurationException, TransformerException, IOException {
        if (null != paramList && paramList.size() > 0) {
            for (int i = 0; i < paramList.size(); i++) {
                mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                currentTime = new Date();
                mTime = mSimpleDateFormat.format(currentTime);
                String filePath = env.getProperty("FILE.ROWDATA.PATH");
                paramList.get(i).put("file_path", filePath);
                // 데이터 축적 파일 처리
                mergeXml(paramList.get(i));
            }
        }
    }

    /**
     * 서버에 수신된 데이터를 축적 파일에 저장(파일이 없으면 파일생성 함수호출, 파일이 있다면 기존의 파일에 저장함수 호출)
     *
     * @param paramMap
     * @return void
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     * throws SAXException
     */
    public void mergeXml(Map<String, Object> paramMap)
            throws ParserConfigurationException, TransformerException,  IOException {
        String filePath = paramMap.get("filePath").toString();

        File folder = new File(filePath + "/" + mTime);

        // 파일 생성 폴더가 없을경우 폴더 생성
        if (!folder.exists()) {
            folder.setExecutable(false,true);
            folder.setReadable(true);
            folder.setWritable(false,true);
            folder.mkdirs();
        }

        File f = new File(filePath + "/" + mTime + "/" + paramMap.get("mCode") + "_" + mTime + ".xml");
        if (f.exists()) {
            addNodeXml(paramMap);
        } else {
            createXml(paramMap);
        }
    }

    /**
     * 서버에 수신된 데이터를 축적 파일에 저장(신규파일을 생성하여 데이터저장)
     *
     * @param paramMap
     * @return void
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     */
    public void createXml(Map<String, Object> paramMap)
            throws ParserConfigurationException, TransformerException, IOException {

        String filePath = paramMap.get("filePath").toString();

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setExpandEntityReferences(false);
        docFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        docFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        docFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document document = docBuilder.newDocument();
//			document.setXmlStandalone(true);

        Element root = document.createElement("root");
        document.appendChild(root);

        Element dataItem = document.createElement("data");
        dataItem.setAttribute("breakVeryYn", "N");
        dataItem.setAttribute("seq", paramMap.get("seq").toString());
        root.appendChild(dataItem);

        String verfTypeCode = paramMap.get("verfTypeCode").toString();

        // 가열이벤트, 가열품온, 냉장냉동 인 경우 온도만
        if ("H02".equals(verfTypeCode) || "H03".equals(verfTypeCode) || "C01".equals(verfTypeCode)) {
            Element temperMeasVal = document.createElement("temperMeasVal");
            Float temperMeasValF = (Float) paramMap.get("temperMeasVal");

            if (null != paramMap.get("temperMeasVal")) {
                Text temperMeasValText = document.createTextNode(Float.toString(temperMeasValF));
                temperMeasVal.appendChild(temperMeasValText);
            }

            dataItem.appendChild(temperMeasVal);
            // 가열지속인 경우 (온도, 속도)
        } else if ("H01".equals(verfTypeCode)) {
            Element temperMeasVal = document.createElement("temperMeasVal");
            Float temperMeasValF = (Float) paramMap.get("temperMeasVal");

            if (null != paramMap.get("temperMeasVal")) {
                Text temperMeasValText = document.createTextNode(Float.toString(temperMeasValF));
                temperMeasVal.appendChild(temperMeasValText);
            }

            dataItem.appendChild(temperMeasVal);

            Element speedMeasVal = document.createElement("speedMeasVal");
            Integer speedMeasValInt = (Integer) paramMap.get("speedMeasVal");

            if (null != paramMap.get("speedMeasVal")) {
                Text speedMeasValText = document.createTextNode(Integer.toString(speedMeasValInt));
                speedMeasVal.appendChild(speedMeasValText);
            }

            dataItem.appendChild(speedMeasVal);
            // 가열압력이벤트인 경우(온도, 압력)
        } else if ("H04".equals(verfTypeCode)) {
            Element temperMeasVal = document.createElement("temperMeasVal");
            Float temperMeasValF = (Float) paramMap.get("temperMeasVal");

            if (null != paramMap.get("temperMeasVal")) {
                Text temperMeasValText = document.createTextNode(Float.toString(temperMeasValF));
                temperMeasVal.appendChild(temperMeasValText);
            }

            dataItem.appendChild(temperMeasVal);

            Element pressureMeasVal = document.createElement("pressureMeasVal");
            Float pressureMeasValF = (Float) paramMap.get("pressureMeasVal");

            if (null != paramMap.get("pressureMeasVal")) {
                Text pressureMeasValText = document.createTextNode(Float.toString(pressureMeasValF));
                pressureMeasVal.appendChild(pressureMeasValText);
            }

            dataItem.appendChild(pressureMeasVal);
            // 금속 검출인 경우
        } else if ("D01".equals(verfTypeCode)) {
            Element mdYnMeasVal = document.createElement("mdYnMeasVal");
            Integer mdYnMeasValF = (Integer) paramMap.get("mdYnMeasVal");

            if (null != paramMap.get("mdYnMeasVal")) {
                Text mdYnMeasValText = document.createTextNode(Integer.toString(mdYnMeasValF));
                mdYnMeasVal.appendChild(mdYnMeasValText);
            }

            dataItem.appendChild(mdYnMeasVal);
        } else {

        }

        String measTime = (String) paramMap.get("measTime");

        Element currentTime = document.createElement("currentTime");
        Text currentTimeText = document.createTextNode(measTime);
        currentTime.appendChild(currentTimeText);
        dataItem.appendChild(currentTime);

        // XML 파일로 쓰기
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // 정렬 스페이스4칸
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // 들여쓰기
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); // doc.setXmlStandalone(true); 했을때 붙어서

        DOMSource source = new DOMSource(document);
        FileOutputStream fos = new FileOutputStream(
                new File(filePath + "/" + mTime + "/" + paramMap.get("mCode") + "_" + mTime + ".xml"));
        StreamResult result = new StreamResult(fos);
        transformer.transform(source, result);
        fos.close();

        log.info("createXml success!!");
    }

    /**
     * 서버에 수신된 데이터를 축적 파일에 저장(기존의 파일에 데이터저장)
     *
     * @param paramMap
     * @return void
     * @throws TransformerException
     * @throws IOException
     * throws SAXException
     * @throws ParserConfigurationException
     */
    public void addNodeXml(Map<String, Object> paramMap)
            throws TransformerException, IOException, ParserConfigurationException {

        String filePath = paramMap.get("filePath").toString();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setExpandEntityReferences(false);
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = null;
        try {
            document = documentBuilder.parse(filePath + "/" + mTime + "/" + paramMap.get("mCode") + "_" + mTime + ".xml");
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        }

        Element root = document.getDocumentElement();

        Element dataItem = document.createElement("data");
        dataItem.setAttribute("breakVeryYn", "N");
        dataItem.setAttribute("seq", paramMap.get("seq").toString());
        root.appendChild(dataItem);

        String verfTypeCode = paramMap.get("verfTypeCode").toString();

        // 가열이벤트, 가열품온, 냉장냉동 인 경우 온도만
        if ("H02".equals(verfTypeCode) || "H03".equals(verfTypeCode) || "C01".equals(verfTypeCode)) {
            Element temperMeasVal = document.createElement("temperMeasVal");
            Float temperMeasValF = (Float) paramMap.get("temperMeasVal");

            if (null != paramMap.get("temperMeasVal")) {
                Text temperMeasValText = document.createTextNode(Float.toString(temperMeasValF));
                temperMeasVal.appendChild(temperMeasValText);
            }

            dataItem.appendChild(temperMeasVal);
            // 가열지속인 경우 (온도, 속도)
        } else if ("H01".equals(verfTypeCode)) {
            Element temperMeasVal = document.createElement("temperMeasVal");
            Float temperMeasValF = (Float) paramMap.get("temperMeasVal");

            if (null != paramMap.get("temperMeasVal")) {
                Text temperMeasValText = document.createTextNode(Float.toString(temperMeasValF));
                temperMeasVal.appendChild(temperMeasValText);
            }

            dataItem.appendChild(temperMeasVal);

            Element speedMeasVal = document.createElement("speedMeasVal");
            Integer speedMeasValInt = (Integer) paramMap.get("speedMeasVal");

            if (null != paramMap.get("speedMeasVal")) {
                Text speedMeasValText = document.createTextNode(Integer.toString(speedMeasValInt));
                speedMeasVal.appendChild(speedMeasValText);
            }

            dataItem.appendChild(speedMeasVal);
            // 가열압력이벤트인 경우(온도, 압력)
        } else if ("H04".equals(verfTypeCode)) {
            Element temperMeasVal = document.createElement("temperMeasVal");
            Float temperMeasValF = (Float) paramMap.get("temperMeasVal");

            if (null != paramMap.get("temperMeasVal")) {
                Text temperMeasValText = document.createTextNode(Float.toString(temperMeasValF));
                temperMeasVal.appendChild(temperMeasValText);
            }

            dataItem.appendChild(temperMeasVal);

            Element pressureMeasVal = document.createElement("pressureMeasVal");
            Float pressureMeasValF = (Float) paramMap.get("pressureMeasVal");

            if (null != paramMap.get("pressureMeasVal")) {
                Text pressureMeasValText = document.createTextNode(Float.toString(pressureMeasValF));
                pressureMeasVal.appendChild(pressureMeasValText);
            }

            dataItem.appendChild(pressureMeasVal);
            // 금속 검출인 경우
        } else if ("D01".equals(verfTypeCode)) {
            Element mdYnMeasVal = document.createElement("mdYnMeasVal");
            Integer mdYnMeasValF = (Integer) paramMap.get("mdYnMeasVal");

            if (null != paramMap.get("mdYnMeasVal")) {
                Text mdYnMeasValText = document.createTextNode(Integer.toString(mdYnMeasValF));
                mdYnMeasVal.appendChild(mdYnMeasValText);
            }

            dataItem.appendChild(mdYnMeasVal);
        } else {

        }

        Element currentTime = document.createElement("currentTime");

        if (null != paramMap.get("measTime")) {
            String measTime = (String) paramMap.get("measTime");
            Text currentTimeText = document.createTextNode(measTime);
            currentTime.appendChild(currentTimeText);
        }

        dataItem.appendChild(currentTime);

        // XML 파일로 쓰기
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new FileOutputStream(
                new File(filePath + "/" + mTime + "/" + paramMap.get("mCode") + "_" + mTime + ".xml")));

        transformer.transform(source, result);

        log.info("addNodeXml success!!");
    }

    /**
     * 온도 바이트 버퍼값을 float값으로 변환
     *
     * param ByteBuf byteBuf
     * return Float
     */
    public Float getTempF(ByteBuf byteBuf) {

        Float tempF = null;
        Long tempL = null;

        if (null != byteBuf) {

            byte[] temp = new byte[4];
            StringBuilder tempStr = new StringBuilder();

            temp[0] = byteBuf.getByte(6);
            temp[1] = byteBuf.getByte(7);
            temp[2] = byteBuf.getByte(8);
            temp[3] = byteBuf.getByte(9);

            for (byte b : temp) {
                tempStr.append(String.format("%02X", b & 0xff));
            }

            tempL = Long.parseLong(tempStr.toString(), 16);
            tempF = Float.intBitsToFloat(tempL.intValue());
        }

        return tempF;
    }

    /**
     * 압력 바이트 버퍼값을 float값으로 변환
     *
     * param ByteBuf byteBuf
     * return Float
     */
    public Float getPressF(ByteBuf byteBuf) {

        Float pressF = null;

        Long pressL = null;

        if (null != byteBuf) {

            byte[] press = new byte[4];
            StringBuilder pressStr = new StringBuilder();

            press[0] = byteBuf.getByte(10);
            press[1] = byteBuf.getByte(11);
            press[2] = byteBuf.getByte(12);
            press[3] = byteBuf.getByte(13);

            for (byte b : press) {
                pressStr.append(String.format("%02X", b & 0xff));
            }

            pressL = Long.parseLong(pressStr.toString(), 16);
            pressF = Float.intBitsToFloat(pressL.intValue());
        }

        return pressF;
    }

    /**
     * 속도 바이트 버퍼값을 int값으로 변환
     *
     * param ByteBuf byteBuf
     * return int
     */
    public int getSpeed(ByteBuf byteBuf) {

        int speedInt = 0;

        Long speedL = null;

        if (null != byteBuf) {

            byte[] speed = new byte[2];
            StringBuilder speedStr = new StringBuilder();

            speed[0] = byteBuf.getByte(10);
            speed[1] = byteBuf.getByte(11);

            for (byte b : speed) {
                speedStr.append(String.format("%02X", b & 0xff));
            }

            speedL = Long.parseLong(speedStr.toString(), 16);
            speedInt = speedL.intValue();
        }
        return speedInt;
    }

    /**
     * 금속검출 바이트 버퍼값을 int값으로 변환
     *
     * param ByteBuf byteBuf
     * return int
     */
    public int getMetalDetection(ByteBuf byteBuf, int functionCode) {

        int mdYnInt = 0;

        if (null != byteBuf) {
            if(functionCode == 4) {
                mdYnInt = byteBuf.getByte(6);
            }
            else if (functionCode == 5) {
                mdYnInt = byteBuf.getByte(7);
            }
            mdYnInt -= 48; //AddOn By KMJ AT 21.10.08
            log.info("getMetalDetection = " + mdYnInt); //AddOn By KMJ AT 21.10.08
        }

        return mdYnInt;
    }

    /**
     * 금속종류 바이트 버퍼값을 int값으로 변환
     *
     * param ByteBuf byteBuf
     * return int
     */
    public int getMetalDetectionKind(ByteBuf byteBuf) {

        int mdKindInt = 0;

        if (null != byteBuf) {
            mdKindInt = byteBuf.getByte(6);
            mdKindInt -= 48; //AddOn By KMJ At 21.10.08 (금속종류)
            log.info("getMetalDetectionKind = " + mdKindInt); //AddOn By KMJ AT 21.10.08
        }
        return mdKindInt;
    }

    /**
     * 설비 event 여부를 설정 함
     *
     * param paramMap
     * return void
     */
     public void updateRecCcpEvent(Map<String, Object> paramMap) {
        String mCode = null;
        if (null != paramMap.get("mCode")) {
            mCode = paramMap.get("mCode").toString();
            if (mCode.length() >= 2) {
                mCode = mCode.substring(0, 2);
                switch (mCode) {
                    case "CH":  //가열기
                        paramMap.put("lCode", "C0010");
                        break;
                    case "CC": //냉장창고
                        paramMap.put("lCode", "C0020");
                        break;
                    case "CD": //금속검출
                        paramMap.put("lCode", "C0030");
                        break;
                }
                if (1 == Integer.parseInt(paramMap.get("eventYn").toString())) {
                    paramMap.put("eventYn", "Y");
                } else {
                    paramMap.put("eventYn", "N");
                }
                heatMapper.updateRecCcpEvent(paramMap);
            }
        }
    }
}
