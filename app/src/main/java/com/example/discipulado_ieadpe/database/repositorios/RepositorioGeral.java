package com.example.discipulado_ieadpe.database.repositorios;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.discipulado_ieadpe.database.entities.Contato;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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

    public LiveData<List<Contato>> carregarContatos(){
        MutableLiveData<List<Contato>> contatos = new MutableLiveData<>();
        db.collection(MEMBROS_DA_EQUIPE).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Contato> listaDeContatos = new ArrayList<>();
            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                Contato contato = documentSnapshot.toObject(Contato.class);
                listaDeContatos.add(contato);
            }
            contatos.setValue(listaDeContatos);
        }).addOnFailureListener(e -> {
        });
        return contatos;
    }
    //MutableLiveData vai transmitir a mensagem de sucesso ou erro ao viewModel
    public MutableLiveData<String> addContato (Contato contato){

        MutableLiveData<String> mensagemParaUsuario = new MutableLiveData<>();

        Map<String, Object> membro = new HashMap<>();
        membro.put("Nome", contato.nomeDoMembro);
        membro.put("Função", contato.funcao);
        membro.put("Congregação", contato.congregacao);
        membro.put("Telefone", contato.telefone);

        db.collection(MEMBROS_DA_EQUIPE)
                .document(contato.nomeDoMembro).set(membro)
                .addOnSuccessListener(unused -> mensagemParaUsuario.setValue("Contato adicionado com sucesso"))
                .addOnFailureListener(e -> mensagemParaUsuario.setValue("Erro: " + e.getMessage()));
        return mensagemParaUsuario;
    }
//    public int atualizarContato (Contato contato){
//        return 0;
//    }

    public void deletarContato (Contato contato, MutableLiveData<String> mensagemParaUsuario){

        db.collection(MEMBROS_DA_EQUIPE).document(contato.nomeDoMembro)
                .delete()
                .addOnSuccessListener(aVoid -> mensagemParaUsuario.setValue("Membro excluído com sucesso"))
                .addOnFailureListener(e -> mensagemParaUsuario.setValue("Erro: " +e.getMessage()));
    }
}
