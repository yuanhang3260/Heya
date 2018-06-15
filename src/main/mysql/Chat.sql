CREATE TABLE ChatDialog (
  dialog_id VARCHAR(32) NOT NULL PRIMARY KEY,
  username_1 VARCHAR(32) NOT NULL,
  username_2 VARCHAR(32) NOT NULL,
  user_1_read_time BIGINT NOT NULL DEFAULT 0,
  user_2_read_time BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE ChatMessage (
  f_dialog_id VARCHAR(32) NOT NULL,
  message_id VARCHAR(32) NOT NULL PRIMARY KEY,
  username VARCHAR(32) NOT NULL,
  content VARCHAR(2048) NOT NULL,
  create_time BIGINT NOT NULL DEFAULT 0,

  FOREIGN KEY (f_dialog_id) REFERENCES ChatDialog(dialog_id)
);
