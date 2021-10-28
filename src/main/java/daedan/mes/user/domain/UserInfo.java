package daedan.mes.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
//@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private static final long serialVersionUID = 1L;
    public static final String S3_PATH = "https://s3.ap-northeast-2.amazonaws.com/kms-bucket-01";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="userId",nullable = false)
    private long userId;

    /*ERP사용자번호*/
    @Column(name="erpUserNo", length = 20)
    private String erpUserNo;

    @Column(name="deptNo", columnDefinition = "int default 0")
    private Long deptNo;

    @Column(name="userNm",nullable = false, length = 30)
    private String userNm;

    /*직위코드*/
    @Column(name="userPosn", columnDefinition = "numeric default 0")
    private Long userPosn;

    /*원료검수가능여부*/
    @Column(name="matrInspYn", nullable = false, length = 1, columnDefinition = "char default 'N'")
    private String matrInspYn;


    /*제품검수가능여부*/
    @Column(name="prodInspYn",nullable = false, length = 1, columnDefinition = "char default 'N'")
    private String prodInspYn;


    /*입사일자*/
    @Column(name="entrDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date entrDt;

    /*직종구분(220):사무직,생산직*/
    @Column(name="ocpnKind", nullable = false)
    private Long ocpnKind;

    /*채용구분(230):내국인,외국인,용역*/
    @Column(name="emplKind")
    private Long emplKind;

    /*상태(2900):근무,휴직,정직,퇴사*/
    @Column(name="userStat")
    private Long userStat;

    @Basic(fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name="secrtNo",nullable = false, length = 100)
    private String secrtNo;     /*비번*/

    @OneToOne
    @JoinColumn(name = "custInfo")
    private CustInfo custInfo;

     @Column(name = "mailAddr", length = 100)
    private String mailAddr;

    @Column(name="fileNo", columnDefinition = "int default 0")
    private long fileNo;
    /*이동전화번호*/
    @Column(name="cellNo")
    private  byte[] cellNo;

    /*사용자토큰*/
    @Column(name="token", length = 8000)
    private String token;


    @Column(name="procCont" )
    private String procCont;

    /*최종접근메뉴명*/
    @Column(name="accPath", length = 100)
    private String accPath;


    /*사용권한코드*/
    @Column(name="authcd", columnDefinition = "numeric default 0")
    private  Long authCd;

    /*내선번호*/
    @Column(name="extNo" , columnDefinition = "varchar(20) ")
    private String extNo;

    /*휴대폰식별번호*/
    @Column(name="cellId")
    private String cellId;

    @Column(name="makeSeq", columnDefinition = "numeric default 0")
    private Integer makeSeq;


    @Column(name="regId")
    private Long regId;

    @Column(name="modId")
    private Long modId;

    @Column(name="regIp")
    private String regIp;

    @Column(name="modIp")
    private String modIp;

    @Column(name="regDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="moddt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

    @Column(name="usedYn",nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;


    @Enumerated(EnumType.STRING)
    @Column(name="userTp",nullable = false, length = 10)
    private UserType userTp;

    @Enumerated(EnumType.STRING)
    @Column(name="indsTp",nullable = true, length = 10)
    private IndsType indsTp;

}

