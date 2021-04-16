import java.util.*;
import java.util.stream.IntStream;

public class Bubble {
    private BubbleStatement statement;
    private String bubbleView;
    private int x;
    private int y;
    private double value;

    //Clone
    public Bubble bubbleClone(){
        Bubble clone = new Bubble();
        clone.statement = this.statement;
        clone.bubbleView = this.bubbleView;
        clone.x = this.x;
        clone.y = this.y;
        clone.value = this.value;
        return clone;
    }

    //    Constructors
    public Bubble(int n, int x, int y) {
        switch (n) {
            case 2 -> {
                bubbleView = "(3)";
                statement = BubbleStatement.EMPTY;
                value = 0.3;
            }
            case 1 -> {
                bubbleView = "(2)";
                statement = BubbleStatement.PUFFY;
                value = 0.5;
            }
            case 0 -> {
                bubbleView = "(1)";
                statement = BubbleStatement.READY_TO_EXPLODE;
                value = 1;
            }
        }
    }
    public Bubble() {
    }

    //    Getters and Setters
    public BubbleStatement getStatement() {
        return statement;
    }
    public String getBubbleView() {
        return bubbleView;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public void setStatement(BubbleStatement statement) {
        this.statement = statement;
    }
    public void setBubbleView(String bubbleView) {
        this.bubbleView = bubbleView;
    }

    //Bubble methods
    public boolean touched(int x, int y, List<List<Bubble>> grid, boolean checkIfGameOrNot) {
        GameGrid gameGrid = GameGrid.getGameGrid();
        switch (grid.get(x).get(y).getStatement()) {
            case EMPTY -> {
                grid.get(x).get(y).setBubbleView("(2)");
                if(checkIfGameOrNot) {
                    gameGrid.gridStamp();
                    gameGrid.getMovesOfGame()
                            .append(gameGrid.numberedList)
                            .append(") Alle coordinate x: ")
                            .append(x)
                            .append(" y: ")
                            .append(y)
                            .append(" la bolla è passata a mezza gonfia.\n");
                    gameGrid.numberedList++;
                }
                grid.get(x).get(y).setStatement(BubbleStatement.PUFFY);
                grid.get(x).get(y).setValue(0.5);
                return true;
            }
            case PUFFY -> {
                grid.get(x).get(y).setBubbleView("(1)");
                if(checkIfGameOrNot) {
                    gameGrid.gridStamp();
                    gameGrid.getMovesOfGame()
                            .append(gameGrid.numberedList)
                            .append(") Alle coordinate x: ")
                            .append(x)
                            .append(" y: ")
                            .append(y)
                            .append(" la bolla è passata a pronta per esplodere.\n");
                    gameGrid.numberedList++;
                }
                grid.get(x).get(y).setStatement(BubbleStatement.READY_TO_EXPLODE);
                grid.get(x).get(y).setValue(1);
                return true;
            }
            case READY_TO_EXPLODE -> {
                grid.get(x).get(y).setBubbleView("   ");
                if(checkIfGameOrNot) {
                    gameGrid.gridStamp();
                    gameGrid.getMovesOfGame()
                            .append(gameGrid.numberedList)
                            .append(") Alle coordinate x: ")
                            .append(x)
                            .append(" y: ")
                            .append(y)
                            .append(" la bolla è esplosa.\n");
                    gameGrid.numberedList++;
                }
                grid.get(x).get(y).setStatement(BubbleStatement.EXPLODED);
                explosion(x, y, grid, checkIfGameOrNot);
                grid.get(x).get(y).setValue(0);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private void explosion(int x, int y, List<List<Bubble>> grid, boolean checkIfGameOrNot) {
        //limiti max delle liste
        int maxX = 4;
        int maxY = 5;
        //propagazione a sinistra
        IntStream.range(1, 6)
                .filter(i -> y - i >= 0)
                .filter(i -> grid.get(x).get(y - i).getStatement() != BubbleStatement.EXPLODED)
                .findFirst()
                .ifPresent(i -> grid.get(x).get(y - i).touched(x, (y - i), grid, checkIfGameOrNot));
        //propagazione in alto
        IntStream.range(1, 5)
                .filter(i -> x - i >= 0)
                .filter(i -> grid.get(x - i).get(y).getStatement() != BubbleStatement.EXPLODED)
                .findFirst()
                .ifPresent(i -> grid.get(x - i).get(y).touched((x - i), y, grid, checkIfGameOrNot));
        //propagazione a destra
        IntStream.range(1, 6)
                .filter(i -> y + i <= 5)
                .filter(i -> grid.get(x).get(y + i).getStatement() != BubbleStatement.EXPLODED)
                .findFirst()
                .ifPresent(i -> grid.get(x).get(y + i).touched(x, (y + i), grid, checkIfGameOrNot));
        //propagazione in basso
        IntStream.range(1, 5)
                .filter(i -> x + i <= 4)
                .filter(i -> grid.get(x + i).get(y).getStatement() != BubbleStatement.EXPLODED)
                .findFirst()
                .ifPresent(i -> grid.get(x + i).get(y).touched((x + i), y, grid, checkIfGameOrNot));
    }

    public double checkExplosion(int x, int y, BubbleStatement statement) {
        GameGrid grid = GameGrid.getGameGrid();
        //controlli se 2° livello è già controllato da altre bolle
        boolean flagL = false;
        boolean flagUp = false;
        boolean flagR = false;
        double bubbleToCount = 0;

        //propagazione a sinistra
        if (lPropagation(grid.getGrid(), x, y) == 1) {
            flagL = true;
            bubbleToCount +=
                    lPropagation(grid.getGrid(), x, y)+

                            lPropagation(grid.getGrid(), x, y - 1) +
                            upPropagation(grid.getGrid(), x, y - 1) +
                            dwPropagation(grid.getGrid(), x, y - 1);
        } else {
            bubbleToCount += lPropagation(grid.getGrid(), x, y);
        }

        //propagazione in alto
        if (upPropagation(grid.getGrid(), x, y) == 1) {
            flagUp = true;
            bubbleToCount +=
                    upPropagation(grid.getGrid(), x, y)+

                            upPropagation(grid.getGrid(), x - 1, y) +
                            rPropagation(grid.getGrid(), x - 1, y);
            if (!flagL) {
                bubbleToCount += lPropagation(grid.getGrid(), x - 1, y);
            }
        } else{
            bubbleToCount += upPropagation(grid.getGrid(), x, y);
        }

        //propagazione a destra
        if (rPropagation(grid.getGrid(), x, y) == 1) {
            flagR = true;
            bubbleToCount +=
                    rPropagation(grid.getGrid(), x, y)+

                            rPropagation(grid.getGrid(), x, y+1) +
                            dwPropagation(grid.getGrid(), x, y + 1);
            if (!flagUp) {
                bubbleToCount += upPropagation(grid.getGrid(), x, y + 1);
            }
        } else{
            bubbleToCount += rPropagation(grid.getGrid(), x, y);
        }

        //propagazione in basso
        if (dwPropagation(grid.getGrid(), x, y) == 1) {
            bubbleToCount++;
            bubbleToCount +=
                    dwPropagation(grid.getGrid(), x, y)+

                            dwPropagation(grid.getGrid(), x + 1, y);
            if (!flagR) {
                bubbleToCount += rPropagation(grid.getGrid(), x + 1, y);
            }
            if (!flagL) {
                bubbleToCount += lPropagation(grid.getGrid(), x + 1, y);
            }
        } else {
            bubbleToCount += dwPropagation(grid.getGrid(), x, y);
        }

        if(statement == BubbleStatement.READY_TO_EXPLODE){
            bubbleToCount += checkExplosion(x, y, BubbleStatement.PUFFY);
        }
        else if(statement == BubbleStatement.PUFFY){
            bubbleToCount += checkExplosion(x, y, BubbleStatement.EMPTY);
        }
        return bubbleToCount;
    }

    private double lPropagation(List<List<Bubble>> grid, int x, int y) {
        return IntStream.range(1, 2)
                .filter(i -> y - i >= 0 && y-i <= 5)
                .filter(i -> x >= 0 && x <= 4)
                .mapToDouble(i -> grid.get(x).get(y - i).getValue())
                .findFirst()
                .orElse(0);
    }

    private double upPropagation(List<List<Bubble>> grid, int x, int y) {
        return IntStream.range(1, 2)
                .filter(i -> x - i >= 0 && x-i <= 4)
                .filter(i -> y >= 0 && y <= 5)
                .mapToDouble(i -> grid.get(x - i).get(y).getValue())
                .findFirst()
                .orElse(0);
    }

    private double rPropagation(List<List<Bubble>> grid, int x, int y) {
        return IntStream.range(1, 2)
                .filter(i -> y + i <= 5 && y >= 0)
                .filter(i -> x <= 4 && x >= 0)
                .mapToDouble(i -> grid.get(x).get(y + 1).getValue())
                .findFirst()
                .orElse(0);
    }

    private double dwPropagation(List<List<Bubble>> grid, int x, int y) {
        return IntStream.range(1, 2)
                .filter(i -> x + i <= 4 && x+i >= 0)
                .filter(i -> y <=5 && y >= 0)
                .mapToDouble(i -> grid.get(x + 1).get(y).getValue())
                .findFirst()
                .orElse(0);
    }

}

