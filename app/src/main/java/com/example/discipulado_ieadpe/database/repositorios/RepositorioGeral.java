package com.example.discipulado_ieadpe.database.repositorios;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.discipulado_ieadpe.database.AppDatabase;
import com.example.discipulado_ieadpe.database.entities.Contato;

import java.util.ArrayList;
import java.util.List;

public class RepositorioGeral {

    AppDatabase db;
    public RepositorioGeral(Context context){
        db = AppDatabase.getDatabase(context);
    }

    public LiveData<List<Contato>> carregarContatos(){
        return db.contatoDao().carregarContatos();
    }
    public void addContato (Contato contato){
        db.contatoDao().inserirContato(contato);
    }
    public int atualizarContato (Contato contato){
       return db.contatoDao().atualizarContato(contato);
    }

    public void deletarContato (Contato contato){
        db.contatoDao().deletarContato(contato);
    }
}
