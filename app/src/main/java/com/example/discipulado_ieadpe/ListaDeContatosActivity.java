package com.example.discipulado_ieadpe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListaDeContatosActivity extends AppCompatActivity {

    TextView tituloListaDeContatos, legendaBtnAddMembro;
    ImageButton btnAddMembro;
    RecyclerView listaDeContatosRecyclerView;
    ListaDeContatosAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_de_contatos_activity);

        tituloListaDeContatos = findViewById(R.id.titulo_lista_de_contatos);
        legendaBtnAddMembro = findViewById(R.id.sub_btn_add_membro);
        btnAddMembro = findViewById(R.id.btn_add_membro);
        //TODO Extrair dados do login em SharedPreferences,
        // verificar privilégios,
        // para talvez ocultar o botão de add membros.
        // Passar informações do login para o adapter do recyclerView.
        listaDeContatosRecyclerView = findViewById(R.id.recyclerview_lista_de_contatos);
        adapter = new ListaDeContatosAdapter(new ArrayList<>());
        listaDeContatosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaDeContatosRecyclerView.setAdapter(adapter);
        listaDeContatosRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //TODO Criar um viewmodel,
        // criar nele uma lista de contatos
        // alimentado pelo database
        // e observar mudanças para atualizar a lista de contatos

        btnAddMembro.setOnClickListener(view -> abrirActivityDeEdicaoDeMembro());
    }

    private void abrirActivityDeEdicaoDeMembro(){
        startActivity(new Intent(this, AddMembroActivity.class));
    }
}
