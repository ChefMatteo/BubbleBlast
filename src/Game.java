import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Game {
    public static void main(String[] args) {
        GameGrid gameGrid = GameGrid.getGameGrid();
        Start(gameGrid);
//        gameGrid.HeadTest();
        boolean flag = true;
/*
        Ciclo continuo fino ad esaurimento mosse o eventuale vincita
*/

        while (gameGrid.MovesLeftController() && flag) {
            flag = InGame(gameGrid);
        }
/*
        Stampa file di log
*/

        try {
            FileWriter writer = new FileWriter("MovesOfGame.txt");
            writer.write(gameGrid.getMovesOfGame().toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Games methods
    private static void Start(GameGrid gameGrid) {
        System.out.println
                ("""
                                BUBBLEBLAST START!
                                inserisci quale bolla toccare con [riga(A)][colonna(1)]
                                le righe vanno da "A" a "E"
                                le colonne vanno da "1" a "6"
                               \s
                                le bolle sgonfie sono rappresentate da (3)
                                le bolle mezze gonfie sono rappresentate da (2)
                                le bolle che stanno per esplodere sono rappresentate da (1)
                        """);
        System.out.println("Hai a disposizione " + gameGrid.getMovesLeft() + " tentativi!");
        gameGrid.getMovesOfGame().append("Partita iniziata con ").append(gameGrid.getMovesLeft()).append(" tentativi.\n");
        gameGrid.GridStamp();
    }

    private static boolean InGame(GameGrid gameGrid) {
        Scanner sc = new Scanner(System.in);
        return gameGrid.Move(sc.next());
    }
}


