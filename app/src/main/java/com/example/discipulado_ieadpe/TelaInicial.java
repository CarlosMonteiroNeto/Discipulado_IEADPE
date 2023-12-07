package com.example.discipulado_ieadpe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//Tela inicial após o login
public class TelaInicial extends AppCompatActivity {

    //TODO Definir demais views da tela inicial
    TextView titulobtnContatos;
    ImageButton btnContatos;
    Button btnSair;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("dados do login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        setContentView(R.layout.tela_inicial);
        titulobtnContatos = findViewById(R.id.titulo_btn_contatos);
        btnContatos = findViewById(R.id.btn_contatos);
        btnSair = findViewById(R.id.btn_sair);

        btnContatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaInicial.this, ListaDeContatosActivity.class);
                startActivity(intent);
            }
        });

        btnSair.setOnClickListener(view -> {
            editor.putBoolean(MainActivity.CHAVE_LOGIN_AUTOMATICO, false);
            editor.apply();
            startActivity(new Intent(TelaInicial.this, MainActivity.class));
        });
    }
}
