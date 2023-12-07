package com.example.discipulado_ieadpe.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.discipulado_ieadpe.database.entities.Contato;
import com.example.discipulado_ieadpe.database.repositorios.ContatoRep;

import java.util.ArrayList;

public class ListaDeContatosViewModel extends AndroidViewModel {


    private final ContatoRep contatoRep;
    private final LiveData<ArrayList<Contato>> contatos;
    public ListaDeContatosViewModel(@NonNull Application application) {
        super(application);
        contatoRep = new ContatoRep(this.getApplication());
        contatos = contatoRep.carregarContatos();
    }
    public LiveData<ArrayList<Contato>> getContatos(){return contatos;}

    public void addContato (Contato contato){contatoRep.addContato(contato);}
}
