COPY (SELECT f.userid as user,p.tweetid as relevant_tweet FROM posts p INNER JOIN following f ON (p.userid = f.following_userid)) TO '/tmp/tweetsforuser.csv' WITH CSV;
