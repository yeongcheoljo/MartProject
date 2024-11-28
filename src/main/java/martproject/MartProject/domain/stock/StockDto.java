package martproject.MartProject.domain.stock;

import lombok.*;
import martproject.MartProject.domain.goods.Goods;

import java.time.LocalDate;
import java.util.Date;

public class StockDto {


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class RequestStockDto {
        private Long stock_num;

        private Goods goods;

        private int stock;

        private int stock_sell_price;

        private LocalDate stock_date;

        private LocalDate stock_expiration_date;

        private int stock_discount;

        private String stock_place;

        public Stock toEntity(){
            return Stock.builder()
                    .stock_num(stock_num)
                    .goods(goods)
                    .stock(stock)
                    .stock_sell_price(stock_sell_price)
                    .stock_expiration_date(stock_expiration_date)
                    .stock_date(stock_date)
                    .stock_discount(stock_discount)
                    .stock_place(stock_place)
                    .build();
        }
    }
}
