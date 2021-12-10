package daedan.mes.matr.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class BrnchInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="brnch_no",nullable = false)
    private Long brnchNo;

    /*모분류번호*/
    @Column(name="parBrnchNo",nullable = false)
    private Long parBrnchNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*분류명*/
    @Column(name="brnchNm",nullable = false, length=80)
    private String brnchNm;

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

    @Column(name="modDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

    @Column(name="usedYn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;
}

