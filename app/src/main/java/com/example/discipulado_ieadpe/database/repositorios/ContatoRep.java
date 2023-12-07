package com.example.discipulado_ieadpe.database.repositorios;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.discipulado_ieadpe.database.AppDatabase;
import com.example.discipulado_ieadpe.database.entities.Contato;

import java.util.ArrayList;

public class ContatoRep {

    AppDatabase db;
    public ContatoRep (Context context){
        db = AppDatabase.getDatabase(context);
    }

    public LiveData<ArrayList<Contato>> carregarContatos(){
        return db.contatoDao().carregarContatos();
    }

    public void addContato (Contato contato){
        db.contatoDao().inserirContato(contato);
    }

    public void deletarContato (Contato contato){
        db.contatoDao().deletarContato(contato);
    }
}
