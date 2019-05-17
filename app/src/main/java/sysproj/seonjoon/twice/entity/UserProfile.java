package sysproj.seonjoon.twice.entity;

public class UserProfile {

    private String name;
    private String profileImage;
    private String email;

    private UserProfile() {
    }

    private UserProfile(Builder b) {
        name = b.name;
        profileImage = b.profileImage;
        email = b.email;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "name=\'" + name + '\'' +
                "email=\'" + email + "\'" +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }

    public static class Builder {

        private String name;
        private String profileImage = null;
        private String email = null;

        public Builder(String name) {
            this.name = name;
        }

        public Builder profileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public Builder userEmail(String email){
            this.email = email;
            return this;
        }

        public UserProfile build() {
            return new UserProfile(this);
        }
    }

}
