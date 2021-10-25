package daedan.mes.cmmn.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
class ExchgUnit {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="exchg_no",nullable = false, columnDefinition = "numeric")
    private Long exchgNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*환산대상단위코드*/
    @Column(name="unit_no",nullable = false, columnDefinition = "numeric")
    private Long unitNo;
    /*환산율 : ex : 대상단위코드가 kg 인 경우 기준단위는 g이므로 1000으로설정할 것*/
    @Column(name="exchg_rt",nullable = false, columnDefinition = "numeric")
    private Long exchgRt;

}
