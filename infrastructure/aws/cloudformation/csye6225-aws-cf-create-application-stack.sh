#!/bin/sh

STACK_NAME=$1
STACKID=""
STATUS=""
SUBNET_NAME_DB="subnet-group"
EC2_SG_ID=$(aws ec2 describe-security-groups --filters Name=tag:aws:cloudformation:logical-id,Values=csye6225webapp | jq -r '.SecurityGroups[0].GroupId')
RDS_SG_ID=$(aws ec2 describe-security-groups --filters Name=tag:aws:cloudformation:logical-id,Values=SecurityGroupForDBServers | jq -r '.SecurityGroups[0].GroupId')
SubnetOneId=$(aws ec2 describe-subnets --filters Name=tag:aws:cloudformation:logical-id,Values=SubnetForWebServers | jq -r '.Subnets[0].SubnetId')
SubnetTwoId=$(aws ec2 describe-subnets --filters Name=tag:aws:cloudformation:logical-id,Values=SubnetForAutoScaling | jq -r '.Subnets[0].SubnetId')
VpcId=$(aws ec2 describe-vpcs --filters Name=tag:aws:cloudformation:logical-id,Values=vpc | jq -r '.Vpcs[0].VpcId')
SSLCertificateArn=$(aws acm list-certificates --certificate-statuses ISSUED | jq -r '.CertificateSummaryList[0].CertificateArn')
LoadBalancerSecurityGroup=$(aws ec2 describe-security-groups --filters Name=tag:aws:cloudformation:logical-id,Values=SecurityGroupForLoadBalancer | jq -r '.SecurityGroups[0].GroupId')

STACKID=$(aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-application.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=IdOfImage,ParameterValue=ami-66506c1c ParameterKey=TypeOfInstance,ParameterValue=t2.micro ParameterKey=NameOfBucket,ParameterValue=s3.csye6225-spring2018-guobei.me ParameterKey=ClassofDBInstance,ParameterValue=db.t2.medium ParameterKey=IdentiferofDBinstance,ParameterValue=csye6225-spring2018 ParameterKey=NameofDB,ParameterValue=csye6225 ParameterKey=usernameOfMaster,ParameterValue=csye6225master ParameterKey=passwordOfMaster,ParameterValue=csye6225password ParameterKey=TypeofEngine,ParameterValue=MySQL ParameterKey=versionOfEngine,ParameterValue=5.6.37 ParameterKey=SubnetGroupNameofDB,ParameterValue=$SUBNET_NAME_DB ParameterKey=IdofEC2SecurityGroups,ParameterValue=$EC2_SG_ID ParameterKey=TypeofVolume,ParameterValue=gp2 ParameterKey=SizeofVolume,ParameterValue=16 ParameterKey=SubnetOneId,ParameterValue=$SubnetOneId ParameterKey=SubnetTwoId,ParameterValue=$SubnetTwoId ParameterKey=VpcId,ParameterValue=$VpcId ParameterKey=TagKey,ParameterValue=Name ParameterKey=TagValue,ParameterValue=csye6225-EC2Instance ParameterKey=KeyName,ParameterValue=csye6225-EC2Instance ParameterKey=IdofRDSSecurityGroups,ParameterValue=$RDS_SG_ID ParameterKey=SSLCertificateArn,ParameterValue=$SSLCertificateArn ParameterKey=LoadBalancerSecurityGroup,ParameterValue=$LoadBalancerSecurityGroup)
aws cloudformation wait stack-create-complete --stack-name $STACK_NAME
STATUS=$(aws cloudformation describe-stack-events --stack-name $STACK_NAME | jq -r '.StackEvents[0].ResourceStatus')

if [ ! -z "$STACKID" ] && [ "$STATUS" = "CREATE_COMPLETE" ]; then
  #statements
  echo "Success!"
 else
   echo "Failed!"
 fi
