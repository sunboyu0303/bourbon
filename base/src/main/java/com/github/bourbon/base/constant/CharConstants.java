package com.github.bourbon.base.constant;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 11:07
 */
public final class CharConstants {

    public static final char MIN_VALUE = Character.MIN_VALUE;

    public static final char MAX_VALUE = Character.MAX_VALUE;

    public static final char DEFAULT = (char) 0;

    public static final Class<?> TYPE = Character.TYPE;

    public static final Class<?> PRIMITIVE_CLASS = char.class;

    public static final Class<?> BOXED_CLASS = Character.class;

    public static final char TAB = '\t';
    public static final char BACKSLASH = '\\';
    public static final char CR = '\r';
    public static final char LF = '\n';
    public static final char DOUBLE_QUOTES = '"';
    public static final char SINGLE_QUOTE = '\'';
    public static final char AT = '@';
    public static final char EQUAL = '=';
    public static final char GREATER_THAN = '>';
    public static final char LESSER_THAN = '<';
    public static final char AND = '&';
    public static final char SEMICOLON = ';';
    public static final char QUESTION_MASK = '?';
    public static final char SLASH = '/';
    public static final char HYPHEN = '-';
    public static final char BANG = '!';
    public static final char UNDERLINE = '_';
    public static final char SPACE = ' ';
    public static final char COMMA = ',';
    public static final char DOT = '.';
    public static final char COLON = ':';
    public static final char PLUS = '+';
    public static final char POUND = '#';
    public static final char DOLLAR = '$';
    public static final char ARRAY_SEPARATOR = '|';
    public static final char PERCENT = '%';

    public static final char LEFT_BRACES = '{';
    public static final char RIGHT_BRACES = '}';
    public static final char LEFT_BRACKETS = '[';
    public static final char RIGHT_BRACKETS = ']';
    public static final char LEFT_PARENTHESES = '(';
    public static final char RIGHT_PARENTHESES = ')';

    public static final char SMALL_A = 'a';
    public static final char SMALL_B = 'b';
    public static final char SMALL_C = 'c';
    public static final char SMALL_D = 'd';
    public static final char SMALL_E = 'e';
    public static final char SMALL_F = 'f';
    public static final char SMALL_G = 'g';

    public static final char SMALL_H = 'h';
    public static final char SMALL_I = 'i';
    public static final char SMALL_J = 'j';
    public static final char SMALL_K = 'k';
    public static final char SMALL_L = 'l';
    public static final char SMALL_M = 'm';
    public static final char SMALL_N = 'n';

    public static final char SMALL_O = 'o';
    public static final char SMALL_P = 'p';
    public static final char SMALL_Q = 'q';

    public static final char SMALL_R = 'r';
    public static final char SMALL_S = 's';
    public static final char SMALL_T = 't';

    public static final char SMALL_U = 'u';
    public static final char SMALL_V = 'v';
    public static final char SMALL_W = 'w';

    public static final char SMALL_X = 'x';
    public static final char SMALL_Y = 'y';
    public static final char SMALL_Z = 'z';

    public static final char A = 'A';
    public static final char B = 'B';
    public static final char C = 'C';
    public static final char D = 'D';
    public static final char E = 'E';
    public static final char F = 'F';
    public static final char G = 'G';

    public static final char H = 'H';
    public static final char I = 'I';
    public static final char J = 'J';
    public static final char K = 'K';
    public static final char L = 'L';
    public static final char M = 'M';
    public static final char N = 'N';

    public static final char O = 'O';
    public static final char P = 'P';
    public static final char Q = 'Q';

    public static final char R = 'R';
    public static final char S = 'S';
    public static final char T = 'T';

    public static final char U = 'U';
    public static final char V = 'V';
    public static final char W = 'W';

    public static final char X = 'X';
    public static final char Y = 'Y';
    public static final char Z = 'Z';

    public static final char ZERO = '0';
    public static final char ONE = '1';
    public static final char TWO = '2';
    public static final char THREE = '3';
    public static final char FOUR = '4';
    public static final char FIVE = '5';
    public static final char SIX = '6';
    public static final char SEVEN = '7';
    public static final char EIGHT = '8';
    public static final char NINE = '9';

    public static final char[] EMPTY_CHAR_ARRAY = {};

    public static final Character[] EMPTY_CHAR_BOXED_ARRAY = {};
    /**
     * {@code "['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f']"}
     */
    public static final char[] BASE16 = {
            ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, SMALL_A, SMALL_B, SMALL_C, SMALL_D, SMALL_E, SMALL_F
    };
    /**
     * {@code "['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
     * 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
     * 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
     * '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '+', '/', '=']"}
     */
    public static final char[] BASE64 = {
            A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z,
            SMALL_A, SMALL_B, SMALL_C, SMALL_D, SMALL_E, SMALL_F, SMALL_G, SMALL_H, SMALL_I, SMALL_J,
            SMALL_K, SMALL_L, SMALL_M, SMALL_N, SMALL_O, SMALL_P, SMALL_Q, SMALL_R, SMALL_S, SMALL_T,
            SMALL_U, SMALL_V, SMALL_W, SMALL_X, SMALL_Y, SMALL_Z, ZERO, ONE, TWO, THREE, FOUR, FIVE,
            SIX, SEVEN, EIGHT, NINE, PLUS, SLASH, EQUAL
    };

    private CharConstants() {
    }
}