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

    @Column(name="custNo" , columnDefinition = "numeric")
    private Long custNo;

    @Column(name="matrNo",nullable = false , columnDefinition = "numeric")
    private Long matrNo;

    @Column(name="prodNo",nullable = false, columnDefinition = "numeric" )
    private Long prodNo;

    @Column(name="consistRt", precision=10, scale=8 )
    private  float consistRt;

    @Column(name="needQty", precision=8, scale=2)
    private  float needQty;

    /*제품 bom 레벨*/
    @Column(name="bomLvl", columnDefinition = "numeric default 1" )
    private Long bomLvl;

    /*구매필요여부*/
    @Column(name="pursYn", columnDefinition = "char default 'Y'")
    private String pursYn;


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
