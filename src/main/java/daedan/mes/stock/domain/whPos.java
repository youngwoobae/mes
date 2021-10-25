package daedan.mes.stock.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class whPos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="pos_no",nullable = false, columnDefinition = "numeric")
    private Long posNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*창고번호*/
    @Column(name="wh_no",nullable = false, columnDefinition = "numeric")
    private Long whNo;

    /*행번호*/
    @Column(name="row_idx",nullable = false, columnDefinition = "numeric")
    private Integer rowIdx;

    /*열번호*/
    @Column(name="col_idx",nullable = false, columnDefinition = "numeric")
    private Integer colIdx;

    /*단번호*/
    @Column(name="stair_idx",nullable = false, columnDefinition = "numeric")
    private Integer stairIdx;

    @Column(name="able_yn",nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String ableYn;

    @Column(name="used_yn",nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="reg_id",nullable = false, columnDefinition = "numeric")
    private Long regId;

    @Column(name="mod_id",nullable = false, columnDefinition = "numeric")
    private Long modId;

    @Column(name="reg_ip",nullable = false, length = 20)
    private String regIp;

    @Column(name="mod_ip",nullable = false, length = 20)
    private String modIp;

    @Column(name="reg_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="mod_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;


}
