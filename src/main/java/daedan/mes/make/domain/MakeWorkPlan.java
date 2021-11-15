package daedan.mes.make.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MakeWorkPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="planNo",nullable = false, columnDefinition = "numeric")
    private Long planNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*분류번호*/
    @Column(name="brnch_no",nullable = false )
    private Long brnchNo;

    @Column(name="planDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date planDt;

    /*지시내용*/
    @Column(name="textArea", length = 4000)
    private String textArea;

    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

}
