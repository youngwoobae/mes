package daedan.mes.modbus.rtu.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class RtuInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="rtu_no",nullable = false, columnDefinition = "numeric")
    private Long rtuNo;

    /*인식식별자*/
    @Column(name="unit_Id",nullable = false, columnDefinition = "numeric")
    private int unitId;

    /*통신포트명*/
    @Column(name="port_nm",nullable = false)
    private String portNm;

    /*수신비트수*/
    @Column(name="data_len",nullable = false, columnDefinition = "int default 0")
    private int dataLen;

    /*패리티*/
    @Column(name="parity",nullable = false, columnDefinition = "int default 0")
    private int parity;

    @Column(name="off_set",nullable = false, columnDefinition = "int default 0")
    private int offSet;

    /*반복카운터*/
    @Column(name="repeat",nullable = false, columnDefinition = "int default 0")
    private int repeat;

    @Column(name="stop_bit",nullable = false, columnDefinition = "int default 1")
    private int stopBit;

    @Column(name="baud_rt",nullable = false, columnDefinition = "int default 9600")
    private int baudRt;

    @Column(name="encoding",nullable = false, length=5, columnDefinition = "varchar default 'RTU'")
    private String encoding;

    @Column(name="start_addr",nullable = false, columnDefinition = "int default 1")
    private int startAddr;

}
