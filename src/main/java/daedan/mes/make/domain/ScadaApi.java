package daedan.mes.make.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class ScadaApi {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="apiNo",nullable = false, columnDefinition = "numeric")
    private Long apiNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*설치장소*/
    @Column(name="spotNo", nullable = false)
    private Long spotNo;

    /*API장비*/
    @Column(name="equipNo", nullable = false)
    private Long equipNo;

    /*APIURL*/
    @Column(name="apiUrl", length = 200)
    private String apiUrl;


    @Column(name="usedYn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;
}
