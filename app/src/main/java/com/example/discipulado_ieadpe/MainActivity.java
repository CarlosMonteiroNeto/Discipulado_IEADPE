package com.example.discipulado_ieadpe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    TextView tituloLogin, tituloSenha, tituloBtnContatos;
    EditText editLogin, editSenha;
    Button btnLogin;
    ImageButton btnContatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tituloLogin = findViewById(R.id.titulo_login);
        editLogin = findViewById(R.id.edit_login);
        tituloSenha = findViewById(R.id.titulo_senha);
        editSenha = findViewById(R.id.edit_senha);
        btnLogin = findViewById(R.id.btn_login);
        tituloBtnContatos = findViewById(R.id.titulo_btn_contatos);
        btnContatos = findViewById(R.id.btn_contatos);

        btnContatos.setOnClickListener(view -> abrirListaDeContatos());
    }

    private void abrirListaDeContatos(){
        startActivity(new Intent(this, ListaDeContatosActivity.class));
    }
}