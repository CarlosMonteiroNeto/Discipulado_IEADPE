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

    public MutableLiveData<String> addContato (Contato contato){
        List<Contato> contatosAtuais = contatos.getValue();
        MutableLiveData<String> mensagem = new MutableLiveData<>();
        if (contatosAtuais == null){
            contatosAtuais = new ArrayList<>();
        }
        mensagem = repositorioGeral.addContato(contato);
        boolean substituir = false;
        for (Contato contatoExistente : contatosAtuais){
            if (contatoExistente.getNomeDoMembro().equals(contato.getNomeDoMembro())){
                contatosAtuais.set(contatosAtuais.indexOf(contatoExistente), contato);
                substituir = true;
                break;
            }
        }
        if (!substituir){
            contatosAtuais.add(contato);
        }
        contatos.setValue(contatosAtuais);
        return mensagem;
    }

    //    public int atualizarContato (Contato contato){
//        return repositorioGeral.atualizarContato(contato);
//    }
    public MutableLiveData<String> deletarContato (Contato contato){
        List<Contato> contatosAtuais = contatos.getValue();
        MutableLiveData<String> mensagem = new MutableLiveData<>();
        mensagem.setValue(repositorioGeral.deletarContato(contato).getValue());
        contatosAtuais.remove(contato);
        contatos.setValue(contatosAtuais);
        return mensagem;
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
