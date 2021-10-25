package daedan.mes.make.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class WgtchkDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="api_no",nullable = false, columnDefinition = "numeric")
    private Long apiNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*상품번호*/
    @Column(name="prod_no", nullable = false)
    private Long prodNo;

    /*APIURL*/
    @Column(name="prod_nm", length = 100)
    private String prodNm;


    /*시작일시*/
    @Column(name="fr_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frDt;

    /*종료일시*/
    @Column(name="to_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDt;

    /*통과수량*/
    @Column(name="pass_qty",nullable = false, columnDefinition = "numeric default 0" , precision=10, scale=0)
    private  Float passQty;

    /*금속검출수량*/
    @Column(name="metal_qty",nullable = false, columnDefinition = "numeric default 0" , precision=10, scale=0)
    private  Float metalQty;

}
