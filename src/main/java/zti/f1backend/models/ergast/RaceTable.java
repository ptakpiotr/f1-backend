package zti.f1backend.models.ergast;

@lombok.Data
public class RaceTable {
    private String season;
    private String round;
    private Race[] races;
}