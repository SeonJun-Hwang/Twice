package sysproj.seonjoon.twice.entity;

public class UserProfile {

    private String name;
    private String profileImage;

    private UserProfile() {}

    private UserProfile(Builder b){
        name = b.name;
        profileImage = b.profileImage;
    }

    public String getName () { return name ; }
    public String getProfileImage() {return profileImage ; }

    public static class Builder{

        private String name;
        private String profileImage = null;

        public Builder(String name){
            this.name = name;
        }

        public Builder profileImage(String profileImage){
            this.profileImage = profileImage;
            return this;
        }

        public UserProfile build(){
            return new UserProfile(this);
        }
    }

}
