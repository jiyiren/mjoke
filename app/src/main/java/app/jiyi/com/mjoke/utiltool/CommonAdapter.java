package app.jiyi.com.mjoke.utiltool;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by JIYI on 2015/8/27.
 * 通用adapter
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List< T> mDatas ;
    private int mLayoutId ;
    public CommonAdapter(Context context ,List<T > datas,int layoutId){
        mContext =context;
        mDatas =datas;
        mLayoutId =layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem( int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder=ViewHolder.get( mContext,convertView ,parent, mLayoutId ,position);

        convert(viewHolder ,getItem(position));

        return viewHolder.getConvertView() ;
    }

    public abstract void convert(ViewHolder holder, T t);

}
