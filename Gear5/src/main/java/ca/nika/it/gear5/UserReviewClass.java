package ca.nika.it.gear5;

public class UserReviewClass {

    String overall, userName, userPhone, userEmail, userComment;

    public UserReviewClass(){

    }

    public UserReviewClass(String overall, String userName, String userPhone, String userEmail, String userComment) {
        this.overall = overall;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userComment = userComment;
    }

    public String getOverall() {
        return overall;
    }

    public void setOverall(String overall) {
        this.overall = overall;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }
}
