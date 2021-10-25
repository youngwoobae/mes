package daedan.mes.modbus.socket.service;

public interface SocketService {
    String sendByteMsgType1(String ip, int port, byte[] msg);
    int[] sendByteMsgType2(String ip, int port, byte[] msg);
}
