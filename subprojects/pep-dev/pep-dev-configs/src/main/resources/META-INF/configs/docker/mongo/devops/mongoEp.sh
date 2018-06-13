#!/bin/bash

mongod --noprealloc --smallfiles --replSet pep &
PID="$!"

echo "WAITING FOR INIT"
sleep 3

echo "--> rs init"
mongo /code/devops/current/init-rs.js
echo "--> rs init [DONE]"

sleep 3

echo "--> user init"
mongo /code/devops/current/init-user.js
echo "--> user init [DONE]"

kill $PID

echo "WAITING FOR KILL"
sleep 3

mongod --noprealloc --smallfiles --auth --replSet pep --bind_ip_all
