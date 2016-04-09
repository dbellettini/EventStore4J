package eu.bellettini.eventstore4j.data;

import java.util.UUID;

public interface EventRepository {
    long count();

    void clean();

    void store(String aggregateId, long expectedVersion, WriteEvent... events);

    void store(String aggregateId, WriteEvent... events);

    ReadEvent findOneById(UUID id);
}
