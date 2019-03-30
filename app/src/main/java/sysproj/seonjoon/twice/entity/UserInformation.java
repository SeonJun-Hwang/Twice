package sysproj.seonjoon.twice.entity;

import com.facebook.AccessToken;

public class UserInformation {

    private String id;
    private String password;
    private String email;
    private AccessToken facebookToken;

    public UserInformation(String id, String password, String email) {
        this.id = id;
        this.password = password;
        this.email = email;
    }

    public UserInformation getUserInfo() {
        return this;
    }

    public String getId() {
        return this.id;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setFacebookToken(AccessToken fbToken){
        this.facebookToken = fbToken;
    }

    public AccessToken getFacebookToken() {
        return facebookToken;
    }
}
