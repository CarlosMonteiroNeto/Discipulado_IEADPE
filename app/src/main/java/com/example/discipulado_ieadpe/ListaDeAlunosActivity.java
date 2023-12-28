package com.example.discipulado_ieadpe;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.discipulado_ieadpe.database.entities.Aluno;
import com.example.discipulado_ieadpe.viewmodels.ListaDeAlunosViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListaDeAlunosActivity extends AppCompatActivity {

    TextView tituloListaDeAlunos, subBtnAddAluno;
    ImageButton btnAddAluno;
    SharedPreferences sharedPreferences;
    RecyclerView listaDeAlunosRecycler;
    ListaDeAlunosAdapter adapter;
    List<Aluno> alunos;
    ListaDeAlunosViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_alunos);
        tituloListaDeAlunos = findViewById(R.id.titulo_lista_de_alunos);
        subBtnAddAluno = findViewById(R.id.sub_btn_add_aluno);
        btnAddAluno = findViewById(R.id.btn_add_aluno);
        sharedPreferences = getSharedPreferences(MainActivity.DADOS_DE_LOGIN, Context.MODE_PRIVATE);
        String usuarioLogado = sharedPreferences.getString(MainActivity.CHAVE_USUARIO, MainActivity.USUARIO_PADRAO);

        alunos = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(ListaDeAlunosViewModel.class);
        viewModel.carregarAlunos(usuarioLogado);
        listaDeAlunosRecycler = findViewById(R.id.recyclerview_lista_de_alunos);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK){
                Intent dados = result.getData();
                if(dados != null && dados.hasExtra("aluno a adicionar")){
                    if(dados.hasExtra("aluno a excluir")){
                        Aluno alunoAExcluir = (Aluno) dados.getSerializableExtra("aluno a excluir");
                        viewModel.deletarAluno(alunoAExcluir);
                    }
                    Aluno alunoNovo = (Aluno)dados.getSerializableExtra("aluno a adicionar");
                    viewModel.addAluno(alunoNovo);
                }
            }
        });

        adapter = new ListaDeAlunosAdapter(alunos, usuarioLogado, viewModel, launcher);
        listaDeAlunosRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listaDeAlunosRecycler.setAdapter(adapter);
        listaDeAlunosRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        listaDeAlunosRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        viewModel.getMensagemDeExclusao().observe(ListaDeAlunosActivity.this, mensagem -> {
            if (mensagem != null) {
                Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getMensagemDeEdicao().observe(this, mensagem -> {
            if (mensagem != null) {
                Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getAlunosPorCongregacao().observe(this, alunos -> adapter.atualizarItens(alunos));

        btnAddAluno.setOnClickListener(view -> abrirActivityDeEdicaoDeAluno(launcher));
    }
    private void abrirActivityDeEdicaoDeAluno(ActivityResultLauncher<Intent> launcher){
        launcher.launch(new Intent(this, AddEditAlunoActivity.class));
    }
}