package daedan.mes.proc.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//공정별 기본투입인원
@Data
@Entity
@NoArgsConstructor
public class ProcMp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="proc_grp_no",nullable = false)
    private Long procGrpNo;

    @Column(name="proc_grp_cd",nullable = false)
    private Long procGrpCd;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*공정생산단위:*/
    @Column(name="user_id",nullable = false, columnDefinition = "numeric default 0")
    private Long userId;

    @Column(name="usedYn" ,nullable = false, length = 1)
    private String usedYn;
}
