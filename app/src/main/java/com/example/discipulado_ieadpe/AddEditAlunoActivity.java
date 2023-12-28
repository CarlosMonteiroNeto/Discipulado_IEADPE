package com.example.discipulado_ieadpe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.discipulado_ieadpe.database.entities.Aluno;

import java.util.UUID;

public class AddEditAlunoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_aluno);

        Aluno alunoNovo = new Aluno();
        alunoNovo.setID(UUID.randomUUID().toString());

        //TODO Verificar o objeto com chave "origem" no Intent, para editar ou apenas visualizar o objeto passado.
    }
}