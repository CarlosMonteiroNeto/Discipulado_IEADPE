package com.example.discipulado_ieadpe;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.discipulado_ieadpe.database.entities.Contato;
import com.example.discipulado_ieadpe.viewmodels.ListaDeContatosViewModel;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ListaDeContatosAdapter extends RecyclerView.Adapter<ListaDeContatosViewholder> {

    private List<Contato> contatos;
    private final String usuarioLogado;
    //    private ContatoDeleteListener contatoDeleteListener;
    private final ListaDeContatosViewModel viewModel;
    private final ActivityResultLauncher<Intent> launcher;

    public ListaDeContatosAdapter(List<Contato> contatos, String usuarioLogado, ListaDeContatosViewModel viewModel, ActivityResultLauncher<Intent> launcher){
        this.contatos = ordenarContatos(contatos);
        this.usuarioLogado = usuarioLogado;
        this.viewModel = viewModel;
        this.launcher = launcher;
    }


    @NonNull
    @Override
    public ListaDeContatosViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListaDeContatosViewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_lista_de_contatos, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListaDeContatosViewholder holder, int position) {

        Contato contato = contatos.get(position);
        String nomeDoMembro = contato.getNomeDoMembro();
        String funcao = contato.getFuncao();
        String congregacao = contato.getCongregacao();
        String telefone = contato.getTelefone();
        holder.nomeDoMembro.setText(nomeDoMembro);
        holder.funcaoDoMembro.setText(funcao);
        holder.congregacaoDoMembro.setText(congregacao);
        holder.telefoneDoMembro.setText(telefone);


        if (!Objects.equals(usuarioLogado, "Supervisão")
                && !Objects.equals(usuarioLogado, MainActivity.USUARIO_PADRAO)) {
            if (!congregacao.equals(usuarioLogado)) {
                holder.btnEditarMembro.setVisibility(View.INVISIBLE);
                holder.btnExcluirMembro.setVisibility(View.INVISIBLE);
            }
        }
        if (Objects.equals(usuarioLogado, MainActivity.USUARIO_PADRAO)) {
            holder.btnEditarMembro.setVisibility(View.INVISIBLE);
            holder.btnExcluirMembro.setVisibility(View.INVISIBLE);
        }

//        verificarWhatsApp(holder.btnAbrirWhatsapp, Utils.desformatarTelefone(telefone));

        holder.btnLigar.setOnClickListener(view -> discarTelefone(view,
                Utils.desformatarTelefone(telefone)));

        holder.btnAbrirWhatsapp.setOnClickListener(view -> abrirConversaWhatsApp(view,
                Utils.desformatarTelefone(telefone),
                nomeDoMembro));


        holder.btnEditarMembro.setOnClickListener(view -> {
            Contato contatoAEditar = new Contato();
            contatoAEditar.setNomeDoMembro(nomeDoMembro);
            contatoAEditar.setFuncao(funcao);
            contatoAEditar.setCongregacao(congregacao);
            contatoAEditar.setTelefone(telefone);
            contatoAEditar.setDataDeNascimento(contatos.get(position).getDataDeNascimento());
            Intent intent = new Intent(view.getContext(), AddEditMembroActivity.class);
            intent.putExtra(ListaDeContatosActivity.CHAVE_INTENT_DADOS_DO_MEMBRO, contatoAEditar);
            launcher.launch(intent);
        });

        holder.btnExcluirMembro.setOnClickListener(view -> {
//            int adapterPosition = holder.getAdapterPosition();
//            if (adapterPosition != RecyclerView.NO_POSITION) {
//                Contato contatoAExcluir = contatos.get(adapterPosition);
//            }
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
            dialogBuilder.setTitle("Atenção!");
            dialogBuilder.setMessage("Deseja excluir " + nomeDoMembro + " da equipe?");
            dialogBuilder.setPositiveButton("Sim", (dialog, which) -> viewModel.deletarContato(contato).observeForever(mensagem -> {
                if (mensagem != null) {
                    Toast.makeText(view.getContext(), mensagem, Toast.LENGTH_SHORT).show();
                }
            }));
            dialogBuilder.setNegativeButton("Não", (dialogInterface, i) -> dialogInterface.dismiss());
            dialogBuilder.show();

        });
    }

    @Override
    public int getItemCount () {
        return contatos != null ? contatos.size() : 0;
    }

    public void atualizarItens (List < Contato > contatos) {
        this.contatos = ordenarContatos(contatos);
        notifyDataSetChanged();
    }
    public void discarTelefone(View view, String telefone) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + telefone));
        view.getContext().startActivity(intent);
    }

    public void abrirConversaWhatsApp(View view, String telefone, String nomeDoContato) {
        String mensagem = "A paz do Senhor, "+ nomeDoContato +"! Tudo bem?";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone= +55" + telefone + "&text=" + mensagem));
        view.getContext().startActivity(intent);
    }

    private List<Contato>ordenarContatos(List<Contato> contatosDesordenados) {
        Comparator<Contato> comparador = Comparator
                .comparing((Contato c) -> {
                    switch (c.getCongregacao()) {
                        case "Supervisão":
                            return 0;
                        case "Matriz":
                            return 1;
                        default:
                            return 2;
                    }
                })
                .thenComparing(Contato::getCongregacao)
                .thenComparing((Contato c) -> {
                    switch (c.getFuncao()) {
                        case "Supervisor das campanhas":
                            return 0;
                        case "Vice-supervisor das campanhas":
                            return 1;
                        case "Coordenador do discipulado":
                            return 2;
                        case "Secretária da coordenação":
                            return 3;
                        case "Vice secretária da coordenação":
                            return 4;
                        case "Vice-coordenador do discipulado":
                            return 5;
                        case "Assistente de congregação":
                            return 6;
                        case "Dirigente de campanha":
                            return 7;
                        case "Vice-dirigente de campanha":
                            return 8;
                        case "Professor(a) do discipulado":
                            return 9;
                        case "Secretária do discipulado":
                            return 10;
                        case "Vice-secretária do discipulado":
                            return 11;
                        default:
                            return 12; // Qualquer outra função não listada
                    }
                })
                .thenComparing(Contato::getNomeDoMembro);

        return contatosDesordenados.stream()
                .sorted(comparador)
                .collect(Collectors.toList());
    }
//    public void verificarWhatsApp(View view, String telefone) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone= +55" + telefone));
//        PackageManager packageManager = view.getContext().getPackageManager();
//        if (intent.resolveActivity(packageManager) == null) {
//            view.setVisibility(View.GONE);
//        }
//    }
}
