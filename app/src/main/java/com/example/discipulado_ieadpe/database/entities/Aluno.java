package com.example.discipulado_ieadpe.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "aluno")
public class Aluno {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "nome do aluno")
    public String nomeDoAluno;
}
