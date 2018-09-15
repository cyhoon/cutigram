package jeff.cutigram.model.request;

import java.util.List;

public class PostRequest {
    List<FileRequest> files;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<FileRequest> getFiles() {
        return files;
    }

    public void setFiles(List<FileRequest> files) {
        this.files = files;
    }
}
