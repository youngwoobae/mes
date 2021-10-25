package daedan.mes.equip.domain;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

//설비별자재bom
@Data
@Entity
@NoArgsConstructor

public class EquipReport {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="equip_report_no",nullable = false)
    private long equipReportNo;

    /*기록번호*/
    @Column(name = "mngr_hstr_no", nullable = false)
    private Long mngrHstrNo;

    /*설비번호*/
    @Column(name="spot_equip_no",nullable = false )
    private Long spotEquipNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*제품번호*/
    @Column(name="prod_no",nullable = false )
    private Long prodNo;

    /*시편테스트_FE:*/
    @Column(name="fe_yn", length= 1, columnDefinition = "char default 'N'")
    private String feYn;

    /*시편테스트_SUS:*/
    @Column(name="sus_yn", length= 1, columnDefinition = "char default 'N'")
    private String susYn;

    /*제품 + 시편테스트_FE:*/
    @Column(name="prod_fe_yn", length= 1, columnDefinition = "char default 'N'")
    private String prodFeYn;

    /*제품 + 시편테스트_SUS:*/
    @Column(name="prod_sus_yn", length= 1, columnDefinition = "char default 'N'")
    private String prodSusYn;

    /*시편테스트_상품:*/
    @Column(name="prod_yn", length= 1, columnDefinition = "char default 'N'")
    private String prodYn;

    //측정일시
    @Column(name = "meas_dt", nullable = false)
    private String measDt;

    /*테스트구분 0:테스트없음, 140:FE 통과, 2:SES통과, 12:FE&SUS통과, 123:FE&SUS&제품통과*/
    @Column(name="test_tp" ,columnDefinition = "numeric default 0" )
    private Integer testTp;

    /*비고*/
    @Column(name="rmk", length = 2000 )
    private String rmk;

    @Column(name="reg_id")
    private long regId;

    @Column(name="mod_id")
    private long modId;

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

    @Column(name="used_yn",nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;

}