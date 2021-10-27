package daedan.mes.qc.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class ProdOwhDoc {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="docNo",nullable = false, columnDefinition = "numeric")
    private Long docNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*서류명*/
    @Column(name="docNm",nullable = false,  length = 100)
    private String docNm;

    @Column(name="chkYn",nullable = false, length = 1 , columnDefinition = "char default 'N'")
    private String chkYn;

    @Column(name="regId", columnDefinition = "numeric")
    private Long regId;

    @Column(name="modId", columnDefinition = "numeric")
    private Long modId;

    @Column(name="regIp", length = 20)
    private String regIp;

    @Column(name="modIp", length = 20)
    private String modIp;

    @Column(name="regdt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="modDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

}
