package quackstagram.utilities.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

import quackstagram.utilities.BaseRepository;

public class ProfitMaximisation extends BaseRepository {

    static String choices = """
                                1. List all users who have more than X followers where X can be any integer value.
                                2. Show the total number of posts made by each user.
                                3. Find all comments made on a particular user's post.
                                4. Display the top X most liked posts.
                                5. Count the number of posts each user has liked.
                                6. List all users who haven't made a post yet.
                                7. List users who follow each other.
                                8. Show the user with the highest number of posts.
                                9. List the top X users with the most followers.
                                10. Find posts that have been liked by all users.
                                11. Display the most active user (based on posts, comments, and likes).
                                12. Find the average number of likes per post for each user.
                                13. Show posts that have more comments than likes.
                                14. List the users who have liked every post of a specific user.
                                15. Display the most popular post of each user (based on likes).
                                16. Find the user(s) with the highest ratio of followers to following.
                                17. Show the month with the highest number of posts made.
                                18. Identify users who have not interacted with a specific user's posts.
                                19. Display the user with the greatest increase in followers in the last X days.
                                20. Find users who are followed by more than X% of the platform users.
                                
                                -----------------
                                |Type -1 to quit.|
                                -----------------
                            """;

    String query1 = """
                    SELECT followed_user AS username
                    FROM Follows
                    GROUP BY followed_user
                    HAVING COUNT(follower_user) > ?;""";
    String query2 = """
                    SELECT u.username,
                    COUNT(p.username) AS total_posts
                    FROM Users u LEFT JOIN Posts p ON u.username = p.username
                    GROUP BY u.username;""";

    String query3 = """
                    SELECT comment_id, comment 
                    FROM Comments
                    WHERE post_id = ?;""";
    String query4 = """
                    SELECT p.post_id, COUNT(l.liker_user) AS like_count
                    FROM Posts p
                    JOIN Likes l ON p.post_id = l.post_id
                    GROUP BY p.post_id
                    ORDER BY like_count DESC
                    LIMIT ?;""";
    String query5 = """
                    SELECT u.username,
                    COUNT(l.liker_user) AS has_liked
                    FROM Users u LEFT JOIN Likes l ON u.username = l.liker_user
                    GROUP BY u.username;""";
    ;
    String query6 = """
                    SELECT username
                    FROM Users
                    WHERE username NOT IN (
                    SELECT username
                    FROM Posts
                    );""";
    String query7 = """                    
                    SELECT a.follower_user AS user_1, b.follower_user AS user_2
                    FROM Follows a JOIN Follows b ON a.follower_user = b.followed_user AND b.follower_user = a.followed_user
                    WHERE a.follower_user < b.follower_user;""";
    String query8 = """
                    SELECT username
                    FROM posts
                    GROUP BY username
                    ORDER BY COUNT(*) DESC
                    LIMIT 1;""";
    String query9 = """
                    SELECT followed_user AS username, COUNT(follower_user) AS followers
                    FROM Follows
                    GROUP BY followed_user
                    ORDER BY followers DESC
                    LIMIT ?;""";
    String query10 = """
                    SELECT post_id
                    FROM Likes
                    GROUP BY post_id
                    HAVING COUNT(DISTINCT liker_user) = (SELECT COUNT(*) FROM Users);""";
    String query11 = """
                    SELECT username
                    FROM (
                        SELECT u.username,
                            (COUNT(DISTINCT p.post_id) +
                            COUNT(DISTINCT c.comment_id) +
                            COUNT(DISTINCT l.post_id)) AS activity_score
                        FROM Users u
                        LEFT JOIN Posts p ON u.username = p.username
                        LEFT JOIN Comments c ON u.username = c.comm_user
                        LEFT JOIN Likes l ON u.username = l.liker_user
                        GROUP BY u.username
                    ) AS user_activity
                    ORDER BY activity_score DESC
                    LIMIT 1;""";
    String query12 = """
                    SELECT post_likes.username, AVG(post_likes.like_count) AS average_likes_per_post
                    FROM (
                        SELECT p.username, p.post_id, COUNT(l.post_id) AS like_count
                        FROM Posts p
                        LEFT JOIN Likes l ON p.post_id = l.post_id
                        GROUP BY p.username, p.post_id
                    ) AS post_likes
                    GROUP BY post_likes.username;""";
    String query13 = """
                    SELECT p.post_id, p.username AS posted_by
                    FROM posts p
                    LEFT JOIN comments c ON p.post_id = c.post_id
                    LEFT JOIN likes l ON p.post_id = l.post_id
                    GROUP BY p.post_id
                    HAVING COUNT(DISTINCT c.comment_id) > COUNT(DISTINCT l.liker_user);""";
    String query14 = """
                    SELECT l.liker_user AS user, p.username AS user_whose_posts_have_all_been_liked
                    FROM Likes l JOIN Posts p USING (post_id)
                    WHERE p.username = ?
                    GROUP BY l.liker_user
                    HAVING COUNT(l.post_id) = (
                    SELECT COUNT(*)
                    FROM Posts
                    WHERE username = ?
                    );""";
    String query15 = """
                    SELECT p.username, p.post_id
                    FROM Posts p
                    JOIN (
                        SELECT p.username, p.post_id, COUNT(l.liker_user) AS like_count
                        FROM Posts p
                        LEFT JOIN Likes l ON p.post_id = l.post_id
                        GROUP BY p.username, p.post_id
                    ) d ON p.post_id = d.post_id
                    JOIN (
                        SELECT username, MAX(like_count) AS max_likes
                        FROM (
                            SELECT p.username, p.post_id, COUNT(l.liker_user) AS like_count
                            FROM Posts p
                            LEFT JOIN Likes l ON p.post_id = l.post_id
                            GROUP BY p.username, p.post_id
                        ) like_counts
                        GROUP BY username
                    ) max_likes ON p.username = max_likes.username AND d.like_count = max_likes.max_likes;""";
    String query16 = """
                    SELECT username,
                    IF(following_count > 0, follower_count / following_count, 0) AS follower_to_following_ratio
                    FROM (
                    SELECT u.username,
                    (SELECT COUNT(*) FROM follows WHERE follower_user = u.username) AS following_count,
                    (SELECT COUNT(*) FROM follows WHERE followed_user = u.username) AS follower_count
                    FROM users u
                    ) user_counts
                    ORDER BY follower_to_following_ratio DESC
                    LIMIT ?;""";
    String query17 = """
                    SELECT DATE_FORMAT(day, "%M") AS month, COUNT(*) AS post_count
                    FROM Posts
                    GROUP BY month
                    ORDER BY post_count DESC
                    LIMIT 1;""";
    String query18 = """
                    SELECT username
                    FROM Users
                    WHERE username NOT IN (
                        SELECT DISTINCT liker_user
                        FROM Likes
                        WHERE post_id IN (SELECT post_id FROM Posts WHERE username = ?)
                    )
                    AND username NOT IN (
                        SELECT DISTINCT comm_user
                        FROM Comments
                        WHERE post_id IN (SELECT post_id FROM Posts WHERE username = ?)
                    );""";
    String query19 = """
                    SELECT followed_user,
                    COUNT(*) AS increase_in_followers
                    FROM follows
                    WHERE DATE(timestamp) >= CURDATE() - INTERVAL ? DAY
                    GROUP BY followed_user
                    ORDER BY increase_in_followers DESC
                    LIMIT 1;""";
    String query20 = """
                    SELECT followed_user
                    FROM follows
                    GROUP BY followed_user
                    HAVING COUNT(DISTINCT follower_user) > (
                    SELECT COUNT(DISTINCT username) * (? / 100)
                    FROM users
                    );""";

