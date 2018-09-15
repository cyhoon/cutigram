package jeff.cutigram.model.response;

import java.util.Date;
import java.util.List;

public class BoardResponse {
    private int idx;
    private String content;
    private Date writeDate;
    private Date modifyDate;
    private WriterResponse writer;
    private List<LikeResponse> likes;
    private List<FileResponse> files;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(Date writeDate) {
        this.writeDate = writeDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public WriterResponse getWriter() {
        return writer;
    }

    public void setWriter(WriterResponse writer) {
        this.writer = writer;
    }

    public List<LikeResponse> getLikes() {
        return likes;
    }

    public void setLikes(List<LikeResponse> likes) {
        this.likes = likes;
    }

    public List<FileResponse> getFiles() {
        return files;
    }

    public void setFiles(List<FileResponse> files) {
        this.files = files;
    }
}
