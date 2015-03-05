package br.com.finan.util;

/**
 *
 * @author Wesley Luiz
 */
public final class StringUtil {

    /**
     * Método responsável por transformar a primeira letra de uma
     * <code>String</code> em maiúscula.
     *
     * @author Wesley Luiz
     * @param valor - <code>String</code> que vai ser transformada.
     * @return Retorna o valor transformado.
     */
    public static String toPrimeiraLetraMaiuscula(final String valor) {
        return valor.substring(0, 1).toUpperCase().concat(valor.substring(1));
    }
}
