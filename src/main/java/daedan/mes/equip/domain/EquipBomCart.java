package daedan.mes.equip.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//설비별자재 BOM CART
@Data
@Entity
@NoArgsConstructor
public class EquipBomCart {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cart_no",nullable = false)
    private Long cartNo;

    @Column(name="equip_No",nullable = false)
    private Long equipNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="matr_No",nullable = false)
    private Long matrNo;

    @Column(name="need_qty" )
    private Long needQty;

    @Column(name="user_id",nullable = false )
    private Long userId;

    /*
    @ManyToOne( fetch=FetchType.LAZY)
    @JoinColumn(name="equip_no")
    private EquipInfo equipInfo;
    public EquipInfo getEquipInfo(long equip_no) {
        return equipInfo;
    }
    public void  setEquipInfo(EquipInfo equipInfo) {
        this.equipInfo = equipInfo;
    }


    @ManyToOne( fetch=FetchType.LAZY)
    @JoinColumn(name="matr_no")
    private EquipMatr equipMatr;
    public EquipMatr getEquipMatr(long matr_no) {
        return equipMatr;
    }
    public void  setEquipMatr(EquipMatr equipMatr) {
        this.equipMatr = equipMatr;
    }
    */
}
