package com.gmail.chattest.ui.aadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmail.chattest.R;
import com.gmail.chattest.model.entity.Resources;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class AdapterResource extends RecyclerView.Adapter<AdapterResource.ViewHolder> {
    private View view;
    private List<Resources> list;
    private AdapterCustomListeners.OnItemClick itemClick;
    private AdapterCustomListeners.OnItemClick longlistener;

    public AdapterResource(){
        this.list = new ArrayList<>();
        this.itemClick = item -> {};
    }

    public void setData( List<Resources> items ){
        this.list = items;
    }

    public void setListener( AdapterCustomListeners.OnItemClick listener ) {
        this.itemClick = listener;
    }

    public void setOnLongListener( AdapterCustomListeners.OnItemClick longListener){
        this.longlistener = longListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resource , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try{
            Resources current = list.get(position);

            if ( current.getName() != null )
                holder.name.setText(current.getName());

            if ( current.getType() != null )
                holder.type.setText(current.getType());

            if ( current.getLocalPath() != null )
                holder.localpath.setText(current.getLocalPath());

            if ( current.getRemotePath() != null )
                holder.remotepath.setText(current.getRemotePath());

            if (!current.isRemote()){
                holder.progressBar.setProgress(current.getProgress());
            } else {
                holder.progressBar.setProgress(100);
            }

            holder.size.setText(String.valueOf(current.getSize()));

            holder.remote.setText(current.isRemote() ? "si" : "no");

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_name) TextView name;
        @BindView(R.id.tv_type) TextView type;
        @BindView(R.id.tv_remote) TextView remote;
        @BindView(R.id.tv_path_local) TextView localpath;
        @BindView(R.id.tv_path_remote) TextView remotepath;
        @BindView(R.id.progress) ProgressBar progressBar;
        @BindView(R.id.tv_size) TextView size;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView);
        }

        @OnClick(R.id.root)
        public void click(){
            itemClick.onItem(getAdapterPosition());
        }

        @OnLongClick(R.id.root)
        public boolean onLong(){
            longlistener.onItem(getAdapterPosition());
            return true;
        }
    }

}
