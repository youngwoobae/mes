package daedan.mes.code.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class  CodeInfo {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="code_no",nullable = false, columnDefinition = "numeric")
    private Long codeNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="par_code_no",nullable = false, columnDefinition = "numeric")
    private Long parCodeNo;

    @Column(name="code_seq",nullable = false, columnDefinition = "int default 0")
    private Integer codeSeq;

    @Column(name="code_nm",nullable = false, length = 100)
    private String codeNm;

    @Column(name="code_alais",nullable = false, length = 100)
    private String codeAlais;

    @Column(name="code_brief",nullable = false, length = 100)
    private String codeBrief;

    @Column(name="code_ref" ,columnDefinition = "numeric default 0")
    private Long codeRef;

    /*연관한계기준코드(schCcpStdCode.ccp_lmt_std_no)*/
    @Column(name="ccp_lmt_std_no", length = 1, columnDefinition = "char default 'N'")
    private String ccpLmtStdNo;

    @Column(name="sys_code_yn", length = 1, columnDefinition = "char default 'N'")

    private String sysCodeYn;

    /*CCP 연동여부 (작업코드(par_indc_no = 720) 가 살균,중량선별,금속검출 공정인 경우 설정되어야 함.)*/
    @Enumerated(EnumType.STRING)
    @Column(name="ccp_tp",  length = 10 , columnDefinition = "varchar default 'NONE'")
    private CcpType ccpTp;

    @Column(name="used_yn", length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="mod_able_yn",nullable = false, length = 1, columnDefinition = "char default 'N'")
    private String modableYn;


    @Column(name="reg_id", columnDefinition = "numeric")
    private Long regId;

    @Column(name="mod_id", columnDefinition = "numeric")
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
