package martproject.MartProject.domain.goods;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "goods")
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "goods_num", unique = true, nullable = false)
    private Long goods_num;

    @Column(length = 100, nullable = false)
    private String goods_name;

    @Column(length = 100, nullable = false)
    private String goods_picture;

    @Column(length = 100, nullable = false)
    private int goods_buy_price;

    @Builder
    public Goods(String goods_name, String goods_picture, int goods_buy_price) {
        this.goods_name = goods_name;
        this.goods_picture = goods_picture;
        this.goods_buy_price = goods_buy_price;

    }
}
