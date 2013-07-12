CREATE TABLE users(
    userid serial PRIMARY KEY,
    name VARCHAR (150) NOT NULL,
    password VARCHAR (50) NOT NULL,
    email VARCHAR (200) UNIQUE NOT NULL
	
);


CREATE TABLE posts(
	tweetid bigserial Primary Key,	
	userid integer NOT NULL references users(userid),
	content VARCHAR(150) NOT NULL
	
);

CREATE INDEX userid_index1 ON posts(userid);

CREATE TABLE following(
	userid integer NOT NULL references users(userid),
	following_userid integer NOT NULL references users(userid),
	PRIMARY KEY(userid,following_userid)
);

CREATE INDEX userid_index2 ON Following(userid);
CREATE INDEX following_userid_index ON Following(following_userid);

CREATE TABLE tweetsforuser(
	userid integer NOT NULL references users(userid),
	tweetid bigint NOT NULL references posts(tweetid),
	PRIMARY KEY(userid,tweetid)
);

CREATE INDEX userid_index3 ON tweetsforuser(userid);



			
