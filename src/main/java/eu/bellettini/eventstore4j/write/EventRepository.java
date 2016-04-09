package eu.bellettini.eventstore4j.write;

import java.util.UUID;

public interface EventRepository {
    long count();

    void clean();

    void store(EventDTO... events);

    EventDTO findOneById(UUID id);
}
