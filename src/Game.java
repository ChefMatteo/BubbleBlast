import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        GameGrid game = GameGrid.getGameGrid();
        start(game);
        inGame(game);
        endGame(game);
    }

    //Games methods
    private static void start(GameGrid gameGrid) {
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
        long startTime = System.currentTimeMillis();
        gameGrid.movesLeft();
        System.out.println("Risolto in: " + (System.currentTimeMillis() - startTime) + " millisecondi");
        System.out.println("Hai a disposizione " + gameGrid.getMovesLeft() + " tentativi!\n");
        gameGrid.getMovesOfGame().append("Partita iniziata con ").append(gameGrid.getMovesLeft()).append(" tentativi.\n");
        gameGrid.gridStamp();
    }

    private static void inGame(GameGrid gameGrid) {
        Scanner sc = new Scanner(System.in);
        boolean flag = true;
        while (gameGrid.movesLeftController() && flag) {
            flag = gameGrid.move(sc.next());
        }
    }

    private static void endGame(GameGrid gameGrid){
        try {
            FileWriter writer = new FileWriter("MovesOfGame.txt");
            writer.write(gameGrid.getMovesOfGame().toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


