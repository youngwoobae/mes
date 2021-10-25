package daedan.mes.prod.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class ProdCapa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="capa_no",nullable = false, columnDefinition = "numeric")
    private Long capaNo;

    /*제품형태(par_code_no = 2000)*/
    @Column(name="prod_shape", columnDefinition = "numeric default 0" )
    private Long prodShape;

    /*일최대생산량*/
    @Column(name="dayCapa", columnDefinition = "numeric default 0" )
    private Integer dayCapa;

}
