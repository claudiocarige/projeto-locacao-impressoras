CREATE TABLE address
(
    id      BIGSERIAL PRIMARY KEY,
    city    VARCHAR(60),
    country VARCHAR(60),
    number  VARCHAR(20),
    state   VARCHAR(30),
    street  VARCHAR(30)
);

CREATE TABLE customer_contract
(
    id                       BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    printing_franchise_pb    INTEGER,
    printing_franchise_color INTEGER,
    monthly_amount           DOUBLE PRECISION,
    contract_time            SMALLINT,
    start_contract           DATE,
    printer_type_pb          VARCHAR(60) CHECK (printer_type_pb IN
                                                 ('LASER_BLACK_AND_WHITE_EASY', 'LASER_BLACK_AND_WHITE_MEDIUM',
                                                  'LASER_BLACK_AND_WHITE_HARD', 'LASER_BLACK_AND_WHITE_EXTREME',
                                                  'LASER_COLOR_EASY', 'LASER_COLOR_MEDIUM', 'LASER_COLOR_HARD',
                                                  'LASER_COLOR_EXTREME', 'INKJET_COLOR_EASY', 'INKJET_COLOR_MEDIUM',
                                                  'INKJET_COLOR_HARD')),
    printer_type_color       VARCHAR(60) CHECK (printer_type_color IN
                                                 ('LASER_BLACK_AND_WHITE_EASY', 'LASER_BLACK_AND_WHITE_MEDIUM',
                                                  'LASER_BLACK_AND_WHITE_HARD', 'LASER_BLACK_AND_WHITE_EXTREME',
                                                  'LASER_COLOR_EASY', 'LASER_COLOR_MEDIUM', 'LASER_COLOR_HARD',
                                                  'LASER_COLOR_EXTREME', 'INKJET_COLOR_EASY', 'INKJET_COLOR_MEDIUM',
                                                  'INKJET_COLOR_HARD'))
);

CREATE TABLE customer
(
    id                  BIGSERIAL PRIMARY KEY,
    pay_day             SMALLINT    NOT NULL,
    address_id          BIGINT UNIQUE,
    contract_id         BIGINT UNIQUE,
    cpf                 VARCHAR(14) UNIQUE,
    cnpj                VARCHAR(18) UNIQUE,
    dtype               VARCHAR(50) NOT NULL,
    bank_code           VARCHAR(25),
    client_name         VARCHAR(180),
    financial_situation VARCHAR(25) CHECK (financial_situation IN ('PAGO', 'PENDENTE', 'INADIMPLENTE', 'CANCELADO')),
    phone_number        VARCHAR(20),
    primary_email       VARCHAR(20) UNIQUE,
    whatsapp            VARCHAR(20),
    CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES address (id),
    CONSTRAINT fk_contract FOREIGN KEY (contract_id) REFERENCES customer_contract (id)
);

CREATE TABLE customer_email_list
(
    customer_id BIGINT NOT NULL,
    email_list  VARCHAR(100),
    CONSTRAINT fk_customer_email_list FOREIGN KEY (customer_id) REFERENCES customer (id)
);

CREATE TABLE multi_printer
(
    id                         BIGSERIAL PRIMARY KEY,
    impression_counter_before  INTEGER,
    impression_counter_initial INTEGER,
    impression_counter_now     INTEGER,
    machine_value              DOUBLE PRECISION,
    monthly_printer_amount     DOUBLE PRECISION,
    print_type                 SMALLINT CHECK (print_type BETWEEN 0 AND 10),
    printing_franchise         INTEGER,
    customer_id                BIGINT,
    brand                      VARCHAR(30),
    machine_status             VARCHAR(25) CHECK (machine_status IN ('LOCADA', 'DISPONIVEL', 'MANUTENCAO', 'INATIVA')),
    model                      VARCHAR(40),
    serial_number              VARCHAR(60) UNIQUE,
    CONSTRAINT fk_multi_printer_customer FOREIGN KEY (customer_id) REFERENCES customer (id)
);