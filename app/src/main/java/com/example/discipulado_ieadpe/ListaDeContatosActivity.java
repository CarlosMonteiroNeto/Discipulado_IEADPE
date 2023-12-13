package com.example.discipulado_ieadpe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discipulado_ieadpe.viewmodels.ListaDeContatosViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class ListaDeContatosActivity extends AppCompatActivity {

    TextView tituloListaDeContatos, legendaBtnAddMembro;
    ImageButton btnAddMembro;
    SharedPreferences sharedPreferences;
    RecyclerView listaDeContatosRecyclerView;
    ListaDeContatosAdapter adapter;

    ListaDeContatosViewModel viewModel;

    public static final String CHAVE_INTENT_DADOS_DO_MEMBRO = "CHAVE_INTENT_DADOS_DO_MEMBRO";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_de_contatos_activity);

        viewModel = new ViewModelProvider(this).get(ListaDeContatosViewModel.class);
        tituloListaDeContatos = findViewById(R.id.titulo_lista_de_contatos);
        legendaBtnAddMembro = findViewById(R.id.sub_btn_add_membro);
        btnAddMembro = findViewById(R.id.btn_add_membro);

        sharedPreferences = getSharedPreferences("dados de login", Context.MODE_PRIVATE);
        String usuarioLogado = sharedPreferences.getString(MainActivity.CHAVE_USUARIO, MainActivity.USUARIO_PADRAO);
        if (!Objects.equals(usuarioLogado, "Supervisão")){btnAddMembro.setVisibility(View.INVISIBLE);}

        listaDeContatosRecyclerView = findViewById(R.id.recyclerview_lista_de_contatos);
        adapter = new ListaDeContatosAdapter(new ArrayList<>(), usuarioLogado, viewModel);
        listaDeContatosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaDeContatosRecyclerView.setAdapter(adapter);
        listaDeContatosRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //TODO Precisa abrir outra Thread?
        viewModel.getContatos().observe(ListaDeContatosActivity.this, contatos -> adapter.atualizarItens(contatos));

        btnAddMembro.setOnClickListener(view -> abrirActivityDeEdicaoDeMembro());
    }

    private void abrirActivityDeEdicaoDeMembro(){
        startActivity(new Intent(this, AddEditMembroActivity.class));
    }
}
