package daedan.mes.matr.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class MatrCmpyCart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cart_no",nullable = false)
    private Long cartNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="user_id",nullable = false)
    private Long userId;

    @Column(name="cmpy_no",nullable = false)
    private Long cmpyNo;

    @Column(name="matr_no",nullable = false)
    private Long MatrNo;

/*
    @ManyToOne( fetch=FetchType.LAZY)
    @JoinColumn(name="matr_no")
    private MatrInfo matrInfo;
    public MatrInfo getMatrInfo() {
        return matrInfo;
    }
    public void setMatrInfo(MatrInfo matrInfo) {
        this.matrInfo = matrInfo;
    }
*/
}
