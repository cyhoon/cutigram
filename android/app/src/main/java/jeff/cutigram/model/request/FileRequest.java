package jeff.cutigram.model.request;

public class FileRequest {
    private String fileType = "image";
    private String fileSrc;

    public FileRequest(String fileSrc) {
        this.fileSrc = fileSrc;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileSrc() {
        return fileSrc;
    }

    public void setFileSrc(String fileSrc) {
        this.fileSrc = fileSrc;
    }
}
