package jeff.cutigram.model.request;

import java.util.List;

public class FilesRequest {
    List<FileRequest> files;

    public List<FileRequest> getFiles() {
        return files;
    }

    public void setFiles(List<FileRequest> files) {
        this.files = files;
    }
}
