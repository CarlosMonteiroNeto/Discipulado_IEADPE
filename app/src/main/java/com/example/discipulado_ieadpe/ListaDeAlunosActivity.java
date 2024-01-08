package com.example.discipulado_ieadpe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.discipulado_ieadpe.database.entities.Aluno;
import com.example.discipulado_ieadpe.viewmodels.ListaDeAlunosViewModel;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class ListaDeAlunosActivity extends AppCompatActivity implements AddEditAlunoFragment.OnAlunoCreatedListener{

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
        FragmentManager fm = getSupportFragmentManager();

        alunos = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(ListaDeAlunosViewModel.class);
        viewModel.carregarAlunos(usuarioLogado);
        listaDeAlunosRecycler = findViewById(R.id.recyclerview_lista_de_alunos);

//        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            if (result.getResultCode() == Activity.RESULT_OK){
//                Intent dados = result.getData();
//                if(dados != null && dados.hasExtra("aluno a adicionar")){
//                    if(dados.hasExtra("aluno a excluir")){
//                        Aluno alunoAExcluir = (Aluno) dados.getSerializableExtra("aluno a excluir");
//                        viewModel.deletarAluno(alunoAExcluir);
//                    }
//                    Aluno alunoNovo = (Aluno)dados.getSerializableExtra("aluno a adicionar");
//                    viewModel.addAluno(alunoNovo);
//                }
//            }
//        });
        adapter = new ListaDeAlunosAdapter(alunos);
        adapter.setOnWriteClickListener(new ListaDeAlunosAdapter.OnWriteClickListener() {
            @Override
            public void onUpdateClick(Aluno aluno) {
                AddEditAlunoFragment fragment = new AddEditAlunoFragment();
                Bundle bundle = new Bundle();
                bundle.putString(MainActivity.CHAVE_USUARIO, usuarioLogado);
                bundle.putSerializable("objeto", aluno);
                fragment.setArguments(bundle);
                fm.beginTransaction().add(R.id.fragment_lista, fragment)
                        .addToBackStack(null).commit();
            }
            @Override
            public void onDeleteClick(int position) {
                viewModel.deletarAluno(position);
            }
        });
        listaDeAlunosRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listaDeAlunosRecycler.setAdapter(adapter);
        listaDeAlunosRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        listaDeAlunosRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

//        viewModel.getMensagemDeExclusao().observe(ListaDeAlunosActivity.this, mensagem -> {
//            if (mensagem != null) {
//                Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
//            }
//        });
//        viewModel.getMensagemDeEdicao().observe(this, mensagem -> {
//            if (mensagem != null) {
//                Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
//            }
//        });

        viewModel.getAlunosPorCongregacao().observe(this, alunos -> adapter.atualizarItens(alunos));

        //Esse Bundle é retornado das 3 operações do Firestore: add, update e delete. O RecyclerView só é atualizado se a mensagem não for de erro.
        //Add: só retorna "objeto". delete: só retorna "position". update: retorna "objeto" e "position". Por isso a seleção abaixo.
        //O AddEditFragment é fechado caso uma dessas duas operaç~eos tenham sucesso.
        viewModel.getWritePackage().observe(this, bundle -> {
            List<Aluno> lista = viewModel.getAlunosPorCongregacao().getValue();
            if(!bundle.getString("mensagem").startsWith("Erro")){
                if (bundle.containsKey("position")){
                    if (bundle.containsKey("objeto")){
                        lista.set(bundle.getInt("position"), (Aluno)bundle.getSerializable("objeto"));
                        fm.beginTransaction().remove(fm.findFragmentById(R.id.fragment_lista)).commit();
                    } else {
                        lista.remove(bundle.getInt("position"));
                    }
                } else {
                    lista.add((Aluno)bundle.getSerializable("objeto"));
                    fm.beginTransaction().remove(fm.findFragmentById(R.id.fragment_lista)).commit();
                }
                viewModel.setAlunosPorCongregacao(lista);
            }
            Toast.makeText(this, bundle.getString("mensagem"), Toast.LENGTH_SHORT).show();
        });

        btnAddAluno.setOnClickListener(view -> {
            AddEditAlunoFragment fragment = new AddEditAlunoFragment();
            Bundle bundle = new Bundle();
            bundle.putString(MainActivity.CHAVE_USUARIO, usuarioLogado);
            fragment.setArguments(bundle);
            fm.beginTransaction().add(R.id.fragment_lista, fragment)
                    .addToBackStack(null).commit();
        });
    }
//    private void abrirActivityDeEdicaoDeAluno(ActivityResultLauncher<Intent> launcher){
//        launcher.launch(new Intent(this, AddEditAlunoFragment.class));
//    }

    //O aluno só tem id se já vier do Firestore. O id é o documentID do firestore, passado para o objeto na hora que ele é baixado.
    // Se o id é nulo, é porquê não está no Firestore ainda.
    @Override
    public void OnAlunoCreated(Aluno aluno) {
        if (aluno.getID() != null){
            viewModel.atualizarAluno(aluno);
        } else {
            viewModel.addAluno(aluno);
        }
    }
}