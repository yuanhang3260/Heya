CREATE TABLE User (
  uid VARCHAR(128) NOT NULL PRIMARY KEY,
  username VARCHAR(32) NOT NULL,
  email VARCHAR(64) NOT NULL,
  password VARCHAR(64) NOT NULL,

  name VARCHAR(64),
  birth DATE,
  sex VARCHAR(8),
  phone VARCHAR(32),
  links VARCHAR(512),
  relationship VARCHAR(512)
);

CREATE TABLE UserSchool (
  ueid VARCHAR(128) NOT NULL PRIMARY KEY,
  fuid VARCHAR(128),
  fsid VARCHAR(128),
  major VARCHAR(256),
  startYear int,
  endYear int,
  FOREIGN KEY (fuid) REFERENCES User(uid),
  FOREIGN KEY (fsid) REFERENCES School(sid)
);