    public int getChoice() throws Exception {
        int choice;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("Select which query you want to execute, by entering the corresponding number.");
            System.out.println("Type 'all' to run all queries.");
            System.out.println();
            System.out.println("(Type 'ls' if you want to see the query list again): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice == -1 || choice >= 1 && choice <= 20) {
                    return choice;
                } else {
                    String input = scanner.nextLine().trim().toLowerCase();
                    switch (input) {
                        case "ls":
                            System.out.print(choices);
                            continue;
                        case "all":
                            gatherInputs(scanner);
                            return -1; // Exit after running all queries
                        default:
                            System.out.println("Invalid input. Enter 1-20 or -1 to exit.");
                    }
                }
            } else {
                String input = scanner.nextLine().trim().toLowerCase();
                switch (input) {
                    case "ls":
                        System.out.print(choices);
                        continue;
                    case "all":
                        gatherInputs(scanner);
                        return -1; // Exit after running all queries
                    default:
                        System.out.println("Invalid input. Enter 1-20 or -1 to exit.");
                }
            }
        }
    }

    private void gatherInputs(Scanner scanner) throws Exception {
        System.out.println("Would you like to run all queries with randomised X and username values, or pick one X and one username for all queries to use?");
        System.out.println("Type 0 for random, 1 for your choice, 2 to go back: ");
        int choice;
        int number;
        String username;
        while (true) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                if (choice == 0 || choice == 1) {
                    if (choice == 0) {
                        Random random = new Random();
                        number = random.nextInt(101);
                        username = getRandomUsername();

                        System.out.println("Your random X: " + number + ", your random user: " + username);
                        System.out.println("Reroll? (type y or n)");

                        String input = scanner.nextLine().trim().toLowerCase();
                        switch (input) {
                            case "y" -> {
                                continue;
                            }
                            case "n" ->
                                runAllQueries(username, number);
                            default ->
                                System.out.println("Invalid input.");
                        }
                    } else if (choice == 1) {
                        username = getUsername();
                        number = getNumber();
                        runAllQueries(username, number);
                        return;
                    }
                } else if (choice == 2) {
                    return;
                } else {
                    System.out.println("Invalid input. Enter 0, 1, or 2.");
                }
            } else {
                System.out.println("Invalid input. Enter 0, 1, or 2.");
                scanner.nextLine();
            }
        }
    }

    private void runAllQueries(String username, int number) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int queriesWithNoResults = 0;
        Scanner scanner = new Scanner(System.in);

        for (int i = 1; i < 21; i++) {
            try {
                connection = getConnection();
                resultSet = switch (i) {
                    case 1, 3, 4, 9, 16, 19, 20 ->
                        executeQuery(connection, statement, getQuery(i), number);
                    case 14, 18 ->
                        executeQuery(connection, statement, getQuery(i), username, username);
                    default ->
                        executeQuery(connection, statement, getQuery(i));
                };

                if (resultSet.next()) {
                    printResultSet(resultSet);
                } else {
                    System.out.println("No results found for query " + i);
                    queriesWithNoResults++;
                }
                if (i != 20) {
                    int n = 1 + i;
                    System.out.print("Next up is query " + (n) + ". ");
                }
                System.out.print("Press Enter to continue...");
                scanner.nextLine();
            } finally {
                closeResources(connection, statement, resultSet);
            }
        }
        System.out.println("Number of queries with no results: " + queriesWithNoResults);
        scanner.nextLine(); // Consume the last Enter key press
        scanner.close(); // Close the scanner
    }

    public String getRandomUsername() throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String username = null;

        try {
            connection = getConnection();
            String query = "SELECT username FROM users ORDER BY RAND() LIMIT 1;";
            resultSet = executeQuery(connection, statement, query);
            if (resultSet.next()) {
                username = resultSet.getString("username");
            }
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return username;
    }

    public String getQuery(int choice) {
        return switch (choice) {
            case 1 ->
                query1;
            case 2 ->
                query2;
            case 3 ->
                query3;
            case 4 ->
                query4;
            case 5 ->
                query5;
            case 6 ->
                query6;
            case 7 ->
                query7;
            case 8 ->
                query8;
            case 9 ->
                query9;
            case 10 ->
                query10;
            case 11 ->
                query11;
            case 12 ->
                query12;
            case 13 ->
                query13;
            case 14 ->
                query14;
            case 15 ->
                query15;
            case 16 ->
                query16;
            case 17 ->
                query17;
            case 18 ->
                query18;
            case 19 ->
                query19;
            case 20 ->
                query20;
            default ->
                null;
        };
    }

    private String getUsername() throws ClassNotFoundException, Exception {
        String username;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Enter username to use in query: ");
            if (sc.hasNext()) {
                username = sc.next();
                if (checkIfUserExists(username)) {
                    return username;
                } else {
                    System.out.println("Not an actual user. Enter a valid username: ");
                }
            } else {
                System.out.println("Invalid input. Enter a username: ");
                sc.next();
            }
            sc.close();
        }
    }

    private int getNumber() {
        int number;
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.print("Enter int for X: ");
            if (s.hasNextInt()) {
                number = s.nextInt();
                if (number >= 0) {
                    return number;
                } else {
                    System.out.println("Invalid input. Enter a positive integer: ");
                }
            } else {
                System.out.println("Invalid input. Enter a positive integer: ");
                s.next();
            }
            s.close();
        }
    }

    private boolean checkIfUserExists(String username) throws SQLException, ClassNotFoundException, Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            String query = "SELECT * FROM Users WHERE username = ?";
            resultSet = executeQuery(connection, statement, query, username);
            return resultSet.next();
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    private void execute(String query, int choice) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int[] moreInfo = {1, 3, 4, 9, 16, 18, 19, 20};
        int[] needsName = {14, 18};
        String username;
        int number;

        try {
            connection = getConnection();
            if (IntStream.of(moreInfo).anyMatch(x -> x == choice)) {
                if (IntStream.of(needsName).anyMatch(x -> x == choice)) {
                    username = getUsername();
                    resultSet = executeQuery(connection, statement, query, username, username);
                } else {
                    number = getNumber();
                    resultSet = executeQuery(connection, statement, query, number);
                }
                if (resultSet.next()) {
                    printResultSet(resultSet);
                } else {
                    throw new Exception("No results found.");
                }
            } else {
                resultSet = executeQuery(connection, statement, query);
                if (resultSet.next()) {
                    printResultSet(resultSet);
                } else {
                    throw new Exception("No results found.");
                }
            }

        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    public void printResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnCount = rsmd.getColumnCount();

        // Print column names
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(rsmd.getColumnName(i) + "\t");
        }
        System.out.println();

        // Print rows
        do {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultSet.getString(i) + "\t");
            }
            System.out.println();
        } while (resultSet.next());
    }

    public void start() throws Exception {
        int choice;
        while ((choice = getChoice()) != -1) {
            try {
                execute(getQuery(choice), choice);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Please give us a good grade <3");
    }

    public static void main(String[] args) {
        ProfitMaximisation profitMaximisation = new ProfitMaximisation();
        System.out.print(choices);
        try {
            profitMaximisation.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
