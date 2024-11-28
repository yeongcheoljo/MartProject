package martproject.MartProject.domain.preorderGoods;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import martproject.MartProject.domain.member.Member;
import martproject.MartProject.domain.stock.Stock;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "preordergoods")
public class PreorderGoods {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "preorder_goods_num", unique = true, nullable = false)
    private Long preorder_goods_num;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Stock.class)
    @JoinColumn(name = "stock_num", updatable = false, nullable = false)
    private Stock stock;

    @Column(nullable = false)
    private String preorder_goods_sale;

    @Builder
    public PreorderGoods(Long preorder_goods_num, Stock stock, String preorder_goods_sale) {
        this.preorder_goods_num = preorder_goods_num;
        this.stock = stock;
        this.preorder_goods_sale = preorder_goods_sale;
    }
}
