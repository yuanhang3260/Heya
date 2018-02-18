CREATE TABLE UserDetail (
  uid INTEGER NOT NULL PRIMARY KEY,
  name CHAR(32),
  birth DATE,
  sex ENUM("male", "female"),
  education VARCHAR(512),
  work CHAR(64),
  places VARCHAR(512),
  phone CHAR(32),
  links VARCHAR(512),
  relationship VARCHAR(512),
  FOREIGN KEY (uid) REFERENCES Users(uid)
);
