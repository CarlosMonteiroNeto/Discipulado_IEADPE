package com.example.discipulado_ieadpe.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.discipulado_ieadpe.database.Daos.AlunoDao;
import com.example.discipulado_ieadpe.database.Daos.ContatoDao;
import com.example.discipulado_ieadpe.database.entities.Aluno;
import com.example.discipulado_ieadpe.database.entities.Contato;

@Database(entities = {Contato.class, Aluno.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "banco_de_dados")
                    .build();
        }
        return INSTANCE;
    }
    public abstract ContatoDao contatoDao();
    public abstract AlunoDao alunoDao();
}
