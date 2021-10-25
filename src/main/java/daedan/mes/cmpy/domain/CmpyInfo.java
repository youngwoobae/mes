package daedan.mes.cmpy.domain;

import daedan.mes.file.domain.FileInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class CmpyInfo {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cmpyNo")
    private Long cmpyNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*ERP 연동 코드*/
    @Column(name="erpCd", length = 20 )
    private String erpCd;

    /*회사유형(법인,개인)*/
    @Column(name="cmpyTp")
    private Long cmpyTp;

    /*회사명*/
    @Column(name="cmpyNm", length = 250)
    private String cmpyNm;

    /*대표자명*/
    @Column(name="cmanNm", length = 250)
    private String cmanNm;

    /*담당자전화*/
    @Column(name="cmanCellNo", length = 30)
    private String cmanCellNo;

    /*대표전화*/
    @Column(name="telNo", length = 30)
    private String telNo;

    /*사업자번호*/
    @Column(name="saupNo", length = 15)
    private String saupNo;

    /*업태*/
    @Column(name="bcdn" )
    private Long bcdn;

    /*종목*/
    @Column(name="kind" )
    private Long kind;


    @Column(name="hp", length = 30)
    private String hp;

    @Column(name="faxNo", length = 30)
    private String faxNo;

    /*법인,개인구분*/
    @Column(name="mngrGbnCd",nullable = false, columnDefinition = "bigint default 0")
    private long mngrGbnCd;

    @OneToOne
    @JoinColumn(name = "fileNo")
    private FileInfo fileInfo;

    //@Column(name="file_no" ,columnDefinition = "bigint default 0")
    //private long fileNo;

    /*대표메일:사용중지(21.07;.23)*/
    @Column(name="reprMailAddr" ,length = 50)
    private String reprMailAddr;

    /*주소*/
    @Column(name="addr", length = 200)
    private String addr;

    @Column(name="usedYn",nullable = false,  columnDefinition = "char(1) default 'Y'")
    private String usedYn;


    @Column(name="regId")
    private Long regId;

    @Column(name="modId")
    private Long modId;

    @Column(name="regIp", length = 20)
    private String regIp;

    @Column(name="modIp", length = 20)
    private String modIp;

    @Column(name="regDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="modDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;


}
