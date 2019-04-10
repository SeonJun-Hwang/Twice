package sysproj.seonjoon.twice.entity;

import java.util.ArrayList;
import java.util.Date;

public abstract class Post{

    private Date createDate;
    private String user;
    private String contentText;
    private String profileImage;
    private PostRFS postRFS;
    private ArrayList<PostMedia> imageList;
    private ArrayList<PostExtendInfo> extendInfo;

    protected Post(Builder b){
        this.createDate = b.createDate;
        this.user = b.user;
        this.contentText = b. contentText;
        this.profileImage = b.profileImage;
        this.postRFS = b.postRFS;
        this.imageList = b.imageList;
        this.extendInfo = b.extendInfo;
    }

    // Getters
    public String getUser() {
        return user;
    }

    public String getContentText() {
        return contentText;
    }

    public ArrayList<PostMedia> getImageList() {
        return imageList;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public PostRFS getRFS() {
        return postRFS;
    }

    public ArrayList<PostExtendInfo> getExtendInfo() {
        return extendInfo;
    }

    // Method
    public int getViewType() {
        return imageList == null ? 1 : 2;
    }

    public static ArrayList<Post> mergePost(ArrayList<Post> src1, ArrayList<Post> src2) {

        ArrayList<Post> result = new ArrayList<>();

        if (src1 != null || src2 != null)
        {
            if (src1 == null)
                return src2;
            else if (src2 == null)
                return src1;

            int leftPivot = 0, rightPivot = 0;
            int leftLength = src1.size(), rightLength = src2.size();

            while (leftPivot < leftLength && rightPivot < rightLength) {
                Post left = src1.get(leftPivot);
                Post right = src2.get(rightPivot);

                if (left.getCreateDate().compareTo(right.getCreateDate()) > 0) {
                    result.add(left);
                    leftPivot++;
                } else {
                    result.add(right);
                    rightPivot++;
                }
            }

            while (leftPivot < leftLength)
                result.add(src1.get(leftPivot++));

            while (rightPivot < rightLength)
                result.add(src2.get(rightPivot++));
        }

        return result;
    }

    public abstract static class Builder {
        private Date createDate;
        private String user;
        private String contentText;
        private String profileImage = null;
        private PostRFS postRFS;
        private ArrayList<PostMedia> imageList = null;
        private ArrayList<PostExtendInfo> extendInfo = null;

        public Builder(String user, String contentText, String createTime, PostRFS postRFS){
            this.user = convertUser(user);
            this.contentText = convertText(contentText);
            this.createDate = convertDate(createTime);
            this.postRFS = postRFS;
        }

        public Builder profileImage(String str){
            profileImage = convertProfileImageUrl(str);
            return this;
        }

        public Builder imageList(ArrayList imageList){
            this.imageList = imageList;
            return this;
        }

        public Builder extendInfo(ArrayList extendInfo){
            this.extendInfo = extendInfo;
            return this;
        }

        private String convertUser(String str) {
            return str;
        }

        private String convertText(String str) {
            return str;
        }

        private String convertProfileImageUrl(String str) {
            return str;
        }

        protected abstract Date convertDate(String str);

        public abstract Post build();
    }
}
