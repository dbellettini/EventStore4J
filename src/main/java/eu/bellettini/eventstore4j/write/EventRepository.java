package eu.bellettini.eventstore4j.write;

import java.util.UUID;

public interface EventRepository {
    long count();

    void clean();

    void store(String aggregateId, long expectedVersion, WriteEvent... events);

    ReadEvent findOneById(UUID id);
}
