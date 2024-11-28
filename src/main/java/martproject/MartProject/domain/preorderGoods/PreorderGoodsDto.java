package martproject.MartProject.domain.preorderGoods;

import lombok.*;
import martproject.MartProject.domain.stock.Stock;

public class PreorderGoodsDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class RequestPreorderGoodsDto{
        private Long preorder_goods_num;

        private Stock stock;

        private String preorder_goods_sale;

        public PreorderGoods toEntity(){
            return PreorderGoods.builder()
                    .preorder_goods_num(preorder_goods_num)
                    .stock(stock)
                    .preorder_goods_sale(preorder_goods_sale)
                    .build();
        }
    }
}
