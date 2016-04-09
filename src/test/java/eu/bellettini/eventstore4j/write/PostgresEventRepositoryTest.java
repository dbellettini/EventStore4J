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
        WriteEvent event = anEvent();

        repository.store("42", 0, event);

        assertEquals(1L, repository.count());

        assertStoredEventEquals(event);
    }

    @Test
    public void itShouldStoreAnEventBatch()
    {
        WriteEvent[] events = {anEvent(), anEvent()};
        repository.store("42", 0, events);

        assertEquals(events.length, repository.count());
        for(WriteEvent event : events) {
            assertStoredEventEquals(event);
        }
    }

    @Test(expected = ConsistencyException.class)
    public void itShouldAvoidNonSubsequentAggregateVersionsInDifferentBatches()
    {
        WriteEvent[] events = {anEvent(), anEvent()};

        repository.store("42", 0, events);
        repository.store("42", 3, anEvent());
    }

    private void assertStoredEventEquals(WriteEvent event) {
        ReadEvent retrieved = repository.findOneById(event.getId());

        assertEquals(event, retrieved.toWriteEvent());
        assertEquals(retrieved.getReceivedAt(), clock.instant());
    }

    private WriteEvent anEvent() {
        UUID uuid = UUID.randomUUID();

        return new WriteEvent(
                uuid,
                Instant.ofEpochMilli(1459589665000L),
                "somewhere",
                "something-happened",
                1,
                "{foo: \"bar\"}"
        );
    }
}


