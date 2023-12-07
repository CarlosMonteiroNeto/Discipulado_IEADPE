package com.example.discipulado_ieadpe;

import androidx.appcompat.app.AppCompatActivity;

//Essa Activity tanto add quanto edita membros. Pode ser inicializada com a intent vazia através do botão "add membro",
// como através do botão de editar ao lado de um contato da lista, disponível apenas para dirigentes e acima.
// Neste segundo caso, vem os dados do contato junto com a intent, que preenchem automaticamente as views, bloqueando a edição do nome do contato.
public class AddEditMembroActivity extends AppCompatActivity {
    //todo Verificar nível de acesso e só carregar funções que podem ser alteradas nos Spinners de função e congregação
    // TODO Criar layout,
    // addviews: spinner de cargo,
    // edittext nome,
    // spinner de função,
    // spinner de congregação
    // edittext telefone,
    // botão concluir,
    // títulos das views.
    //
    // TODO Verificar se há dados de contato no intent.
    //  Caso sim: extrair os dados do intent e preencher as views.
    //  Bloquear a edição do nome do contato.

    //TODO Verificar duplicidade de função para Supervisor, coordenador para qualquer local,
    // e dirigente por congregação. Levar em conta os vices.
    // Caso haja duplicidade, mostrar AlertDialog com nome do anterior, perguntando se deseja alterar ou voltar
}
