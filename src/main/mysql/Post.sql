CREATE TABLE Post (
  pid VARCHAR(128) NOT NULL PRIMARY KEY,
  fuid VARCHAR(128),
  CreateTime DATE NOT NULL,
  Content VARCHAR(1024),
  Pictures VARCHAR(1024),
  FOREIGN KEY (fuid) REFERENCES User(uid),
);
