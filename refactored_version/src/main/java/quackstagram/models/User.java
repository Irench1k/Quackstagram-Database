package quackstagram.models;

import java.util.ArrayList;
import java.util.Arrays;

public class User extends AbstractModel<User> {
    private String username;
    private String password;
    private String bio;
    private ArrayList<String> followingUsers; // other users that this one follows
    private int followersCount;
    private int postsCount;

    public User(String username, String password, String bio, ArrayList<String> followingUsers,
                int followersCount, int postsCount) {
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.followingUsers = followingUsers;
        this.followersCount = followersCount;
        this.postsCount = postsCount;
    }

    public static User createInstance(String[] args) throws RuntimeException {
        if (args.length != 6) {
            System.out.println(String.join(", ", args));
            throw new RuntimeException("Couldn't parse users line, expected 6 arguments!");
        }
        String username = args[0];
        String password = args[1];
        String bio = args[2];
        ArrayList<String> followingUsers = new ArrayList<>(Arrays.asList(args[3].split(" ")));
        int followersCount = Integer.parseInt(args[4]);
        int postsCount = Integer.parseInt(args[5]);

        return new User(username, password, bio, followingUsers, followersCount, postsCount);
    }

    @Override
    public String[] serialize() {
        return new String[] {
            username,
            password,
            bio,
            String.join(" ", followingUsers),
            Integer.toString(followersCount),
            Integer.toString(postsCount)
        };
    }

    @Override
    public boolean isUpdatable() {
        return true;
    }

    @Override
    public boolean isIdEqualTo(User user) {
        return this.username.equals(user.getUsername());
    }

    // Getter methods for user details

    /**
     * Returns the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the bio of the user.
     *
     * @return the bio of the user
     */
    public String getBio() {
        return bio;
    }

    /**
     * Sets the bio of the user.
     *
     * @param bio the new bio of the user
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Returns the number of posts by the user.
     *
     * @return the post count of the user
     */
    public int getPostsCount() {
        return postsCount;
    }

    /**
     * Returns the number of followers of the user.
     *
     * @return the followers count of the user
     */
    public int getFollowersCount() {
        return followersCount;
    }

    /**
     * Returns the number of users this user is following.
     *
     * @return the following count of the user
     */
    public int getFollowingCount() {
        return this.followingUsers.size();
    }

    public boolean isPasswordEqual(String suppliedPassword) {
        return this.password.equals(suppliedPassword);
    }

    // Setter methods for followers and following counts

    /**
     * Sets the number of followers of the user.
     *
     * @param followersCount the new followers count
     */
    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    /**
     * Sets the post count for the user.
     *
     * @param postsCount the new post count
     */
    public void setPostCount(int postCount) {
        this.postsCount = postCount;
    }

    public boolean followsUser(User targetUser) {
        for (String followedUser : this.followingUsers) {
            if (followedUser.equals(targetUser.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public void addUserToFollow(User targetUser) {
        if (isIdEqualTo(targetUser)) {
            // can't follow ourselves
            return;
        }

        if (followsUser(targetUser)) {
            // Already following
            return;
        }

        followingUsers.add(targetUser.getUsername());
    }

    public String getProfileImagePath() {
        return "img/profile/" + this.username + ".png";
    }

    public ArrayList<String> getFollowingUsers() {
        return new ArrayList<String>(followingUsers);
    }
}