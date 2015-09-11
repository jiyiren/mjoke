package app.jiyi.com.mjoke.bean;

/**
 * Created by JIYI on 2015/9/8.
 */
public class OneCommentBean {
    private String com_id;
    private String joke_id;
    private String user_id;
    private String user_name;
    private String com_content;
    private String com_lou;
    private String com_time;

    public OneCommentBean(){

    }

    public String getJoke_id() {
        return joke_id;
    }

    public void setJoke_id(String joke_id) {
        this.joke_id = joke_id;
    }

    public String getCom_lou() {
        return com_lou;
    }

    public void setCom_lou(String com_lou) {
        this.com_lou = com_lou;
    }

    public String getCom_content() {
        return com_content;
    }

    public void setCom_content(String com_content) {
        this.com_content = com_content;
    }

    public String getCom_id() {
        return com_id;
    }

    public void setCom_id(String com_id) {
        this.com_id = com_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCom_time() {
        return com_time;
    }

    public void setCom_time(String com_time) {
        this.com_time = com_time;
    }
}
