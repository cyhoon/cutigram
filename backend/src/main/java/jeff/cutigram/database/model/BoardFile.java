package jeff.cutigram.database.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "board_file")
@Getter
@Setter
@NoArgsConstructor
public class BoardFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_src", nullable = false)
    private String fileSrc;

    @Builder
    public BoardFile(String fileType, String fileSrc, Board board) {
        this.fileType = fileType;
        this.fileSrc = fileSrc;
        this.board = board;
    }

    @ManyToOne
    @JsonBackReference
//    @JoinColumn(name = "board_idx", referencedColumnName = "idx", nullable = false, updatable = false)
    private Board board;
}
