package com.example.discipulado_ieadpe;

public class Utils {

    public static String desformatarTelefone(String numeroFormatado){
        if (numeroFormatado != null){
            return numeroFormatado.replaceAll("[^0-9]","");
        } else {
            return numeroFormatado;
        }

    }
    public static String formatarTelefone(String numeroPuro){
        if (numeroPuro.length() == 11) {
            // Formata o número para o padrão desejado
            return String.format("(%s) %s %s-%s",
                    numeroPuro.substring(0, 2),
                    numeroPuro.substring(2, 3),
                    numeroPuro.substring(3, 7),
                    numeroPuro.substring(7));
        } else {
            return numeroPuro;
        }
    }
}
