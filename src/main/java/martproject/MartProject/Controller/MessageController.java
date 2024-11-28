package martproject.MartProject.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import martproject.MartProject.domain.member.Member;
import martproject.MartProject.domain.message.Message;
import martproject.MartProject.domain.message.MessageDto;
import martproject.MartProject.repository.MemberRepository;
import martproject.MartProject.repository.MessageRepository;
import martproject.MartProject.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class MessageController {

    private final MemberRepository memberRepository;
    private final MessageService messageService;
    private final MessageRepository messageRepository;
    private final HttpServletRequest httpServletRequest;

    public MessageController(MemberRepository memberRepository, MessageService messageService, MessageRepository messageRepository, HttpServletRequest httpServletRequest) {
        this.memberRepository = memberRepository;
        this.messageService = messageService;
        this.messageRepository = messageRepository;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping("messagebox")
    public String messagebox(Model model) {
        HttpSession session = httpServletRequest.getSession();
        String id = (String) session.getAttribute("id");
        List<Message> messageListAll = messageRepository.findAll();
        List<Message> messageListN = new ArrayList<Message>();
        List<Message> messageListY = new ArrayList<Message>();
        for (Message message : messageListAll) {
            if(message.getMember().getId().equals(id)){
                if(message.getMessage_check().equals("N")){
                    messageListN.add(message);
                }else {
                    messageListY.add(message);
                }
            }
        }
        model.addAttribute("messageListN", messageListN);
        model.addAttribute("messageListY", messageListY);
        return "messagebox";
    }

    @GetMapping("messagesend")
    public String messagesend(Model model) {
        List<Message> messageListAll = messageRepository.findAll();
        List<Message> messageList = new ArrayList<Message>();
        for (Message message : messageListAll) {
            if(message.getMember().getId().equals("admin")){
                messageList.add(message);
            }
        }
        model.addAttribute("messageList", messageList);
        return "messagesend";
    }

    @PostMapping("messagesend")
    public String messagesend(@RequestParam("message_content") String message_content,
                              @RequestParam("message_name") String message_name) {

        if(message_content != null) {
            List<Member> memberlist = memberRepository.findAll();
            LocalDate today = LocalDate.now();

            for(Member member : memberlist) {
                MessageDto.RequestMessageDto messageDto = MessageDto.RequestMessageDto.builder()
                        .member(member)
                        .message_name(message_name)
                        .message_send_date(today)
                        .message_check("N")
                        .message_content(message_content)
                        .build();

                messageService.sendMessage(messageDto);
            }

            return "redirect:/messagesend";
        }else{
            return "messagesend";
        }
    }

    @GetMapping("messagedetail/{message_num}")
    public String messagedetail(@PathVariable("message_num") Long message_num, Model model){
        Optional<Message> selectedMessageOptional = messageRepository.findById(message_num);
        if(selectedMessageOptional.isPresent()){
            Message selectedMessage = selectedMessageOptional.get();
            model.addAttribute("selectedMessage", selectedMessage);
            if(selectedMessage.getMessage_check().equals("N")){
                MessageDto.RequestMessageDto messageDto = MessageDto.RequestMessageDto.builder()
                        .message_num(message_num)
                        .member(selectedMessage.getMember())
                        .message_name(selectedMessage.getMessage_name())
                        .message_send_date(selectedMessage.getMessage_send_date())
                        .message_content(selectedMessage.getMessage_content())
                        .message_check("Y")
                        .build();
                messageService.MessageCheck(messageDto);
            }
        }
        return "messagedetail";
    }
}
