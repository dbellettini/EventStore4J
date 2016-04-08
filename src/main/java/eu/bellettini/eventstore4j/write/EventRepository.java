package eu.bellettini.eventstore4j.write;

import java.util.UUID;

public interface EventRepository {
    long count();

    void clean();

    void store(EventDTO event);

    EventDTO findOneById(UUID id);
}
