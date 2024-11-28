package martproject.MartProject.domain.message;

import lombok.*;
import martproject.MartProject.domain.member.Member;

import java.time.LocalDate;

public class MessageDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class RequestMessageDto{
        private Long message_num;

        private Member member;

        private LocalDate message_send_date;

        private String message_check;

        private String message_content;

        private String message_name;

        public Message toEntity(){
            return Message.builder()
                    .message_num(message_num)
                    .member(member)
                    .message_name(message_name)
                    .message_content(message_content)
                    .message_check(message_check)
                    .message_send_date(message_send_date)
                    .build();
        }
    }
}
