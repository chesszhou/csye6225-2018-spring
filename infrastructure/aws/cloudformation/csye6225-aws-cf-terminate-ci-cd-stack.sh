#!/bin/sh

STACK_NAME=$1
STATUS=1

STATUS=$(aws cloudformation describe-stacks --stack-name $STACK_NAME)

if [ -z "$STATUS" ]; then
  echo "The stack you entered does not exist!"
else
  aws s3 rm s3://code-deploy.csye6225-spring2018-zhouyiy.me --recursive
  aws cloudformation delete-stack --stack-name $STACK_NAME
  aws cloudformation wait stack-delete-complete --stack-name $STACK_NAME
  STATUS=$(aws cloudformation describe-stacks --stack-name $STACK_NAME)
  if [ -z "$STATUS" ]; then
     echo "Stack deletion succeeded!"
   else
     echo "Stack deletion failed!"
  fi
fi
