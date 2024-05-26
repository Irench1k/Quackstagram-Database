DELETE FROM comments
WHERE EXISTS (
    SELECT 1
    FROM posts p
    WHERE comments.post_id = p.post_id
    AND comments.day < p.day
);