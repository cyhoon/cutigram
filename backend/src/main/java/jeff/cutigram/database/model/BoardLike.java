package jeff.cutigram.database.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BoardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idx;

    @Column(name = "push_date")
    @CreationTimestamp
    private LocalDateTime pushDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_idx", referencedColumnName = "idx", nullable = false, updatable = false)
    private Board board;

    @Builder
    public BoardLike(Board board, User user) {
        this.board = board;
        this.user = user;
    }
}
