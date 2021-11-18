package daedan.mes.proc.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

//공정관리
@Data
@Entity
@NoArgsConstructor
public class ProcInfo {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="procNo",nullable = false)
    private Long procNo;

    @Column(name="procCd",nullable = false)
    private Long procCd;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*공정생산단위:*/
    @Column(name="procUnit",nullable = false, columnDefinition = "numeric default 0")
    private Long procUnit;

    /*소요시간:*/
    @Column(name="needHm",nullable = false, columnDefinition = "int default 0")
    private Integer needHm;

    /*CCP관리번호:*/
    @Column(name="ccp_no", columnDefinition = "numeric default 0")
    private Long ccpNo;

    //공정내용
    @Column(name="procCtnt", length = 4000)
    private String procCtnt;

    /*최대생산가능수량:*/
    @Column(name="maxMakeQty",nullable = false, columnDefinition = "int default 0")
    private Integer maxMakeQty;

    /*투입인원숙련도(상):*/
    @Column(name="usedMpLvlt",nullable = false, columnDefinition = "int default 0")
    private Integer usedMpLvlt;

    /*투입인원숙련도(중):*/
    @Column(name="usedMpLvlm",nullable = false, columnDefinition = "int default 0")
    private Integer usedMpLvlm;

    /*투입인원숙련도(하):*/
    @Column(name="usedMpLvlb",nullable = false, columnDefinition = "int default 0")
    private Integer usedMpLvlb;

    /*출력순서*/
    @Column(name="procSeq",nullable = false, columnDefinition = "int default 0")
    private Integer procSeq;

    @Column(name="fileNo")
    private Long fileNo;

    @Column(name="usedYn" ,nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="regId" ,columnDefinition = "bigint default 0")
    private Long regId;

    @Column(name="modId" ,columnDefinition = "bigint default 0")
    private Long modId;

    @Column(name="regIp" , length = 20)
    private String regIp;

    @Column(name="modIp" , length = 20)
    private String modIp;

    @Column(name="regDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="modDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

}
