package martproject.MartProject.service;

import lombok.extern.slf4j.Slf4j;
import martproject.MartProject.domain.member.Member;
import martproject.MartProject.domain.member.MemberDto;
import martproject.MartProject.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String memberJoin(MemberDto.RequestMemberDto dto){
        Member member = dto.toEntity();
        memberRepository.save(member);
        log.info("DB에 회원 저장 성공");

        return member.getId();
    }
}
