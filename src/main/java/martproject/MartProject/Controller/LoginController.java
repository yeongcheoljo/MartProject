package martproject.MartProject.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import martproject.MartProject.domain.member.MemberDto;
import martproject.MartProject.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class LoginController {

    private final LoginService loginService;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public LoginController(LoginService loginService, HttpServletRequest httpServletRequest) {
        this.loginService = loginService;
        this.httpServletRequest = httpServletRequest;
    }

    //로그인페이지로 이동
    @GetMapping("login")
    public String login(Model model){
        model.addAttribute("memberDto", new MemberDto.RequestMemberDto());
        return "login";
    }

    //로그아웃
    @GetMapping("logout")
    public String logout(){
        HttpSession session = httpServletRequest.getSession(false);
        session.invalidate();
        return "redirect:/";
    }

    //로그인
    @PostMapping("/login")
    public String login(@Valid MemberDto.RequestMemberDto memberDto, BindingResult bindingResult, Model model){
        model.addAttribute("memberDto", memberDto);

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
            return "login";
        }

        String result = loginService.login(memberDto);
        if(result == null){
            result = "fail";
        }

        if(result.equals("success")){
            log.info("로그인 성공");
        }else{
            log.info("로그인 실패");
            return "login";
        }

        return "redirect:/";
    }
}
