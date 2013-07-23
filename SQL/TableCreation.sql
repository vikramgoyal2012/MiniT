CREATE TABLE users(
    email VARCHAR (200) Primary Key,
    name VARCHAR (50) NOT NULL,
    password VARCHAR (50) NOT NULL
);

CREATE INDEX email_index1 ON users(email);

CREATE TABLE posts(
	tweetid VARCHAR(33) Primary Key,	
	email VARCHAR(200) NOT NULL,
	content VARCHAR(141) NOT NULL,	
	tstamp VARCHAR(30) NOT NULL
);

CREATE INDEX email_index2 ON posts(email);
CREATE INDEX tweetid_index1 ON posts(content);

CREATE TABLE following(
	uemail VARCHAR(200) NOT NULL,
	femail VARCHAR(200) NOT NULL, 
	PRIMARY KEY(uemail,femail)
);

CREATE INDEX uemail_index2 ON following(uemail);
CREATE INDEX femail_index ON following(femail);

CREATE TABLE tweetsforuser(
	email VARCHAR(200) NOT NULL,
	tweetid VARCHAR(33) NOT NULL,
	tstamp VARCHAR(30) NOT NULL,
	PRIMARY KEY(email,tweetid)
);

CREATE INDEX email_index3 ON tweetsforuser(email);

CREATE TABLE tokens(
	token VARCHAR(200) NOT NULL,
	email VARCHAR(200) NOT NULL,
	PRIMARY KEY(token)
);

CREATE INDEX token_index on tokens(token);


			
