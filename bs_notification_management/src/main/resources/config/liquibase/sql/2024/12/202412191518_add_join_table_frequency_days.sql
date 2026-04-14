CREATE TABLE IF NOT EXISTS bs_frequency_days (
    frequency_id UUID NOT NULL,
    day_id UUID NOT NULL,
    PRIMARY KEY (frequency_id, day_id),
    CONSTRAINT fk_frequency_days__frequency FOREIGN KEY (frequency_id) REFERENCES bs_frequency (id) ON DELETE CASCADE,
    CONSTRAINT fk_frequency_days__days FOREIGN KEY (day_id) REFERENCES bs_days (id) ON DELETE CASCADE
);
