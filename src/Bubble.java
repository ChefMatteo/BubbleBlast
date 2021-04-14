import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class Bubble {
    private BubbleStatement statement;
    private String bubbleView;

    //    Constructors
    public Bubble(int n) {
        switch(n) {
            case 2 -> {
                bubbleView = "(3)";
                statement = BubbleStatement.EMPTY;
            }
            case 1 -> {
                bubbleView = "(2)";
                statement = BubbleStatement.PUFFY;
            }
            case 0 -> {
                bubbleView = "(1)";
                statement = BubbleStatement.READY_TO_EXPLODE;
            }
        }
}

    //    Getters and Setters
    public BubbleStatement getStatement() {
        return statement;
    }
    public String getBubbleView() {
        return bubbleView;
    }

    //Bubble methods
    public boolean Touched(int x, int y) {
        GameGrid grid = GameGrid.getGameGrid();
        switch (statement) {
            case EMPTY -> {
                bubbleView = "(2)";
                grid.GridStamp();
                grid.getMovesOfGame()
                        .append(grid.numberedList)
                        .append(") Alle coordinate x: ")
                        .append(x)
                        .append(" y: ")
                        .append(y)
                        .append(" la bolla è passata a mezza gonfia.\n");
                grid.numberedList++;
                statement = BubbleStatement.PUFFY;
                return true;
            }
            case PUFFY -> {
                bubbleView = "(1)";
                grid.GridStamp();
                grid.getMovesOfGame()
                        .append(grid.numberedList)
                        .append(") Alle coordinate x: ")
                        .append(x)
                        .append(" y: ")
                        .append(y)
                        .append(" la bolla è passata a pronta per esplodere.\n");
                grid.numberedList++;
                statement = BubbleStatement.READY_TO_EXPLODE;
                return true;
            }
            case READY_TO_EXPLODE -> {
                bubbleView = "   ";
                grid.GridStamp();
                grid.getMovesOfGame()
                        .append(grid.numberedList)
                        .append(") Alle coordinate x: ")
                        .append(x)
                        .append(" y: ")
                        .append(y)
                        .append(" la bolla è esplosa.\n");
                grid.numberedList++;
                statement = BubbleStatement.EXPLODED;
                Explosion(x, y);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private void Explosion(int x, int y) {
        GameGrid grid = GameGrid.getGameGrid();
        //limiti max delle liste
        int maxX = 4;
        int maxY = 5;
        //propagazione a sinistra
        IntStream.range(1, 6)
                .filter(i -> y - i >= 0)
                .filter(i -> grid.getGrid().get(x).get(y-i).getStatement() != BubbleStatement.EXPLODED)
                .findFirst()
                .ifPresent(i -> grid.getGrid().get(x).get(y-i).Touched(x, (y-i)));
        //propagazione in alto
        IntStream.range(1, 5)
                .filter(i -> x-i >= 0)
                .filter(i -> grid.getGrid().get(x-i).get(y).getStatement() != BubbleStatement.EXPLODED)
                .findFirst()
                .ifPresent(i -> grid.getGrid().get(x-i).get(y).Touched((x-i), y));
        //propagazione a destra
        IntStream.range(1, 6)
                .filter(i -> y + i <= 5)
                .filter(i -> grid.getGrid().get(x).get(y+i).getStatement() != BubbleStatement.EXPLODED)
                .findFirst()
                .ifPresent(i -> grid.getGrid().get(x).get(y+i).Touched(x, (y+i)));
        //propagazione in basso
        IntStream.range(1, 5)
                .filter(i -> x+i <= 4)
                .filter(i -> grid.getGrid().get(x+i).get(y).getStatement() != BubbleStatement.EXPLODED)
                .findFirst()
                .ifPresent(i -> grid.getGrid().get(x+i).get(y).Touched((x+i), y));
    }

    public int CheckExplosion(int x, int y, BubbleStatement statement) {
        GameGrid grid = GameGrid.getGameGrid();
        //limiti max delle liste
        int maxX = 4;
        int maxY = 5;
        long bubbleReadyToExplodeCounter = 0;
        //propagazione a sinistra
        if(grid.getGrid().get(x).get(y).getStatement() == BubbleStatement.READY_TO_EXPLODE) {
            bubbleReadyToExplodeCounter += IntStream.range(1, 2)
                    .filter(i -> y - i >= 0)
                    .filter(i -> grid.getGrid().get(x).get(y - i).getStatement() == statement)
                    .count();

            //propagazione in alto
            bubbleReadyToExplodeCounter += IntStream.range(1, 2)
                    .filter(i -> x - i >= 0)
                    .filter(i -> grid.getGrid().get(x - i).get(y).getStatement() == statement)
                    .count();

            //propagazione a destra
            bubbleReadyToExplodeCounter += IntStream.range(1, 2)
                    .filter(i -> y + 1 <= maxY)
                    .filter( i-> grid.getGrid().get(x).get(y + 1).getStatement() == statement)
                    .count();

            //propagazione in basso
            bubbleReadyToExplodeCounter += IntStream.range(1, 2)
                    .filter(i -> x + 1 <= maxX)
                    .filter(i -> grid.getGrid().get(x + 1).get(y).getStatement() == statement)
                    .count();
        }
        return (int) bubbleReadyToExplodeCounter;
    }


}


