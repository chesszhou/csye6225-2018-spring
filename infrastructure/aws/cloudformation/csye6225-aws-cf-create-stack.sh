#!/bin/sh

STACK_NAME=$1
SUCCESS=1

SUCCESS=$(aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-networking.json)

if [ "$SUCCESS" != 1 ]; then
  #statements
  echo "Success!"
else
  echo "Failed!"
fi
