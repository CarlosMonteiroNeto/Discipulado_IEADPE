package com.example.discipulado_ieadpe.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.discipulado_ieadpe.database.Daos.AlunoDao;
import com.example.discipulado_ieadpe.database.Daos.ContatoDao;
import com.example.discipulado_ieadpe.database.entities.Aluno;
import com.example.discipulado_ieadpe.database.entities.Contato;

@Database(entities = {Contato.class, Aluno.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    private static final Migration MIGRATION_1_2 = new MigrationFrom1To2();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "banco_de_dados")
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return INSTANCE;
    }
    public abstract ContatoDao contatoDao();
    public abstract AlunoDao alunoDao();

    public static class MigrationFrom1To2 extends Migration {

        public MigrationFrom1To2() {
            super(1, 2);
        }

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `contato_new` " +
                    "(`nome do membro` TEXT NOT NULL PRIMARY KEY, `função` TEXT, `congregação` TEXT, `telefone` TEXT)");

            database.execSQL("INSERT INTO `contato_new` (`nome do membro`, `função`, `congregação`, `telefone`) " +
                    "SELECT `nome do membro`, `função`, `congregação`, CAST(`telefone` AS TEXT) FROM `contato`");

            database.execSQL("DROP TABLE `contato`");
            database.execSQL("ALTER TABLE `contato_new` RENAME TO `contato`");
        }
    }
}
