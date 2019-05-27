package sysproj.seonjoon.twice.entity;

public class BookPostVO {

    private long id;
    private boolean checkFacebook, checkInstargram, checkTwitter;
    private long date;
    private String message;

    public BookPostVO(long id, boolean cf, boolean ci, boolean ct, long date, String message) {
        this.id = id;
        this.checkFacebook = cf;
        this.checkInstargram = ci;
        this.checkTwitter = ct;
        this.date = date;
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public String getDateToString() {
        String min = "" + date % 100;
        String hour = "" + (date % 10000) / 100;
        String day = "" + (date % 1000000) / 10000;
        String month = "" + (date % 100000000) / 1000000;
        String year = "" + (date / 100000000);

        return year + "-" + month + "-" + day + " " + hour + ":" + min;
    }

    public boolean isCheckFacebook() {
        return checkFacebook;
    }

    public boolean isCheckInstargram() {
        return checkInstargram;
    }

    public boolean isCheckTwitter() {
        return checkTwitter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }
}
