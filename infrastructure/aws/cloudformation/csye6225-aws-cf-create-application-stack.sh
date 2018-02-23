#!/bin/sh

STACK_NAME=$1
SUCCESS=""
WAIT=""
SUBNET_NAME_DB="subnet-group"
EC2_SUBNET_ID=$(aws ec2 describe-subnets --filters Name=tag:aws:cloudformation:logical-id,Values=SubnetForWebServers | jq -r '.Subnets[0].SubnetId')
EC2_SG_ID=$(aws ec2 describe-security-groups --filters Name=tag:aws:cloudformation:logical-id,Values=csye6225webapp | jq -r '.SecurityGroups[0].GroupId')



SUCCESS=$(aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-application.json --parameters ParameterKey=IdOfImage,ParameterValue=ami-66506c1c ParameterKey=TypeOfInstance,ParameterValue=t2.micro ParameterKey=NameOfBucket,ParameterValue=s3.csye6225-spring2018-guobei.me ParameterKey=ClassofDBInstance,ParameterValue=db.t2.medium ParameterKey=IdentiferofDBinstance,ParameterValue=csye6225-spring2018 ParameterKey=NameofDB,ParameterValue=csye6225 ParameterKey=usernameOfMaster,ParameterValue=csye6225master  ParameterKey=passwordOfMaster,ParameterValue=csye6225password ParameterKey=TypeofEngine,ParameterValue=MySQL ParameterKey=versionOfEngine,ParameterValue=5.6.37 ParameterKey=SubnetGroupNameofDB,ParameterValue=$SUBNET_NAME_DB ParameterKey=IdofEC2Subnet,ParameterValue=$EC2_SUBNET_ID ParameterKey=IdofEC2SecurityGroups,ParameterValue=$EC2_SG_ID ParameterKey=TypeofVolume,ParameterValue=gp2 ParameterKey=SizeofVolume,ParameterValue=16)

WAIT=$(aws cloudformation wait stack-create-complete --stack-name $STACK_NAME)
if [ ! -z "$SUCCESS" ] && [ -z "$WAIT" ]; then
  #statements
  echo "Success!"
 else
   echo "Failed!"
 fi
