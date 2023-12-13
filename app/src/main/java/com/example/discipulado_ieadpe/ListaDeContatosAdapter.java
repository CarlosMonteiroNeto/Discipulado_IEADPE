package com.example.discipulado_ieadpe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discipulado_ieadpe.canarinho.formatador.FormatadorTelefone;
import com.example.discipulado_ieadpe.database.entities.Contato;
import com.example.discipulado_ieadpe.viewmodels.ListaDeContatosViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class ListaDeContatosAdapter extends RecyclerView.Adapter<ListaDeContatosViewholder> {

    private ArrayList <Contato> contatos;
    private String usuarioLogado;

    private ListaDeContatosViewModel viewModel;
    public ListaDeContatosAdapter(ArrayList<Contato> contatos, String usuarioLogado, ListaDeContatosViewModel viewModel){
        this.contatos = contatos;
        this.usuarioLogado = usuarioLogado;
        this.viewModel = viewModel;
    }


    @NonNull
    @Override
    public ListaDeContatosViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListaDeContatosViewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_de_contatos_viewholder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListaDeContatosViewholder holder, int position) {

        Contato contato = contatos.get(position);
        holder.nomeDoMembro.setText(contato.nomeDoMembro);
        holder.funcaoDoMembro.setText(contato.funcao);
        holder.congregacaoDoMembro.setText(contato.congregacao);
        holder.telefoneDoMembro.setText(FormatadorTelefone.TELEFONE.formata(String.valueOf(contato.telefone)));

        if (!Objects.equals(usuarioLogado, "Supervisão")
                && !Objects.equals(usuarioLogado, MainActivity.USUARIO_PADRAO)) {
            if (!holder.congregacaoDoMembro.getText().toString().equals(usuarioLogado)) {
                holder.btnEditarMembro.setVisibility(View.INVISIBLE);
                holder.btnExcluirMembro.setVisibility(View.INVISIBLE);
            }
        }
        if (Objects.equals(usuarioLogado, MainActivity.USUARIO_PADRAO)) {
            holder.btnEditarMembro.setVisibility(View.INVISIBLE);
            holder.btnExcluirMembro.setVisibility(View.INVISIBLE);
        }
        holder.btnEditarMembro.setOnClickListener(view -> {
            Contato contatoAEditar = new Contato();
            contatoAEditar.nomeDoMembro = holder.nomeDoMembro.getText().toString();
            contatoAEditar.funcao = holder.funcaoDoMembro.getText().toString();
            contatoAEditar.congregacao = holder.congregacaoDoMembro.getText().toString();
            contatoAEditar.telefone = Integer.parseInt(FormatadorTelefone.TELEFONE.desformata(holder.telefoneDoMembro.getText().toString()));
            Intent intent = new Intent(view.getContext(), AddEditMembroActivity.class);
            intent.putExtra(ListaDeContatosActivity.CHAVE_INTENT_DADOS_DO_MEMBRO, contatoAEditar);
            view.getContext().startActivity(intent);
        });

        holder.btnExcluirMembro.setOnClickListener(view -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
            dialogBuilder.setTitle("Atenção!");
            dialogBuilder.setMessage("Deseja excluir " + holder.nomeDoMembro.getText().toString() + " da equipe?");
            dialogBuilder.setPositiveButton("Sim", (dialog, which) -> {
                Contato contatoAExcluir = new Contato();
                contatoAExcluir.nomeDoMembro = holder.nomeDoMembro.getText().toString();
                viewModel.excluirContato(contatoAExcluir);
            });
            dialogBuilder.setNegativeButton("Não", (dialogInterface, i) -> {
                // Fecha o diálogo quando o botão "Cancelar" é pressionado
                dialogInterface.dismiss();
            });
            dialogBuilder.show();
        });
    }

    @Override
    public int getItemCount () {
        return contatos != null ? contatos.size() : 0;
    }

    public void atualizarItens (ArrayList < Contato > contatos) {
        this.contatos = contatos;
        notifyDataSetChanged();
    }
}
