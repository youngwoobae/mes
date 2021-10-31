package daedan.mes.spot.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class SpotEquipTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "spotEquipTempNo", nullable = false)
    private Long equipTempNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    //작업장별설비관리번호
    @Column(name = "spotEquipNo", nullable = false)
    private Long spotEquipNo;

    //측정값
    @Column(name = "measVal",  nullable = false,  precision=7, scale=3 , columnDefinition = "numeric default 0")
    private Float measVal;

    //측정일시
    @Column(name = "unixHms", nullable = false)
    private Long unixHms;
}
