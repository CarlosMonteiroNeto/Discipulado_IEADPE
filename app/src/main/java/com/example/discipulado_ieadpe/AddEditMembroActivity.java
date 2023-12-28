package com.example.discipulado_ieadpe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//Essa Activity tanto add quanto edita membros. Pode ser inicializada com a intent vazia através do botão "add membro",
// como através do botão de editar ao lado de um contato da lista, disponível apenas para usuários com permissão.
// Neste segundo caso, vem os dados do contato junto com a intent, que preenchem automaticamente as views, bloqueando a edição do nome do contato.
public class AddEditMembroActivity extends AppCompatActivity {
    TextView tituloNomeDoUsuario, tituloAddNomeDoMembro, tituloSpnCargo, tituloAddTelefone, tituloDataDeNascimento;
    Spinner spnCongregacao, spnCargo;
    EditText editAddNomeDoMembro;
    MaskedEditText maskedEditAddTelefone, maskedEditDataDeNascimento;
    Button btnConcluirAddEditMembro;
    ArrayAdapter<CharSequence> spnCargoAdapter;
    ListaDeContatosViewModel viewModel;
    List<Contato> listaDeContatos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_editar_membro);
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
        spnCongregacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = spnCongregacao.getSelectedItem().toString();
                if ("Supervisão".equals(selectedItem)) {
                    ArrayAdapter<CharSequence> newAdapter = ArrayAdapter.createFromResource(AddEditMembroActivity.this, R.array.funcoes_supervisor, android.R.layout.simple_spinner_item);
                    newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnCargo.setAdapter(newAdapter);
                } else {
                    ArrayAdapter<CharSequence> newAdapter = ArrayAdapter.createFromResource(AddEditMembroActivity.this, R.array.funcoes_congregacao, android.R.layout.simple_spinner_item);
                    newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnCargo.setAdapter(newAdapter);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        tituloNomeDoUsuario = findViewById(R.id.nome_da_congregacao);
        tituloNomeDoUsuario.setText(usuarioLogado);
        tituloAddNomeDoMembro = findViewById(R.id.titulo_add_nome_do_membro);
        tituloSpnCargo = findViewById(R.id.titulo_spinner_cargo);
        tituloAddTelefone = findViewById(R.id.titulo_add_telefone);
        tituloDataDeNascimento = findViewById(R.id.titulo_add_data_de_nascimento);
        editAddNomeDoMembro = findViewById(R.id.edit_add_nome_do_membro);
        maskedEditAddTelefone = findViewById(R.id.edit_add_telefone);
        maskedEditDataDeNascimento = findViewById(R.id.edit_add_data_de_nascimento);

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
            maskedEditDataDeNascimento.setText(contatoAEditar.getDataDeNascimento());
        }
        btnConcluirAddEditMembro = findViewById(R.id.btn_add_edit_membro);

        btnConcluirAddEditMembro.setOnClickListener(v -> {
            if (editAddNomeDoMembro.getText().toString().trim().isEmpty()
                    || !numeroCelularEhValido(Objects.requireNonNull(maskedEditAddTelefone.getText()).toString())
                    || !EhdataSulamericanaValida(maskedEditDataDeNascimento.getText().toString())){
                if (editAddNomeDoMembro.getText().toString().trim().isEmpty()) {
                    editAddNomeDoMembro.setError("Insira um nome");
                }
                if (!numeroCelularEhValido(Objects.requireNonNull(maskedEditAddTelefone.getText()).toString())) {
                    maskedEditAddTelefone.setError("Número inválido");
                }
                if(!EhdataSulamericanaValida(maskedEditDataDeNascimento.getText().toString())){
                    maskedEditDataDeNascimento.setError("Data inválida");
                }
                return;
            }
            Contato contatoNovo = new Contato();
            contatoNovo.setCongregacao(usuarioLogado.equals("Supervisão") ? spnCongregacao.getSelectedItem().toString() : tituloNomeDoUsuario.getText().toString());
            contatoNovo.setFuncao(spnCargo.getSelectedItem().toString());
            contatoNovo.setNomeDoMembro(editAddNomeDoMembro.getText().toString());
            contatoNovo.setTelefone(maskedEditAddTelefone.getText().toString());
            contatoNovo.setDataDeNascimento(maskedEditDataDeNascimento.getText().toString());
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
            Intent resultIntent = new Intent();
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
                    resultIntent.putExtra("contato a excluir", finalContatoAtualNaFuncao);
                    // O método addContato retorna uma mensagem de sucesso ou erro.
                    // Caso seja de erro fecha o dialog sem fechar a activity
                    retornarContato(contatoNovo, resultIntent);
                });
                dialogBuilder.setNegativeButton("Não", (dialog, i) -> {
                    // Fecha o dialog quando o botão "Cancelar" é pressionado
                    dialog.dismiss();
                });
                dialogBuilder.show();
            } else {
                retornarContato(contatoNovo, resultIntent);
            }
//            });
            // Garante que só há uma pessoa por cargo em cada congregação, exceto para professor, que pode haver vários.

        });
    }
    private void retornarContato(Contato contatoNovo, Intent resultIntent){

//        if (Objects.requireNonNull(mensagem.getValue()).startsWith("Erro")) {
//            Toast.makeText(getApplicationContext(), mensagem.getValue(), Toast.LENGTH_SHORT).show();
//        } else {
        resultIntent.putExtra("contato a adicionar", contatoNovo);
        setResult(Activity.RESULT_OK, resultIntent);
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
    public boolean EhdataSulamericanaValida(String date){
        // Verifica o padrão da string usando uma expressão regular
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            return false; // Não corresponde ao padrão "##/##/####"
        }

        // Divide a string da data em dia, mês e ano
        String[] partesData = date.split("/");

        // Verifica se os componentes são numéricos
        for (String parte : partesData) {
            try {
                Integer.parseInt(parte);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        // Converte os componentes para inteiros
        int dia = Integer.parseInt(partesData[0]);
        int mes = Integer.parseInt(partesData[1]);
        int ano = Integer.parseInt(partesData[2]);

        // Verifica se o dia, mês e ano estão dentro dos limites aceitáveis
        if (dia < 1 || dia > 31 || mes < 1 || mes > 12 || ano < 1900) {
            return false;
        }
        // Verifica meses com 30 dias
        if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30) {
            return false;
        }
        // Verifica fevereiro e anos bissextos
        if (mes == 2) {
            if (ano % 4 == 0 && (ano % 100 != 0 || ano % 400 == 0)) { // Ano bissexto
                if (dia > 29) {
                    return false;
                }
            } else {
                if (dia > 28) {
                    return false;
                }
            }
        }

        // Obtém a data atual e define o limite de 10 anos atrás
        Calendar dataAtual = Calendar.getInstance();
        dataAtual.add(Calendar.YEAR, -10);

        // Cria a data limite de 10 anos atrás
        Date dataLimite = dataAtual.getTime();

        // Converte a data fornecida para um objeto Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date dataInformada;
        try {
            dataInformada = sdf.parse(date);
        } catch (Exception e) {
            return false;
        }

        // Verifica se a data está dentro do limite de 10 anos atrás
        return dataInformada.before(dataLimite);
    }
}