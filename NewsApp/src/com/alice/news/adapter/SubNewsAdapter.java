package com.alice.news.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.alice.news.R;
import com.alice.news.asynctask.NewsImageAsyncTask;
import com.alice.news.asynctask.NewsImageThread;
import com.alice.news.entity.News;
import com.alice.news.fragment.SubNewsFragment;

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
    ViewHolder holder = null;
    public SubNewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    public List<News> addList(List<News> list) {
        if (list != null && newsList != null) {
            this.newsList.addAll(list);
        }
        return this.newsList;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_news_list, parent,false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.item_news_cover);
            holder.subject = (TextView) convertView.findViewById(R.id.item_news_subject);
            holder.changed = (TextView) convertView.findViewById(R.id.item_news_changed);
            holder.summary = (TextView) convertView.findViewById(R.id.item_news_summary);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        //TODO 加载图片
        new NewsImageAsyncTask(new NewsImageAsyncTask.ImageCallBack() {
            @Override
            public void sendImage(Bitmap bitmap) {
                holder.image.setImageBitmap(bitmap);
            }
        }).execute(SubNewsFragment.NEWSDIE+newsList.get(position).getCover());
//        new NewsImageThread().start();
//        holder.image.setImageResource(R.drawable.ic_launcher);
//        holder.image.setImageBitmap(newsList.get(position).getCover());
        holder.subject.setText(newsList.get(position).getSubject());
        holder.changed.setText(newsList.get(position).getChanged());
        holder.summary.setText(newsList.get(position).getSummary());
        return convertView;
    }

    private static class  ViewHolder{
        ImageView image;
        TextView subject;
        TextView changed;
        TextView summary;
    }
}
