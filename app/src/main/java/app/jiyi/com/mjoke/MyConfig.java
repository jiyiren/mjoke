package app.jiyi.com.mjoke;

/**
 * Created by JIYI on 2015/8/21.
 */
public class MyConfig {

    public static final String DOWNLOAD_URL="http://jiyiren.github.io";//关于界面的分享应用用到下载app的连接
    public static final String CURRENT_BANBEN="当前版本1.0.0";//当前版本号
    public static final int CURRENT_BANBEN_NUM=1;//当前版本,可以用以后的版本大于此，升级

    public static final String SHARE_WEIXIN_TEXT_HOUZHUI="(--来自mjoke)";
    //请求的url地址
//    public static final String SERVER_IP="http://10.60.41.171:8088";//服务器地址
//    public static final String BASE_PROJECT=SERVER_IP+"/joke";//项目基地址
    public static final String BASE_PROJECT="http://mjoke.cn-hangzhou.aliapp.com";//项目基地址

    public static final String BASE_SERVLET=BASE_PROJECT+"/servlet";//Servlet基地址
    public static final String BASE_IMG=BASE_PROJECT+"/images";//图片文件保存的基地址
    public static final String BASE_IMG_CONTENT=BASE_PROJECT+"/imgcontent";//内容图片文件保存的基地址

    /*Servlet*/
    public static final String URL_REGISTER=BASE_SERVLET+"/UserRegister";
    public static final String URL_LOGIN=BASE_SERVLET+"/UserLogin";
    public static final String URL_UPLOAD_IMG=BASE_SERVLET+"/ReceiveUserPic";
    public static final String URL_MODIFY_USERINFO=BASE_SERVLET+"/ModifyUserInfo";
    public static final String URL_GETJOKES=BASE_SERVLET+"/GetJokes";
    public static final String URL_GETUSERINFO=BASE_SERVLET+"/GetInfoById";
    public static final String URL_PUBLISHJOKE=BASE_SERVLET+"/PublishJoke";//发表图文消息
    public static final String URL_PUBLISHJOKE_NOIMG=BASE_SERVLET+"/PublishNoImgJoke";//发表图文消息
    public static final String URL_USERCOLLECT=BASE_SERVLET+"/UserCollect";//收藏
    public static final String URL_USERLOOK=BASE_SERVLET+"/UserLook";//查看
    public static final String URL_GETUSERCOLLECT=BASE_SERVLET+"/GetUserCollect";//收藏
    public static final String URL_GETUSER_ABOUT_JOKE=BASE_SERVLET+"/GetUserAboutJokes";//获取所有用户相关的jokes
    public static final String URL_SENDQUES=BASE_SERVLET+"/Question";//问题反馈
    public static final String URL_GETTOPJOKES=BASE_SERVLET+"/GetTopJokes";//获取topjokes
    public static final String URL_GETALLCOMMENTBYID=BASE_SERVLET+"/GetComments";//获取评论
    public static final String URL_SENDCOMMENT=BASE_SERVLET+"/InsertComment";//插入评论
    public static final String URL_UPDATE=BASE_SERVLET+"/GetTheLastBanben";//更新版本
    public static final String URL_ZAN=BASE_SERVLET+"/Zan";//赞
    public static final String BASE_SHARE_TO_OTHER=BASE_PROJECT+"/joke.jsp?1459050189joke_id=";//分享的界面

    //请求参数键
    public static final String KEY_USERNAME="username";
    public static final String KEY_PWD="userpwd";
    public static final String KEY_TYPE="type";
    public static final String KEY_TOKEN="token";
    public static final String KEY_SEX="sex";
        public static final String VALUE_SEX_MAN="man";
        public static final String VALUE_SEX_WOMAN="woman";
    public static final String KEY_MOTTO="motto";
    public static final String KEY_JOKE_ID="joke_id";
    public static final String KEY_SCROLL_TYPE="scroll_type";
        public static final String SCROLL_REFRESH="1";//是刷新加载最新数据
        public static final String SCROLL_LOADINGMORE="2";//还是加载更多的旧的数据
    public static final String KEY_COUNT="count";
    public static final String KEY_DATAS="datas";

    public static final String KEY_JOKE_TYPE="type";//joke的type
    public static final String KEY_JOKE_CONTENT="content";//joke的content
    public static final String KEY_JOKE_ISHASIMG="ishasimg";//joke的是否有图片

