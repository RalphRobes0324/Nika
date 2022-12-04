// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5.LoginSetup;

public class UserClass {
    String username,password, email, phone, fullName;
    int currency, topScore, topScore2, topScore3, topScore4, topScore5;

    public UserClass() {

    }




    public UserClass(String username, String password, String email, int currency, int topScore, String phone, String fullName,
                     int topScore2, int topScore3, int topScore4, int topScore5) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.currency = currency;
        this.topScore = topScore;
        this.phone = phone;
        this.fullName = fullName;
        this.topScore2 = topScore2;
        this.topScore3 = topScore3;
        this.topScore4 = topScore4;
        this.topScore5 = topScore5;
    }

    public int getTopScore2() {
        return topScore2;
    }

    public void setTopScore2(int topScore2) {
        this.topScore2 = topScore2;
    }

    public int getTopScore3() {
        return topScore3;
    }

    public void setTopScore3(int topScore3) {
        this.topScore3 = topScore3;
    }

    public int getTopScore4() {
        return topScore4;
    }

    public void setTopScore4(int topScore4) {
        this.topScore4 = topScore4;
    }

    public int getTopScore5() {
        return topScore5;
    }

    public void setTopScore5(int topScore5) {
        this.topScore5 = topScore5;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getTopScore() {
        return topScore;
    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }
}
