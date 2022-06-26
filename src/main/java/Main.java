import service.StationDataCollectorService;

public class Main {

    private final static String BROKER_URL = "tcp://localhost:61616";

    public static void main(String [] args){

        StationDataCollectorService stationDataCollectorService = new StationDataCollectorService("DATA COLLECTION", "DATA COLLECTION DONE", BROKER_URL);
        stationDataCollectorService.run();

    }
}
