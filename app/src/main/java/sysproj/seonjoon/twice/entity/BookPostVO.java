package sysproj.seonjoon.twice.entity;

public class BookPostVO {
    private boolean chekFacebook, chekInstargram, chekTwitter;
    private String date, message;
    private boolean extend;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isChekFacebook() {
        return chekFacebook;
    }

    public void setChekFacebook(boolean chekFacebook) {
        this.chekFacebook = chekFacebook;
    }

    public boolean isChekInstargram() {
        return chekInstargram;
    }

    public void setChekInstargram(boolean chekInstargram) {
        this.chekInstargram = chekInstargram;
    }

    public boolean isChekTwitter() {
        return chekTwitter;
    }

    public void setChekTwitter(boolean chekTwitter) {
        this.chekTwitter = chekTwitter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isExtend() {
        return extend;
    }

    public void setExtend(boolean extend) {
        this.extend = extend;
    }
}
