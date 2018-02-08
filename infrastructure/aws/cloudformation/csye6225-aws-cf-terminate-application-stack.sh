#!/bin/sh

STACK_NAME=$1
SUCCESS=1
EC2INSTANCEID=""
STATUS=1

EC2INSTANCEID=$(aws ec2 describe-instances --filters Name=tag:Name,Values=$STACK_NAME-csye6225-EC2Instance | jq -r '.Reservations[].Instances[].InstanceId')
if [ -z "$EC2INSTANCEID" ]; then
  echo "The stack you entered does not exist or there are no ec2 instance in entered stack!"
else
  aws ec2 modify-instance-attribute --instance-id $EC2INSTANCEID --no-disable-api-termination
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
