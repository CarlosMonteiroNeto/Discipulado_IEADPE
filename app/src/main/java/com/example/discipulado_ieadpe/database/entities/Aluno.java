package com.example.discipulado_ieadpe.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (tableName = "aluno")
public class Aluno implements Serializable {

    public Aluno(){}

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "nome do aluno")
    private String nomeDoAluno;

    @NonNull
    public String getNomeDoAluno() {
        return nomeDoAluno;
    }
    public void setNomeDoAluno(@NonNull String nomeDoAluno) {
        this.nomeDoAluno = nomeDoAluno;
    }
}
