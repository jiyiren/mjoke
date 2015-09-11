package app.jiyi.com.mjoke.bean;

/**
 * Created by JIYI on 2015/9/6.
 */
public class UserLookBean {
    private String joke_id;
    private String user_id;
    public UserLookBean(){

    }

    public UserLookBean(String joke_id, String user_id) {
        this.joke_id = joke_id;
        this.user_id = user_id;
    }

    public String getJoke_id() {
        return joke_id;
    }

    public void setJoke_id(String joke_id) {
        this.joke_id = joke_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
