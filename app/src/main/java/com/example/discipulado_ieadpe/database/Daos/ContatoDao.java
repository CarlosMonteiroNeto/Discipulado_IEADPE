package com.example.discipulado_ieadpe.database.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.discipulado_ieadpe.database.entities.Contato;

import java.util.ArrayList;

@Dao
public interface ContatoDao {

    @Insert
    void inserirContato (Contato contato);

    @Query("SELECT * FROM contato")
    LiveData<ArrayList<Contato>> carregarContatos();

    @Update
    int atualizarContato(Contato contato);

    @Delete
    void deletarContato (Contato contato);

}
