CREATE TABLE Users (
  uid INTEGER NOT NULL PRIMARY KEY,
  username CHAR(32) NOT NULL,
  email CHAR(64) NOT NULL,
  password CHAR(64) NOT NULL,
  name CHAR(32),
  birth DATE,
  sex ENUM("male", "female"),
  work CHAR(64),
  live CHAR(64)
);
