package martproject.MartProject.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import martproject.MartProject.domain.member.Member;
import martproject.MartProject.domain.member.MemberDto;
import martproject.MartProject.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private final MemberRepository memberRepository;
    private final HttpServletRequest httpServletRequest;

    public LoginService(MemberRepository memberRepository, HttpServletRequest httpServletRequest) {
        this.memberRepository = memberRepository;
        this.httpServletRequest = httpServletRequest;
    }

    public String login(MemberDto.RequestMemberDto dto){
        Optional<Member> findMember = memberRepository.findById(dto.getId());

        if(findMember.isPresent()){
            Member member = findMember.get();

            if(member.getPassword().equals(dto.getPassword())){
                //아이디 비밀번호 일치시
                HttpSession session = httpServletRequest.getSession(true);
                session.setAttribute("id", member.getId());
                session.setMaxInactiveInterval(1800);
                return "success";
            } else {
                //비밀번호가 일치하지 않을시 널값 리턴
                return null;
            }
        }else {
            //id가 존재하지 않을 경우 널값 리턴
            return null;
        }
    }
}
