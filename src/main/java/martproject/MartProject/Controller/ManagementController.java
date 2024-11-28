package martproject.MartProject.Controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import martproject.MartProject.domain.goods.Goods;
import martproject.MartProject.domain.preorderGoods.PreorderGoods;
import martproject.MartProject.domain.stock.Stock;
import martproject.MartProject.domain.stock.StockDto;
import martproject.MartProject.domain.stock.stockUpdateForm;
import martproject.MartProject.repository.GoodsRepository;
import martproject.MartProject.repository.PreorderGoodsRepository;
import martproject.MartProject.repository.StockRepository;
import martproject.MartProject.service.StockService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Controller
public class ManagementController {

    private final StockService stockService;
    private final StockRepository stockRepository;
    private final GoodsRepository goodsRepository;
    private final PreorderGoodsRepository preorderGoodsRepository;

    public ManagementController(StockService stockService, StockRepository stockRepository, GoodsRepository goodsRepository, PreorderGoodsRepository preorderGoodsRepository) {
        this.stockService = stockService;
        this.stockRepository = stockRepository;
        this.goodsRepository = goodsRepository;
        this.preorderGoodsRepository = preorderGoodsRepository;
    }

    @GetMapping("/management")
    public String management(Model model) {
        model.addAttribute("stockDto", new StockDto.RequestStockDto());
        return "management";
    }

    @GetMapping("/stockorder")
    public String stockorder(Model model){
        List<Goods> goodsList = goodsRepository.findAll();
        model.addAttribute("goodsList", goodsList);
        model.addAttribute("stockDto", new StockDto.RequestStockDto());
        return "stockorder";
    }

    @GetMapping("/stockmanagement")
    public String stockmanagement(Model model) {
        List<Stock> stockList = this.stockRepository.findAll();
        model.addAttribute("stockList", stockList);
        return "stockmanagement";
    }

    @GetMapping("/stockdelete")
    public String stockdelete(Model model) {
        List<Stock> stockList = this.stockRepository.findAll();
        model.addAttribute("stockList", stockList);
        return "stockdelete";
    }

    @GetMapping("/goodsdetail/{stock_num}")
    public String stockdetail(@PathVariable("stock_num") Long stock_num, Model model) {
        Optional<Stock> selectedStockOptional = stockRepository.findById(stock_num);
        List<PreorderGoods> preorderGoodsList = preorderGoodsRepository.findAll();
        String isPreorder = "N";
        if (selectedStockOptional.isPresent()) {
            Stock selectedStock = selectedStockOptional.get();
            for (PreorderGoods preorderGoods : preorderGoodsList) {
                if(preorderGoods.getPreorder_goods_sale().equals("Y")
                        && preorderGoods.getStock().getStock_num().equals(selectedStock.getStock_num())){
                    isPreorder = "Y";
                }
            }
            int maxSelectable = Math.min(3, selectedStock.getStock());
            int maxSelectableGoods = Math.min(5, selectedStock.getStock());
            model.addAttribute("maxSelectableGoods", maxSelectableGoods);
            model.addAttribute("maxSelectable", maxSelectable);
            model.addAttribute("isPreorder", isPreorder);
            model.addAttribute("selectedStock", selectedStock);
        }
        return "goodsdetail";
    }

    @PostMapping("/order")
    public String order(@Valid @ModelAttribute("stockDto") StockDto.RequestStockDto stockDto, BindingResult bindingResult, Model model) {
        model.addAttribute("stockDto", stockDto);

        if (bindingResult.hasErrors()) {
            /* 유효성 검사를 통과하지 못 한 필드와 메시지 핸들링 */
            Map<String, String> errorMap = new HashMap<>();

            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put("valid_"+error.getField(), error.getDefaultMessage());
                log.info("로그인 실패 ! error message : "+error.getDefaultMessage());
            }

            /* Model에 담아 view resolve */
            for(String key : errorMap.keySet()) {
                model.addAttribute(key, errorMap.get(key));
            }

            return "management";
        }

        LocalDate today = LocalDate.now();

        stockDto.setStock_sell_price(stockDto.getGoods().getGoods_buy_price());
        stockDto.setStock_discount(0);
        stockDto.setStock_place("창고");
        stockDto.setStock_expiration_date(today.plusWeeks(1));
        stockDto.setStock_date(today);

        stockService.stockJoin(stockDto);
        log.info("재고등록 성공");

        return "management";
    }

    @PostMapping("/stockUpdate")
    public String stockUpdate(@Valid @ModelAttribute stockUpdateForm form,
                              BindingResult bindingResult, Model model) {

        List<Stock> stockList = this.stockRepository.findAll();
        model.addAttribute("stockList", stockList);

        if (bindingResult.hasErrors()) {
            /* 유효성 검사를 통과하지 못 한 필드와 메시지 핸들링 */
            Map<String, String> errorMap = new HashMap<>();

            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put("valid_"+error.getField(), error.getDefaultMessage());
                log.info("재고변경 실패 ! error message : "+error.getDefaultMessage());
            }

            /* Model에 담아 view resolve */
            for(String key : errorMap.keySet()) {
                model.addAttribute(key, errorMap.get(key));
            }

            return "stockmanagement";
        }

        for (int i = 0; i < form.getSelectedItems().size(); i++){
            Optional<Goods> goodsOptional = goodsRepository.findById(form.getGoodsNum().get(i));
            if(goodsOptional.isPresent()) {
                Goods goods = goodsOptional.get();
                StockDto.RequestStockDto stockDto = StockDto.RequestStockDto.builder()
                        .stock_num(form.getSelectedItems().get(i))
                        .stock(form.getStockCount().get(i))
                        .stock_sell_price(form.getSellPrice().get(i))
                        .stock_discount(form.getDiscount().get(i))
                        .stock_place(form.getStockPlace().get(i))
                        .stock_expiration_date(LocalDate.parse(form.getStockExpDate().get(i)))
                        .stock_date(LocalDate.parse(form.getStockDate().get(i)))
                        .goods(goods)
                        .build();

                stockService.updateStock(stockDto);
            }
        }

        return "redirect:/stockmanagement";
    }

    @PostMapping("/stockDelete")
    public String stockDelete(@Valid @RequestParam("stock_num")List<Long> stock_num, Model model) {
        if(stock_num.isEmpty()) {
            return "redirect:/stockdelete";
        }else{
            for (Long aLong : stock_num) {
                stockRepository.deleteById(aLong);
            }

            return "redirect:/stockdelete";
        }
    }
}
