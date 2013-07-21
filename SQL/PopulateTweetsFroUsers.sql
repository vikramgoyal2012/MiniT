COPY (SELECT f.uemail as user,p.tweetid as relevant_tweet, p.tstamp as tstamp
	FROM posts p INNER JOIN following f ON (p.email = f.femail)) 
	TO '/tmp/tweetsforuser.csv' WITH CSV;
