package daedan.mes.cmpy.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class CmpyDlvPlc {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="plc_no")
    private long plcNo;

    @Column(name="cmpy_no")
    private Long cmpyNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*기본배송지여부*/
    @Column(name="base_plc_yn" ,nullable = false )
    private String basePlcYn;

    /*배송장소명*/
    @Column(name="plc_nm" ,nullable = false)
    private String plcNm;

    /*배송지주소*/
    @Column(name="plc_addr" ,nullable = false)
    private String plcAddr;

    @Column(name="used_yn",nullable = false,  columnDefinition = "char(1) default 'Y'")
    private String usedYn;

    @Column(name="reg_id")
    private Long regId;

    @Column(name="mod_id")
    private Long modId;

    @Column(name="reg_ip", length = 20)
    private String regIp;

    @Column(name="mod_ip", length = 20)
    private String modIp;

    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="mod_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;


    public Date getRegDt(){
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Date date = new Date();
        return  new Timestamp(date.getTime());
    }

    public Date getModDt(){
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Date date = new Date();
        return  new Timestamp(date.getTime());
    }


}
