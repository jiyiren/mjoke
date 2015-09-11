package app.jiyi.com.mjoke.utiltool;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JIYI on 2015/8/27.
 */
public class ViewHolder {
    private SparseArray<View> mViews;
    private int mPosition ;
    private View mConvertView;

    public ViewHolder(Context context ,ViewGroup parent,int layoutId,int position){
        this .mPosition=position ;
        mViews =new SparseArray<View>() ;
        mConvertView = LayoutInflater.from(context).inflate(layoutId ,parent,false );
        mConvertView .setTag(this) ;
    }

    public static ViewHolder get(Context context ,View convertView, ViewGroup parent,int layoutId ,int position){
        if (convertView==null){
            return new ViewHolder(context,parent ,layoutId, position);
        } else{
            ViewHolder viewHolder= (ViewHolder) convertView.getTag() ;
            viewHolder. mPosition=position;
            return viewHolder;
        }
    }

    /**
     * 通过viewId获取 view
     * @param viewId
     * @param <T>
     * @return
     */

    public <T extends View> T getView (int viewId){
        View view=mViews .get(viewId);
        if(view== null){
            view=mConvertView.findViewById(viewId) ;
            mViews .put(viewId,view) ;
        }
        return (T) view ;
    }

    public View getConvertView(){
        return mConvertView;
    }
}
