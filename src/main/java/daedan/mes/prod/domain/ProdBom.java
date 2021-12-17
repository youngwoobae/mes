package daedan.mes.prod.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
    public class ProdBom {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="bomNo",nullable = false, columnDefinition = "numeric")
    private Long bomNo;

    /*모bom관리번호*/
    @Column(name="par_bom_no", columnDefinition = "numeric")
    private Long parBomNo;

    /*고객번호*/
    @Column(name="custNo" ,nullable = false, columnDefinition = "numeric")
    private Long custNo;

    /*품번*/
    @Column(name="prod_no",nullable = false, columnDefinition = "numeric" )
    private Long prodNo;

    /*자재번호*/
    @Column(name="matr_no",nullable = false , columnDefinition = "numeric")
    private Long matrNo;

    /*함량비율(백분율,소숫점 8자리까지)*/
    @Column(name="consistRt", precision=10, scale=8 )
    private  float consistRt;

    /*소요량 (부자재인경우 함량비율 대신 사용됨)*/
    @Column(name="needQty", precision=8, scale=2)
    private  float needQty;

    /*제품 bom 레벨*/
    @Column(name="bomLvl", columnDefinition = "numeric default 1" )
    private Long bomLvl;

    /*구매필요여부 : 외부에서 구매되는 경우 'Y' ,자체조달인 경우 'N' : 일반적으로 복합제품인 경우 자사제품이 구성요소로 포함되므로 'N'으로 설정됨*/
    @Column(name="pursYn", columnDefinition = "char default 'Y'")
    private String pursYn;

    /*사용구분*/
    @Column(name="usedYn",nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;

    /*
    @ManyToOne( fetch=FetchType.LAZY)
    @JoinColumn(name="prod_no")
    private ProdInfo prodInfo;
    public ProdInfo getProdInfo(long prodNo) {
        return prodInfo;
    }
    public void  setProdInfo(ProdInfo prodInfo) {
        this.prodInfo = prodInfo;
    }

    @ManyToOne( fetch=FetchType.LAZY)
    @JoinColumn(name="matr_no")
    private MatrInfo matrInfo;
    public MatrInfo getMatrInfo(long matrNo) {
        return matrInfo;
    }
    public void  setMatrInfo(MatrInfo matrInfo) {
        this.matrInfo = matrInfo;
    }
    */

    @Column(name="reg_id", columnDefinition = "numeric")
    private Long regId;

    @Column(name="mod_id", columnDefinition = "numeric")
    private Long modId;

    @Column(name="reg_ip", length = 20)
    private String regIp;

    @Column(name="mod_ip", length = 20)
    private String modIp;

    @Column(name="reg_dt")
    private Date regDt;

    @Column(name="mod_dt")
    private Date modDt;


}
