use quackstagram;

CREATE TABLE banned_words_for_moderation (
    word VARCHAR(255) PRIMARY KEY
);

INSERT INTO banned_words_for_moderation (word) VALUES ('meanie'), ('stupid'), ('i hate ducks'), ('irina isnt the coolest'), ('vjosa is dumb and annoying'), ('anime is fake'), ('chickens are better'), ('i get the feeling this platform doesnt allow us free speech'), ('asuka owns rei'), ('onions'), ('bastard'), ('noob'), ('rentfree'), ('ronaldo'), ('instagram'), ('ugly'), ('canard');


-- Function to check for banned words
CREATE FUNCTION is_niceys(caption VARCHAR(255))
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE banned_word_count INT;
    DECLARE is_nice INT;
    
    SELECT COUNT(*)
    INTO banned_word_count
    FROM banned_words_for_moderation
    WHERE INSTR(caption, word) > 0;
    
    -- Is nice if no mean words :)
   	-- not nice if mean words
    IF banned_word_count = 0 THEN
        SET is_nice = 1;
    ELSE
        SET is_nice = 0;
    END IF;
    
    RETURN is_nice;
END;


-- Simple procedure for grabbing all user posts
-- for when viewing one specific profile, for example.
CREATE PROCEDURE get_user_posts(IN p_username VARCHAR(12))
BEGIN
    SELECT post_id, username, caption, day
    FROM posts
    WHERE username = p_username
    ORDER BY day DESC;
end;


-- Procedure for potential future updates of the Quackstagram
-- platform; useful when a virality/recommended posts feature
-- gets implemented.
CREATE PROCEDURE calculate_virality_rank(
    IN username VARCHAR(12), 
    IN days_interval INT, 
    OUT virality_rank FLOAT
)
BEGIN
    DECLARE recent_interactions FLOAT;
    
    SELECT p.post_id, 
           (COUNT(DISTINCT l.liker_user) + 1.5 * COUNT(DISTINCT c.comm_user)) AS interaction_count
    INTO @post_id, @recent_interactions
    FROM posts p
    LEFT JOIN likes l ON p.post_id = l.post_id AND l.timestamp >= NOW() - INTERVAL days_interval DAY
    LEFT JOIN comments c ON p.post_id = c.post_id AND c.day >= NOW() - INTERVAL days_interval DAY
    WHERE p.username = username
    GROUP BY p.post_id
    ORDER BY interaction_count DESC
    LIMIT 1;
    
    SET virality_rank = @recent_interactions;
end;


-- Trigger to check for banned words in the caption
-- before making new post
CREATE TRIGGER check_for_banned_words
BEFORE INSERT ON posts
FOR EACH ROW
BEGIN
    IF NOT is_niceys(NEW.caption) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Caption contains banned words, and so post insertion has been aborted';
    END IF;
END;


-- Calls the procedure to calculate user rank 
-- every 10 posts uploaded to the platform.

-- A preview feature as of now, for potential future
-- use and refinement.
CREATE TRIGGER after_post_insert
AFTER INSERT ON posts
FOR EACH ROW
BEGIN
    DECLARE post_count INT;
    SET post_count = (SELECT COUNT(*) FROM posts WHERE username = NEW.username);

    IF post_count % 10 = 0 THEN
        CALL calculate_virality_rank(NEW.username, 30, @virality_rank);
    END IF;
END;