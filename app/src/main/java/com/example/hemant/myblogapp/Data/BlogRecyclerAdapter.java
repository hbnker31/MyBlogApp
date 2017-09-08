package com.example.hemant.myblogapp.Data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hemant.myblogapp.Model.Blog;
import com.example.hemant.myblogapp.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

/**
 * Created by Hemant on 26-08-2017.
 */

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Blog> blogList;

    public BlogRecyclerAdapter(Context context, List<Blog>list) {
            blogList=list;
        this.context=context;
    }

    @Override
    public BlogRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(BlogRecyclerAdapter.ViewHolder holder, int position) {
        Blog blog=blogList.get(position);
        String ImageURL=null;

        holder.title.setText(blog.getTitle());
        holder.desc.setText(blog.getDesc());

        java.text.DateFormat dateFormat=java.text.DateFormat.getDateInstance();
        String formattedDate=dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());

        holder.timestamp.setText(formattedDate);

        ImageURL=blog.getImage();

        Picasso.with(context).load(ImageURL).into(holder.imageView);

        //Use Picasso to load image



    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,desc,timestamp;
        public ImageView imageView;
        String userId;
        public ViewHolder(View itemView,Context ctx) {

            super(itemView);
            context=ctx;
            title=(TextView) itemView.findViewById(R.id.posttitlelist);
            desc=(TextView)itemView.findViewById(R.id.postdesclist);
            imageView=(ImageView) itemView.findViewById(R.id.postImageList);
            timestamp=(TextView)itemView.findViewById(R.id.posttimestamplist);
            userId=null;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }
}
