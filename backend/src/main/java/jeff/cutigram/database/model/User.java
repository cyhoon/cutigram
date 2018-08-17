package jeff.cutigram.database.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(name = "display_name", nullable = false, length = 50)
    private String displayName;

    @Column(length = 200)
    private String introduce;

    @Column(length = 50)
    private String gender;

    @Column(name = "phone_number", length = 15, unique = true)
    private String phoneNumber;

    @Column(name = "photo_src", length = 300)
    private String photoSrc;

//    @OneToMany(mappedBy = "board")
//    private Board board;
//
//    @OneToMany(mappedBy = "board_like")
//    private BoardLike boardLike;
//
//    @OneToMany(mappedBy = "comment")
//    private Comment comment;
}
