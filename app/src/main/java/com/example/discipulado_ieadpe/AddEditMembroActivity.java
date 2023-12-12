package com.example.discipulado_ieadpe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.discipulado_ieadpe.canarinho.validator.Validador;
import com.example.discipulado_ieadpe.canarinho.watcher.MascaraNumericaTextWatcher;
import com.example.discipulado_ieadpe.canarinho.watcher.TelefoneTextWatcher;
import com.example.discipulado_ieadpe.canarinho.watcher.evento.EventoDeValidacao;
import com.example.discipulado_ieadpe.database.entities.Contato;
import com.example.discipulado_ieadpe.viewmodels.ListaDeContatosViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//Essa Activity tanto add quanto edita membros. Pode ser inicializada com a intent vazia através do botão "add membro",
// como através do botão de editar ao lado de um contato da lista, disponível apenas para dirigentes e acima.
// Neste segundo caso, vem os dados do contato junto com a intent, que preenchem automaticamente as views, bloqueando a edição do nome do contato.
public class AddEditMembroActivity extends AppCompatActivity {

    SharedPreferences loginPreferences;

    TextView tituloNomeDoUsuario, tituloAddNomeDoMembro, tituloSpnCargo, tituloAddTelefone;
    Spinner spnCongregacao, spnCargo;
    EditText editAddNomeDoMembro, editAddTelefone;

    Button btnConcluirAddEditMembro;
    ArrayAdapter<CharSequence> spnCargoAdapter;

    ListaDeContatosViewModel viewModel;
    ArrayList<Contato> listaDeContatos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_editar_membro);
        viewModel = new ViewModelProvider(this).get(ListaDeContatosViewModel.class);
        new Thread(() -> listaDeContatos = viewModel.getContatos().getValue()).start();

        loginPreferences = getSharedPreferences("Dados do login", Context.MODE_PRIVATE);
        String usuarioLogado = loginPreferences.getString(MainActivity.CHAVE_USUARIO, MainActivity.USUARIO_PADRAO);
        if (!usuarioLogado.equals("Supervisão")){
            spnCongregacao.setVisibility(View.GONE);
            spnCargoAdapter = ArrayAdapter.createFromResource(this, R.array.funcoes_congregacao, android.R.layout.simple_spinner_item);
        } else {
            spnCongregacao = findViewById(R.id.spinner_congregacoes);
            ArrayAdapter<CharSequence>spnCongregacaoAdapter = ArrayAdapter.createFromResource(this, R.array.usuarios, android.R.layout.simple_spinner_item);
            spnCongregacaoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnCongregacao.setAdapter(spnCongregacaoAdapter);
            spnCargoAdapter = ArrayAdapter.createFromResource(this, R.array.funcoes_supervisor, android.R.layout.simple_spinner_item);
        }
        spnCargo = findViewById(R.id.spinner_cargo);
        spnCargo.setAdapter(spnCargoAdapter);
        tituloNomeDoUsuario = findViewById(R.id.nome_da_congregacao);
        tituloNomeDoUsuario.setText(usuarioLogado);
        tituloAddNomeDoMembro = findViewById(R.id.titulo_add_nome_do_membro);
        tituloSpnCargo = findViewById(R.id.titulo_spinner_cargo);
        tituloAddTelefone = findViewById(R.id.titulo_add_telefone);
        editAddNomeDoMembro = findViewById(R.id.edit_add_nome_do_membro);
        editAddTelefone = findViewById(R.id.edit_add_telefone);
        editAddTelefone.addTextChangedListener(new TelefoneTextWatcher(new EventoDeValidacao() {
            @Override
            public void invalido(String valorAtual, String mensagem) {

            }

            @Override
            public void parcialmenteValido(String valorAtual) {

            }

            @Override
            public void totalmenteValido(String valorAtual) {

            }
        }));
        btnConcluirAddEditMembro = findViewById(R.id.btn_add_edit_membro);

        btnConcluirAddEditMembro.setOnClickListener(v -> {
            Contato contato = new Contato();
            contato.congregacao = (usuarioLogado.equals("Supervisão") ? spnCongregacao.getPrompt().toString() : tituloNomeDoUsuario.getText().toString());
            contato.funcao = spnCargo.getPrompt().toString();
            contato.nomeDoMembro = editAddNomeDoMembro.getText().toString();
            //TODO Validar o telefone com canarinho
            contato.telefone = Integer.parseInt(editAddTelefone.getText().toString());

            List<Contato> contatosPorCongregacao = listaDeContatos.stream()
                    .filter(contatoGeral -> contatoGeral.congregacao.equals(contato.congregacao))
                    .collect(Collectors.toList());

            ArrayList<String> cargosPorCongregacao = new ArrayList<>();
            for (Contato contatoPorCongregacao : contatosPorCongregacao) {
                cargosPorCongregacao.add(contatoPorCongregacao.funcao);
            }
            if (!spnCargo.getPrompt().toString().equals("Professor(a) do discipulado")
                    && cargosPorCongregacao.contains(spnCargo.getPrompt().toString())) {

                Contato contatoAtualNaFuncao = new Contato();
                for (Contato contatoAtual : contatosPorCongregacao) {
                    if (contatoAtual.funcao.equals(spnCargo.getPrompt().toString())) {
                        contatoAtualNaFuncao = contatoAtual;
                        break;
                    }
                }
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setTitle("Já existe uma pessoa como " + spnCargo.getPrompt().toString());
                dialogBuilder.setMessage("É " + contatoAtualNaFuncao.nomeDoMembro + ". Deseja alterar?");
                Contato finalContatoAtualNaFuncao = contatoAtualNaFuncao;
                dialogBuilder.setPositiveButton("Alterar", (dialog, which) -> {
                    viewModel.excluirContato(finalContatoAtualNaFuncao);
                });
                dialogBuilder.setNegativeButton("Cancelar", (dialog, which) -> {
                });
                dialogBuilder.show();
            }
            if (listaDeContatos.stream().noneMatch(contato1 -> contato1.nomeDoMembro.equals(contato.nomeDoMembro))) {
                viewModel.addContato(contato);
            } else {
                viewModel.atualizarContato(contato);
            }
            finish();
        });
    }
}
    //
    // TODO Verificar se há dados de contato no intent. Ou seja, é uma edição e não uma adição.
    //  Caso sim: extrair os dados do intent e preencher as views.
    //  Bloquear a edição do nome do contato.

    //TODO Verificar duplicidade de função para Supervisor, coordenador para qualquer local,
    // e dirigente por congregação. Levar em conta os vices.
    // Caso haja duplicidade, mostrar AlertDialog com nome do anterior, perguntando se deseja alterar ou voltar
