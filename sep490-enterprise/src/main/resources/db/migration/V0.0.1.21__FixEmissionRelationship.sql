-- Fix typo
UPDATE emission_factor
SET unit_denominator = 'MILLION_CUBIC_METER'
WHERE unit_denominator = 'MILLION_CUBIC_METER THÔ ĐẦU VÀO';

-- Remove unnecessary relationship
ALTER TABLE emission_activity DROP COLUMN emission_source_id;