    //获取所有用户有关的jokes
    public static final String USER_ABOUT_JOKE_TYPE="joketype";//这是个人中心浏览，分享或者收藏的类型
    public static final String USER_ABOUT_JOKE_TYPE_COLLECT="0";//收藏
    public static final String USER_ABOUT_JOKE_TYPE_LOOK="1";//浏览
    public static final String USER_ABOUT_JOKE_TYPE_SHARE="2";//分享
    public static final String USER_ABOUT_JOKE_TYPE_COMMENT="3";//评论
    public static final String USER_ABOUT_JOKE_TYPE_SELFPUB="4";//自己发表的

    //问题反馈
    public static final String QUES_CONTENT="quescontent";//反馈问题内容
    public static final String QUES_EMAIL="quesemail";//反馈问题email
    //评论
    public static final String COMMENT_CONTENT="com_content";
    public static final String COMMENT_CUR_LOU="cur_lou";
    public static final String DB_COM_ID="com_id";
    public static final String DB_COM_JOKE_ID="joke_id";
    public static final String DB_COM_USER_ID="user_id";
    public static final String DB_COM_USERNAME="user_name";
    public static final String DB_COM_CONTENT="com_content";
    public static final String DB_COM_LOU="com_lou";
    public static final String DB_COM_TIME="com_time";
    //更新
    public static final String USER_BANBENNUM="user_banbennum";
    public static final String TYPE_ZAN="zan_type";
    public static final String TYPE_ZAN_INC="0";//增加赞
    public static final String TYPE_ZAN_DES="1";//减少赞

    public static final String FRAGMENT_TYPE="fragtype";//fragment的类型
    public static final String FRAGMENT_TYPE_NEW="0";//fragment的类型-最新
    public static final String FRAGMENT_TYPE_TOP="1";//fragment的类型-top
    public static final String FRAGMENT_TYPE_JOKE="2";//fragment的类型-joke
    public static final String FRAGMENT_TYPE_GIRL="3";//fragment的类型-girl

    //获取返回值键
    public static final String KEY_RESULT="result";
        public static final String RESULT_SUCCESS="1";
        public static final String RESULT_FAIL="0";
        public static final String RESULT_FAIL_NOMAN="01";
        public static final String RESULT_FAIL_PWDFAIL="02";
    public static final String FAIL_NODATA="03";//查询没有数据可更新了

    public static final String JOKE_ID="joke_id";
    public static final String JOKE_USER_ID="user_id";
    public static final String JOKE_USERNAME="username";
    public static final String JOKE_USERSEX="sex";
    public static final String JOKE_TYPE="type";
    public static final String JOKE_TIME="timecreate";
    public static final String JOKE_CONTENT="content";
    public static final String JOKE_IMGURL="imgurl";
    public static final String JOKE_ISHASIMG="ishasimg";
    public static final String JOKE_SHARE="share_count";
    public static final String JOKE_COLLECT="collect_count";
    public static final String JOKE_LOOK="look_count";
    //本地缓存文件参数
    public static final String BASE_DIR_NAME="happyjoke";
    public static final String BASE_DIR_IMAGE_DOWNLOAD="download";

    //sharedpreference
    public static final String SHAREDPREFERENCE_NAME="mjoke";//sharedpreference文件名
    public static final String SHARED_ISLOGIN="islogin";//是否登录
    public static final String SHARED_USER_TOKEN="token";
    public static final String SHARED_USER_NAME="username";
    public static final String SHARED_USER_PWD="userpwd";
    public static final String SHARED_USER_SEX="usersex";
    public static final String SHARED_USER_MOTTO="usermotto";
    public static final String SHARED_USER_HEADERURL="userhead";
    public static final String SHARED_THEME_COLOR="themecolor";//主题颜色
    public static final String SHARED_IS_FLOTING="isfloting";//是否显示悬浮按钮
    //其他常量
    public static final int SUCCESS=1;//handler 成功
    public static final int FAIL=2;//handler 失败
    public static final int SELECT_IMG_FROM_PHOTO=1;//选择相册
    public static final int SELECT_IMG_FROM_CAMERA=2;//选择拍照
    public static final int SELECT_IMG_CUT_FINISHED=3;//剪切结束




}
