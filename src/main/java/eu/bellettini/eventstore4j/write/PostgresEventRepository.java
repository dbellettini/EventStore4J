package eu.bellettini.eventstore4j.write;

import java.sql.*;
import java.util.UUID;

public class PostgresEventRepository implements EventRepository, AutoCloseable {
    private final Connection connection;


    public PostgresEventRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public long count() {
        try (
                Statement stmt = connection.createStatement();
                ResultSet result = stmt.executeQuery("SELECT COUNT(*) FROM events")
        ) {
            result.next();

            return result.getLong(1);
        } catch (SQLException e) {
            throw new EventStoreException(e);
        }
    }

    @Override
    public void clean() {

        Statement stmt;
        try {
            stmt = connection.createStatement();
            stmt.execute("TRUNCATE TABLE events");
        } catch (SQLException e) {
            throw new EventStoreException(e);
        }
    }

    @Override
    public void store(EventDTO... events) {
        final String sql = "INSERT INTO events " +
                "(event_id, aggregate_id, aggregate_version, created_at, source," +
                "type, type_version, payload, received_at) " +
                "VALUES (?::uuid,?,?,?,?,?,?,?::json,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (EventDTO event: events) {
                int i = 0;
                stmt.setString(++i, event.getId().toString());
                stmt.setString(++i, event.getAggregateId());
                stmt.setLong(++i, event.getAggregateVersion());
                stmt.setTimestamp(++i, Timestamp.from(event.getCreatedAt()));
                stmt.setString(++i, event.getSource());
                stmt.setString(++i, event.getType());
                stmt.setInt(++i, event.getTypeVersion());
                stmt.setString(++i, event.getPayload().toString());
                stmt.setTimestamp(++i, Timestamp.from(event.getReceivedAt()));
                stmt.execute();
            }
        } catch (SQLException e) {
            throw new EventStoreException(e);
        }
    }

    @Override
    public EventDTO findOneById(UUID id) {
        final String sql = "SELECT " +
                "event_id::text, aggregate_id, aggregate_version, created_at, source, type, type_version, " +
                "payload::text, received_at " +
                "FROM events WHERE event_id = ?::uuid";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id.toString());

            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();

            return dtoFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new EventStoreException(e);
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    private EventDTO dtoFromResultSet(ResultSet resultSet) throws SQLException {
        return new EventDTO(
                UUID.fromString(resultSet.getString(1)),
                resultSet.getString(2),
                resultSet.getLong(3),
                resultSet.getTimestamp(4).toInstant(),
                resultSet.getString(5),
                resultSet.getString(6),
                resultSet.getInt(7),
                resultSet.getString(8),
                resultSet.getTimestamp(9).toInstant()
        );
    }
}
