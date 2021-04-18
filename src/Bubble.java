import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.*;
import java.util.stream.IntStream;

@NoArgsConstructor
@Data
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
        this.x = x;
        this.y = y;
    }

    //Methods
    public boolean touched(int x, int y, List<List<Bubble>> grid, boolean checkIfGameOrNot, int nTimes) {
        GameGrid gameGrid = GameGrid.getGameGrid();
        switch (grid.get(x).get(y).getStatement()) {
            case EMPTY -> {
                grid.get(x).get(y).setBubbleView("(2)");
                if(checkIfGameOrNot) {
                    gameGrid.gridStamp();
                    gameGrid.getMovesOfGame()
                            .append(gameGrid.getNumberedList())
                            .append(") Alle coordinate x: ")
                            .append(x)
                            .append(" y: ")
                            .append(y)
                            .append(" la bolla è passata a mezza gonfia.\n");
                    gameGrid.setNumberedList(gameGrid.getNumberedList()+1);
                }
                grid.get(x).get(y).setStatement(BubbleStatement.PUFFY);
                grid.get(x).get(y).setValue(0.5);
            }
            case PUFFY -> {
                grid.get(x).get(y).setBubbleView("(1)");
                if(checkIfGameOrNot) {
                    gameGrid.gridStamp();
                    gameGrid.getMovesOfGame()
                            .append(gameGrid.getNumberedList())
                            .append(") Alle coordinate x: ")
                            .append(x)
                            .append(" y: ")
                            .append(y)
                            .append(" la bolla è passata a pronta per esplodere.\n");
                    gameGrid.setNumberedList(gameGrid.getNumberedList()+1);
                }
                grid.get(x).get(y).setStatement(BubbleStatement.READY_TO_EXPLODE);
                grid.get(x).get(y).setValue(1);
            }
            case READY_TO_EXPLODE -> {
                grid.get(x).get(y).setBubbleView("   ");
                if(checkIfGameOrNot) {
                    gameGrid.gridStamp();
                    gameGrid.getMovesOfGame()
                            .append(gameGrid.getNumberedList())
                            .append(") Alle coordinate x: ")
                            .append(x)
                            .append(" y: ")
                            .append(y)
                            .append(" la bolla è esplosa.\n");
                    gameGrid.setNumberedList(gameGrid.getNumberedList()+1);
                }
                grid.get(x).get(y).setStatement(BubbleStatement.EXPLODED);
                explosion(x, y, grid, checkIfGameOrNot);
                grid.get(x).get(y).setValue(0);
            }
            default -> {
                return false;
            }
        }
        if(nTimes == 2){
            touched(x, y, grid, checkIfGameOrNot, 1);
        }
        else if (nTimes == 3){
            touched(x, y, grid, checkIfGameOrNot, 2);
        }
        return true;
    }

    private void explosion(int x, int y, List<List<Bubble>> grid, boolean checkIfGameOrNot) {
        //propagazione a sinistra
        IntStream.range(1, 6)
                .filter(i -> y - i >= 0)
                .filter(i -> grid.get(x).get(y - i).getStatement() != BubbleStatement.EXPLODED)
                .findFirst()
                .ifPresent(i -> grid.get(x).get(y - i).touched(x, (y - i), grid, checkIfGameOrNot, 1));
        //propagazione in alto
        IntStream.range(1, 5)
                .filter(i -> x - i >= 0)
                .filter(i -> grid.get(x - i).get(y).getStatement() != BubbleStatement.EXPLODED)
                .findFirst()
                .ifPresent(i -> grid.get(x - i).get(y).touched((x - i), y, grid, checkIfGameOrNot, 1));
        //propagazione a destra
        IntStream.range(1, 6)
                .filter(i -> y + i <= 5)
                .filter(i -> grid.get(x).get(y + i).getStatement() != BubbleStatement.EXPLODED)
                .findFirst()
                .ifPresent(i -> grid.get(x).get(y + i).touched(x, (y + i), grid, checkIfGameOrNot, 1));
        //propagazione in basso
        IntStream.range(1, 5)
                .filter(i -> x + i <= 4)
                .filter(i -> grid.get(x + i).get(y).getStatement() != BubbleStatement.EXPLODED)
                .findFirst()
                .ifPresent(i -> grid.get(x + i).get(y).touched((x + i), y, grid, checkIfGameOrNot, 1));
    }

    public double checkExplosion(int x, int y/*, BubbleStatement statement*/, List<List<Bubble>> grid) {
        boolean flagL = false;
        boolean flagUp = false;
        boolean flagR = false;
        double bubbleToCount = 0;

        //propagazione a sinistra
        if (lPropagation(grid, x, y) == 1) {
            flagL = true;
            bubbleToCount +=
                    lPropagation(grid, x, y)+

                            lPropagation(grid, x, y - 1) +
                            upPropagation(grid, x, y - 1) +
                            dwPropagation(grid, x, y - 1);
        } else {
            bubbleToCount += lPropagation(grid, x, y);
        }

        //propagazione in alto
        if (upPropagation(grid, x, y) == 1) {
            flagUp = true;
            bubbleToCount +=
                    upPropagation(grid, x, y)+

                            dwPropagation(grid, x, y)+

                            upPropagation(grid, x - 1, y) +
                            rPropagation(grid, x - 1, y);
            if (!flagL) {
                bubbleToCount += lPropagation(grid, x - 1, y);
            }
        } else{
            bubbleToCount += upPropagation(grid, x, y);
        }

        //propagazione a destra
        if (rPropagation(grid, x, y) == 1) {
            flagR = true;
            bubbleToCount +=
                    rPropagation(grid, x, y)+

                            rPropagation(grid, x, y+1) +
                            dwPropagation(grid, x, y + 1);
            if (!flagUp) {
                bubbleToCount += upPropagation(grid, x, y + 1);
            }
        } else{
            bubbleToCount += rPropagation(grid, x, y);
        }

        //propagazione in basso
        if (dwPropagation(grid, x, y) == 1) {
            bubbleToCount++;
            bubbleToCount +=
                    dwPropagation(grid, x, y)+

                            dwPropagation(grid, x + 1, y);
            if (!flagR) {
                bubbleToCount += rPropagation(grid, x + 1, y);
            }
            if (!flagL) {
                bubbleToCount += lPropagation(grid, x + 1, y);
            }
        } else {
            bubbleToCount += dwPropagation(grid, x, y);
        }

/*
        if(statement == BubbleStatement.READY_TO_EXPLODE){
            bubbleToCount += checkExplosion(x, y, BubbleStatement.PUFFY, grid);
        }
        else if(statement == BubbleStatement.PUFFY){
            bubbleToCount += checkExplosion(x, y, BubbleStatement.EMPTY, grid);
        }
*/
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
                .mapToDouble(i -> grid.get(x).get(y + i).getValue())
                .findFirst()
                .orElse(0);
    }

    private double dwPropagation(List<List<Bubble>> grid, int x, int y) {
        return IntStream.range(1, 2)
                .filter(i -> x + i <= 4 && x+i >= 0)
                .filter(i -> y <=5 && y >= 0)
                .mapToDouble(i -> grid.get(x + i).get(y).getValue())
                .findFirst()
                .orElse(0);
    }

}

