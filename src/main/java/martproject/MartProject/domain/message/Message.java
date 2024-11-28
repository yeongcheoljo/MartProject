package martproject.MartProject.domain.message;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import martproject.MartProject.domain.member.Member;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "message_num", unique = true, nullable = false)
    private Long message_num;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Member.class)
    @JoinColumn(name = "member_id", updatable = false, nullable = false)
    private Member member;

    @Column(nullable = false)
    private String message_name;

    @Column(nullable = false)
    private LocalDate message_send_date;

    @Column(nullable = false)
    private String message_check;

    @Column(nullable = false)
    private String message_content;

    @Builder
    public Message(Long message_num, Member member, LocalDate message_send_date, String message_check, String message_content, String message_name) {
        this.message_num = message_num;
        this.member = member;
        this.message_name = message_name;
        this.message_send_date = message_send_date;
        this.message_check = message_check;
        this.message_content = message_content;
    }
}
