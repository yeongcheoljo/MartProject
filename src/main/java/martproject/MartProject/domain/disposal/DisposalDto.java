package martproject.MartProject.domain.disposal;


import lombok.*;
import martproject.MartProject.domain.goods.Goods;

import java.time.LocalDate;

public class DisposalDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class RequestDisposalDto {
        private Long disposal_num;

        private Goods goods;

        private int disposal_amount;

        private LocalDate disposal_expiration_date;

        private LocalDate disposal_date;

        public Disposal toEntity(){
            return Disposal.builder()
                    .disposal_num(disposal_num)
                    .goods(goods)
                    .disposal_amount(disposal_amount)
                    .disposal_expiration_date(disposal_expiration_date)
                    .disposal_date(disposal_date)
                    .build();
        }
    }
}
