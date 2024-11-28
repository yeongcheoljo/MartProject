package martproject.MartProject.domain.preorderInfo;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import martproject.MartProject.domain.member.Member;
import martproject.MartProject.domain.stock.Stock;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "preorderinfo")
public class PreorderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "preorder_info_num", unique = true, nullable = false)
    private Long preorder_info_num;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Member.class)
    @JoinColumn(name = "member_id", updatable = false, nullable = false)
    private Member member;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Stock.class)
    @JoinColumn(name = "stock_num", updatable = false, nullable = false)
    private Stock stock;

    @Column(nullable = false)
    private int buy_count;

    @Column(nullable = false)
    private LocalDate preorder_date;

    @Column(nullable = false)
    private String ispreorder;

    @Builder
    public PreorderInfo(Long preorder_info_num, Member member, Stock stock, LocalDate preorder_date, String ispreorder, int buy_count) {
        this.preorder_info_num = preorder_info_num;
        this.member = member;
        this.stock = stock;
        this.buy_count = buy_count;
        this.preorder_date = preorder_date;
        this.ispreorder = ispreorder;
    }
}
