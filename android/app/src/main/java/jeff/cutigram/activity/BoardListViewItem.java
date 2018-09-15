package jeff.cutigram.activity;

import java.util.List;

import jeff.cutigram.model.response.FileResponse;
import jeff.cutigram.model.response.LikeResponse;
import jeff.cutigram.model.response.WriterResponse;

public class BoardListViewItem {
    private int idx;
    private String content;
    private WriterResponse writer;
    private List<FileResponse> fileResponseList;
    private List<LikeResponse> likes;

    public BoardListViewItem(int idx, WriterResponse writer, String content, List<FileResponse> fileResponseList, List<LikeResponse> likes) {
        this.idx = idx;
        this.writer = writer;
        this.content = content;
        this.fileResponseList = fileResponseList;
        this.likes = likes;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public List<FileResponse> getFileResponseList() {
        return fileResponseList;
    }

    public void setFileResponseList(List<FileResponse> fileResponseList) {
        this.fileResponseList = fileResponseList;
    }

    public WriterResponse getWriter() {
        return writer;
    }

    public void setWriter(WriterResponse writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<LikeResponse> getLikes() {
        return likes;
    }

    public void setLikes(List<LikeResponse> likes) {
        this.likes = likes;
    }
}
