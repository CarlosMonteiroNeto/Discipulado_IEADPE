package com.example.discipulado_ieadpe.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.discipulado_ieadpe.database.entities.Contato;
import com.example.discipulado_ieadpe.database.repositorios.RepositorioGeral;
import java.util.List;

public class ListaDeContatosViewModel extends AndroidViewModel{


    private final RepositorioGeral repositorioGeral;
    private final LiveData<List<Contato>> contatos;
    private final MutableLiveData<String> mensagemDeExclusao = new MutableLiveData<>();

    public ListaDeContatosViewModel(@NonNull Application application) {
        super(application);
        repositorioGeral = new RepositorioGeral();
        contatos = repositorioGeral.carregarContatos();
    }
    public LiveData<List<Contato>> getContatos(){return contatos;}

    public MutableLiveData<String> addContato (Contato contato){
        return repositorioGeral.addContato(contato);}

//    public int atualizarContato (Contato contato){
//        return repositorioGeral.atualizarContato(contato);
//    }
    public void deletarContato (Contato contato){
        repositorioGeral.deletarContato(contato, mensagemDeExclusao);
    }
    public MutableLiveData<String> getMensagemDeExclusao(){
        return mensagemDeExclusao;
    }
//    @Override
//    public MutableLiveData<String> onContatoDeleted(Contato contato){
//        return repositorioGeral.deletarContato(contato);
//    }
}
