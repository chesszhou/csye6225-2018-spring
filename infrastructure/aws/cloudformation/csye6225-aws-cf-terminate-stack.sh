#!/bin/sh

STACK_NAME=$1

SUCCESS=1

<<<<<<< HEAD
SUCCESS=$(aws cloudformation delete-stack --stack-name $STACK_NAME)

if [ -z "$SUCCESS" ]; then
=======
STATUS=1

SUCCESS=$(aws cloudformation delete-stack --stack-name $STACK_NAME)

aws cloudformation wait stack-delete-complete --stack-name $STACK_NAME

STATUS=$(aws cloudformation describe-stacks --stack-name $STACK_NAME)

if [ -z "$STATUS" ]; then
>>>>>>> 1c86b117a9ccd63ba04824d6eb2344d8f06f5a1d
  #statements
  echo "Success"
else
  echo "Failed"
fi
