-- Users
INSERT INTO users (id, username, first_name, last_name, email, date_of_birth, country)
VALUES
    (1, 'user1', 'John', 'Doe', 'john@example.com', '1990-01-01', 'USA'),
    (2, 'user2', 'Jane', 'Smith', 'jane@example.com', '1992-05-15', 'UK'),
    (3, 'user3', 'Bob', 'Johnson', 'bob@example.com', '1988-11-30', 'Canada');

-- Tracks
INSERT INTO track (track_id, artists, album_name, track_name, popularity, duration_ms, explicit, 
                  danceability, energy, key, loudness, mode, speechiness, acousticness, 
                  instrumentalness, liveness, valence, tempo, time_signature, track_genre)
VALUES
    ('track1', 'Artist1', 'Album1', 'Summer Vibes', 80, 180000, false, 
     0.8, 0.7, 1, -5.0, 1, 0.1, 0.2, 0.0, 0.3, 0.8, 120.0, 4, 'Pop'),
    ('track2', 'Artist2', 'Album2', 'Winter Dreams', 75, 240000, false, 
     0.6, 0.8, 5, -6.0, 0, 0.05, 0.1, 0.1, 0.4, 0.6, 130.0, 4, 'Rock'),
    ('track3', 'Artist3', 'Album3', 'Autumn Leaves', 70, 300000, false, 
     0.4, 0.5, 3, -7.0, 1, 0.02, 0.8, 0.2, 0.2, 0.4, 110.0, 3, 'Jazz'),
    ('track4', 'Artist4', 'Album4', 'Spring Flowers', 65, 360000, false, 
     0.3, 0.4, 7, -8.0, 1, 0.01, 0.9, 0.3, 0.1, 0.3, 100.0, 4, 'Classical'),
    ('track5', 'Artist5', 'Album5', 'Midnight Stars', 85, 200000, false, 
     0.9, 0.9, 0, -4.0, 1, 0.1, 0.1, 0.0, 0.5, 0.9, 140.0, 4, 'Electronic');

-- Favorites with different timestamps for activity analysis
INSERT INTO favorite (user_id, track_id, rating, comment, is_public, created_at, last_played)
VALUES
-- User 1's favorites
(1, 'track1', 5, 'Great summer song!', true, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(1, 'track2', 4, 'Nice rock track', true, '2024-01-01 14:00:00', '2024-01-01 14:00:00'),
(1, 'track3', 3, 'Relaxing jazz', true, '2024-01-02 09:00:00', '2024-01-02 09:00:00'),

-- User 2's favorites
(2, 'track1', 5, 'Love this song!', true, '2024-01-01 11:00:00', '2024-01-01 11:00:00'),
(2, 'track4', 4, 'Beautiful classical piece', true, '2024-01-02 15:00:00', '2024-01-02 15:00:00'),
(2, 'track5', 5, 'Amazing electronic track', true, '2024-01-03 20:00:00', '2024-01-03 20:00:00'),

-- User 3's favorites
(3, 'track2', 4, 'Good rock song', true, '2024-01-01 16:00:00', '2024-01-01 16:00:00'),
(3, 'track3', 5, 'Perfect jazz', true, '2024-01-02 19:00:00', '2024-01-02 19:00:00'),
(3, 'track5', 3, 'Nice electronic', true, '2024-01-03 21:00:00', '2024-01-03 21:00:00'); 