package com.example.discipulado_ieadpe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.discipulado_ieadpe.database.entities.Aluno;

import java.util.UUID;

public class AddEditAlunoFragment extends Fragment {

    private OnAlunoCreatedListener listener;

    Button botaoConcluir;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAlunoCreatedListener){
            listener = (OnAlunoCreatedListener) context;
        } else {
            throw new ClassCastException();
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_aluno, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            Aluno alunoAEditar = (Aluno) args.getSerializable("objeto");

        }
        botaoConcluir = view.findViewById(R.id.botao_concluir_aluno);
        botaoConcluir.setOnClickListener(v -> {
            Aluno aluno = new Aluno();
            //TODO preencher os campos do aluno
            listener.OnAlunoCreated(aluno);
        });
    }
    public interface OnAlunoCreatedListener{
        public void OnAlunoCreated(Aluno aluno);
    }

    //TODO Verificar o objeto com chave "origem" no Intent, para editar ou apenas visualizar o objeto passado.

}