package com.example.discipulado_ieadpe.database.repositorios;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.discipulado_ieadpe.MainActivity;
import com.example.discipulado_ieadpe.database.entities.Aluno;
import com.example.discipulado_ieadpe.database.entities.Contato;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RepositorioGeral {

//    AppDatabase db;
//    AppDatabase db;
private final FirebaseFirestore db;
    private final String MEMBROS_DA_EQUIPE = "Equipe";
    private final String CONGREGACAO = "Congregação";
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

    public MutableLiveData<List<Aluno>> carregarAlunosPorCongregacao(String congregacao){
        MutableLiveData<List<Aluno>> alunos = new MutableLiveData<>();
        db.collection(ALUNOS).whereEqualTo("congregacao", congregacao)
                .get().addOnCompleteListener(task -> {
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
    public Bundle addAluno (String usuarioLogado, Aluno aluno){

        Bundle writePackage = new Bundle();

        db.collection(MainActivity.CHAVE_USUARIO).document(usuarioLogado).collection(ALUNOS)
                .add(aluno)
                .addOnSuccessListener(unused -> {
                    writePackage.putString("mensagem", "Adicionado com sucesso");
                    writePackage.putSerializable("objeto", aluno);
                })
                .addOnFailureListener(e -> writePackage.putString("mensagem", "Erro: " +e.getMessage()));
        return writePackage;
    }

    public Bundle atualizarAluno(String usuarioLogado, Aluno aluno, int position){
        Bundle writePackage = new Bundle();
        db.collection(MainActivity.CHAVE_USUARIO).document(usuarioLogado)
                .collection(ALUNOS).document(aluno.getID())
                .set(aluno)
                .addOnSuccessListener(unused -> {
                    writePackage.putString("mensagem", "Atualizado com sucesso");
                    writePackage.putInt("position", position);
                    writePackage.putSerializable("objeto", aluno);
                })
                .addOnFailureListener(e -> writePackage.putString("mensagem", "Erro: " +e.getMessage()));
        return writePackage;
    }

    public Bundle deletarAluno (String usuarioLogado, String idDoAluno, int position){

        Bundle writePackage = new Bundle();
        db.collection(MainActivity.CHAVE_USUARIO).document(usuarioLogado)
                .collection(ALUNOS).document(idDoAluno)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    writePackage.putString("mensagem", "Membro excluído com sucesso");
                    writePackage.putInt("position", position);
                })
                .addOnFailureListener(e -> writePackage.putString("mensagem", "Erro: " +e.getMessage()));
        return writePackage;
    }

}
