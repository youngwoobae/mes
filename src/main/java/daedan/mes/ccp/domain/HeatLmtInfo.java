package daedan.mes.ccp.domain;

import daedan.mes.code.domain.CodeInfo;
import daedan.mes.spot.domain.SpotEquip;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class HeatLmtInfo {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="lmt_no")
    private Long lmtNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*한계기준코드(코드정보:2100)*/
    @Column(name="heatTp",nullable = false, columnDefinition = "numeric default 0")
    private Long heatTp;

    /*최고가열온도*/
    @Column(name="maxHeat" ,nullable = false, precision=5, scale=1 , columnDefinition = "numeric default 0")
    private Float maxHeat;

    /*최저가열온도*/
    @Column(name="minHeat" ,nullable = false, precision=5, scale=1 , columnDefinition = "numeric default 0")
    private Float minHeat;

    /*최고가열시간(분)*/
    @Column(name="maxHeatTime" ,nullable = false, precision=5 , columnDefinition = "numeric default 0")
    private Float maxHeatTime;

    /*최저가열시간(분)*/
    @Column(name="minHeatTime" ,nullable = false, precision=5, columnDefinition = "numeric default 0")
    private Float minHeatTime;

    @Column(name="usedYn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="modId")
    private Long modId;

    @Column(name="regId")
    private Long regId;

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
