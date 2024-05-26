use quackstagram;

CREATE VIEW post_engagement_analytics_5_24 AS
SELECT 
    p.post_id,
    p.username AS post_owner,
    RANK() OVER (ORDER BY COUNT(DISTINCT l.liker_user) + COUNT(DISTINCT c.comment_id) DESC) AS absolute_popularity_rank,
    RANK() OVER (ORDER BY COUNT(DISTINCT l.liker_user) / NULLIF(COUNT(DISTINCT f.follower_user), 0) DESC) AS relative_engagement_rank,
    COUNT(DISTINCT l.liker_user) / NULLIF(COUNT(DISTINCT f.follower_user), 0) AS followers_to_likes_ratio,
    COUNT(DISTINCT l.liker_user) AS likes_received,
    COUNT(DISTINCT c.comment_id) AS comments_received,
    COUNT(DISTINCT f.follower_user) AS followers
FROM 
    posts p
LEFT JOIN 
    follows f ON p.username = f.followed_user
LEFT JOIN 
    likes l ON p.post_id = l.post_id
LEFT JOIN 
    comments c ON p.post_id = c.post_id
GROUP BY 
    p.post_id, p.username
HAVING 
    COUNT(DISTINCT f.follower_user) >= 1;
