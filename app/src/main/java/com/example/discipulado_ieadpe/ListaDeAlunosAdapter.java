package com.example.discipulado_ieadpe;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discipulado_ieadpe.database.entities.Aluno;
import com.example.discipulado_ieadpe.viewmodels.ListaDeAlunosViewModel;

import java.util.List;

public class ListaDeAlunosAdapter extends RecyclerView.Adapter<ListaDeAlunosAdapter.ListaDeAlunosViewholder>{

    private List<Aluno> alunos;
    //TODO Usar usuário logado para definir se vai carregar tudo ou só os alunos da congregação
//    private final String usuarioLogado;
//    private final ListaDeAlunosViewModel viewModel;
//    ActivityResultLauncher<Intent> launcher;
    public static final String CHAVE_INTENT_DADOS_DO_ALUNO = "Dados do aluno";
    private OnWriteClickListener listener;


    public ListaDeAlunosAdapter(List<Aluno> alunos){
        this.alunos = alunosOrdenados(alunos);
//        this.usuarioLogado = usuarioLogado;
//        this.viewModel = viewModel;
//        this.launcher = launcher;
    }

    @NonNull
    @Override
    public ListaDeAlunosViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListaDeAlunosViewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_lista_de_alunos, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListaDeAlunosViewholder holder, int position) {

            Aluno aluno = alunos.get(position);
            String nomeDoAluno = aluno.getNomeDoAluno();
            String congregacao = aluno.getCongregacao();
            String turma = aluno.getTurma();
            String telefone = aluno.getTelefone();
            holder.nomeDoAluno.setText(nomeDoAluno);
            holder.congregacaoDoAluno.setText(congregacao);
            holder.turmaDoAluno.setText(turma);
            holder.telefoneDoAluno.setText(telefone);

            holder.btnFichaCompleta.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), AddEditAlunoFragment.class);
                intent.putExtra(CHAVE_INTENT_DADOS_DO_ALUNO, aluno);
                intent.putExtra("origem", "botão ficha completa");
                v.getContext().startActivity(intent);
            });

        holder.btnLigar.setOnClickListener(view -> discarTelefone(view,
                Utils.desformatarTelefone(telefone)));

        holder.btnAbrirWhatsapp.setOnClickListener(view -> abrirConversaWhatsApp(view,
                Utils.desformatarTelefone(telefone),
                nomeDoAluno));

        holder.btnEditarAluno.setOnClickListener(view -> {
            listener.onUpdateClick(aluno);
//            Intent intent = new Intent(view.getContext(), AddEditAlunoFragment.class);
//            intent.putExtra(CHAVE_INTENT_DADOS_DO_ALUNO, aluno);
//            intent.putExtra("origem", "botão editar aluno");
//            launcher.launch(intent);
        });

        holder.btnExcluirAluno.setOnClickListener(view -> {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
            dialogBuilder.setTitle("Atenção!");
            dialogBuilder.setMessage("Deseja excluir " + nomeDoAluno + "?");
            dialogBuilder.setPositiveButton("Sim", (dialog, which) -> listener.onDeleteClick(position));
            dialogBuilder.setNegativeButton("Não", (dialogInterface, i) -> dialogInterface.dismiss());
            dialogBuilder.show();

        });
    }

    @Override
    public int getItemCount() {
        return alunos != null ? alunos.size() : 0;
    }

    public void atualizarItens (List <Aluno> alunos) {
        this.alunos = alunosOrdenados(alunos);
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

    public List<Aluno> alunosOrdenados(List<Aluno> listaDesordenada) {

        listaDesordenada.sort((aluno1, aluno2) -> {
            int comparacaoTurma = aluno1.getTurma().compareTo(aluno2.getTurma());
            if (comparacaoTurma != 0) {
                return comparacaoTurma;
            } else {
                return aluno1.getNomeDoAluno().compareTo(aluno2.getNomeDoAluno());
            }
        });

        return listaDesordenada;
    }
    public static class ListaDeAlunosViewholder extends RecyclerView.ViewHolder {

        public TextView nomeDoAluno, congregacaoDoAluno, turmaDoAluno, telefoneDoAluno;
        public ImageButton btnFichaCompleta, btnLigar, btnAbrirWhatsapp, btnEditarAluno, btnExcluirAluno;

        public ListaDeAlunosViewholder(@NonNull View itemView) {
            super(itemView);
            nomeDoAluno = itemView.findViewById(R.id.nome_do_aluno);
            congregacaoDoAluno = itemView.findViewById(R.id.congregacao_do_aluno);
            turmaDoAluno = itemView.findViewById(R.id.turma_do_aluno);
            telefoneDoAluno = itemView.findViewById(R.id.telefone_do_aluno);
            btnFichaCompleta = itemView.findViewById(R.id.btn_ficha_completa);
            btnLigar = itemView.findViewById(R.id.btn_ligar);
            btnAbrirWhatsapp = itemView.findViewById(R.id.btn_abrir_whatsapp);
            btnEditarAluno = itemView.findViewById(R.id.btn_editar_aluno);
            btnExcluirAluno = itemView.findViewById(R.id.btn_excluir_aluno);
        }
    }

    public void setOnWriteClickListener(OnWriteClickListener listener){
        this.listener = listener;
    }
    public interface OnWriteClickListener{
        public void onUpdateClick(Aluno aluno);
        public void onDeleteClick(int position);
    }
}
