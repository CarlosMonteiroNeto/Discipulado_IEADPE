package com.example.discipulado_ieadpe.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "contato")
public class Contato {

    @PrimaryKey
    @ColumnInfo (name = "nome do membro")
    public String nomeDoMembro;

    @ColumnInfo (name = "função")
    public String funcao;

    @ColumnInfo (name = "congregação")
    public String congregacao;

    @ColumnInfo (name = "telefone")
    public int telefone;
}
