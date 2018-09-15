package jeff.cutigram.model.request;

public class BoardModifyRequest {
    private int idx;
    private String content;

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
}
