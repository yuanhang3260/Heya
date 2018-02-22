CREATE TABLE UserDetail (
  uid INTEGER NOT NULL PRIMARY KEY,
  name CHAR(64),
  birth DATE,
  sex ENUM("male", "female"),
  education VARCHAR(4096),
  work VARCHAR(4096),
  places VARCHAR(4096),
  phone CHAR(32),
  links VARCHAR(512),
  relationship VARCHAR(512),
  FOREIGN KEY (uid) REFERENCES Users(uid)
);
