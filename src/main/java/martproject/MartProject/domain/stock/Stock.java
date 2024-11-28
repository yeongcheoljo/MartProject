package martproject.MartProject.domain.stock;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import martproject.MartProject.domain.goods.Goods;

import java.time.LocalDate;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "stock_num", unique = true, nullable = false)
    private Long stock_num;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Goods.class)
    @JoinColumn(name = "goods_num", updatable = false, nullable = false)
    private Goods goods;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private int stock_sell_price;

    @Column(nullable = false)
    private LocalDate stock_expiration_date;

    @Column(nullable = false)
    private LocalDate stock_date;

    @Column(nullable = false)
    private int stock_discount;

    @Column(length = 30, nullable = false)
    private String stock_place;

    @Builder
    public Stock(Long stock_num, Goods goods, int stock, int stock_sell_price, LocalDate stock_date, int stock_discount, String stock_place, LocalDate stock_expiration_date) {
        this.stock_num = stock_num;
        this.goods = goods;
        this.stock = stock;
        this.stock_sell_price = stock_sell_price;
        this.stock_date = stock_date;
        this.stock_discount = stock_discount;
        this.stock_place = stock_place;
        this.stock_expiration_date = stock_expiration_date;
    }
}
