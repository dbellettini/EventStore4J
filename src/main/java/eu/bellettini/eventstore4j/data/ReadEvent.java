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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReadEvent readEvent = (ReadEvent) o;

        if (aggregateVersion != readEvent.aggregateVersion) return false;
        if (typeVersion != readEvent.typeVersion) return false;
        if (!id.equals(readEvent.id)) return false;
        if (!aggregateId.equals(readEvent.aggregateId)) return false;
        if (!createdAt.equals(readEvent.createdAt)) return false;
        if (!source.equals(readEvent.source)) return false;
        if (!type.equals(readEvent.type)) return false;
        if (!payload.toString().equals(readEvent.payload.toString())) return false;
        return receivedAt.equals(readEvent.receivedAt);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + aggregateId.hashCode();
        result = 31 * result + (int) (aggregateVersion ^ (aggregateVersion >>> 32));
        result = 31 * result + createdAt.hashCode();
        result = 31 * result + source.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + typeVersion;
        result = 31 * result + payload.toString().hashCode();
        result = 31 * result + receivedAt.hashCode();
        return result;
    }
}
