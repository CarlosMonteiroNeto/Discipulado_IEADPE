package com.example.discipulado_ieadpe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    TextView tituloLogin, tituloSenha, tituloBtnContatos;
    EditText editLogin, editSenha;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("dados de login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean(CHAVE_LOGIN_AUTOMATICO, LOGIN_AUTOMATICO_PADRAO)){
            startActivity(new Intent(this, TelaInicial.class));
        } else {
            editor.putString(CHAVE_USUARIO, USUARIO_PADRAO);
            editor.apply();
        }
        setContentView(R.layout.activity_main);
        tituloLogin = findViewById(R.id.titulo_login);
        editLogin = findViewById(R.id.edit_login);
        tituloSenha = findViewById(R.id.titulo_senha);
        editSenha = findViewById(R.id.edit_senha);
        checkSalvarLogin = findViewById(R.id.checkbox_salvar_login);
        btnLogin = findViewById(R.id.btn_login);
        tituloBtnContatos = findViewById(R.id.titulo_btn_contatos);
        btnContatos = findViewById(R.id.btn_contatos);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Criar registro de login e senha.
                // Checar se confere. Salvar em Shared preferences.
                //TODO condicionar as ações abaixo caso o login e senha existem e conferem.
                // Caso contrário, dar avisos de não confere, senha incorreta etc.
                if (checkSalvarLogin.isChecked()){
                    editor.putBoolean(CHAVE_LOGIN_AUTOMATICO, true);
                }
                editor.apply();
                startActivity(new Intent(MainActivity.this, TelaInicial.class));
                // Salvar escolha de login automático.
            }
        });

        btnContatos.setOnClickListener(view -> abrirListaDeContatos());
    }

    private void abrirListaDeContatos(){
        startActivity(new Intent(this, ListaDeContatosActivity.class));
    }
}