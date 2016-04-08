BEGIN;

CREATE TABLE IF NOT EXISTS events
(
  event_id          UUID      NOT NULL PRIMARY KEY,

  aggregate_id      TEXT      NOT NULL,
  aggregate_version INTEGER   NOT NULL,
  created_at        TIMESTAMP NOT NULL,
  source            TEXT      NOT NULL,

  type              TEXT      NOT NULL,
  type_version      INTEGER   NOT NULL DEFAULT 1,

  payload           JSON      NOT NULL DEFAULT '{}',

  received_at       TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS events_type ON events (type);
CREATE INDEX IF NOT EXISTS events_received_at ON events USING BRIN (received_at);
CREATE INDEX IF NOT EXISTS events_created_at ON events (created_at);
CREATE UNIQUE INDEX IF NOT EXISTS events_optimistic_lock ON events (aggregate_id, aggregate_version);

COMMIT;
