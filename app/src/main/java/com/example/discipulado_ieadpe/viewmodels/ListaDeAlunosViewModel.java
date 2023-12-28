package com.example.discipulado_ieadpe.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.discipulado_ieadpe.database.entities.Aluno;
import com.example.discipulado_ieadpe.database.repositorios.RepositorioGeral;

import java.util.ArrayList;
import java.util.List;

public class ListaDeAlunosViewModel extends AndroidViewModel {

    private final RepositorioGeral repositorioGeral;
    private MutableLiveData<List<Aluno>> alunosPorCongregacao;
    private MutableLiveData<String> mensagemDeExclusao = new MutableLiveData<>();
    private MutableLiveData<String> mensagemDeEdicao = new MutableLiveData<>();

    public ListaDeAlunosViewModel(@NonNull Application application) {
        super(application);
        repositorioGeral = new RepositorioGeral();
    }

    public LiveData<List<Aluno>> getAlunosPorCongregacao(){return alunosPorCongregacao;}

    public void carregarAlunos(String congregacao){
        alunosPorCongregacao = repositorioGeral.carregarAlunosPorCongregacao(congregacao);
    }
    public void setAlunos(MutableLiveData<List<Aluno>> alunos) {
        this.alunosPorCongregacao = alunos;
    }

    public void addAluno (Aluno aluno){
        List<Aluno> alunosAtuais = alunosPorCongregacao.getValue();
        if (alunosAtuais == null){
            alunosAtuais = new ArrayList<>();
        }
        mensagemDeEdicao = repositorioGeral.addAluno(aluno);
//        if(mensagem.getValue().equals("Contato adicionado com sucesso")){
        boolean substituir = false;
        for (Aluno alunoExistente : alunosAtuais){
            if (alunoExistente.getID().equals(aluno.getID())){
                alunosAtuais.set(alunosAtuais.indexOf(alunoExistente), aluno);
                substituir = true;
                break;
            }
        }
        if (!substituir){
            alunosAtuais.add(aluno);
        }
        alunosPorCongregacao.setValue(alunosAtuais);
//        }
    }

    //    public int atualizarContato (Contato contato){
//        return repositorioGeral.atualizarContato(contato);
//    }
    public void deletarAluno (Aluno aluno){
        List<Aluno> alunosAtuais = alunosPorCongregacao.getValue();
        mensagemDeExclusao = repositorioGeral.deletarAluno(aluno);
//        if(mensagemDeExclusao != null
//                && mensagemDeExclusao.getValue() != null
//                && mensagemDeExclusao.getValue().equals("Membro excluído com sucesso")){
        alunosAtuais.remove(aluno);
        alunosPorCongregacao.setValue(alunosAtuais);
//        }
    }
    public MutableLiveData<String> getMensagemDeExclusao(){
        return mensagemDeExclusao;
    }

    public MutableLiveData<String> getMensagemDeEdicao() {
        return mensagemDeEdicao;
    }

}
