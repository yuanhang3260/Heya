CREATE TABLE Friends (
  uid_1 VARCHAR(128) NOT NULL,
  username_1 VARCHAR(32) NOT NULL,
  uid_2 VARCHAR(128) NOT NULL,
  username_2 VARCHAR(32) NOT NULL,

  PRIMARY KEY (uid_1, uid_2),
  FOREIGN KEY (uid_1) REFERENCES User(uid),
  FOREIGN KEY (uid_2) REFERENCES User(uid)
);
