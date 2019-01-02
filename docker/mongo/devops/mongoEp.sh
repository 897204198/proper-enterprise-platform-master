#!/bin/bash

# Using 'pep' as replica set name
mongod --replSet pep &
PID="$!"

echo "WAITING FOR INIT"
sleep 15

echo "--> rs init"
mongo /code/devops/current/init-rs.js
echo "--> rs init [DONE]"

sleep 10

echo "--> user init"
mongo /code/devops/current/init-user.js
echo "--> user init [DONE]"

kill $PID

echo "WAITING FOR KILL"
sleep 10

# Use auth
mongod --auth --replSet pep --bind_ip_all
# or not
#mongod --replSet pep --bind_ip_all
