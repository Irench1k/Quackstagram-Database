use quackstagram

DELETE FROM notifications
WHERE (post_id, sender_id) NOT IN (
    SELECT post_id, liker_user
    FROM likes
);