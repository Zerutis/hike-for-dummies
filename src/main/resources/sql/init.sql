CREATE TABLE IF NOT EXISTS hike (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    distance NUMERIC(6,2),
    elevation INT,
    difficulty VARCHAR(255),
    description VARCHAR(2000)
);

INSERT INTO hike (name, distance, elevation, difficulty, description) VALUES
    ('Hike 1', 12.57, 150, 'Easy', 'Hike 1 description'),
    ('Hike 2', 14.94, 500, 'Medium', 'Hike 2 description'),
    ('Hike 3', 7.23, 905, 'Hard', 'Hike 3 description');
