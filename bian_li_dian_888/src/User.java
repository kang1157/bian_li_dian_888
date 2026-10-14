import java.io.Serializable;

public class User implements Serializable {
   String userName;//用户名
   String password;//密码

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
