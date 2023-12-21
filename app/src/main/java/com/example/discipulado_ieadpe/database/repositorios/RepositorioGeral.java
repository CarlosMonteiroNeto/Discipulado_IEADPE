package com.example.discipulado_ieadpe.database.repositorios;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.discipulado_ieadpe.database.entities.Contato;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositorioGeral {

//    AppDatabase db;
    private final FirebaseFirestore db;
    private final String MEMBROS_DA_EQUIPE = "Equipe";
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
}
