package com.example.randylaptop.proyectofinal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.support.v7.widget.AppCompatDrawableManager.get;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder > {
    public ArrayList<Publicar> items;
    public final  Context context;

    public MyAdapter(ArrayList<Publicar> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {

            return R.layout.card_view_lay;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(viewType,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Publicar publicar = items.get(position);

        holder.descripcion.setText(items.get(position).getDescripcion());
        holder.lugar.setText(publicar.getLugar());
        Picasso.with(context).load(publicar.getImgURL()).fit().centerCrop().into(holder.imagen);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView descripcion,lugar;
        public ImageView imagen;

    public MyViewHolder(View itemView) {
        super(itemView);

        descripcion = itemView.findViewById(R.id.descripcion);
        lugar = itemView.findViewById(R.id.ubicacion);
        imagen = itemView.findViewById(R.id.imagen);
    }
}

}
