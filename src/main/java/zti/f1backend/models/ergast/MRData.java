package zti.f1backend.models.ergast;

@lombok.Data
public class MRData {
    private String xmlns;
    private String series;
    private String url;
    private String limit;
    private String offset;
    private String total;
    private RaceTable raceTable;
}