package game.Model;

/**
 * Record for scoreboard
 */
public class Record {
    /**
     * Name of the record.
     */
    String name;
    /**
     * Achieved score.
     */
    Integer score;

    /**
     * Records constructor with parameters
     * @param name name of the record
     * @param score achieved score
     */
    public Record(String name, Integer score){
    this.name=name;
    this.score=score;
    }

    /**
     * name's getter
     * @return name of the record
     */
    public String getName() {
        return name;
    }

    /**
     * score's getter
     * @return value of score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Record in String form
     * @return readable String form of record
     */
    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
