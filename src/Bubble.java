import java.util.Objects;

public class Bubble {
    private BubbleStatement statement;
    private String appearance;

    public void Touched(){
        if(statement == BubbleStatement.EMPTY) {
            statement = BubbleStatement.PUFFY;
            appearance = "(2)";
        }
        if(statement == BubbleStatement.PUFFY) {
            statement = BubbleStatement.READY_TO_EXPLODE;
            appearance = "(1)";

        }
        if(statement == BubbleStatement.READY_TO_EXPLODE) {
            statement = BubbleStatement.EXPLODED;
            appearance = " ";
        }
    }

//    Constructors
    public Bubble(int n) {
        if(n == 2) {
            appearance = "(3)";
            statement = BubbleStatement.EMPTY;
        }
        if(n == 1) {
            appearance = "(2)";
            statement = BubbleStatement.PUFFY;
        }
        if(n == 0) {
            appearance = "(1)";
            statement = BubbleStatement.READY_TO_EXPLODE;
    }

}

//    Getters and Setters
    public BubbleStatement getStatement() {
        return statement;
    }
    public void setStatement(BubbleStatement statement) {
        this.statement = statement;
    }
    public String getAppearance() {
        return appearance;
    }
    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }

}


