package martproject.MartProject.domain.preorderInfo;

import lombok.*;
import martproject.MartProject.domain.member.Member;
import martproject.MartProject.domain.stock.Stock;

import java.time.LocalDate;

public class PreorderInfoDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class RequestPreorderInfoDto {

        private Long preorder_info_num;

        private Member member;

        private Stock stock;

        private int buy_count;

        private LocalDate preorder_date;

        private String ispreorder;

        public PreorderInfo toEntity(){
            return PreorderInfo.builder()
                    .preorder_info_num(preorder_info_num)
                    .member(member)
                    .stock(stock)
                    .buy_count(buy_count)
                    .preorder_date(preorder_date)
                    .ispreorder(ispreorder)
                    .build();
        }
    }
}
