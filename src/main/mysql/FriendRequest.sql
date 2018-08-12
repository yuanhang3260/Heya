CREATE TABLE FriendRequest (
  username_1 VARCHAR(32) NOT NULL,
  username_2 VARCHAR(32) NOT NULL,
  status VARCHAR(32) NOT NULL,

  PRIMARY KEY (username_1, username_2)
);
