package com.example.discipulado_ieadpe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import com.example.discipulado_ieadpe.database.entities.Contato;
import com.example.discipulado_ieadpe.viewmodels.ListaDeContatosViewModel;
import com.vicmikhailau.maskededittext.MaskedEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//Essa Activity tanto add quanto edita membros. Pode ser inicializada com a intent vazia através do botão "add membro",
// como através do botão de editar ao lado de um contato da lista, disponível apenas para usuários com permissão.
// Neste segundo caso, vem os dados do contato junto com a intent, que preenchem automaticamente as views, bloqueando a edição do nome do contato.
public class AddEditMembroActivity extends AppCompatActivity {
    TextView tituloNomeDoUsuario, tituloAddNomeDoMembro, tituloSpnCargo, tituloAddTelefone, nomeDaCongregacao;
    Spinner spnCongregacao, spnCargo;
    EditText editAddNomeDoMembro;
    MaskedEditText editAddTelefone;
    Button btnConcluirAddEditMembro;
    ArrayAdapter<CharSequence> spnCargoAdapter;
    ListaDeContatosViewModel viewModel;
    List<Contato> listaDeContatos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_editar_membro);
        viewModel = new ViewModelProvider(this).get(ListaDeContatosViewModel.class);
        new Thread(() -> {
            listaDeContatos = viewModel.getContatos().getValue();
            if (listaDeContatos==null){
//                Contato contatoVazio = new Contato();
//                contatoVazio.nomeDoMembro = "Sem nome";
                listaDeContatos=new ArrayList<>();
            }
        }).start();

        SharedPreferences sharedPreferences = getSharedPreferences("dados de login", Context.MODE_PRIVATE);
        String usuarioLogado = sharedPreferences.getString(MainActivity.CHAVE_USUARIO, MainActivity.USUARIO_PADRAO);
        Log.d("ValorUsuarioLogado", usuarioLogado);
        spnCongregacao = findViewById(R.id.spinner_congregacoes);
        if (!usuarioLogado.equals("Supervisão")){
            spnCongregacao.setVisibility(View.GONE);
            spnCargoAdapter = ArrayAdapter.createFromResource(this, R.array.funcoes_congregacao, android.R.layout.simple_spinner_item);
        } else {
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

        Intent intent = getIntent();
        if (intent.getSerializableExtra(ListaDeContatosActivity.CHAVE_INTENT_DADOS_DO_MEMBRO)!= null){
            Contato contatoAEditar = (Contato) intent.getSerializableExtra(ListaDeContatosActivity.CHAVE_INTENT_DADOS_DO_MEMBRO);
            editAddTelefone.setText(contatoAEditar.telefone);
            editAddNomeDoMembro.setText(contatoAEditar.nomeDoMembro);
            editAddNomeDoMembro.setFocusable(false); // Impede o foco
            editAddNomeDoMembro.setClickable(false); // Impede cliques
            editAddNomeDoMembro.setCursorVisible(false); // Esconde o cursor
            editAddNomeDoMembro.setTextColor(Color.GRAY);
        }
        btnConcluirAddEditMembro = findViewById(R.id.btn_add_edit_membro);

        btnConcluirAddEditMembro.setOnClickListener(v -> {
            if (editAddNomeDoMembro.getText().toString().trim().isEmpty()
                    || !numeroCelularEhValido(Objects.requireNonNull(editAddTelefone.getText()).toString())){
                if (editAddNomeDoMembro.getText().toString().trim().isEmpty()){
                    editAddNomeDoMembro.setError("Insira um nome");
                }
                if (!numeroCelularEhValido(Objects.requireNonNull(editAddTelefone.getText()).toString())) {
                    editAddTelefone.setError("Número inválido");
                }
                return;
            }
            Contato contatoNovo = new Contato();
            contatoNovo.congregacao = (usuarioLogado.equals("Supervisão") ? spnCongregacao.getSelectedItem().toString() : tituloNomeDoUsuario.getText().toString());
            contatoNovo.funcao = spnCargo.getSelectedItem().toString();
            contatoNovo.nomeDoMembro = editAddNomeDoMembro.getText().toString();
            contatoNovo.telefone = editAddTelefone.getUnMaskedText();

            Log.d("Lista de contatos", listaDeContatos.stream().findFirst().get().nomeDoMembro);
            List<Contato> contatosPorCongregacao = listaDeContatos.stream()
                    .filter(contatoGeral -> contatoGeral.congregacao.equals(contatoNovo.congregacao))
                    .collect(Collectors.toList());

            ArrayList<String> cargosPorCongregacao = new ArrayList<>();
            for (Contato contatoPorCongregacao : contatosPorCongregacao) {
                cargosPorCongregacao.add(contatoPorCongregacao.funcao);
            }
            // Garante que só há uma pessoa por cargo em cada congregação, exceto para professor, que pode haver vários.
            if (!contatoNovo.funcao.equals("Professor(a) do discipulado")
                    && cargosPorCongregacao.contains(contatoNovo.funcao)) {

                Contato contatoAtualNaFuncao = new Contato();
                for (Contato contatoAtual : contatosPorCongregacao) {
                    if (contatoAtual.funcao.equals(contatoNovo.funcao)) {
                        contatoAtualNaFuncao = contatoAtual;
                        break;
                    }
                }
                //Caso já haja alguém com esta função na congregação, possibilita excluir o membro antigo e logo após,
                // adicionar ou atualizar o cargo do membro novo.
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setTitle("Já existe uma pessoa como " + contatoNovo.funcao);
                dialogBuilder.setMessage("É " + contatoAtualNaFuncao.nomeDoMembro + ". Deseja alterar?");
                Contato finalContatoAtualNaFuncao = contatoAtualNaFuncao;
                dialogBuilder.setPositiveButton("Alterar", (dialog, which) -> {
                    new Thread(() -> viewModel.excluirContato(finalContatoAtualNaFuncao)).start();
                });
                dialogBuilder.setNegativeButton("Não", (dialogInterface, i) -> {
                    // Fecha o diálogo quando o botão "Cancelar" é pressionado
                    dialogInterface.dismiss();
                });
                dialogBuilder.show();
            }
            new Thread(() -> viewModel.atualizarContato(contatoNovo)).start();

            finish();
        });
    }
    public boolean numeroCelularEhValido (String numero) {
        // Expressão regular para validar números de celular brasileiros
        String regex = "^\\([1-9]{2}\\)\\s9\\s[6-9]{1}[0-9]{3}\\-[0-9]{4}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(numero);

        return matcher.matches();
    }
}