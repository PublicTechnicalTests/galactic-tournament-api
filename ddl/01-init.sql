-- ============================================================================
-- DDL - GALACTIC TOURNAMENT API DATABASE
-- Database: galactic_tournament
-- DBMS: PostgreSQL 16
-- ============================================================================

-- ============================================================================
-- TABLE: ESPECIES (Species)
-- Description: Stores information about galactic species participating in tournaments
-- ============================================================================

CREATE TABLE IF NOT EXISTS especies (
    specie_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    power INTEGER NOT NULL,
    ability VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_specie_name UNIQUE (name),
    CONSTRAINT check_power_positive CHECK (power > 0)
);

-- Create indexes on especies table
CREATE INDEX IF NOT EXISTS idx_specie_name ON especies (name);
CREATE INDEX IF NOT EXISTS idx_specie_power ON especies (power DESC);
CREATE INDEX IF NOT EXISTS idx_specie_creation_date ON especies (creation_date DESC);

-- Add table comments
COMMENT ON TABLE especies IS 'Table storing information about galactic species';
COMMENT ON COLUMN especies.specie_id IS 'Unique identifier for the species';
COMMENT ON COLUMN especies.name IS 'Name of the species (unique)';
COMMENT ON COLUMN especies.power IS 'Power level of the species (must be positive)';
COMMENT ON COLUMN especies.ability IS 'Special ability of the species';
COMMENT ON COLUMN especies.creation_date IS 'Timestamp when the species was created';

-- ============================================================================
-- TABLE: COMBATS (Battles)
-- Description: Records battles between two species and determines the winner
-- ============================================================================

CREATE TABLE IF NOT EXISTS combats (
    combat_id BIGSERIAL PRIMARY KEY,
    first_contender_id BIGINT NOT NULL,
    second_contender_id BIGINT NOT NULL,
    winner_id BIGINT NOT NULL,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Key Constraints
    CONSTRAINT fk_combat_first_contender
        FOREIGN KEY (first_contender_id)
        REFERENCES especies(specie_id) ON DELETE RESTRICT,

    CONSTRAINT fk_combat_second_contender
        FOREIGN KEY (second_contender_id)
        REFERENCES especies(specie_id) ON DELETE RESTRICT,

    CONSTRAINT fk_combat_winner
        FOREIGN KEY (winner_id)
        REFERENCES especies(specie_id) ON DELETE RESTRICT,

    -- Business Logic Constraints
    CONSTRAINT check_different_contenders
        CHECK (first_contender_id != second_contender_id),

    CONSTRAINT check_winner_is_contender
        CHECK (winner_id IN (first_contender_id, second_contender_id))
);

-- Create indexes on combats table
CREATE INDEX IF NOT EXISTS idx_winner_id ON combats (winner_id);
CREATE INDEX IF NOT EXISTS idx_firstContender ON combats (first_contender_id);
CREATE INDEX IF NOT EXISTS idx_secondContender ON combats (second_contender_id);
CREATE INDEX IF NOT EXISTS idx_combat_creation_date ON combats (creation_date DESC);

-- Add table comments
COMMENT ON TABLE combats IS 'Table storing battle records between species';
COMMENT ON COLUMN combats.combat_id IS 'Unique identifier for the battle';
COMMENT ON COLUMN combats.first_contender_id IS 'ID of the first species in the battle';
COMMENT ON COLUMN combats.second_contender_id IS 'ID of the second species in the battle';
COMMENT ON COLUMN combats.winner_id IS 'ID of the winning species';
COMMENT ON COLUMN combats.creation_date IS 'Timestamp when the battle occurred';

-- ============================================================================
-- TABLE: RANKINGS (Rankings)
-- Description: Tracks victory count for each species
-- ============================================================================

CREATE TABLE IF NOT EXISTS rankings (
    ranking_id BIGSERIAL PRIMARY KEY,
    specie_id BIGINT NOT NULL UNIQUE,
    victorias BIGINT NOT NULL DEFAULT 0,

    -- Foreign Key Constraint
    CONSTRAINT fk_ranking_specie
        FOREIGN KEY (specie_id)
        REFERENCES especies(specie_id) ON DELETE CASCADE,

    -- Business Logic Constraint
    CONSTRAINT uk_ranking_specie UNIQUE (specie_id),
    CONSTRAINT check_victorias_non_negative CHECK (victorias >= 0)
);

-- Create indexes on rankings table
CREATE INDEX IF NOT EXISTS idx_victories ON rankings (victorias DESC);
CREATE INDEX IF NOT EXISTS idx_specie_id ON rankings (specie_id);
CREATE INDEX IF NOT EXISTS idx_ranking_specie ON rankings (specie_id);

-- Add table comments
COMMENT ON TABLE rankings IS 'Table storing victory rankings for each species';
COMMENT ON COLUMN rankings.ranking_id IS 'Unique identifier for the ranking record';
COMMENT ON COLUMN rankings.specie_id IS 'ID of the species (one ranking per species)';
COMMENT ON COLUMN rankings.victorias IS 'Number of battles won by this species';

-- ============================================================================
-- SEQUENCES (If you need explicit sequence management)
-- ============================================================================

CREATE SEQUENCE IF NOT EXISTS especies_specie_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS combats_combat_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS rankings_ranking_id_seq
    START WITH 1
    INCREMENT BY 1;

-- ============================================================================
-- VIEWS (Optional - For common queries)
-- ============================================================================

-- View: Species with their victory count
CREATE OR REPLACE VIEW vw_species_with_victories AS
SELECT
    e.specie_id,
    e.name,
    e.power,
    e.ability,
    COALESCE(r.victorias, 0) AS victories,
    e.creation_date
FROM especies e
LEFT JOIN rankings r ON e.specie_id = r.specie_id
ORDER BY COALESCE(r.victorias, 0) DESC, e.power DESC;

-- View: Battle history with species names
CREATE OR REPLACE VIEW vw_battle_history AS
SELECT
    c.combat_id,
    c.creation_date,
    e1.name AS first_contender_name,
    e1.power AS first_contender_power,
    e2.name AS second_contender_name,
    e2.power AS second_contender_power,
    winner.name AS winner_name
FROM combats c
JOIN especies e1 ON c.first_contender_id = e1.specie_id
JOIN especies e2 ON c.second_contender_id = e2.specie_id
JOIN especies winner ON c.winner_id = winner.specie_id
ORDER BY c.creation_date DESC;

-- ============================================================================
-- STORED PROCEDURES (Optional - For common operations)
-- ============================================================================

-- Procedure: Record a new battle and update rankings
CREATE OR REPLACE FUNCTION sp_record_battle(
    p_first_contender_id BIGINT,
    p_second_contender_id BIGINT,
    p_winner_id BIGINT,
    OUT p_combat_id BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Insert the battle record
    INSERT INTO combats (first_contender_id, second_contender_id, winner_id, creation_date)
    VALUES (p_first_contender_id, p_second_contender_id, p_winner_id, CURRENT_TIMESTAMP)
    RETURNING combat_id INTO p_combat_id;

    -- Update the winner's ranking
    UPDATE rankings
    SET victorias = victorias + 1
    WHERE specie_id = p_winner_id;

    -- If ranking doesn't exist, create it
    IF NOT FOUND THEN
        INSERT INTO rankings (specie_id, victorias)
        VALUES (p_winner_id, 1)
        ON CONFLICT (specie_id) DO UPDATE
        SET victorias = rankings.victorias + 1;
    END IF;
END;
$$;

-- ============================================================================
-- END OF DDL
-- ============================================================================

