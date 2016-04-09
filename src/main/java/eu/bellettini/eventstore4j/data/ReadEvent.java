package eu.bellettini.eventstore4j.data;

import org.json.JSONObject;

import java.time.Instant;
import java.util.UUID;

public final class ReadEvent {
    private final UUID id;

    private final String aggregateId;

    private final long aggregateVersion;

    private final Instant createdAt;

    private final String source;

    private final String type;

    private final int typeVersion;

    private final JSONObject payload;

    private final Instant receivedAt;

    public ReadEvent(UUID id, String aggregateId, long aggregateVersion, Instant createdAt, String source, String type,
                     int typeVersion, JSONObject payload, Instant receivedAt) {
        this.id = id;
        this.aggregateId = aggregateId;
        this.aggregateVersion = aggregateVersion;
        this.createdAt = createdAt;
        this.source = source;
        this.type = type;
        this.typeVersion = typeVersion;
        this.payload = payload;
        this.receivedAt = receivedAt;
    }

    public ReadEvent(UUID id, String aggregateId, long aggregateVersion, Instant createdAt, String source, String type,
                     int typeVersion, String payload, Instant receivedAt) {
        this(id, aggregateId, aggregateVersion, createdAt, source, type, typeVersion, new JSONObject(payload), receivedAt);
    }

    public UUID getId() {
        return id;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public long getAggregateVersion() {
        return aggregateVersion;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getSource() {
        return source;
    }

    public String getType() {
        return type;
    }

    public int getTypeVersion() {
        return typeVersion;
    }

    public JSONObject getPayload() {
        return payload;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public WriteEvent toWriteEvent() {
        return new WriteEvent(getId(), getCreatedAt(), getSource(), getType(), getTypeVersion(), getPayload());
    }
}
