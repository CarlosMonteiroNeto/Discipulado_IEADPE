package com.example.discipulado_ieadpe.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.discipulado_ieadpe.database.entities.Contato;
import com.example.discipulado_ieadpe.database.repositorios.RepositorioGeral;

import java.util.ArrayList;
import java.util.List;

public class ListaDeContatosViewModel extends AndroidViewModel {


    private final RepositorioGeral repositorioGeral;
    private final LiveData<List<Contato>> contatos;

    public ListaDeContatosViewModel(@NonNull Application application) {
        super(application);
        repositorioGeral = new RepositorioGeral(this.getApplication());
        contatos = repositorioGeral.carregarContatos();
    }
    public LiveData<List<Contato>> getContatos(){return contatos;}

    public void addContato (Contato contato){
        repositorioGeral.addContato(contato);}

    public int atualizarContato (Contato contato){
        return repositorioGeral.atualizarContato(contato);
    }
    public void excluirContato (Contato contato){
        repositorioGeral.deletarContato(contato);
    }
}
