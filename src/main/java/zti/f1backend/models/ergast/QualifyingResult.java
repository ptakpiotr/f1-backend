package zti.f1backend.models.ergast;

@lombok.Data
public class QualifyingResult {
    private String number;
    private String position;
    private Driver driver;
    private Constructor constructor;
    private String q1;
    private String q2;
    private String q3;
}