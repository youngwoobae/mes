package daedan.mes.modbus.socket.service;

import daedan.mes.common.service.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Map;

@Service("socketService")
public class SocketServiceImpl implements SocketService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    private Socket mSocket;
    private BufferedReader mIn;
    private PrintWriter mOut;
    private String recvMsg = "";
    private int DECTECTION_COMMAND_3A = 58; //to hex = 3A
    private int DECTECTION_COMMAND_03 = 3; //to hex = 04 : 온도제어(하담푸드)
    private int DECTECTION_COMMAND_04 = 4; //to hex = 04 : 온도제어(하담푸드)

    @Override
    public String sendByteMsgType1(String ip, int port, byte[] msg) {
        String tag = "RotService.sendByteMsg => ";
        InputStream dis = null;
        OutputStream dos = null;
        String rsltDectionStr = "00000000";
        int BUF_SIZE = 117;
        int COMMAND_POS = 7;
        ByteBuffer bf = ByteBuffer.allocate(BUF_SIZE);
        try {
            mSocket = new Socket(ip, port);
            log.info(tag + ip + " 연결됨 / 송신메세지 = " + msg);
            mSocket.setSoTimeout(1000 * 5);

            dos = mSocket.getOutputStream();//출력스트림
            dos.write(msg); //Binary 전송
            dos.flush();

            dis = mSocket.getInputStream();//입력스트림
            int idx = -1;
            while (++idx < BUF_SIZE) {
                try {
                    int b = dis.read();
                    // log.info("byte read value[ " + idx + "] = " + StringUtil.fixLenString(Integer.toHexString(b),2));
                    bf.put((byte) b);
                } catch (SocketTimeoutException te) {
                    log.error("수신대기 시간초과.최종수신 문자열위치 = " + idx + " / " + te.getMessage());
                    break;
                }
            }
            log.info(tag + "최종수신문자 = " + StringUtil.bytesToHex(new byte[bf.remaining()]));
            int command = bf.get(COMMAND_POS);
            if (command == DECTECTION_COMMAND_3A) {  //정상테이터 수신
                /*
                log.info("aaaaaaaaaaa.command.107 = " + bf.get(107));
                log.info("aaaaaaaaaaa.stx.108 = " + bf.get(108));
                log.info("aaaaaaaaaaa.년.109 = " + bf.get(109));
                log.info("aaaaaaaaaaa.월.110 = " + bf.get(110));
                log.info("aaaaaaaaaaa.일.111 = " + bf.get(111));
                log.info("aaaaaaaaaaa.수량.112 = " + bf.get(112));
                log.info("aaaaaaaaaaa.수량.113 = " + bf.get(113));
                log.info("aaaaaaaaaaa.수량.114 = " + bf.get(114));
                log.info("aaaaaaaaaaa.수량.115= " + bf.get(115));
                 */
                StringBuffer buf = new StringBuffer();
                //이하 이상하게도 검출수량만 little endian으로 들어옴.
                buf.append( StringUtil.fixLenString(Integer.toHexString(bf.get(115)),2))
                        .append(StringUtil.fixLenString(Integer.toHexString(bf.get(114)),2))
                        .append(StringUtil.fixLenString(Integer.toHexString(bf.get(113)),2))
                        .append(StringUtil.fixLenString(Integer.toHexString(bf.get(112)),2));

                rsltDectionStr = buf.toString();
            }
            if (command == DECTECTION_COMMAND_3A) {  //온도데이터(하담푸드) 정상테이터 수신
                log.info(tag + "최종수신문자 = " + StringUtil.bytesToHex(new byte[bf.remaining()]));
            }
        } catch (IOException e) {
            log.error("연결실패1 IP = " + ip + " : port = " + port);
            // e.printStackTrace();
        }
        finally {
            try {
                if (dis != null) {
                    dis.close();
                }
                if (dos != null) {
                    dos.close();
                }

                if (mSocket != null) {
                    mSocket.close();
                }
                log.error("연결리소스 해제.IP = " + ip + " : port = " + port );
            }
            catch (IOException e) {
                // e.printStackTrace();
                log.error("연결실패2 IP = " + ip + " : port = " + port);
                //log.error(e.getMessage());
            }
            log.info(tag + "최종수신문자 = " + StringUtil.bytesToHex(new byte[bf.remaining()]));
        }
        return rsltDectionStr;
    }

    /*하담푸드 온도제어*/
    @Override
    public int[] sendByteMsgType2(String ip, int port, byte[] msg) {
        String tag = "RotService.sendByteMsgType2 => ";
        String rsltDectionStr = "00000000";
        int BUF_SIZE = 255; //송신문자열(msg)의 10,11번째 hex byte에 있음. (rotServiceImpl의 184행 참조)
        int FUN_CODE_POS = 3;
        InputStream dis = null;
        OutputStream dos = null;
        //ByteBuffer bf = ByteBuffer.allocate(BUF_SIZE);
        int[] nResult = new int[7];
        try {
            mSocket = new Socket(ip, port);
            log.info(tag + ip + " 연결됨 / 송신메세지 = " + msg);
            mSocket.setSoTimeout(1000 * 1);
            dos = mSocket.getOutputStream(); //binary 전
            dos.write(msg);
            dos.flush();

            dis = mSocket.getInputStream();//입력스트림
            int idx = -1;
            while (++idx < 7) {
                try {
                    nResult[idx] = dis.read();
                    log.info(tag + "수신문자열[ " + idx + " ] = " + nResult[idx]);
                } catch (SocketTimeoutException te) {
                    log.error(tag + "수신대기 시간초과.최종수신 문자열위치 = " + idx + " / " + te.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            log.error("연결실패.IP = " + ip + " : port = " + port + " 사유 = " + e.getMessage());
            // e.printStackTrace();
        }
        finally {
            try {
                if (dis != null) {
                    dis.close();
                }
                if (dos != null) {
                    dos.close();
                }
                if (mSocket != null) {
                    mSocket.close();
                }
                log.error("연결리소스 해제.IP = " + ip + " : port = " + port );
            }
            catch (IOException e) {
                log.error("연결해제 오류.IP = " + ip + " : port = " + port + " 사유 = " + e.getMessage());
            }
            return nResult;
        }

    }

}
