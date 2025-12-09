import java.util.Scanner;

public class tcaCAP_NAM {

    final static Scanner LER = new Scanner(System.in);

    // posição inicial do jogador (linha e coluna)
    static int posicaoJogadorLinha = 6;
    static int posicaoJogadorcoluna = 5;

    // posição e direção do fantasma 1
    static int fantasma1Linha = 3;
    static int fantasma1Coluna = 3;
    static boolean descendo1 = true;

    // posição e direção do fantasma 2
    static int fantasmaLinha2 = 7;
    static int fantasmaColuna2 = 8;
    static boolean descendo2 = true;

    // moedas (true = ainda não pegou, false = já pegou)
    static boolean moeda1 = true;
    static boolean moeda2 = true;
    static boolean moeda3 = true;
    static boolean moeda4 = true;

    // comando digitado pelo jogador
    static String movimentoOuSair = null;

    // status de vitória
    static boolean venceu = false;

    // dificuldade
    static int dificuldade = 0;

    public static void main(String[] args) {

        // tamanho da matriz do mapa
        int colunaMatriz = 12;
        int linhaMatriz = 11;

        // matriz do mapa
        char[][] matrizMapa = new char[linhaMatriz][colunaMatriz];

        // escolher dificuldade
        System.out.println("ESOLHA A DIFICULDADE DO JOGO ANTES DE COMEÇAR");
        System.out.println("---------------------------------------------");
        System.out.println("FÁCIL:1   MÉDIO:2   DIFÍCIL:3   IMPOSSÍVEL:4");
        System.out.println("---------------------------------------------");
        dificuldade = lerNumInt();

        // thread dos inimigos (fantasmas)
        new Thread(() -> {
            while (true) {
                inimigo1(linhaMatriz, matrizMapa);
                inimigo2(linhaMatriz, matrizMapa);
                verificarColisao();
                montarAndImprimirMapa(matrizMapa, linhaMatriz, colunaMatriz);

                switch (dificuldade) {
                    case 1:
                        try {
                            Thread.sleep(400);
                        } catch (Exception e) {
                        }
                        break;
                    case 2:
                        try {
                            Thread.sleep(300);
                        } catch (Exception e) {
                        }
                        break;
                    case 3:
                        try {
                            Thread.sleep(200);
                        } catch (Exception e) {
                        }
                        break;
                    case 4:
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                        }
                        break;
                    default:
                        try {
                            Thread.sleep(300);
                        } catch (Exception e) {
                        }
                }

            }
        }).start();

