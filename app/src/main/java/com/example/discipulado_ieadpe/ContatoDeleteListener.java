package com.example.discipulado_ieadpe;

import androidx.lifecycle.MutableLiveData;

import com.example.discipulado_ieadpe.database.entities.Contato;

//Essa interface existiria para possibilitar excluir um contato de dentro do Adapter do RecyclerView, sem ferir a divisão de tarefas.
//O viewModel implementa esta interface. O adapter instancia o viewmodel e exclui o contato pelo método onContatoDeleted.
// O retorno de um MutableLiveData é apra transmitir uma mensagem de sucesso ou erro do Firestore através do repositório.

//Foi resolvido de outro jeito
public interface ContatoDeleteListener {
    MutableLiveData<String> onContatoDeleted(Contato contato);
}
