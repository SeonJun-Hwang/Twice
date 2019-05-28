package sysproj.seonjoon.twice.entity;

public class TwiceTime {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public TwiceTime(Builder b) {
        this.year = b.year;
        this.month = b.month;
        this.day = b.day;
        this.hour = b.hour;
        this.minute = b.minute;
    }

    public int getMonth() {
        return year;
    }

    public int getYear() {
        return year;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    @Override
    public String toString() {
        return year + "-" + month + "-" + day + " " + hour + ":" + minute;
    }

    public String toFormatString(){
        return String.format("%04d%02d%02d%02d%02d",year,month,day,hour,minute);
    }

    public static class Builder {

        private int year;
        private int month;
        private int day;
        private int hour = 0;
        private int minute = 0;

        public Builder(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        public Builder Hour(int hour) {
            this.hour = hour;
            return this;
        }

        public Builder Minute(int minute) {
            this.minute = minute;
            return this;
        }

        public TwiceTime build() {
            return new TwiceTime(this);
        }
    }

}
