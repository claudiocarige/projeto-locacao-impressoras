CREATE TABLE Ticket
(
    id            SERIAL PRIMARY KEY,
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    status        VARCHAR(20) CHECK (status IN ('OPEN', 'IN_PROGRESS', 'CLOSED', 'ERROR')),
    priority      VARCHAR(20) CHECK (priority IN ('URGENT', 'HIGH', 'MEDIUM', 'LOW')),
    type          VARCHAR(20) CHECK (type IN ('TECHNICAL_VISIT', 'REFILL', 'TONER')),
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP,
    closed_at      TIMESTAMP,
    client_name    VARCHAR(180),
    technical_name VARCHAR(180),
    client_id     BIGINT NOT NULL,
    technical_id  BIGINT NOT NULL
);

