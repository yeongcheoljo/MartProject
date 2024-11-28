package martproject.MartProject.Controller;

import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import martproject.MartProject.domain.member.Member;
import martproject.MartProject.domain.member.MemberDto;
import martproject.MartProject.domain.preorderGoods.PreorderGoods;
import martproject.MartProject.domain.preorderInfo.PreorderInfo;
import martproject.MartProject.domain.preorderInfo.PreorderInfoDto;
import martproject.MartProject.domain.stock.Stock;
import martproject.MartProject.repository.MemberRepository;
import martproject.MartProject.repository.PreorderGoodsRepository;
import martproject.MartProject.repository.PreorderInfoRepository;
import martproject.MartProject.repository.StockRepository;
import martproject.MartProject.service.LoginService;
import martproject.MartProject.service.MemberService;
import martproject.MartProject.service.PreorderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Slf4j
@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private final MemberService memberService;
    private final StockRepository stockRepository;
    private final PreorderGoodsRepository preorderGoodsRepository;
    private final MemberRepository memberRepository;
    private final PreorderInfoRepository preorderInfoRepository;
    private final PreorderService preorderService;

    @Autowired
    public MainController(MemberService memberService, StockRepository stockRepository, PreorderGoodsRepository preorderGoodsRepository, MemberRepository memberRepository, PreorderInfoRepository preorderInfoRepository, PreorderService preorderService){
        this.memberService = memberService;
        this.stockRepository = stockRepository;
        this.preorderGoodsRepository = preorderGoodsRepository;
        this.memberRepository = memberRepository;
        this.preorderInfoRepository = preorderInfoRepository;
        this.preorderService = preorderService;
    }

    //메인페이지 이동
    @GetMapping("/")
    public String main(Model model){
        List<Stock> stockListAll = stockRepository.findAll();
        List<Stock> stockList = new ArrayList<>();
        List<PreorderGoods> preorderGoodsList = preorderGoodsRepository.findAll();
        List<Stock> preorderList = new ArrayList<>();
        for (Stock stock : stockListAll) {
            for (PreorderGoods preorderGoods : preorderGoodsList) {
                if(preorderGoods.getPreorder_goods_sale().equals("Y")
                        && preorderGoods.getStock().getStock_num().equals(stock.getStock_num())){
                    preorderList.add(stock);
                }else{
                    stockList.add(stock);
                }
            }
        }

        model.addAttribute("stockList", stockList);
        model.addAttribute("preorderList", preorderList);
        return "main";
    }

    //회원가입페이지 이동
    @GetMapping("join")
    public String signup(Model model){
        model.addAttribute("memberDto", new MemberDto.RequestMemberDto());
        return "join";
    }

    //회원가입
    @PostMapping("/joinNew")
    public String joinNew(@Valid MemberDto.RequestMemberDto memberDto, BindingResult bindingResult, Model model){
        model.addAttribute("memberDto", memberDto);

        if(bindingResult.hasErrors()){

            /* 유효성 검사를 통과하지 못 한 필드와 메시지 핸들링 */
            Map<String, String> errorMap = new HashMap<>();

            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put("valid_"+error.getField(), error.getDefaultMessage());
                log.info("회원가입 실패 ! error message : "+error.getDefaultMessage());
            }

            /* Model에 담아 view resolve */
            for(String key : errorMap.keySet()) {
                model.addAttribute(key, errorMap.get(key));
            }

            /* 회원가입 페이지로 리턴 */
            return "join";
        }

        //회원가입 성공 시
        memberService.memberJoin(memberDto);
        log.info("회원가입 성공");

        return "redirect:/";
    }

    //마이페이지로 이동
    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model){
        Optional<Member> optionalMember = memberRepository.findById(session.getAttribute("id").toString());
        if(optionalMember.isPresent()){
            Member member = optionalMember.get();
            model.addAttribute("member", member);
        }

        List<PreorderInfo> preorderInfoList = preorderInfoRepository.findAll();
        List<PreorderInfo> myPreorderInfoList = new ArrayList<>();
        for(PreorderInfo preorderInfo : preorderInfoList){
            if(preorderInfo.getMember().getId().equals(session.getAttribute("id").toString())) {
                myPreorderInfoList.add(preorderInfo);
            }
        }
        model.addAttribute("myPreorderInfoList", myPreorderInfoList);

        return "mypage";
    }

    //상품예약
    @PostMapping("/buyPreorder")
    public String buyPreorder(@RequestParam("preorder_info_num")Long preorder_info_num , Model model){
        Optional<PreorderInfo> optionalPreorderInfo = preorderInfoRepository.findById(preorder_info_num);
        if(optionalPreorderInfo.isPresent()) {
            PreorderInfo preorderInfo = optionalPreorderInfo.get();

            PreorderInfoDto.RequestPreorderInfoDto preorderInfoDto = PreorderInfoDto.RequestPreorderInfoDto.builder()
                    .preorder_info_num(preorderInfo.getPreorder_info_num())
                    .preorder_date(preorderInfo.getPreorder_date())
                    .buy_count(preorderInfo.getBuy_count())
                    .member(preorderInfo.getMember())
                    .stock(preorderInfo.getStock())
                    .ispreorder("Y")
                    .build();

            preorderService.RegistPreorder(preorderInfoDto);
        }

        return "redirect:/mypage";
    }
}
