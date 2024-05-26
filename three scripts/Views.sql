-- Quackstagram™ 2004-2024

-- 2024-05, The Netherlands

-- Irina Iarlykanova & Vjosa Shabanaj

-- Views providing insight into user behaviour, content 
-- popularity, and system analytics.

-- Quackstagram™ by Cheapo Software Solutions Incorporated
-- —hereafter CSS Inc.—reserves all rights. Unauthorized use 
-- or reproduction of this dataset without the express 
-- written permission of Cheapo Software Solutions Incorporated 
-- is strictly prohibited.

use quackstagram;

-- Data analytics on user behaviour on Quackstagram™.

-- Specifically, user interactivity,
-- as defined by posts liked, users followed, and comments left.

-- Snapshot comparing data one year ago to now
-- date: May 2024

-- Sorted per user.
-- Besides the obvious columns of total follows, likes,
-- and comments given, as well as the increase of these
-- counts as compared to the previous year, there are
-- two columns of 'interactivity rankings', as well
-- as one column showing the change in this ranking,
-- whether positive or negative.
-- A higher rank means the user engaging with the platform actively,
-- while a lower ranking points towards the user being more of a lurker.
-- Users... lurk moar.

CREATE VIEW user_interactivity_past_year AS
SELECT
    subquery.username,
    subquery.interactivity_ranking,
    subquery.interactivity_ranking_year_ago,
    -- Has to be cast to deal with negative change
    CAST(subquery.interactivity_ranking AS SIGNED) - CAST(subquery.interactivity_ranking_year_ago AS SIGNED) AS ranking_change,
    subquery.follows_given_total,
    subquery.follows_given_increase_year,
    subquery.comments_left_total,
    subquery.comments_left_increase_year,
    subquery.likes_given_total,
    subquery.likes_given_increase_year
FROM (
    SELECT
        u.username,
	-- Ranking is simply calculated by total likes + comments + follows given,
	-- meaning all three activities weigh equally for the interactivity rank.
        RANK() OVER (ORDER BY (COALESCE(l.total_likes_given, 0) + COALESCE(c.total_comments_left, 0) + COALESCE(f.total_follows_given, 0)) DESC) AS interactivity_ranking,
        RANK() OVER (ORDER BY (COALESCE(l.total_likes_given - l.likes_given_increase_year, 0) + COALESCE(c.total_comments_left - c.comments_left_increase_year, 0) + COALESCE(f.total_follows_given - f.follows_given_increase_year, 0)) DESC) AS interactivity_ranking_year_ago,
        COALESCE(f.total_follows_given, 0) AS follows_given_total,
        COALESCE(f.follows_given_increase_year, 0) AS follows_given_increase_year,
        COALESCE(c.total_comments_left, 0) AS comments_left_total,
        COALESCE(c.comments_left_increase_year, 0) AS comments_left_increase_year,
        COALESCE(l.total_likes_given, 0) AS likes_given_total,
        COALESCE(l.likes_given_increase_year, 0) AS likes_given_increase_year
    FROM
        users u
    LEFT JOIN (
        SELECT 
            liker_user,
            COUNT(*) AS total_likes_given,
            SUM(CASE WHEN timestamp >= CURDATE() - INTERVAL 1 YEAR THEN 1 ELSE 0 END) AS likes_given_increase_year
        FROM 
            likes
        GROUP BY 
            liker_user
    ) l ON u.username = l.liker_user
    LEFT JOIN (
        SELECT 
            comm_user,
            COUNT(*) AS total_comments_left,
            SUM(CASE WHEN day >= CURDATE() - INTERVAL 1 YEAR THEN 1 ELSE 0 END) AS comments_left_increase_year
        FROM 
            comments
        GROUP BY 
            comm_user
    ) c ON u.username = c.comm_user
    LEFT JOIN (
        SELECT 
            follower_user,
            COUNT(*) AS total_follows_given,
            SUM(CASE WHEN timestamp >= CURDATE() - INTERVAL 1 YEAR THEN 1 ELSE 0 END) AS follows_given_increase_year
        FROM 
            follows
        GROUP BY 
            follower_user
    ) f ON u.username = f.follower_user
    GROUP BY
        u.username,
        f.total_follows_given,
        f.follows_given_increase_year,
        c.total_comments_left,
        c.comments_left_increase_year,
        l.total_likes_given,
        l.likes_given_increase_year
    HAVING 
        COALESCE(l.likes_given_increase_year, 0) > 0 
        OR COALESCE(c.comments_left_increase_year, 0) > 0 
        OR COALESCE(f.follows_given_increase_year, 0) > 0
) AS subquery
ORDER BY interactivity_ranking ASC;


-- Data analytics on content popularity on Quackstagram™.

-- Specifically, engagement analytics,
-- as defined by likes, and likes received.

-- Sorted per post, filtering posts from users with no
-- followers, and presented with username for context.
-- The columns likes, followers, and comments columns are once
-- again fairly straightforward. The followers column is also 
-- included for context. Furthermore, and more interestingly,
-- is the column that shows the ratio of followers : likes,
-- and the columns for absolute popularity rank,
-- as well as relative engagement rank.
-- The relative engagement rank sorts by followers divided by
-- likes, to get an indication of realistic engagement.
-- The absolute popularity disregards the follower situation,
-- and is just concerned with total number of interactions on a post.

CREATE VIEW post_engagement_analytics AS
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
 

-- Data analytics on system analytics on Quackstagram™.

-- Specifically, an overview of the platforms most
-- succesful years, as defined by total and aggregated activity counts.

-- Sorted per year, filtered to show only best performing years.
-- Totals are simply totals per year. The averages are those totals
-- divided by the userbase, for an indication of what the average
-- user experienced on the platform.
-- Active user count is a count of users who are considered active
-- in that specific year; the criteria for this is to have 
-- either posted, commented, liked, or followed at least once
-- during the duration of the year.

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
	-- Casting done to prevent ugly years like 2,014
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
