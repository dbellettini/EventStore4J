package eu.bellettini.eventstore4j.write;

import org.json.JSONObject;

import java.time.Instant;
import java.util.UUID;

public final class WriteEvent {
    private final UUID id;

    private final Instant createdAt;

    private final String source;

    private final String type;

    private final int typeVersion;

    private final JSONObject payload;

    public WriteEvent(UUID id, Instant createdAt, String source, String type, int typeVersion, JSONObject payload) {
        this.id = id;
        this.createdAt = createdAt;
        this.source = source;
        this.type = type;
        this.typeVersion = typeVersion;
        this.payload = payload;
    }

    public WriteEvent(UUID id, Instant createdAt, String source, String type, int typeVersion, String payload) {
        this(id, createdAt, source, type, typeVersion, new JSONObject(payload));
    }

    public UUID getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WriteEvent eventDTO = (WriteEvent) o;

        if (typeVersion != eventDTO.typeVersion) return false;
        if (id != null ? !id.equals(eventDTO.id) : eventDTO.id != null) return false;
        if (createdAt != null ? !createdAt.equals(eventDTO.createdAt) : eventDTO.createdAt != null) return false;
        if (source != null ? !source.equals(eventDTO.source) : eventDTO.source != null) return false;
        if (type != null ? !type.equals(eventDTO.type) : eventDTO.type != null) return false;
        return payload != null ? payload.toString().equals(eventDTO.payload.toString()) : eventDTO.payload == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + typeVersion;
        result = 31 * result + (payload != null ? payload.toString().hashCode() : 0);
        return result;
    }
}
