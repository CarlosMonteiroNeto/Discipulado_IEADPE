package com.example.discipulado_ieadpe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.discipulado_ieadpe.database.entities.Aluno;
import com.vicmikhailau.maskededittext.MaskedEditText;

import java.util.Objects;
import java.util.UUID;

public class AddEditAlunoFragment extends Fragment {

    private OnAlunoCreatedListener listener;

    TextView tituloCongregacao, tituloNome, tituloEndereco, tituloTelefone, tituloEscolaridade,
            tituloDataDeNascimento, tituloEstadoCivil, tituloDataDeInicio, tituloNotaBasico, tituloNotaIntermediario, tituloNotaAvancado;
    Spinner spnCongregacao, spnEscolaridade, spnEstadoCivil;

    ArrayAdapter<CharSequence> adapterCongregacao, adapterEscolaridade, adapterEstadoCivil;
    EditText editNome, editEndereco, editNotaBasico, editNotaIntermediario, editNotaAvancado;
    MaskedEditText editTelefone, editDataDeNascimento, editDataDeInicio;

    CheckBox checkNovoConvertido, checkBatizadoNasAguas, checkDesejaSeBatizar;
    Button botaoConcluir;

    Aluno aluno = new Aluno();

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
        String usuarioLogado = args.getString(MainActivity.CHAVE_USUARIO);

        tituloCongregacao = view.findViewById(R.id.titulo_congregacao_do_aluno);
        tituloNome = view.findViewById(R.id.titulo_nome_do_aluno);
        tituloEndereco = view.findViewById(R.id.titulo_endereco_do_aluno);
        tituloTelefone = view.findViewById(R.id.titulo_telefone_do_aluno);
        tituloEscolaridade = view.findViewById(R.id.titulo_escolaridade_do_aluno);
        tituloDataDeNascimento = view.findViewById(R.id.titulo_data_de_nascimento_do_aluno);
        tituloEscolaridade = view.findViewById(R.id.titulo_escolaridade_do_aluno);
        tituloEstadoCivil = view.findViewById(R.id.titulo_estado_civil_do_aluno);
        tituloDataDeInicio = view.findViewById(R.id.titulo_data_de_inicio_do_aluno);
        tituloNotaBasico = view.findViewById(R.id.titulo_nota_basico);
        tituloNotaIntermediario = view.findViewById(R.id.titulo_nota_intermediario);
        tituloNotaAvancado = view.findViewById(R.id.titulo_nota_avancado);
        spnCongregacao = view.findViewById(R.id.spinner_congregacao_do_aluno);
        spnEscolaridade = view.findViewById(R.id.spinner_escolaridade_do_aluno);
        spnEstadoCivil = view.findViewById(R.id.spinner_estado_civil_do_aluno);
        editNome = view.findViewById(R.id.edit_nome_do_aluno);
        editEndereco = view.findViewById(R.id.edit_endereco_do_aluno);
        editTelefone = view.findViewById(R.id.edit_telefone_do_aluno);
        editDataDeNascimento = view.findViewById(R.id.edit_data_de_nascimento_do_aluno);
        editDataDeInicio = view.findViewById(R.id.edit_data_de_inicio_do_aluno);
        editNotaBasico = view.findViewById(R.id.edit_nota_basico);
        editNotaIntermediario = view.findViewById(R.id.edit_nota_intermediario);
        editNotaAvancado = view.findViewById(R.id.edit_nota_avancado);
        checkNovoConvertido = view.findViewById(R.id.checkbox_novo_convertido);
        checkBatizadoNasAguas = view.findViewById(R.id.checkbox_batizado_nas_aguas);
        checkDesejaSeBatizar = view.findViewById(R.id.checkbox_deseja_se_batizar);

        if (Objects.equals(usuarioLogado, "Supervisão")){
            adapterCongregacao = ArrayAdapter.createFromResource(view.getContext(), R.array.usuarios, android.R.layout.simple_spinner_item);
        } else {
            adapterCongregacao = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1);
            adapterCongregacao.add(usuarioLogado);
        }
        spnCongregacao.setAdapter(adapterCongregacao);
        adapterEscolaridade = ArrayAdapter.createFromResource(view.getContext(), R.array.escolaridade, android.R.layout.simple_spinner_item);
        spnEscolaridade.setAdapter(adapterEscolaridade);
        adapterEstadoCivil = ArrayAdapter.createFromResource(view.getContext(), R.array.estado_civil, android.R.layout.simple_spinner_item);
        spnEstadoCivil.setAdapter(adapterEstadoCivil);

        if (args.containsKey("objeto")) {
            aluno = (Aluno) args.getSerializable("objeto");
            editNome.setText(aluno.getNomeDoAluno());
            editEndereco.setText(aluno.getEndereco());
            editTelefone.setText(aluno.getTelefone());
            editDataDeNascimento.setText(aluno.getDataDeNascimento());
            editDataDeInicio.setText(aluno.getDataDeInicio());
            checkNovoConvertido.setChecked(aluno.isNovoConvertido());
            checkBatizadoNasAguas.setChecked(aluno.isBatizadoNasAguas());
            checkDesejaSeBatizar.setChecked(aluno.isDesejaSeBatizar());
            editNotaBasico.setText(aluno.getNotaBasico());
            editNotaIntermediario.setText(aluno.getNotaIntermediario());
            editNotaAvancado.setText(aluno.getNotaAvancado());
        }

        botaoConcluir = view.findViewById(R.id.botao_concluir_aluno);
        botaoConcluir.setOnClickListener(v -> {
            aluno.setNomeDoAluno(editNome.getText().toString());
            aluno.setCongregacao(spnCongregacao.getSelectedItem().toString());
            //TODO ver como vai salvar turma no firestore
            aluno.setTurma("implementar");
            aluno.setEndereco(editEndereco.getText().toString());
            aluno.setTelefone(editTelefone.getMaskString());
            aluno.setEscolaridade(spnEscolaridade.getSelectedItem().toString());
            aluno.setBairro("Nossa Senhora do Ó");
            aluno.setCidade("Ipojuca");
            aluno.setCEP("55592-000");
            aluno.setUF("PE");
            aluno.setEstadoCivil(spnEstadoCivil.getSelectedItem().toString());
            aluno.setDataDeNascimento(editDataDeNascimento.getMaskString());
            aluno.setDataDeInicio(editDataDeInicio.getMaskString());
            aluno.setEhNovoConvertido(checkNovoConvertido.isChecked());
            aluno.setEhBatizadoNasAguas(checkBatizadoNasAguas.isChecked());
            aluno.setDesejaSeBatizar(checkDesejaSeBatizar.isChecked());
            aluno.setNotaBasico(editNotaBasico.getText().toString());
            aluno.setNotaIntermediario(editNotaIntermediario.getText().toString());
            aluno.setNotaAvancado(editNotaAvancado.getText().toString());

            //TODO preencher os campos do aluno
            listener.OnAlunoCreated(aluno);
        });
    }
    public interface OnAlunoCreatedListener{
        public void OnAlunoCreated(Aluno aluno);
    }

    //TODO Verificar o objeto com chave "origem" no Intent, para editar ou apenas visualizar o objeto passado.

}