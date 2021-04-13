import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        GameGrid gameGrid = GameGrid.getGameGrid();
        Start(gameGrid);
        boolean flag = true;
/*
        Ciclo continuo fino ad esaurimento mosse o eventuale vincita
*/
        while (gameGrid.MovesLeftController() && flag) {
            flag = InGame(gameGrid);
        }

    }

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
        gameGrid.GridStamp();
    }

    private static boolean InGame(GameGrid gameGrid) {
        Scanner sc = new Scanner(System.in);
        return gameGrid.Move(sc.next());
    }
}


