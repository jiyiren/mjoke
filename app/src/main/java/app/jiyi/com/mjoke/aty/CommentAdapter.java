package app.jiyi.com.mjoke.aty;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.io.UnsupportedEncodingException;
import java.util.List;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.bean.OneCommentBean;
import app.jiyi.com.mjoke.utiltool.Base64Util;
import app.jiyi.com.mjoke.utiltool.BitmapCache;
import app.jiyi.com.mjoke.utiltool.CommonAdapter;
import app.jiyi.com.mjoke.utiltool.ViewHolder;

/**
 * Created by JIYI on 2015/9/8.
 * 评论的适配器
 */
public class CommentAdapter extends CommonAdapter<OneCommentBean> {

    private Context context;
    private ImageLoader loader;
    public CommentAdapter(Context context, List<OneCommentBean> datas, int layoutId){
        super(context,datas,layoutId);
        if(loader==null) {
            loader = new ImageLoader(App.getAppInstance().getQueues(), new BitmapCache());
        }
    }

    @Override
    public void convert(ViewHolder holder, OneCommentBean oneCommentBean) {
        ImageView iv_header=holder.getView(R.id.item_com_header);
        TextView tv_name=holder.getView(R.id.item_com_name);
        TextView tv_lou=holder.getView(R.id.item_com_lou);
        TextView tv_content=holder.getView(R.id.item_com_content);
        TextView tv_time=holder.getView(R.id.item_com_time);

        if(!oneCommentBean.getUser_id().equals("0")&&oneCommentBean.getUser_id()!=null){

            ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv_header, R.mipmap.user_big_icon,
                    R.mipmap.user_big_icon);
            loader.get(MyConfig.BASE_IMG + "/" + oneCommentBean.getUser_id() + ".png", listener);

            if(oneCommentBean.getUser_name()!=null&&!oneCommentBean.getUser_name().equals("")){
                try {
                    tv_name.setText(new String(Base64Util.decode(oneCommentBean.getUser_name()),"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else{
                tv_name.setText("匿名者");
                iv_header.setImageResource(R.mipmap.user_big_icon);
            }
        }else{
            tv_name.setText("匿名者");
            iv_header.setImageResource(R.mipmap.user_big_icon);
        }

        if(oneCommentBean.getCom_lou()!=null&&!oneCommentBean.getCom_lou().equals("")){
            tv_lou.setText(oneCommentBean.getCom_lou() + "楼");
        }else{
            tv_lou.setText("火星楼");
        }

        if(oneCommentBean.getCom_content()!=null&&!oneCommentBean.getCom_content().equals("")){
            try {
                tv_content.setText(new String(Base64Util.decode(oneCommentBean.getCom_content()),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            tv_content.setText("该楼主跑了~~");
        }

        tv_time.setText(oneCommentBean.getCom_time());

    }
}
