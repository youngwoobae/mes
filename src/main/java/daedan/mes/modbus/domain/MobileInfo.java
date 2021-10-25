package daedan.mes.modbus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MobileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "mobl_no", nullable = false)
    private Long moblNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*작업장설비관리번호*/
    @Column(name="spot_equip_no",nullable = false)
    private Long spotEquipNo;

    /*장치IP*/
    @Column(name="device_ip",nullable = false, length = 20 )
    private String deviceIp;


    /*장치포트번호*/
    @Column(name="device_port",nullable = false, columnDefinition = "int default 512" )
    private Integer devicePort;

    /*장치번호*/
    @Column(name="device_no",nullable = false, columnDefinition = "int default 0"  )
    private Integer deviceNo;

    /*시작주소*/
    @Column(name="fr_addr",   columnDefinition = "int default 0")
    private int frAddr;

    /*읽기갯수*/
    @Column(name="data_len", nullable = false,  columnDefinition = "int default 0")
    private int dataLen;

    /*통신프로트콜(1500:ByTCP, OverTCP, Serial)*/
    @Column(name="protocol",nullable = false, columnDefinition = "numeric default 0 " )
    private Long protocol;

    /*연동메뉴번호*/
    @Column(name="sys_menu_no",nullable = false, columnDefinition = "numeric default 0 " )
    private Long sysMenuNo;

    /*표시여부*/
    @Column(name="disp_yn",nullable = false,  columnDefinition = "char default 'Y'")
    private String dispYn;

    /*사용여부*/
    @Column(name="used_yn" ,nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="reg_id" ,columnDefinition = "numeric")
    private Long regId;

    @Column(name="mod_id" ,columnDefinition = "numeric")
    private Long modId;

    @Column(name="reg_ip" , length = 20)
    private String regIp;

    @Column(name="mod_ip" ,nullable = false, length = 20)
    private String modIp;

    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="mod_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;


}
