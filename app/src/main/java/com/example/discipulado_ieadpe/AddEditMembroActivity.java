package com.example.discipulado_ieadpe;

import android.app.Activity;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import com.example.discipulado_ieadpe.database.entities.Contato;
import com.example.discipulado_ieadpe.viewmodels.ListaDeContatosViewModel;
import com.vicmikhailau.maskededittext.MaskedEditText;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//Essa Activity tanto add quanto edita membros. Pode ser inicializada com a intent vazia através do botão "add membro",
// como através do botão de editar ao lado de um contato da lista, disponível apenas para usuários com permissão.
// Neste segundo caso, vem os dados do contato junto com a intent, que preenchem automaticamente as views, bloqueando a edição do nome do contato.
public class AddEditMembroActivity extends AppCompatActivity {
    TextView tituloNomeDoUsuario, tituloAddNomeDoMembro, tituloSpnCargo, tituloAddTelefone;
    Spinner spnCongregacao, spnCargo;
    EditText editAddNomeDoMembro;
    MaskedEditText maskedEditAddTelefone;
    Button btnConcluirAddEditMembro;
    ArrayAdapter<CharSequence> spnCargoAdapter;
    ListaDeContatosViewModel viewModel;
    List<Contato> listaDeContatos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_editar_membro);
        viewModel = new ViewModelProvider(this).get(ListaDeContatosViewModel.class);
        viewModel.getContatos().observe(this, contatos -> listaDeContatos = contatos);
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.DADOS_DE_LOGIN, Context.MODE_PRIVATE);
        String usuarioLogado = sharedPreferences.getString(MainActivity.CHAVE_USUARIO, MainActivity.USUARIO_PADRAO);
        Log.d("ValorUsuarioLogado", usuarioLogado);
        spnCongregacao = findViewById(R.id.spinner_congregacoes);
        if (!usuarioLogado.equals("Supervisão")) {
            spnCongregacao.setVisibility(View.GONE);
            spnCargoAdapter = ArrayAdapter.createFromResource(this, R.array.funcoes_congregacao, android.R.layout.simple_spinner_item);
        } else {
            ArrayAdapter<CharSequence> spnCongregacaoAdapter = ArrayAdapter.createFromResource(this, R.array.usuarios, android.R.layout.simple_spinner_item);
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
        maskedEditAddTelefone = findViewById(R.id.edit_add_telefone);

        Intent intent = getIntent();
        if (intent.getSerializableExtra(ListaDeContatosActivity.CHAVE_INTENT_DADOS_DO_MEMBRO) != null) {
            Contato contatoAEditar = (Contato) intent.getSerializableExtra(ListaDeContatosActivity.CHAVE_INTENT_DADOS_DO_MEMBRO);
            assert contatoAEditar != null;
            maskedEditAddTelefone.setText(Utils.desformatarTelefone(contatoAEditar.getTelefone()));
            editAddNomeDoMembro.setText(contatoAEditar.getNomeDoMembro());
            editAddNomeDoMembro.setFocusable(false); // Impede o foco
            editAddNomeDoMembro.setClickable(false); // Impede cliques
            editAddNomeDoMembro.setCursorVisible(false); // Esconde o cursor
            editAddNomeDoMembro.setTextColor(Color.GRAY);
        }
        btnConcluirAddEditMembro = findViewById(R.id.btn_add_edit_membro);

        btnConcluirAddEditMembro.setOnClickListener(v -> {
            if (editAddNomeDoMembro.getText().toString().trim().isEmpty()
                    || !numeroCelularEhValido(Objects.requireNonNull(maskedEditAddTelefone.getText()).toString())) {
                if (editAddNomeDoMembro.getText().toString().trim().isEmpty()) {
                    editAddNomeDoMembro.setError("Insira um nome");
                }
                if (!numeroCelularEhValido(Objects.requireNonNull(maskedEditAddTelefone.getText()).toString())) {
                    maskedEditAddTelefone.setError("Número inválido");
                }
                return;
            }
            Contato contatoNovo = new Contato();
            contatoNovo.setCongregacao(usuarioLogado.equals("Supervisão") ? spnCongregacao.getSelectedItem().toString() : tituloNomeDoUsuario.getText().toString());
            contatoNovo.setFuncao(spnCargo.getSelectedItem().toString());
            contatoNovo.setNomeDoMembro(editAddNomeDoMembro.getText().toString());
            contatoNovo.setTelefone(maskedEditAddTelefone.getText().toString());
//            viewModel.getContatos().observe(this, contatos -> {
//                listaDeContatos = contatos;
                List<Contato> contatosPorCongregacao = listaDeContatos.stream()
                        .filter(contatoGeral -> contatoGeral.getCongregacao().equals(contatoNovo.getCongregacao()))
                        .collect(Collectors.toList());

                List<String> cargosPorCongregacao = contatosPorCongregacao
                        .stream()
                        .map(Contato::getFuncao)
                        .distinct()
                        .collect(Collectors.toList());
                if (!contatoNovo.getFuncao().equals("Professor(a) do discipulado")
                        && cargosPorCongregacao.contains(contatoNovo.getFuncao())) {


                    Contato contatoAtualNaFuncao = new Contato();
                    for (Contato contatoAtual : contatosPorCongregacao) {
                        if (contatoAtual.getFuncao().equals(contatoNovo.getFuncao())) {
                            contatoAtualNaFuncao = contatoAtual;
                            break;
                        }
                    }
                    //Caso já haja alguém com esta função na congregação, possibilita excluir o membro antigo e logo após,
                    // adicionar ou atualizar o cargo do membro novo.
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    dialogBuilder.setTitle("Já existe uma pessoa como " + contatoNovo.getFuncao());
                    dialogBuilder.setMessage("É " + contatoAtualNaFuncao.getNomeDoMembro() + ". Deseja alterar?");
                    Contato finalContatoAtualNaFuncao = contatoAtualNaFuncao;
                    dialogBuilder.setPositiveButton("Alterar", (dialog, which) -> {
                        viewModel.deletarContato(finalContatoAtualNaFuncao);
                        // O método addContato retorna uma mensagem de sucesso ou erro.
                        // Caso seja de erro fecha o dialog sem fechar a activity
                        addContatoERetornarMensagem(contatoNovo);
                    });
                    dialogBuilder.setNegativeButton("Não", (dialog, i) -> {
                        // Fecha o dialog quando o botão "Cancelar" é pressionado
                        dialog.dismiss();
                    });
                    dialogBuilder.show();
                } else {
                    addContatoERetornarMensagem(contatoNovo);
                }
//            });
            // Garante que só há uma pessoa por cargo em cada congregação, exceto para professor, que pode haver vários.

        });
    }
    private void addContatoERetornarMensagem(Contato contatoNovo){
        viewModel.addContato(contatoNovo);
//        if (Objects.requireNonNull(mensagem.getValue()).startsWith("Erro")) {
//            Toast.makeText(getApplicationContext(), mensagem.getValue(), Toast.LENGTH_SHORT).show();
//        } else {
//            Intent resultIntent = new Intent();
//            resultIntent.putExtra("mensagem de sucesso", mensagem.getValue());
//            setResult(Activity.RESULT_OK, resultIntent);
            finish();
//        }
    }
    public boolean numeroCelularEhValido (String numero) {
        // Expressão regular para validar números de celular brasileiros
        String regex = "^\\([1-9]{2}\\)\\s9\\s[6-9]{1}[0-9]{3}\\-[0-9]{4}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(numero);

        return matcher.matches();
    }
}