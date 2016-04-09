package eu.bellettini.eventstore4j.write;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Properties;
import java.util.UUID;

public class PostgresEventRepositoryTest {
    private static Connection connection;

    private PostgresEventRepository repository;

    private Clock clock;

    @BeforeClass
    public static void initSchema() throws Exception {
        InputStream inputStream = PostgresEventRepositoryTest.class
                .getClassLoader()
                .getResourceAsStream("postgresql.properties");

        Properties properties = new Properties();
        properties.load(inputStream);
        String url = (String)properties.remove("url");

        connection = DriverManager.getConnection(url, properties);
    }

    @AfterClass
    public static void closeDBConnection() throws Exception {
        connection.close();
    }

    @Before
    public void setUp() throws Exception {
        clock = Clock.fixed(Instant.now(), ZoneId.of("UTC"));
        repository = new PostgresEventRepository(connection, clock);
        repository.clean();
    }

    @Test
    public void itShouldCountZeroWhenNoEventExists() {
        assertEquals(0L, repository.count());
    }

    @Test
    public void itShouldStoreAnEvent() {
        EventDTO event = anEvent(0);

        repository.store(event);

        assertEquals(1L, repository.count());

        assertStoredEventEquals(event);
    }

    @Test
    public void itShouldStoreAnEventBatch()
    {
        EventDTO[] events = {anEvent(0), anEvent(1)};
        repository.store(events);

        assertEquals(events.length, repository.count());
        for(EventDTO event : events) {
            assertStoredEventEquals(event);
        }
    }

    @Test(expected = ConsistencyException.class)
    public void itShouldAvoidNonSubsequentAggregateVersionsOnASingleBatch()
    {
        EventDTO[] events = {anEvent(0), anEvent(2)};
        repository.store(events);
    }

    @Test(expected = ConsistencyException.class)
    public void itShouldAvoidNonSubsequentAggregateVersionsInDifferentBatches()
    {
        EventDTO[] events = {anEvent(0), anEvent(1)};

        repository.store(events);
        repository.store(anEvent(3));
    }

    private void assertStoredEventEquals(EventDTO event) {
        EventDTO retrieved = repository.findOneById(event.getId());
        assertEquals(event, retrieved);
    }

    private EventDTO anEvent(int aggregateVersion) {
        UUID uuid = UUID.randomUUID();

        return new EventDTO(
                uuid,
                "42",
                aggregateVersion,
                Instant.ofEpochMilli(1459589665000L),
                "somewhere",
                "something-happened",
                1,
                "{foo: \"bar\"}",
                clock.instant()
        );
    }
}


