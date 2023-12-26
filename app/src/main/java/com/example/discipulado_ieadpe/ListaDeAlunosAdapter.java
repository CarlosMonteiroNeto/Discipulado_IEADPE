package com.example.discipulado_ieadpe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discipulado_ieadpe.database.entities.Aluno;
import com.example.discipulado_ieadpe.database.entities.Contato;
import com.example.discipulado_ieadpe.viewmodels.ListaDeAlunosViewModel;
import com.example.discipulado_ieadpe.viewmodels.ListaDeContatosViewModel;

import java.util.List;

public class ListaDeAlunosAdapter extends RecyclerView.Adapter<ListaDeAlunosAdapter.ListaDeAlunosViewholder>{

    private List<Aluno> alunos;
    private final String usuarioLogado;
    private final ListaDeAlunosViewModel viewModel;

    public ListaDeAlunosAdapter(List<Aluno> alunos, String usuarioLogado, ListaDeAlunosViewModel viewModel){
        //TODO Vai precisar disso no construtor ou algo a mais?
        this.alunos = alunos;
        this.usuarioLogado = usuarioLogado;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ListaDeAlunosViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListaDeAlunosViewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_lista_de_alunos, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListaDeAlunosViewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return alunos != null ? alunos.size() : 0;
    }

    public void atualizarItens (List <Aluno> alunos) {
        //TODO Ordenar alunos
        this.alunos = alunos;
        notifyDataSetChanged();
    }
    public static class ListaDeAlunosViewholder extends RecyclerView.ViewHolder {

        public TextView nomeDoAluno, congregacaoDoAluno, telefoneDoAluno;
        public ImageButton btnLigar, btnAbrirWhatsapp, btnEditarAluno, btnExcluirAluno;

        public ListaDeAlunosViewholder(@NonNull View itemView) {
            super(itemView);
            nomeDoAluno = itemView.findViewById(R.id.nome_do_aluno);
            congregacaoDoAluno = itemView.findViewById(R.id.congregacao_do_aluno);
            telefoneDoAluno = itemView.findViewById(R.id.telefone_do_aluno);
            btnLigar = itemView.findViewById(R.id.btn_ligar);
            btnAbrirWhatsapp = itemView.findViewById(R.id.btn_abrir_whatsapp);
            btnEditarAluno = itemView.findViewById(R.id.btn_editar_aluno);
            btnExcluirAluno = itemView.findViewById(R.id.btn_excluir_aluno);
        }
    }
}