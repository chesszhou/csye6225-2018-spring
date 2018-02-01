#!/bin/sh

STACK_NAME=$1

SUCCESS=1

SUCCESS=$(aws cloudformation delete-stack --stack-name $STACK_NAME)

if [ -z "$SUCCESS" ]; then
  #statements
  echo "Success"
else
  echo "Failed"
fi
