package jeff.cutigram.database.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idx;

    @Column(nullable = false)
    private String content;

    @Column(name = "write_date")
    private LocalDateTime writeDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_idx", referencedColumnName = "idx", nullable = false, updatable = false)
    private Board board;
}
