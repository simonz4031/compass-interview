# Requirement for Coding Interview Assignment

This assignment will be used as follows:

1. Does the code work and meet the requirements specified below?
2. It may be used to provide a conversation piece for another stage of interviews.

## Objectives

- Write a crawler that hits this endpoint and then proceeds to navigate to each link.
- The application should detect circular links to prevent an infinite loop.
- The application should use Java. Spring is acceptable too.
- There must be sufficient tests.

## Requirements

- Crawl all the links (below) and print out summary statistics at the end:
	- total number of http requests performed throughout the entire application
	- total number of successful requests
	- total number of failed requests (hint: think about what makes something successful or failed)
- You may use gradle, maven, docker, makefile, etc. --> use of gradle/maven wrappers is encouraged.

Starting Point: https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json

#Implement description
the Crawler will expose a get and post API 
get the http://{server}:{port}/  will fetch all the url define in application.properties file which is the above  github josn file
post the http://{server}:{port}/ via jsonurl=your_json_file_url can let service fetch the url you give

## return
service will return json result 
``` 
{
    "totalUrl": xxx,
    "successfulUrl": xxx,
    "failedUrl": xx
}
```
if error happend , it simple return with all field with 0

# How to Run
## Deployment

- Send us a link to your solution's git repo.
- Expect us to build, test, packaging, and run your code.


the git repo url is 
After clone the repo, simple build run
``` 
cd compass-interview
./build.sh 
```
it will automatic builder docker image for the service run at 8080 port
and build.sh will automatic run curl to test it
```
curl  -H "Accept:application/json" http://localhost:8080/
curl  -H "Accept:application/json" -X POST -d "jsonurl=https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json" http://localhost:8080/
```

## What we are looking for?

- We want to see what you would produce as a working product.
- We want to see what you value (e.g. clean code, 12-factor, dry, solid, verbose, terse, etc.).
- We want to see how you think about all phases of developing software: building, testing, running, deploying
- Any amount of building and packaging in order to run is fine; however, the overall goal is to run with **minimal user interaction**. (i.e. one command is usually nice, but not required)

# Follow up and extra
- use multithread and thread pool to enhance to performance 
    implement with concurrent version for 6x speed at 5/24 11am 
- global Error and Exception handle 
- etc
