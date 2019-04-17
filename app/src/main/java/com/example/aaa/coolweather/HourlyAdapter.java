package com.example.aaa.coolweather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by aaa on 2019/4/16.
 */

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {
    private List<Hour>mHourList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView HourlyImage;
        TextView HourlyTime;
        TextView HourlyTmp;
        public ViewHolder(View view){
            super(view);
            HourlyImage=(ImageView)view.findViewById(R.id.hourly_weather_icon);
            HourlyTime=(TextView)view.findViewById(R.id.hourly_time);
            HourlyTmp=(TextView)view.findViewById(R.id.hourly_tmp);
        }
    }

    public HourlyAdapter(List<Hour> mHourList) {
        this.mHourList = mHourList;
    }

    /**
     * 将hourly_item布局加载进来，并使用这个布局创建一个ViewHolder，
     * 返回这个实例
     * @param parent
     * @param viewType
     * @return ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    /**
     * 用于对RecyclerView子项进行赋值的
     * 会在每个子项被滚动到屏幕内的时候执行
     * 通过position得到当前项的Hour实例，然后设置进ViewHolder中。
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder,int position)
    {
        Hour hour=mHourList.get(position);
        holder.HourlyTime.setText(hour.getHourly_time());
        holder.HourlyImage.setImageResource(hour.getHourly_icon_id());
        holder.HourlyTmp.setText(hour.getHourly_tmp());
    }
    @Override
    public int getItemCount(){
        return mHourList.size();
    }
}
