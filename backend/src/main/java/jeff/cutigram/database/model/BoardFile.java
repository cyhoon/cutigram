package jeff.cutigram.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "board_file")
@Getter
@Setter
public class BoardFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idx;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_src", nullable = false)
    private String fileSrc;

    @ManyToOne
    @JoinColumn(name = "board_idx", referencedColumnName = "idx", nullable = false, updatable = false)
    private Board board;
}
