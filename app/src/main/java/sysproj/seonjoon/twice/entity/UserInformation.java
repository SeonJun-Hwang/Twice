package sysproj.seonjoon.twice.entity;

public class UserInformation {

    private String id;
    private String password;
    private String email;

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
}
