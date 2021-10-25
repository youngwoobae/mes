package daedan.mes.equip.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor

public class EquipMngrHstr {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "mngr_hstr_no", nullable = false)
    private Long mngrHstrNo;

    @Column(name = "spot_equip_no", nullable = false)
    private Long spotEquipNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    //최대허용임계값
    @Column(name = "max_lmt_val", nullable = false,  precision=7, scale=3)
    private Float maxLmtVal;

    //최소허용임계값
    @Column(name = "min_lmt_val", nullable = false,  precision=7, scale=3)
    private Float minLmtVal;

    //측정값
    @Column(name = "meas_val",   precision=7, scale=3)
    private Float measVal;

    //측정일시
    @Column(name = "unix_hms", nullable = false)
    private Long unixHms;

    /*작동유무(꼬치접이:0->동작,1->멈줌:*/
    @Column(name="oper_yn", columnDefinition = "char default 'Y'")
    private String operYn;

    /*알람발생유무(꼬치접이:0->미발생,1->발:*/
    @Column(name="alarm_yn", columnDefinition = "char default 'N'")
    private String alarmYn;

    /*시편테스트_FE:*/
    @Column(name="fe_yn", length= 1, columnDefinition = "char default 'N'")
    private String feYn;

    /*시편테스트_SUS:*/
    @Column(name="sus_yn", length= 1, columnDefinition = "char default 'N'")
    private String susYn;


    /*제품 + 시편테스트_FE:*/
    @Column(name="prod_fe_yn", length= 1, columnDefinition = "char default 'N'")
    private String prodFeYn;

    /*제품 + 시편테스트_SUS:*/
    @Column(name="prod_sus_yn", length= 1, columnDefinition = "char default 'N'")
    private String prodSusYn;


    /*시편테스트_상품:*/
    @Column(name="prod_yn", length= 1, columnDefinition = "char default 'N'")
    private String prodYn;

    @Column(name = "oper_no")
    private Long operNo;

    //사용유무
    @Column(name = "used_yn", nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;




}
