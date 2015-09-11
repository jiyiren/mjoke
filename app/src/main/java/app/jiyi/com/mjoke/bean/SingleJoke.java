package app.jiyi.com.mjoke.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JIYI on 2015/8/27.
 */
public class SingleJoke implements Parcelable{
    public static final String TYPE_JOKE="0";//类型1，和mysql里默认的一样为0
    public static final String TYPE_GIRL="1";//类型2
    public static final String TYPE_OTHERS="3";//类型3

    private String joke_id;

    private String user_id;
    private String username;
    private String sex;

    private String type;
    private String createtime;
    private String content;
    private String imgurl;
    private String ishasing;
    private String share_count;
    private String collect_count;
    private String look_count;

    private int iscollect;
    private int iszan;//是否是赞

    public SingleJoke(){

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getIshasing() {
        return ishasing;
    }

    public void setIshasing(String ishasing) {
        this.ishasing = ishasing;
    }

    public String getShare_count() {
        return share_count;
    }

    public void setShare_count(String share_count) {
        this.share_count = share_count;
    }

    public String getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(String collect_count) {
        this.collect_count = collect_count;
    }

    public String getLook_count() {
        return look_count;
    }

    public void setLook_count(String look_count) {
        this.look_count = look_count;
    }

    public int getIscollect() {
        return iscollect;
    }

    public void setIscollect(int iscollect) {
        this.iscollect = iscollect;
    }

    public int getIszan() {
        return iszan;
    }

    public void setIszan(int iszan) {
        this.iszan = iszan;
    }

    @Override
    public String toString() {
        return "SingleJoke [joke_id=" + joke_id + ", user_id=" + user_id
                + ", type=" + type + ", createtime=" + createtime
                + ", content=" + content + ", imgurl=" + imgurl + ", ishasing="
                + ishasing + ", share_count=" + share_count
                + ", collect_count=" + collect_count + ", look_count="
                + look_count + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(joke_id);
        dest.writeString(user_id);
        dest.writeString(username);
        dest.writeString(sex);
        dest.writeString(type);
        dest.writeString(createtime);
        dest.writeString(content);
        dest.writeString(imgurl);
        dest.writeString(ishasing);
        dest.writeString(share_count);
        dest.writeString(collect_count);
        dest.writeString(look_count);
        dest.writeInt(iscollect);
        dest.writeInt(iszan);
    }

    public static final Parcelable.Creator<SingleJoke> CREATOR=new Creator<SingleJoke>() {
        @Override
        public SingleJoke createFromParcel(Parcel source) {
            SingleJoke sj=new SingleJoke();
            sj.setJoke_id(source.readString());
            sj.setUser_id(source.readString());
            sj.setUsername(source.readString());
            sj.setSex(source.readString());
            sj.setType(source.readString());
            sj.setCreatetime(source.readString());
            sj.setContent(source.readString());
            sj.setImgurl(source.readString());
            sj.setIshasing(source.readString());
            sj.setShare_count(source.readString());
            sj.setCollect_count(source.readString());
            sj.setLook_count(source.readString());
            sj.setIscollect(source.readInt());
            sj.setIszan(source.readInt());
            return sj;
        }

        @Override
        public SingleJoke[] newArray(int size) {
            return new SingleJoke[size];
        }
    };
}