        // loop principal do jogador
        while (true) {
            moverPlayer(matrizMapa, linhaMatriz, colunaMatriz);
            verificarVenceu();
            montarAndImprimirMapa(matrizMapa, linhaMatriz, colunaMatriz);

        }
    }

    // monta o mapa e imprimir o mapa
    public static void montarAndImprimirMapa(char[][] matrizMapa, int linhaMatriz, int colunaMatriz) {

        for (int i = 0; i < linhaMatriz; i++) {
            for (int j = 0; j < colunaMatriz; j++) {

                // montar mapa
                // linhas 0 e 10
                if (i == 0 || i == 10 && j >= 0 && j <= 11) {
                    matrizMapa[i][j] = '_';
                }

                // linhas 1, 5 e 9
                if ((i == 1 || i == 5 || i == 9) && (j == 0 || j == 11)) {
                    matrizMapa[i][j] = '|';
                }
                if ((i == 1 || i == 5 || i == 9) && (j != 0 && j != 11)) {
                    matrizMapa[i][j] = ' ';
                }

                // linhas 2 e 8
                if ((i == 2 || i == 8) && (j <= 2)) {
                    matrizMapa[i][j] = '|';
                }
                if ((i == 2 || i == 8) && (j >= 9)) {
                    matrizMapa[i][j] = '|';
                }
                if ((i == 2 || i == 8) && (j > 2 && j < 9)) {
                    matrizMapa[i][j] = ' ';
                }

                // linhas 3 e 7
                if ((i == 3 || i == 7) && j == 0) {
                    matrizMapa[i][j] = '|';
                }
                if ((i == 3 || i == 7) && j == 11) {
                    matrizMapa[i][j] = '|';
                }
                if ((i == 3 || i == 7) && (j >= 4 && j <= 7)) {
                    matrizMapa[i][j] = '|';
                }
                if ((i == 3 || i == 7) && (j != 0 && j != 11 && !(j >= 4 && j <= 7))) {
                    matrizMapa[i][j] = ' ';
                }

                // linhas 4 e 6
                if ((i == 4 || i == 6) && j == 0) {
                    matrizMapa[i][j] = '|';
                }
                if ((i == 4 || i == 6) && j == 11) {
                    matrizMapa[i][j] = '|';
                }
                if ((i == 4 || i == 6) && j == 4) {
                    matrizMapa[i][j] = '|';
                }
                if ((i == 4 || i == 6) && j == 7) {
                    matrizMapa[i][j] = '|';
                }
                if ((i == 4 || i == 6) && (j != 0 && j != 11 && j != 4 && j != 7)) {
                    matrizMapa[i][j] = ' ';
                }
            }
        }

        // coloca o jogador
        matrizMapa[posicaoJogadorLinha][posicaoJogadorcoluna] = 'C';

        // coloca as moedas
        if (moeda1 == true) {
            matrizMapa[1][1] = '0';
        }
        if (moeda2 == true) {
            matrizMapa[1][10] = '0';
        }
        if (moeda3 == true) {
            matrizMapa[9][1] = '0';
        }
        if (moeda4 == true) {
            matrizMapa[9][10] = '0';
        }

        // coloca os fantasmas
        matrizMapa[fantasma1Linha][fantasma1Coluna] = 'W';
        matrizMapa[fantasmaLinha2][fantasmaColuna2] = 'W';

        // imprime mapa
        System.out.print("\033[H\033[2J");
        System.out.flush();

        for (int i = 0; i < linhaMatriz; i++) {
            for (int j = 0; j < colunaMatriz; j++) {
                System.out.print(matrizMapa[i][j]);
            }
            System.out.println();
        }

        System.out.println("USE 'w' 'a' 's' 'd' PARA SE MOVER!!! --- 'Q' PARA SAIR");
        System.out.println("USE 'ENTER'");
    }

    // movimentação do jogador
    public static void moverPlayer(char[][] matrizMapa, int linhaMatriz, int colunaMatriz) {

        int novaLinha = posicaoJogadorLinha;
        int novaColuna = posicaoJogadorcoluna;

        movimentoOuSair = LER.nextLine();

        switch (movimentoOuSair) {
            case "w":
                novaLinha--;
                break;
            case "a":
                novaColuna--;
                break;
            case "s":
                novaLinha++;
                break;
            case "d":
                novaColuna++;
                break;
            case "q":
                System.exit(0);
                break;
        }

        if (matrizMapa[novaLinha][novaColuna] != '|' && matrizMapa[novaLinha][novaColuna] != '_') {
            posicaoJogadorLinha = novaLinha;
            posicaoJogadorcoluna = novaColuna;
        }

        if (posicaoJogadorLinha == 1 && posicaoJogadorcoluna == 1) {
            moeda1 = false;
        }
        if (posicaoJogadorLinha == 1 && posicaoJogadorcoluna == 10) {
            moeda2 = false;
        }
        if (posicaoJogadorLinha == 9 && posicaoJogadorcoluna == 1) {
            moeda3 = false;
        }
        if (posicaoJogadorLinha == 9 && posicaoJogadorcoluna == 10) {
            moeda4 = false;
        }
    }

    // vitória
    public static void verificarVenceu() {
        if (moeda1 == false && moeda2 == false && moeda3 == false && moeda4 == false
                && posicaoJogadorLinha == 6 && posicaoJogadorcoluna == 5) {
            venceu = true;
        }

        if (venceu == true) {
            System.out.println("VOCÊ VENCEU!!! :)");
            System.exit(0);
        }
    }

    // colisão
    public static void verificarColisao() {

        if ((fantasma1Coluna == posicaoJogadorcoluna && fantasma1Linha == posicaoJogadorLinha)
                || (fantasmaColuna2 == posicaoJogadorcoluna && fantasmaLinha2 == posicaoJogadorLinha)) {

            System.out.println("VOCÊ PERDEU!!! :(");
            System.exit(0);
        }
    }

    // movimento fantasma 1
    public static void inimigo1(int linhaMatriz, char matrizMapa[][]) {

        int novaLinhaInimigo1 = fantasma1Linha;

        if (descendo1 == true) {
            novaLinhaInimigo1++;
            if (matrizMapa[novaLinhaInimigo1][3] != '_') {
                fantasma1Linha++;
            } else {
                descendo1 = false;
            }
        }
        if (descendo1 == false) {
            novaLinhaInimigo1--;
            if (matrizMapa[novaLinhaInimigo1][3] != '_') {
                fantasma1Linha--;
            } else {
                descendo1 = true;
            }
        }
    }

    // movimento fantasma 2
    public static void inimigo2(int linhaMatriz, char matrizMapa[][]) {

        int novaLinhaInimigo2 = fantasmaLinha2;
        if (descendo2 == true) {
            novaLinhaInimigo2++;
            if (matrizMapa[novaLinhaInimigo2][8] != '_') {
                fantasmaLinha2++;
            } else {
                descendo2 = false;
            }
        }
        if (descendo2 == false) {
            novaLinhaInimigo2--;
            if (matrizMapa[novaLinhaInimigo2][8] != '_') {
                fantasmaLinha2--;
            } else {
                descendo2 = true;
            }
        }
    }

    public static int lerNumInt() {
        int valor = LER.nextInt();
        return valor;
    }
}