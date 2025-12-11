CREATE TABLE series (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    logo VARCHAR(500)
);

CREATE TABLE sets (
    id VARCHAR(255) PRIMARY KEY,
    series_id VARCHAR(255) NOT NULL REFERENCES series(id),
    name VARCHAR(255) NOT NULL,
    logo VARCHAR(500),
    symbol VARCHAR(500),
    release_date DATE,
    data JSONB DEFAULT '{}'::jsonb
);

CREATE INDEX idx_sets_series_id ON sets(series_id);

CREATE TABLE cards (
    id VARCHAR(255) PRIMARY KEY,
    set_id VARCHAR(255) NOT NULL REFERENCES sets(id),
    image VARCHAR(500),
    local_id VARCHAR(100),
    name VARCHAR(255) NOT NULL,
    rarity VARCHAR(100),
    avg_price DECIMAL(10, 2),
    data JSONB DEFAULT '{}'::jsonb
);

CREATE INDEX idx_cards_set_id ON cards(set_id);
