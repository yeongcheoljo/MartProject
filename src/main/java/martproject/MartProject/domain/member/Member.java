package martproject.MartProject.domain.member;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "member")
public class Member{

    @Id
    @Column(name = "member_id", unique = true, length = 40, nullable = false)
    private String id;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 20, nullable = false)
    private String phonenum;

    @Builder
    public Member(String id, String password, String email, String address, String phonenum){
        this.id = id;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phonenum = phonenum;
    }
}
