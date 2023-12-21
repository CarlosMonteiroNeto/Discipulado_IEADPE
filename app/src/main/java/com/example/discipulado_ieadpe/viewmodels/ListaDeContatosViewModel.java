package com.example.discipulado_ieadpe.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.discipulado_ieadpe.database.entities.Contato;
import com.example.discipulado_ieadpe.database.repositorios.RepositorioGeral;

import java.util.ArrayList;
import java.util.List;

public class ListaDeContatosViewModel extends AndroidViewModel{


    private final RepositorioGeral repositorioGeral;
    private MutableLiveData<List<Contato>> contatos;
    private MutableLiveData<String> mensagemDeExclusao = new MutableLiveData<>();
    private MutableLiveData<String> mensagemDeEdicao = new MutableLiveData<>();

    public ListaDeContatosViewModel(@NonNull Application application) {
        super(application);
        repositorioGeral = new RepositorioGeral();
        contatos = repositorioGeral.carregarContatos();
    }

    public LiveData<List<Contato>> getContatos(){return contatos;}

    public void setContatos(MutableLiveData<List<Contato>> contatos) {
        this.contatos = contatos;
    }

    public void addContato (Contato contato){
        List<Contato> contatosAtuais = contatos.getValue();
        if (contatosAtuais == null){
            contatosAtuais = new ArrayList<>();
        }
        mensagemDeEdicao = repositorioGeral.addContato(contato);
//        if(mensagem.getValue().equals("Contato adicionado com sucesso")){
            boolean substituir = false;
            for (Contato contatoExistente : contatosAtuais){
                if (contatoExistente.getNomeDoMembro().equals(contato.getNomeDoMembro())){
                    contatosAtuais.set(contatosAtuais.indexOf(contatoExistente), contato);
                }
                substituir = true;
                break;
            }
            if (!substituir){
                contatosAtuais.add(contato);
            }
            contatos.setValue(contatosAtuais);
//        }
    }

//    public int atualizarContato (Contato contato){
//        return repositorioGeral.atualizarContato(contato);
//    }
    public void deletarContato (Contato contato){
        List<Contato> contatosAtuais = contatos.getValue();
        mensagemDeExclusao = repositorioGeral.deletarContato(contato);
//        if(mensagemDeExclusao != null
//                && mensagemDeExclusao.getValue() != null
//                && mensagemDeExclusao.getValue().equals("Membro excluído com sucesso")){
        contatosAtuais.remove(contato);
        contatos.setValue(contatosAtuais);
//        }
    }
    public MutableLiveData<String> getMensagemDeExclusao(){
        return mensagemDeExclusao;
    }

    public MutableLiveData<String> getMensagemDeEdicao() {
        return mensagemDeEdicao;
    }
    //    @Override
//    public MutableLiveData<String> onContatoDeleted(Contato contato){
//        return repositorioGeral.deletarContato(contato);
//    }
}
