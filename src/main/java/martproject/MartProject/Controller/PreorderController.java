package martproject.MartProject.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import martproject.MartProject.domain.goods.Goods;
import martproject.MartProject.domain.member.Member;
import martproject.MartProject.domain.preorderGoods.PreorderGoods;
import martproject.MartProject.domain.preorderGoods.PreorderGoodsDto;
import martproject.MartProject.domain.preorderInfo.PreorderInfo;
import martproject.MartProject.domain.preorderInfo.PreorderInfoDto;
import martproject.MartProject.domain.stock.Stock;
import martproject.MartProject.domain.stock.StockDto;
import martproject.MartProject.domain.stock.stockUpdateForm;
import martproject.MartProject.repository.*;
import martproject.MartProject.service.PreorderService;
import martproject.MartProject.service.StockService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

@Controller
@Slf4j
public class PreorderController {

    private final StockRepository stockRepository;
    private final PreorderService preorderService;
    private final PreorderGoodsRepository preorderGoodsRepository;
    private final GoodsRepository goodsRepository;
    private final StockService stockService;
    private final HttpServletRequest httpServletRequest;
    private final PreorderInfoRepository preorderInfoRepository;
    private final MemberRepository memberRepository;

    public PreorderController(StockRepository stockRepository, PreorderService preorderService, PreorderGoodsRepository preorderGoodsRepository, GoodsRepository goodsRepository, StockService stockService, HttpServletRequest httpServletRequest, PreorderInfoRepository preorderInfoRepository, MemberRepository memberRepository) {
        this.stockRepository = stockRepository;
        this.preorderService = preorderService;
        this.preorderGoodsRepository = preorderGoodsRepository;
        this.goodsRepository = goodsRepository;
        this.stockService = stockService;
        this.httpServletRequest = httpServletRequest;
        this.preorderInfoRepository = preorderInfoRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/preorder")
    public String preorder(Model model) {
        List<Stock> stockListAll = stockRepository.findAll();
        List<PreorderGoods> preorderGoodsListAll = preorderGoodsRepository.findAll();
        List<PreorderInfo> preorderInfoList = preorderInfoRepository.findAll();
        List<Stock> ExpCloseStockList = new ArrayList<Stock>();
        List<Stock> stockList =new ArrayList<Stock>();
        LocalDate today = LocalDate.now();

        for (Stock stock : stockListAll) {
            boolean isPreorderGoods = false;

            for (PreorderGoods preorderGoods : preorderGoodsListAll) {
                if (preorderGoods.getStock().getStock_num().equals(stock.getStock_num())) {
                    isPreorderGoods = true;
                    stockList.add(stock);
                    break;
                }
            }

            if (!isPreorderGoods && stock.getStock_expiration_date().minusDays(3).isBefore(today)) {
                ExpCloseStockList.add(stock);
            }
        }
        model.addAttribute("ExpCloseStockList", ExpCloseStockList);
        model.addAttribute("stockList", stockList);
        model.addAttribute("preorderGoodsList", preorderGoodsListAll);
        model.addAttribute("preorderInfoList", preorderInfoList);

        return "preorder";
    }

    @PostMapping("/preorderGoodsSelect")
    public String preorderGoodsSelect(@RequestParam("stock_num")List<Long> stockNumList , Model model) {
        List<Stock> stockListAll = stockRepository.findAll();
        for (Stock stock : stockListAll) {
            for (Long stock_num : stockNumList) {
                if (stock.getStock_num().equals(stock_num)) {
                    PreorderGoodsDto.RequestPreorderGoodsDto preorderGoodsDto = PreorderGoodsDto.RequestPreorderGoodsDto.builder()
                            .preorder_goods_sale("N")
                            .stock(stock)
                            .build();

                    preorderService.RegistPreorderStock(preorderGoodsDto);
                }
            }
        }
        return "redirect:/preorder";
    }

    @PostMapping("/discountUpdate")
    public String stockUpdate(@Valid @ModelAttribute stockUpdateForm form,
                              BindingResult bindingResult, Model model) {

        List<Stock> stockList = this.stockRepository.findAll();
        model.addAttribute("stockList", stockList);

        if (bindingResult.hasErrors()) {
            /* 유효성 검사를 통과하지 못 한 필드와 메시지 핸들링 */
            Map<String, String> errorMap = new HashMap<>();

            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put("valid_"+error.getField(), error.getDefaultMessage());
                log.info("할인율변경 실패 ! error message : {}", error.getDefaultMessage());
            }

            /* Model 담아 view resolve */
            for(String key : errorMap.keySet()) {
                model.addAttribute(key, errorMap.get(key));
            }

            return "preorder";
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

        return "redirect:/preorder";
    }

    @PostMapping("/preorderStart")
    public String preorderStart(@RequestParam("stock_num")Long stock_num , Model model) {
        log.info(String.valueOf(stock_num));
        List<PreorderGoods> preorderGoodsList = preorderGoodsRepository.findAll();
        for (PreorderGoods preorderGoods : preorderGoodsList) {
            if(preorderGoods.getStock().getStock_num().equals(stock_num)){
                PreorderGoodsDto.RequestPreorderGoodsDto preorderGoodsDto = PreorderGoodsDto.RequestPreorderGoodsDto.builder()
                        .preorder_goods_sale("Y")
                        .stock(preorderGoods.getStock())
                        .preorder_goods_num(preorderGoods.getPreorder_goods_num())
                        .build();

                preorderService.RegistPreorderStock(preorderGoodsDto);
            }
        }

        return "redirect:/preorder";
    }

    @PostMapping("/preorderGoods")
    public String preorderGoods(@RequestParam("stock_num")Long stock_num,
                           @RequestParam("buyCount") int buyCount,
                           HttpSession session, Model model) {

        String id = (String) session.getAttribute("id");
        if (id == null) {
            return "redirect:/";
        }

        Optional<Member> optionalFindMember = memberRepository.findById(id);
        if (optionalFindMember.isEmpty()) {
            return "redirect:/";
        }
        Member findMember = optionalFindMember.get();

        Optional<Stock> optionalFindStock = stockRepository.findById(stock_num);
        if (optionalFindStock.isEmpty()) {
            return "redirect:/";
        }
        Stock findStock = optionalFindStock.get();

        LocalDate today = LocalDate.now();

        PreorderInfoDto.RequestPreorderInfoDto preorderInfoDto = PreorderInfoDto.RequestPreorderInfoDto.builder()
                .stock(findStock)
                .member(findMember)
                .preorder_date(today)
                .buy_count(buyCount)
                .ispreorder("N")
                .build();

        preorderService.RegistPreorder(preorderInfoDto);

        return "redirect:/";
    }

    @PostMapping("preorderDelete")
    public String preorderDelete(@RequestParam("preorder_info_num")List<Long> preorder_info_num , Model model) {
        if(preorder_info_num.isEmpty()){
            return "redirect:/preorder";
        }else{
            for (Long aLong : preorder_info_num) {
                preorderInfoRepository.deleteById(aLong);
            }

            return "redirect:/preorder";
        }
    }
}
