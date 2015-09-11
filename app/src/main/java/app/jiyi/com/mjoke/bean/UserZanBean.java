package app.jiyi.com.mjoke.bean;

/**
 * Created by JIYI on 2015/9/6.
 */
public class UserZanBean {
    //
    public static final String TYPE_BAD="0";
    public static final String TYEP_ZAN="1";
    private String joke_id;
    private String user_id;
    private String iszan;
    public UserZanBean(){

    }

    public UserZanBean(String joke_id, String user_id, String iszan) {
        this.joke_id = joke_id;
        this.user_id = user_id;
        this.iszan = iszan;
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

    public String getIszan() {
        return iszan;
    }

    public void setIszan(String iszan) {
        this.iszan = iszan;
    }
}
