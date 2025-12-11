-- Change release_date to VARCHAR to match domain model
ALTER TABLE sets ALTER COLUMN release_date TYPE VARCHAR(50);

-- Add indexes for Sets
CREATE INDEX idx_sets_name ON sets(name);
CREATE INDEX idx_sets_release_date ON sets(release_date);
CREATE INDEX idx_sets_data ON sets USING GIN (data);

-- Add indexes for Cards
CREATE INDEX idx_cards_name ON cards(name);
CREATE INDEX idx_cards_data ON cards USING GIN (data);
