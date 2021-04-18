import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        newGame();
    }

    //Games methods
    private static void newGame(){
        start();
        inGame();
        endGame();
    }

    private static void start() {
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
        GameGrid.getGameGrid().movesLeft();
        System.out.println("Risolto in: " + (System.currentTimeMillis() - startTime) + " millisecondi");
        System.out.println("Hai a disposizione " + GameGrid.getGameGrid().getMovesLeft() + " tentativi!\n");
        GameGrid.getGameGrid().getMovesOfGame()
                .append("Partita iniziata con ")
                .append(GameGrid.getGameGrid().getMovesLeft())
                .append(" tentativi.\n");
        GameGrid.getGameGrid().gridStamp();
    }

    private static void inGame() {
        Scanner sc = new Scanner(System.in);
        boolean flag = true;
        while (GameGrid.getGameGrid().movesLeftController() && flag) {
            flag = GameGrid.getGameGrid().move(sc.next());
        }
        System.out.println("Vuoi rigiocare? premi y/n");
        flag = true;
        while(flag){
            String answer = sc.next().toLowerCase();
            if(answer.equals("y")){
                GameGrid.getGameGrid().reset();
                newGame();
                flag=false;
            }
            else if(answer.equals("n")){
                flag=false;
            }
            else System.out.println("Input errato, riprova");
        }
    }

    private static void endGame(){
        try {
            FileWriter writer = new FileWriter("MovesOfGame.txt");
            writer.write(GameGrid.getGameGrid().getMovesOfGame().toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


