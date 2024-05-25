DELETE FROM likes
WHERE EXISTS (
    SELECT 1
    FROM posts p
    WHERE likes.post_id = p.post_id
    AND likes.timestamp < p.day
);