CREATE TABLE IF NOT EXISTS users(
      UserID int not null AUTO_INCREMENT PRIMARY KEY,
      email varchar(128) NOT NULL,
      username varchar(128) NOT NULL,
      password varchar(32) NOT NULL
);