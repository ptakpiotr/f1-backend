package zti.f1backend.models.ergast;


@lombok.Data
public class Race {
    private String season;
    private String round;
    private String url;
    private String raceName;
    private Circuit circuit;
    private String date;
    private QualifyingResult[] qualifyingResults;
}
