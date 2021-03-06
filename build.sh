#!/usr/bin/env sh

mvn package
docker build -t compass-interview:latest .
echo "docker tag compass-interview:latest someone/compass-interview:latest"
echo "docker push someone/compass-interview"
docker container stop compass-interview
docker container rm compass-interview
docker run --name compass-interview -d -p 8080:8080 --restart always compass-interview
echo "wait 30s to let service start"
sleep 30s
curl  -H "Accept:application/json" http://localhost:8080/
curl  -H "Accept:application/json" -X POST -d "jsonurl=https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json" http://localhost:8080/
echo "demo error request here:"
curl  -H "Accept:application/json" -X POST -d "jsonurl=https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json2" http://localhost:8080/
echo "done!"