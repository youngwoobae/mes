package daedan.mes.modbus.tcp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class TcpInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="tcp_no",nullable = false, columnDefinition = "numeric")
    private Long tcpNo;

    /*수신비트수*/
    @Column(name="oper_no",nullable = false, columnDefinition = "int default 0")
    private Long operNo;

    /*시작번지*/
    @Column(name="start_addr",nullable = false, columnDefinition = "int default 0")
    private int startAddr;

    /*추출데이터길이*/
    @Column(name="data_ㅣLength",nullable = false, columnDefinition = "int default 0")
    private int dataLength;

}
