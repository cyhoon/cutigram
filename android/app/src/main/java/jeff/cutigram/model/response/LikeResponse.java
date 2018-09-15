package jeff.cutigram.model.response;

import java.util.Date;

public class LikeResponse {
    private int idx;
    private Date pushDate;
    private WriterResponse likeUser;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public Date getPushDate() {
        return pushDate;
    }

    public void setPushDate(Date pushDate) {
        this.pushDate = pushDate;
    }

    public WriterResponse getLikeUser() {
        return likeUser;
    }

    public void setLikeUser(WriterResponse likeUser) {
        this.likeUser = likeUser;
    }
}
