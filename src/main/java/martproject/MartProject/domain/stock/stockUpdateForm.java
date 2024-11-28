package martproject.MartProject.domain.stock;

import lombok.Getter;
import lombok.Setter;
import martproject.MartProject.domain.goods.Goods;

import java.util.List;

@Getter
@Setter
public class stockUpdateForm {
    private List<Long> selectedItems;
    private List<Integer> stockCount;
    private List<Integer> sellPrice;
    private List<Integer> discount;
    private List<String> stockDate;
    private List<String> stockExpDate;
    private List<String> stockPlace;
    private List<Long> goodsNum;
}
