use quackstagram;

CREATE TABLE Users (
   username VARCHAR (12) PRIMARY KEY,
   password VARCHAR (20) NOT NULL,
   bio VARCHAR (50)
);

CREATE TABLE Follows (
   follower_user VARCHAR(12),
   followed_user VARCHAR(12),
   timestamp DATETIME,
   PRIMARY KEY (follower_user, followed_user)
);


CREATE TABLE Posts (
   post_id INT AUTO_INCREMENT PRIMARY KEY,
   username VARCHAR(12) NOT NULL,
   caption VARCHAR(255),
   day DATE,
   FOREIGN KEY (username) REFERENCES Users(username)
);

CREATE TABLE Likes (
   notification_id INT AUTO_INCREMENT PRIMARY KEY,
   post_id INT NOT NULL,
   liker_user VARCHAR (12) NOT NULL,
   timestamp DATETIME,
   FOREIGN KEY (post_id) references Posts(post_id)
);


CREATE TABLE Comments (
   comment_id INT AUTO_INCREMENT PRIMARY KEY,
   post_id INT NOT NULL,
   comm_user VARCHAR (12) NOT NULL,
   comment VARCHAR (255),
   day DATE,
   FOREIGN KEY (post_id) REFERENCES Posts(post_id)
);
