package daedan.mes.cmpy.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class CmpyMatr {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cmpyMatrNo" )
    private Long cmpyMatrNo;

    /*매출거래처코드*/
    @Column(name="cmpyNo" ,nullable = false, columnDefinition = "bigint default 0")
    private Long cmpyNo;

    /*사입원료번호*/
    @Column(name="matrNo" ,nullable = false, columnDefinition = "bigint default 0")
    private Long matrNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="used_yn",nullable = false, length = 1, columnDefinition = "char default 'Y'")
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
