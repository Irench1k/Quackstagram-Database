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

-- Trigger to check for banned words before inserting a new comment
CREATE TRIGGER check_for_banned_words
BEFORE INSERT ON posts
FOR EACH ROW
BEGIN
    IF NOT is_niceys(NEW.caption) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Caption contains banned words, and so post insertion has been aborted';
    END IF;
END;


