package com.example.discipulado_ieadpe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    TextView tituloLogin, tituloSenha, tituloBtnContatos;
    EditText editSenha;
    Spinner spnLogin;
    ArrayAdapter <CharSequence> loginAdapter;
    CheckBox checkSalvarLogin;
    Button btnLogin;
    ImageButton btnContatos;
    public static String DADOS_DE_LOGIN = "Dados de login";
    public static String CHAVE_USUARIO = "Usuário logado";
    public static String CHAVE_LOGIN_AUTOMATICO = "CHAVE_LOGIN_AUTOMATICO";
    public static String USUARIO_PADRAO = "";
    public static boolean LOGIN_AUTOMATICO_PADRAO = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(DADOS_DE_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
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
                    String usuarioSelecionado = spnLogin.getSelectedItem().toString();
                    editor.putString(CHAVE_USUARIO, usuarioSelecionado);
                    Log.d("Usuário após putstring", sharedPreferences.getString(MainActivity.CHAVE_USUARIO, MainActivity.USUARIO_PADRAO));
                    if (checkSalvarLogin.isChecked()){
                        editor.putBoolean(CHAVE_LOGIN_AUTOMATICO, true);
                    }
                    editor.apply();
                    Log.d("Usuário após apply", sharedPreferences.getString(MainActivity.CHAVE_USUARIO, MainActivity.USUARIO_PADRAO));
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