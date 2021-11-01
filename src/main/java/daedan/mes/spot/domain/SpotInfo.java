package daedan.mes.spot.domain;

import daedan.mes.file.domain.FileInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
/*작업장정보*/
public class SpotInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="spotNo",nullable = false)
    private Long spotNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    //작업장
    @Column(name = "spot_nm",nullable = false, length = 100)
    private String spotNm;

    //ccp 유형(살균:2031,금속검출:2302)
    @Column(name = "ccp_tp",nullable = false,columnDefinition = "numeric default 0")
    private Long ccpTp;

    //api 분류
    @Column(name = "svc_tp",nullable = false, length = 20 , columnDefinition = "varchar(255) default  'DIRECT'")
    private String svcTp;

    @Column(name = "scada_api", length = 100 )
    private String scadaApi;


    //@OneToOne (fetch = FetchType.LAZY, optional=true)
    //@JoinColumn(name = "fileNo")
    //private FileInfo fileInfo;
    @Column(name="file_no" )
    private Long fileNo;


   //작업장비고
    @Column(name="spot_rmk" , length = 4000)
    private String spotRmk;

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

    @Column(name = "used_yn", nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;

}
