package daedan.mes.modbus.udp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class UdpInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="udp_no",nullable = false, columnDefinition = "numeric")
    private Long udpNo;

    /*Address*/
    @Column(name="inet_addr",nullable = false )
    private String inetAddr;

    /*장비포트*/
    @Column(name="device_port",nullable = false )
    private int devicePort;

    /*offset where to start reading from*/
    @Column(name="off_set",nullable = false )
    private int offSet;

    /*the number of DI's to read*/
    @Column(name="count",nullable = false )
    private int count;

    /*a loop for repeating the transaction*/
    @Column(name="repeat",nullable = false )
    private int repeat;

}
