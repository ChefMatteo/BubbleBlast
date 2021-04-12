import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game {
    public static void main(String[] args) {
        Start();
        InGame();
    }
    private static void Start(){
        System.out.println("\n        BUBBLEBLAST START!\n" +
                "        inserisci quale bolla toccare con [riga(A)][colonna(1)]\n" +
                "        le righe vanno da \"A\" a \"E\"\n" +
                "        le colonne vanno da \"1\" a \"6\"\n" +
                "        \n" +
                "        le bolle sgonfie sono rappresentate da (3)\n" +
                "        le bolle mezze gonfie sono rappresentate da (2)\n" +
                "        le bolle che stanno per esplodere sono rappresentate da (1)\n");
        GameGrid test = GameGrid.getGameGrid();
        test.GridStamp();
    }
    private static void InGame(){

    }

}
