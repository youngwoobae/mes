package daedan.mes.moniter.domain;

import daedan.mes.code.domain.CodeInfo;
import daedan.mes.spot.domain.SpotEquip;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class EquipMine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="mine_no",nullable = false)
    private Long mineNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @OneToOne (fetch = FetchType.LAZY, optional=true)
    @JoinColumn(name = "spotEquipNo")
    private SpotEquip spotEquip;

    //사용자정보
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /*장소_설비번호 */
    @Column(name="spot_equip_cd",nullable = false, columnDefinition = "numeric default 0",  updatable=false, insertable=false)
    private Long spotEquipNo;

}
