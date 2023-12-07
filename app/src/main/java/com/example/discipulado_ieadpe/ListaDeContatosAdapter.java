package com.example.discipulado_ieadpe;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListaDeContatosAdapter extends RecyclerView.Adapter<ListaDeContatosViewholder> {

    private ArrayList<Contato> contatos;
    public ListaDeContatosAdapter(ArrayList<Contato> contatos){this.contatos = contatos;}


    @NonNull
    @Override
    public ListaDeContatosViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListaDeContatosViewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_de_contatos_viewholder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListaDeContatosViewholder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return contatos != null ? contatos.size() : 0;
    }

    public void addItens (ArrayList<Contato> contatos){
        this.contatos = contatos;
        notifyDataSetChanged();
    }
}
