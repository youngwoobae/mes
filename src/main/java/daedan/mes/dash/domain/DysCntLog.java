package daedan.mes.dash.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class DysCntLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="dysCntLogNo",nullable = false)
    private long dycCntLogNo;

    /*고객번호*/
    @Column(name="custNo",nullable = false )
    private Long custNo;

    /*작업장소번호*/
    @Column(name="spotNo",nullable = false )
    private Long spotNo;

    /*발송일자*/
    @Column(name="unix_hms",nullable = false, length = 12)
    private Long unixHms;

    /*수신Msg*/
    @Column(name="rcvMsg", length = 100)
    private String rcvMsg;

    /*작동상태*/
    @Column(name="procCnt",nullable = false, length = 6)
    private Integer procCnt;


    @Column(name="usedYn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;


    @Column(name="reg_id"  )
    private Long regId;

    @Column(name="mod_id" )
    private Long modId;

    @Column(name="reg_ip"  ,length = 20)
    private String regIp;

    @Column(name="mod_ip" ,length = 20)
    private String modIp;

    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="mod_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;
}
