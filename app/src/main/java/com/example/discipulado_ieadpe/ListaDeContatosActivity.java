package com.example.discipulado_ieadpe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discipulado_ieadpe.database.entities.Contato;
import com.example.discipulado_ieadpe.viewmodels.ListaDeContatosViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListaDeContatosActivity extends AppCompatActivity {

    TextView tituloListaDeContatos, legendaBtnAddMembro;
    ImageButton btnAddMembro;
    SharedPreferences sharedPreferences;
    RecyclerView listaDeContatosRecyclerView;
    ListaDeContatosAdapter adapter;
    ListaDeContatosViewModel viewModel;
    List<Contato> contatos;

    public static final String CHAVE_INTENT_DADOS_DO_MEMBRO = "CHAVE_INTENT_DADOS_DO_MEMBRO";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_de_contatos_activity);

//        viewModel = new ViewModelProvider(this).get(ListaDeContatosViewModel.class);
        tituloListaDeContatos = findViewById(R.id.titulo_lista_de_contatos);
        legendaBtnAddMembro = findViewById(R.id.sub_btn_add_membro);
        btnAddMembro = findViewById(R.id.btn_add_membro);

        sharedPreferences = getSharedPreferences(MainActivity.DADOS_DE_LOGIN, Context.MODE_PRIVATE);
        String usuarioLogado = sharedPreferences.getString(MainActivity.CHAVE_USUARIO, MainActivity.USUARIO_PADRAO);
        Log.d("Usuário Logado", usuarioLogado);
        if (Objects.equals(usuarioLogado, MainActivity.USUARIO_PADRAO)) {
            btnAddMembro.setVisibility(View.INVISIBLE);
            legendaBtnAddMembro.setVisibility(View.INVISIBLE);
        }
        contatos = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(ListaDeContatosViewModel.class);
        listaDeContatosRecyclerView = findViewById(R.id.recyclerview_lista_de_contatos);
        adapter = new ListaDeContatosAdapter(contatos, usuarioLogado, viewModel);
        listaDeContatosRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listaDeContatosRecyclerView.setAdapter(adapter);
        listaDeContatosRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        listaDeContatosRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        viewModel.getMensagemDeExclusao().observe(ListaDeContatosActivity.this, mensagem -> {
            if (mensagem != null) {
                Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getMensagemDeEdicao().observe(this, mensagem -> {
            if (mensagem != null) {
                Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getContatos().observe(this, contatos -> adapter.atualizarItens(contatos));

//        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            if (result.getResultCode() == Activity.RESULT_OK){
//                Intent dados = result.getData();
//                if(dados != null){
//                    Toast.makeText(this, dados.getStringExtra("mensagem de sucesso"), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        btnAddMembro.setOnClickListener(view -> abrirActivityDeEdicaoDeMembro());
    }
    private void abrirActivityDeEdicaoDeMembro(){
        startActivity(new Intent(this, AddEditMembroActivity.class));
    }
}
