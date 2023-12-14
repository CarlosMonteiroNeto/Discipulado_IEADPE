package com.example.discipulado_ieadpe;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListaDeContatosViewholder extends RecyclerView.ViewHolder {

    public TextView nomeDoMembro, funcaoDoMembro, congregacaoDoMembro, telefoneDoMembro;
    public ImageButton btnLigar, btnAbrirWhatsapp, btnEditarMembro, btnExcluirMembro;

    public ListaDeContatosViewholder(@NonNull View itemView) {
        super(itemView);

        nomeDoMembro = itemView.findViewById(R.id.nome_do_membro);
        funcaoDoMembro = itemView.findViewById(R.id.funcao_do_membro);
        congregacaoDoMembro = itemView.findViewById(R.id.congregacao_do_membro);
        telefoneDoMembro = itemView.findViewById(R.id.telefone_do_membro);
        btnLigar = itemView.findViewById(R.id.btn_ligar);
        btnAbrirWhatsapp = itemView.findViewById(R.id.btn_abrir_whatsapp);
        btnEditarMembro = itemView.findViewById(R.id.btn_editar_membro);
        btnExcluirMembro = itemView.findViewById(R.id.btn_excluir_membro);
    }
}
