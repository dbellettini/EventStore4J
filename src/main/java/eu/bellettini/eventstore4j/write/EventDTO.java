package eu.bellettini.eventstore4j.write;

import org.json.JSONObject;

import java.time.Instant;
import java.util.UUID;

public final class EventDTO {
    private final UUID id;

    private final String aggregateId;

    private final long aggregateVersion;

    private final Instant createdAt;

    private final String source;

    private final String type;

    private final int typeVersion;

    private final JSONObject payload;

    private final Instant receivedAt;

    public EventDTO(UUID id, String aggregateId, long aggregateVersion, Instant createdAt, String source, String type,
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

    public EventDTO(UUID id, String aggregateId, long aggregateVersion, Instant createdAt, String source, String type,
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventDTO eventDTO = (EventDTO) o;

        int i = 0;
        if (aggregateVersion != eventDTO.aggregateVersion) return false;
        if (typeVersion != eventDTO.typeVersion) return false;
        if (!id.equals(eventDTO.id)) return false;
        if (!aggregateId.equals(eventDTO.aggregateId)) return false;
        if (!createdAt.equals(eventDTO.createdAt)) return false;
        if (!source.equals(eventDTO.source)) return false;
        if (!type.equals(eventDTO.type)) return false;
        if (!payload.toString().equals(eventDTO.payload.toString())) return false;
        return receivedAt.equals(eventDTO.receivedAt);

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

    @Override
    public String toString() {
        return "EventDTO{" +
                "id=" + id +
                ", aggregateId='" + aggregateId + '\'' +
                ", aggregateVersion=" + aggregateVersion +
                ", createdAt=" + createdAt +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", typeVersion=" + typeVersion +
                ", payload=" + payload +
                ", receivedAt=" + receivedAt +
                '}';
    }
}
