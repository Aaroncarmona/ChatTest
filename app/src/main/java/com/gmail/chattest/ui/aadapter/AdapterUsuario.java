package com.gmail.chattest.ui.aadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.chattest.R;
import com.gmail.chattest.model.Usuario;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdapterUsuario extends RecyclerView.Adapter<AdapterUsuario.ViewHolder> {

    private View view;
    private List<Usuario> usuarios;
    private AdapterCustomListeners.OnItemClick onItemClick;

    public AdapterUsuario() {
        usuarios = new ArrayList<>();
        onItemClick = item -> { };
    }

    public void setData( List<Usuario> usuarios){
        this.usuarios = usuarios;
    }

    public void setOnItemClickListener(AdapterCustomListeners.OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario,parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try{
            Usuario current = usuarios.get(position);

            if ( current.getUsuario() != null )
                holder.mtvUsuario.setText(current.getUsuario());

            holder.mtvEdad.setText(String.valueOf(current.getEdad()));

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_usuario) TextView mtvUsuario;
        @BindView(R.id.tv_edad) TextView mtvEdad;

        public ViewHolder(@NonNull View item) {
            super(item);
            ButterKnife.bind(this , item);
        }

        @OnClick(R.id.item_root)
        public void click(){
            onItemClick.onItem(getAdapterPosition());
        }
    }
}
