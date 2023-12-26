package com.example.discipulado_ieadpe.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (tableName = "contato")
public class Contato implements Serializable {

    public Contato(){}

    @PrimaryKey
    @NonNull
    @ColumnInfo (name = "nome do membro")
    private String nomeDoMembro;

    @NonNull
    public String getNomeDoMembro() {
        return nomeDoMembro;
    }
    public void setNomeDoMembro(@NonNull String nomeDoMembro) {
        this.nomeDoMembro = nomeDoMembro;
    }

    @ColumnInfo (name = "função")
    private String funcao;

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    @ColumnInfo (name = "congregação")
    private String congregacao;

    public String getCongregacao() {
        return congregacao;
    }
    public void setCongregacao(String congregacao) {
        this.congregacao = congregacao;
    }

    @ColumnInfo (name = "telefone")
    private String telefone;

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    private String dataDeNascimento;

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }
}
