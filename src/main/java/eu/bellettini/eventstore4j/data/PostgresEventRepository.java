package eu.bellettini.eventstore4j.data;

import java.sql.*;
import java.time.Clock;
import java.util.UUID;

public class PostgresEventRepository implements EventRepository {
    private final Connection connection;
    private final Clock clock;


    public PostgresEventRepository(Connection connection, Clock clock) {
        this.connection = connection;
        this.clock = clock;
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
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("TRUNCATE TABLE events");
        } catch (SQLException e) {
            throw new EventStoreException(e);
        }
    }

    @Override
    public void store(String aggregateId, long expectedVersion, WriteEvent... events) {
        if (nothingToStore(events)) {
            return;
        }

        ensureExpectedVersion(aggregateId, expectedVersion);
        storeEvents(aggregateId, expectedVersion, events);
    }

    private void ensureExpectedVersion(String aggregateId, long expectedVersion) {
        long actualVersion = count(aggregateId);
        if (actualVersion != expectedVersion) {
            throw new ConsistencyException(
                    String.format("Expected version %d got %d", expectedVersion, actualVersion)
            );
        }
    }


    private void storeEvents(String aggregateId, long expectedVersion, WriteEvent[] events) {
        final String sql = "INSERT INTO events " +
                "(event_id, aggregate_id, aggregate_version, created_at, source," +
                "type, type_version, payload, received_at) " +
                "VALUES (?::uuid,?,?,?,?,?,?,?::json,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            long version = expectedVersion;
            for (WriteEvent event: events) {
                int i = 0;
                stmt.setString(++i, event.getId().toString());
                stmt.setString(++i, aggregateId);
                stmt.setLong(++i, version++);
                stmt.setTimestamp(++i, Timestamp.from(event.getCreatedAt()));
                stmt.setString(++i, event.getSource());
                stmt.setString(++i, event.getType());
                stmt.setInt(++i, event.getTypeVersion());
                stmt.setString(++i, event.getPayload().toString());
                stmt.setTimestamp(++i, Timestamp.from(clock.instant()));
                stmt.execute();
            }
        } catch (SQLException e) {
            throw new EventStoreException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new EventStoreException(e);
            }
        }
    }

    @Override
    public ReadEvent findOneById(UUID id) {
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

    private ReadEvent dtoFromResultSet(ResultSet resultSet) throws SQLException {
        return new ReadEvent(
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

    private boolean nothingToStore(WriteEvent[] events) {
        return events.length == 0;
    }

    private long count(String aggregateId) {
        final String sql = "SELECT COUNT(*) FROM events WHERE aggregate_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, aggregateId);

            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();

            return resultSet.getLong(1);
        } catch (SQLException e) {
            throw new EventStoreException(e);
        }
    }
}
