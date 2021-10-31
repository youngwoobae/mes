package daedan.mes.spot.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
/*작업장별설비관리*/
public class SpotEquip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="spotEquipNo",nullable = false)
    private Long spotEquipNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*장소번호*/
    @Column(name="spotNo",nullable = false)
    private Long spotNo;

    //장치유형번호(온도계 ,습도계....)
    @Column(name="equipNo",columnDefinition = "numeric default 0")
    private Long equipNo;

    //최대측정허용값
    @Column(name="max_lmt_val", precision=7, scale=3,columnDefinition = "numeric default 0")
    private Float maxLmtVal;

    //최소측정허용값
    @Column(name="min_lmt_val",  precision=7, scale=3,columnDefinition = "numeric default 0")
    private Float minLmtVal;

    //최대정상허용값
    @Column(name="max_nor_val" , precision=7, scale=3,columnDefinition = "numeric default 0")
    private Float maxNorVal;

    //최소정상허용값
    @Column(name="min_nor_val" , precision=7, scale=3,columnDefinition = "numeric default 0")
    private Float minNorVal;


    //최대정상허용값
    @Column(name="meas_unit" , columnDefinition = "numeric default 0")
    private Long measUnit;

    @Column(name="used_yn",nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;


    @Column(name="reg_id" ,columnDefinition = "numeric")
    private Long regId;

    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="reg_ip" , length = 20)
    private String regIp;


    @Column(name="mod_id" ,columnDefinition = "numeric")
    private Long modId;

    @Column(name="mod_ip" ,length = 20)
    private String modIp;

    @Column(name="mod_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;





}
