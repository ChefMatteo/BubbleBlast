import java.io.FileWriter;
import java.io.IOException;

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
        for(int i = y-1; i >= 0; i--){
            if(grid.getGrid().get(x).get(i).getStatement() != BubbleStatement.EXPLODED){
                grid.getGrid().get(x).get(i).Touched(x, i);
                break;
            }
        }
        //propagazione in alto
        for(int i = x-1; i >= 0; i--){
            if(grid.getGrid().get(i).get(y).getStatement() != BubbleStatement.EXPLODED){
                grid.getGrid().get(i).get(y).Touched(i, y);
                break;
            }
        }
        //propagazione a destra
        for(int i = y+1; i <= maxY; i++){
            if(grid.getGrid().get(x).get(i).getStatement() != BubbleStatement.EXPLODED){
                grid.getGrid().get(x).get(i).Touched(x, i);
                break;
            }
        }
        //propagazione in basso
        for(int i = x+1; i <= maxX; i++){
            if(grid.getGrid().get(i).get(y).getStatement() != BubbleStatement.EXPLODED){
                grid.getGrid().get(i).get(y).Touched(i, y);
                break;
            }
        }
    }



}


