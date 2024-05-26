CREATE VIEW success_years AS
WITH user_activities AS (
    SELECT username, YEAR(day) AS activity_year
    FROM posts
    UNION ALL
    SELECT liker_user AS username, YEAR(timestamp) AS activity_year
    FROM likes
    UNION ALL
    SELECT follower_user AS username, YEAR(timestamp) AS activity_year
    FROM follows
    UNION ALL
    SELECT comm_user AS username, YEAR(day) AS activity_year
    FROM comments
)
SELECT a.year,
       a.post_count,
       a.total_likes,
       a.total_comments,
       a.total_follows,
       ROUND(a.total_likes / u.total_users, 2) AS avg_likes,
       ROUND(a.total_comments / u.total_users, 2) AS avg_comments,
       ROUND(a.total_follows / u.total_users, 2) AS avg_follows,
       COALESCE(au.active_users_count, 0) AS active_users_count
FROM (
    SELECT year,
           COUNT(post_id) AS post_count,
           SUM(total_likes) AS total_likes,
           SUM(total_comments) AS total_comments,
           (SELECT COUNT(*)
            FROM follows f
            WHERE YEAR(f.timestamp) = sub.year) AS total_follows
    FROM (
        SELECT CAST(YEAR(p.day) AS CHAR) AS year,
               p.post_id,
               (SELECT COUNT(*)
                FROM likes l
                WHERE l.post_id = p.post_id) AS total_likes,
               (SELECT COUNT(*)
                FROM comments c
                WHERE c.post_id = p.post_id) AS total_comments
        FROM posts p
        WHERE YEAR(p.day) BETWEEN 2004 AND 2024
    ) sub
    GROUP BY year
    HAVING (total_likes + total_comments + post_count) > 1000
) a
CROSS JOIN (
    SELECT COUNT(DISTINCT username) AS total_users
    FROM users
) u
LEFT JOIN (
    SELECT activity_year AS year,
           COUNT(DISTINCT username) AS active_users_count
    FROM user_activities
    GROUP BY activity_year
) au ON a.year = au.year
ORDER BY a.year;