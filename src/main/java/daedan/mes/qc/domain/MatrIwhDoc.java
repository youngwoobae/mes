package daedan.mes.qc.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MatrIwhDoc {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="doc_no",nullable = false, columnDefinition = "numeric")
    private Long docNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*서류명*/
    @Column(name="doc_nm",nullable = false,  length = 100)
    private String docNm;

    @Column(name="chk_yn",nullable = false, length = 1 , columnDefinition = "char default 'N'")
    private String chkYn;

    @Column(name="reg_id", columnDefinition = "numeric")
    private Long regId;

    @Column(name="mod_id", columnDefinition = "numeric")
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

    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

}
