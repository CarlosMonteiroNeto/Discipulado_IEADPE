package com.example.discipulado_ieadpe.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Map;

@Entity (tableName = "aluno")
public class Aluno implements Serializable {

    public Aluno(){}

    @PrimaryKey
    private String ID;
    @ColumnInfo(name = "nome do aluno")
    private String nomeDoAluno;
    private String congregacao;
    private String turma;
    private String endereco;
    private String telefone;
    private String escolaridade;
    private String bairro;
    private String cidade;
    private String CEP;
    private String UF;
    private String estadoCivil;
    private String dataDeNascimento;
    private String dataDeInicio;
    private String dataDeTermino;
    private boolean ehNovoConvertido;
    private boolean ehBatizadoNasAguas;
    private boolean desejaSeBatizar;
    Map<String, Boolean> aulasAssistidas;

    private String notaBasico;
    private String notaIntermediario;
    private String notaAvancado;

    public String getNomeDoAluno() {
        return nomeDoAluno;
    }
    public void setNomeDoAluno(String nomeDoAluno) {
        this.nomeDoAluno = nomeDoAluno;
    }
    public String getID() {
        return ID;
    }
    public String getCongregacao() {
        return congregacao;
    }
    public String getBairro() {
        return bairro;
    }
    public boolean isBatizadoNasAguas() {
        return ehBatizadoNasAguas;
    }
    public boolean isDesejaSeBatizar() {
        return desejaSeBatizar;
    }
    public boolean isNovoConvertido() {
        return ehNovoConvertido;
    }
    public Map<String, Boolean> getAulasAssistidas() {
        return aulasAssistidas;
    }
    public String getCEP() {
        return CEP;
    }
    public String getCidade() {
        return cidade;
    }
    public String getDataDeInicio() {
        return dataDeInicio;
    }
    public String getDataDeNascimento() {
        return dataDeNascimento;
    }
    public String getDataDeTermino() {
        return dataDeTermino;
    }
    public String getEndereco() {
        return endereco;
    }
    public String getEscolaridade() {
        return escolaridade;
    }
    public String getEstadoCivil() {
        return estadoCivil;
    }
    public String getTelefone() {
        return telefone;
    }
    public String getUF() {
        return UF;
    }
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    public void setAulasAssistidas(Map<String, Boolean> aulasAssistidas) {
        this.aulasAssistidas = aulasAssistidas;
    }
    public void setCEP(String CEP) {
        this.CEP = CEP;
    }
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    public void setCongregacao(String congregacao) {
        this.congregacao = congregacao;
    }
    public void setDataDeInicio(String dataDeInicio) {
        this.dataDeInicio = dataDeInicio;
    }
    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }
    public void setDataDeTermino(String dataDeTermino) {
        this.dataDeTermino = dataDeTermino;
    }

    public void setDesejaSeBatizar(boolean desejaSeBatizar) {
        this.desejaSeBatizar = desejaSeBatizar;
    }
    public void setEhBatizadoNasAguas(boolean ehBatizadoNasAguas) {
        this.ehBatizadoNasAguas = ehBatizadoNasAguas;
    }
    public void setEhNovoConvertido(boolean ehNovoConvertido) {
        this.ehNovoConvertido = ehNovoConvertido;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public void setUF(String UF) {
        this.UF = UF;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getNotaBasico() {
        return notaBasico;
    }

    public void setNotaBasico(String notaBasico) {
        this.notaBasico = notaBasico;
    }

    public String getNotaIntermediario() {
        return notaIntermediario;
    }

    public void setNotaIntermediario(String notaIntermediario) {
        this.notaIntermediario = notaIntermediario;
    }

    public String getNotaAvancado() {
        return notaAvancado;
    }

    public void setNotaAvancado(String notaAvancado) {
        this.notaAvancado = notaAvancado;
    }
}
