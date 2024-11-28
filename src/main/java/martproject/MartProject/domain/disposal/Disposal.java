package martproject.MartProject.domain.disposal;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import martproject.MartProject.domain.goods.Goods;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "disposal")
public class Disposal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "disposal_num", unique = true, nullable = false)
    private Long disposal_num;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Goods.class)
    @JoinColumn(name = "goods_num", updatable = false, nullable = false)
    private Goods goods;

    @Column(nullable = false)
    private int disposal_amount;

    @Column(nullable = false)
    private LocalDate disposal_expiration_date;

    @Column(nullable = false)
    private LocalDate disposal_date;

    @Builder
    public Disposal(Long disposal_num, Goods goods, int disposal_amount, LocalDate disposal_expiration_date, LocalDate disposal_date) {
        this.disposal_num = disposal_num;
        this.goods = goods;
        this.disposal_amount = disposal_amount;
        this.disposal_expiration_date = disposal_expiration_date;
        this.disposal_date = disposal_date;
    }
}
