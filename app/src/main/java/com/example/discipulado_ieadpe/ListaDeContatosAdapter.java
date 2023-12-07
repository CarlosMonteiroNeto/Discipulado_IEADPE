package com.example.discipulado_ieadpe;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discipulado_ieadpe.database.entities.Contato;

import java.util.ArrayList;

public class ListaDeContatosAdapter extends RecyclerView.Adapter<ListaDeContatosViewholder> {

    private ArrayList <Contato> contatos;
    public ListaDeContatosAdapter(ArrayList<Contato> contatos){this.contatos = contatos;}


    @NonNull
    @Override
    public ListaDeContatosViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListaDeContatosViewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_de_contatos_viewholder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListaDeContatosViewholder holder, int position) {
        //TODO verificar informações de login recebidas pela activity,
        // definir o nível de acesso do usuário
        // e não carregar botões de editar e excluir contatos, a depender do nível de acesso.

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
