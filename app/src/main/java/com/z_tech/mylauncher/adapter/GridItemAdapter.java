package com.z_tech.mylauncher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.z_tech.mylauncher.R;
import com.z_tech.mylauncher.bean.GridInfo;

import java.util.List;

public class GridItemAdapter extends BaseAdapter{

    private List<GridInfo> mGridInfoList;    //定义数据类
    private LayoutInflater mInflater;

    public GridItemAdapter(Context context, List<GridInfo> gridInfoList){

        mGridInfoList = gridInfoList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return mGridInfoList.size();
    }

    public Object getItem(int position) {
        return mGridInfoList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();


        if(convertView==null){
            convertView = mInflater.inflate(R.layout.grid_item,null);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //设置holder
        GridInfo mGridInfo =  mGridInfoList.get(position);
        viewHolder.iv.setImageDrawable(mGridInfo.getAppIcon());
        viewHolder.tv.setText(mGridInfo.getTitle());

        return convertView;
    }

    private class ViewHolder{
        ImageView iv;
        TextView tv;
    }

}
