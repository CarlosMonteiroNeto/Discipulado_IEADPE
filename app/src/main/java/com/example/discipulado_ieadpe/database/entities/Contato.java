package com.example.discipulado_ieadpe.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (tableName = "contato")
public class Contato implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo (name = "nome do membro")
    public String nomeDoMembro;

    @ColumnInfo (name = "função")
    public String funcao;

    @ColumnInfo (name = "congregação")
    public String congregacao;

    @ColumnInfo (name = "telefone")
    public String telefone;
}
