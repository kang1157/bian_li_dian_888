import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

   String userName;//用户名
   String password;//密码

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
