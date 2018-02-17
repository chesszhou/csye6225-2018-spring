#!/bin/sh

STACK_NAME=$1
SUCCESS=""
WAIT=""

SUCCESS=$(aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-application.json) 
WAIT=$(aws cloudformation wait stack-create-complete --stack-name $STACK_NAME)
if [ ! -z "$SUCCESS" ] && [ -z "$WAIT" ]; then
  #statements
  echo "Success!"
 else
   echo "Failed!"
 fi
