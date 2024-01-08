package com.example.discipulado_ieadpe.viewmodels;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.discipulado_ieadpe.database.entities.Aluno;
import com.example.discipulado_ieadpe.database.repositorios.RepositorioGeral;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class ListaDeAlunosViewModel extends AndroidViewModel {

    private final RepositorioGeral repositorioGeral;
    private String usuarioLogado;
    private MutableLiveData<List<Aluno>> alunosPorCongregacao = new MutableLiveData<>();
    private MutableLiveData<Bundle> writePackage = new MutableLiveData<>();
//    private MutableLiveData<String> mensagemDeExclusao = new MutableLiveData<>();
//    private MutableLiveData<String> mensagemDeEdicao = new MutableLiveData<>();

    public ListaDeAlunosViewModel(@NonNull Application application) {
        super(application);
        repositorioGeral = new RepositorioGeral();
    }

    public LiveData<List<Aluno>> getAlunosPorCongregacao(){return alunosPorCongregacao;}

    public void setAlunosPorCongregacao(List<Aluno> alunosPorCongregacao) {
        this.alunosPorCongregacao.setValue(alunosPorCongregacao);
    }

    public void carregarAlunos(String congregacao){
        alunosPorCongregacao = repositorioGeral.carregarAlunosPorCongregacao(congregacao);
        this.usuarioLogado = congregacao;
    }

    public MutableLiveData<Bundle> getWritePackage() {
        return writePackage;
    }

    public void addAluno (Aluno aluno){
//        List<Aluno> alunosAtuais = alunosPorCongregacao.getValue();
//        if (alunosAtuais == null){
//            alunosAtuais = new ArrayList<>();
//        }
        writePackage.setValue(repositorioGeral.addAluno(usuarioLogado, aluno));

//        boolean substituir = false;
//        for (Aluno alunoExistente : alunosAtuais){
//            if (alunoExistente.getID().equals(aluno.getID())){
//                alunosAtuais.set(alunosAtuais.indexOf(alunoExistente), aluno);
//                substituir = true;
//                break;
//            }
//        }
//        if (!substituir){
//            alunosAtuais.add(aluno);
//        }
//        alunosPorCongregacao.setValue(alunosAtuais);
    }

    public void atualizarAluno(Aluno aluno){
        OptionalInt OptPosition = IntStream.range(0, alunosPorCongregacao.getValue().size())
                .filter(i -> alunosPorCongregacao.getValue().get(i).getID().equals(aluno.getID()))
                .findFirst();
        int position = OptPosition.getAsInt();
        writePackage.setValue(repositorioGeral.atualizarAluno(usuarioLogado, aluno, position));
    }

    //    public int atualizarContato (Contato contato){
//        return repositorioGeral.atualizarContato(contato);
//    }
    public void deletarAluno (int position){
        String idDoAlunoAExcluir = alunosPorCongregacao.getValue().get(position).getID();
        writePackage.setValue(repositorioGeral.deletarAluno(usuarioLogado, idDoAlunoAExcluir, position));

//        List<Aluno> alunosAtuais = alunosPorCongregacao.getValue();
//        mensagemDeExclusao = repositorioGeral.deletarAluno(aluno);
//        alunosAtuais.remove(aluno);
//        alunosPorCongregacao.setValue(alunosAtuais);
    }
//    public MutableLiveData<String> getMensagemDeExclusao(){
//        return mensagemDeExclusao;
//    }

//    public MutableLiveData<String> getMensagemDeEdicao() {
//        return mensagemDeEdicao;
//    }

}
