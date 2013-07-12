import string
import random
import time
from random import randint
import csv
import sys

def RandStringGenerator(size=6, chars=string.ascii_lowercase):
	return ''.join(random.choice(chars) for x in range(size))

def RandStringGeneratorWithSpace(size=6,chars=string.ascii_lowercase+ " "):
	return ''.join(random.choice(chars) for x in range(size))

def getEmail(i):
	return "user"+str(i)+'@'+RandStringGenerator(randint(4,6))+".com"

def getTweet():
	return RandStringGeneratorWithSpace(randint(4,140))

def getName(id):
	return "user"+str(id)

def getPassword():
	return "qwedsa"

def strTimeProp(start, end, format, prop):

    stime = time.mktime(time.strptime(start, format))
    etime = time.mktime(time.strptime(end, format))

    ptime = stime + prop * (etime - stime)

    return time.strftime(format, time.localtime(ptime))


def randomDate(start, end, prop):
    return strTimeProp(start, end, '%m/%d/%Y %I:%M:%S %p', prop)

def randomDate2(start, end, prop):
    return strTimeProp(start, end, '%m/%d/%Y', prop)

def randomTime(start,end,prop):
	return strTimeProp(start, end, '%I:%M:%S %p', prop)

def getTimeStamp():
	return randomDate("1/1/2009 1:30:00 PM", "1/1/2010 4:50:00 AM", random.random())

def getDate():
	return randomDate2("1/1/2009", "1/1/2010", random.random())

def getTime():
	return randomTime("12:00:00 AM","11:59:59 PM",random.random())

def makeSubscriptionsFile():
	out=csv.writer(open("Subscriptions.csv","w"),delimiter=',',quoting=csv.QUOTE_NONNUMERIC)
	csvreader=csv.reader(open("edges.csv","rb"),delimiter=',',quoting=csv.QUOTE_NONNUMERIC)
	i=1
	for row in csvreader:
		if(row[0]>1000000 or row[1]>1000000):
			'''print str(row[0])+" "+str(row[1])'''
			continue
		if(i>8000000):
			break
		print str(i)+"\n"
		out.writerow([int(row[0]),int(row[1])])
		i=i+1

def makeUsersFile():
	out=csv.writer(open("Users.csv","w"),delimiter=',',quoting=csv.QUOTE_NONNUMERIC)
	for i in range(1,1000000):
		out.writerow([i,getName(i),getPassword(),getEmail(i)])

def makeTweetsFile():
	out=csv.writer(open("Tweets.csv","w"),delimiter=',',quoting=csv.QUOTE_NONNUMERIC)
	for i in range(1,8000000):
		out.writerow([i,randint(1,1000000),getTweet()])
		print i

makeTweetsFile()

