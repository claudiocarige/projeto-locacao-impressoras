CREATE TABLE monthly_payment
(
    id                                   BIGSERIAL PRIMARY KEY,
    amount_printer                       DOUBLE PRECISION,
    excess_value_prints_color            DOUBLE PRECISION,
    excess_value_prints_pb                DOUBLE PRECISION,
    expiration_date                      DATE,
    month_payment                        INTEGER,
    monthly_amount                       DOUBLE PRECISION,
    payment_date                         DATE,
    printing_franchise_color             INTEGER,
    printing_franchise_pb                 INTEGER,
    quantity_prints_color                INTEGER,
    quantity_prints_pb                    INTEGER,
    rate_excess_black_and_white_printing DOUBLE PRECISION,
    rate_excess_color_printing           DOUBLE PRECISION,
    year_payment                         INTEGER,
    customer_id                          BIGINT,
    invoice_number                       VARCHAR(30),
    payment_status                       VARCHAR(25) CHECK (payment_status IN ('PAGO', 'PENDENTE', 'ATRASADO', 'CANCELADO')),
    ticket_number                        VARCHAR(100),
    CONSTRAINT fk_monthly_payment_customer FOREIGN KEY (customer_id) REFERENCES customer (id)
);