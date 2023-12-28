package com.example.discipulado_ieadpe.database.repositorios;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.discipulado_ieadpe.database.entities.Aluno;
import com.example.discipulado_ieadpe.database.entities.Contato;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositorioGeral {

//    AppDatabase db;
//    AppDatabase db;
private final FirebaseFirestore db;
    private final String MEMBROS_DA_EQUIPE = "Equipe";
    private final String ALUNOS = "Alunos";
    public RepositorioGeral(){
        db = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<List<Contato>> carregarContatos(){
        MutableLiveData<List<Contato>> contatos = new MutableLiveData<>();
        db.collection(MEMBROS_DA_EQUIPE).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<Contato> listaContatos = new ArrayList<>();
                for (QueryDocumentSnapshot document: task.getResult()){
                    Contato contato = document.toObject(Contato.class);
                    listaContatos.add(contato);
                }
                contatos.setValue(listaContatos);
            }
        });
        return contatos;
    }
    //MutableLiveData vai transmitir a mensagem de sucesso ou erro ao viewModel
    public MutableLiveData<String> addContato (Contato contato){

        MutableLiveData<String> mensagemParaUsuario = new MutableLiveData<>();

        Map<String, Object> membro = new HashMap<>();
        membro.put("nomeDoMembro", contato.getNomeDoMembro());
        membro.put("funcao", contato.getFuncao());
        membro.put("congregacao", contato.getCongregacao());
        membro.put("telefone", contato.getTelefone());
        membro.put("dataDeNascimento", contato.getDataDeNascimento());

        db.collection(MEMBROS_DA_EQUIPE)
                .document(contato.getNomeDoMembro()).set(membro)
                .addOnSuccessListener(unused -> {
                    mensagemParaUsuario.setValue("Contato adicionado com sucesso");
                    Log.d("Sucesso ao add", "DocumentSnapshot added");
                })
                .addOnFailureListener(e -> {
                    mensagemParaUsuario.setValue("Erro: " + e.getMessage());
                    Log.w("Falha ao add", "Error adding document", e);
                });
        return mensagemParaUsuario;
    }
//    public int atualizarContato (Contato contato){
//        return 0;
//    }

    public MutableLiveData<String> deletarContato (Contato contato){

        MutableLiveData<String> mensagemParaUsuario = new MutableLiveData<>();
        db.collection(MEMBROS_DA_EQUIPE).document(contato.getNomeDoMembro())
                .delete()
                .addOnSuccessListener(aVoid -> mensagemParaUsuario.setValue("Membro excluído com sucesso"))
                .addOnFailureListener(e -> mensagemParaUsuario.setValue("Erro: " +e.getMessage()));
        return mensagemParaUsuario;
    }

    public MutableLiveData<List<Aluno>> carregarAlunos(){
        MutableLiveData<List<Aluno>> alunos = new MutableLiveData<>();
        db.collection(ALUNOS).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<Aluno> listaAlunos = new ArrayList<>();
                for (QueryDocumentSnapshot document: task.getResult()){
                    Aluno aluno = document.toObject(Aluno.class);
                    listaAlunos.add(aluno);
                }
                alunos.setValue(listaAlunos);
            }
        });
        return alunos;
    }
//TODO Corrigir carregando os dados do aluno
    public MutableLiveData<String> addAluno (Aluno aluno){

        MutableLiveData<String> mensagemParaUsuario = new MutableLiveData<>();

        Map<String, Object> membro = new HashMap<>();
        membro.put("nomeDoMembro", aluno.getNomeDoAluno());
//        membro.put("funcao", aluno.getFuncao());
//        membro.put("congregacao", aluno.getCongregacao());
//        membro.put("telefone", aluno.getTelefone());

        db.collection(MEMBROS_DA_EQUIPE)
                .document(aluno.getNomeDoAluno()).set(membro)
                .addOnSuccessListener(unused -> {
                    mensagemParaUsuario.setValue("Contato adicionado com sucesso");
                    Log.d("Sucesso ao add", "DocumentSnapshot added");
                })
                .addOnFailureListener(e -> {
                    mensagemParaUsuario.setValue("Erro: " + e.getMessage());
                    Log.w("Falha ao add", "Error adding document", e);
                });
        return mensagemParaUsuario;
    }
//    public int atualizarContato (Contato contato){
//        return 0;
//    }

    public MutableLiveData<String> deletarAluno (Aluno aluno){

        MutableLiveData<String> mensagemParaUsuario = new MutableLiveData<>();
        db.collection(MEMBROS_DA_EQUIPE).document(aluno.getNomeDoAluno())
                .delete()
                .addOnSuccessListener(aVoid -> mensagemParaUsuario.setValue("Membro excluído com sucesso"))
                .addOnFailureListener(e -> mensagemParaUsuario.setValue("Erro: " +e.getMessage()));
        return mensagemParaUsuario;
    }

}
