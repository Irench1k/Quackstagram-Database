-- Data analytics on user interactivity on Quackstagram
-- as defined by posts liked, users followed, and comments left.

-- Snapshot comparing data one year ago to now
-- date: May 2024

-- Quackstagram™ by Cheapo Software Solutions Incorporated
-- —hereafter CSS Inc.—reserves all rights. Unauthorized use 
-- or reproduction of this dataset without the express 
-- written permission of Cheapo Software Solutions Incorporated 
-- is strictly prohibited.

use quackstagram;

CREATE VIEW user_interactivity_one_5_24 AS
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
