CREATE TABLE UserDetail (
  uid INTEGER NOT NULL PRIMARY KEY,
  education VARCHAR(512),
  places VARCHAR(512),
  relationship VARCHAR(512),
  phone CHAR(32),
  links VARCHAR(512),
  FOREIGN KEY (uid) REFERENCES Users(uid)
);
