package com.alice.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.alice.news.R;
import com.alice.news.entity.News;

import java.util.List;

/**
 * Created by Administrator on 15-7-8.
 * Project: NewsApp
 * User: Alice
 * Data: 15-7-8
 */
public class SubNewsAdapter extends BaseAdapter {
    private Context context;
    private List<News> newsList;

    public SubNewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (newsList != null) {
            ret = newsList.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_news_list, parent,false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.item_news_image);
            holder.title = (TextView) convertView.findViewById(R.id.item_news_title);
            holder.time = (TextView) convertView.findViewById(R.id.item_news_time);
            holder.content = (TextView) convertView.findViewById(R.id.item_news_content);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image.setImageBitmap(newsList.get(position).getBitmap());
        holder.title.setText(newsList.get(position).getTitle());
        holder.time.setText(newsList.get(position).getTime());
        holder.content.setText(newsList.get(position).getContent());
        return convertView;
    }

    private static class  ViewHolder{
        ImageView image;
        TextView title;
        TextView time;
        TextView content;
    }
}
