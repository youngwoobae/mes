package daedan.mes.modbus.sample.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class SampleTempData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="key_no",nullable = false, columnDefinition = "numeric")
    private Long keyNo;

    /*샘플값 */
    @Column(name = "sam_val", nullable = false,  precision=7, scale=3 , columnDefinition = "numeric")
    private Float samVal;

}
