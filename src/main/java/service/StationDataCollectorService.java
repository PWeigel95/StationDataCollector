package service;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class StationDataCollectorService extends BaseService {

    private final String id;

    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/postgres?user=admin&password=password";

    private int totalKwh = 0;

    public StationDataCollectorService(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);

        this.id = UUID.randomUUID().toString();

        System.out.println("Station Data (" + this.id + ") started...");
    }

    @Override
    protected String executeInternal(String input) {

        if(!Objects.equals(input, "ok")){
            String [] list = input.split(":");

            //customerId
            System.out.println("customerId: " + list[0]);
            //stationId
            System.out.println("stationId: " + list[2]);

            /*
            int customerId = Integer.parseInt(input.substring(0,input.indexOf(":")));
            int stationId = Integer.parseInt(input.substring(input.indexOf(":") + 1));

             */

            int customerId = Integer.parseInt(list[0]);
            String invoiceId = list[1];
            int stationId = Integer.parseInt(list[2]);
            int amountOfStations = Integer.parseInt(list[3]);

            System.out.println(getKwhOfStation(customerId, stationId));

            return getKwhOfStation(customerId, stationId) + ":" + amountOfStations + ":" + invoiceId + ":" + customerId;

        }

        return "";
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_CONNECTION);
    }

    public int getKwhOfStation(int customerId, int stationId) {
        try (Connection conn = connect()) {
            String sql = "SELECT SUM(kwh) FROM charging_stations_history WHERE customer_id = ? AND station_id = ? ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, customerId);
            preparedStatement.setInt(2, stationId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return resultSet.getInt(1);
            }
            else return 0;

        } catch (SQLException e) {
            return 0;
        }
    }


}
