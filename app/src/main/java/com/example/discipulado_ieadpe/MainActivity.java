package com.example.discipulado_ieadpe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {


    TextView tituloLogin, tituloSenha, tituloBtnContatos;
    EditText editSenha;
    Spinner spnLogin;
    ArrayAdapter <CharSequence> loginAdapter;
    CheckBox checkSalvarLogin;
    Button btnLogin;

    //TODO Criar link de primeiro acesso,
    // que trasfere para um fragment,
    // que verifica o nome de usuário cadastrado pela coordenação,
    // pede criação e confirmação de senha,
    // salva senha do usuário,
    // retorna para MainActivity
    // e exibe informação "faça login agora"
    ImageButton btnContatos;
    public static String CHAVE_USUARIO = "CHAVE_USUARIO";
    public static String CHAVE_LOGIN_AUTOMATICO = "CHAVE_LOGIN_AUTOMATICO";
    public static String USUARIO_PADRAO = "";
    public static boolean LOGIN_AUTOMATICO_PADRAO = false;
    public SharedPreferences sharedPreferences = getSharedPreferences("dados de login", Context.MODE_PRIVATE);
    public SharedPreferences.Editor editor = sharedPreferences.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (sharedPreferences.getBoolean(CHAVE_LOGIN_AUTOMATICO, LOGIN_AUTOMATICO_PADRAO)){
            startActivity(new Intent(this, TelaInicial.class));
        }
        setContentView(R.layout.activity_main);
        tituloLogin = findViewById(R.id.titulo_login);
        spnLogin = findViewById(R.id.spinner_login);
        loginAdapter = ArrayAdapter.createFromResource(this, R.array.usuarios, android.R.layout.simple_spinner_item);
        loginAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLogin.setAdapter(loginAdapter);
        tituloSenha = findViewById(R.id.titulo_senha);
        editSenha = findViewById(R.id.edit_senha);
        checkSalvarLogin = findViewById(R.id.checkbox_salvar_login);
        btnLogin = findViewById(R.id.btn_login);
        tituloBtnContatos = findViewById(R.id.titulo_btn_contatos);
        btnContatos = findViewById(R.id.btn_contatos);

        btnLogin.setOnClickListener(view -> {

            if (editSenha.getText().toString().trim().isEmpty()){
                editSenha.setError("Por favor, digite a senha");
            } else {
                String[]senhas = getResources().getStringArray(R.array.senhas);
                String senhaCorreta = senhas[spnLogin.getSelectedItemPosition()];
                String senhaFornecida = editSenha.getText().toString();

                if (!senhaFornecida.equals(senhaCorreta)){
                    editSenha.setError("Senha incorreta");
                } else {
                    String usuarioSelecionado = spnLogin.getPrompt().toString();
                    editor.putString(CHAVE_USUARIO, usuarioSelecionado);
                    if (checkSalvarLogin.isChecked()){
                        editor.putBoolean(CHAVE_LOGIN_AUTOMATICO, true);
                    }
                    editor.apply();
                    startActivity(new Intent(MainActivity.this, TelaInicial.class));
                }
            }
        });

        btnContatos.setOnClickListener(view -> abrirListaDeContatos());
    }

    private void abrirListaDeContatos(){
        startActivity(new Intent(this, ListaDeContatosActivity.class));
    }
}