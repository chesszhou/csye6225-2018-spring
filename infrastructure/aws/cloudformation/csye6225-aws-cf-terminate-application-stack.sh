#!/bin/sh

STACK_NAME=$1
STATUS=1

STATUS=$(aws cloudformation describe-stacks --stack-name $STACK_NAME)

if [ -z "$STATUS" ]; then
  echo "The stack you entered does not exist!"
else
  # aws ec2 modify-instance-attribute --instance-id $EC2INSTANCEID --no-disable-api-termination
  aws s3 rm s3://s3.csye6225-spring2018-zhouyiy.me --recursive
  aws cloudformation delete-stack --stack-name $STACK_NAME
  aws cloudformation wait stack-delete-complete --stack-name $STACK_NAME
  STATUS=$(aws cloudformation describe-stacks --stack-name $STACK_NAME)
  if [ -z "$STATUS" ]; then
     echo "Stack deletion succeeded!"
   else
     echo "Stack deletion failed!"
  fi
  # echo "$STATUS"
  # EC2INSTANCEID=$(aws ec2 describe-instances --filters Name=tag:Name,Values=$STACK_NAME-csye6225-EC2Instance | jq -r '.Reservations[].Instances[].InstanceId')

  # if [ -z "$EC2INSTANCEID" ]; then
  #   echo "Stack deletion succeeded!"
  # else
  #   echo "Stack deletion failed!"
  # fi
fi
