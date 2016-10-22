package view;

/**
 *
 * @author fauzianordlund
 */
public class Score {
    private double score;
    public Score(){
        score = 0;
    }
    public void setScore(double score){
        this.score = score;
    }
    public double getScore(){
        return score;
    }
    public void increaseScore(){
        score += score + 1;
    }
    public void setScoreToZero(){
        score = 0;
    }
    @Override
    public String toString(){
        return "Score : " + score;
    }
}
