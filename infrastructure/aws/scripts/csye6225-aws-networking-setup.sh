#!/bin/sh

STACK_NAME=$1

<<<<<<< HEAD
VPC_ID=1

GATEWAY_ID=1

ROUTE_TABLE_ID=1

VPC_ID=$(aws ec2 create-vpc --cidr-block 10.0.0.0/16 | jq -r '.Vpc.VpcId')

if [ "$VPC_ID" != 1 ]; then
=======
VPC_ID=""

GATEWAY_ID=""

ROUTE_TABLE_ID=""

VPC_ID=$(aws ec2 create-vpc --cidr-block 10.0.0.0/16 | jq -r '.Vpc.VpcId')

if [ ! -z "$VPC_ID" ]; then
>>>>>>> 1c86b117a9ccd63ba04824d6eb2344d8f06f5a1d
  #statements
  aws ec2 create-tags --resources $VPC_ID --tags Key=Name,Value=$STACK_NAME-csye6225-vpc
  GATEWAY_ID=$(aws ec2 create-internet-gateway | jq -r '.InternetGateway.InternetGatewayId')
fi

<<<<<<< HEAD
if [ "$GATEWAY_ID" != 1 ]; then
=======
if [ ! -z "$GATEWAY_ID" ]; then
>>>>>>> 1c86b117a9ccd63ba04824d6eb2344d8f06f5a1d
  #statements
  aws ec2 create-tags --resources $GATEWAY_ID --tags Key=Name,Value=$STACK_NAME-csye6225-InternetGateway
  aws ec2 attach-internet-gateway --internet-gateway-id $GATEWAY_ID --vpc-id $VPC_ID
  ROUTE_TABLE_ID=$(aws ec2 create-route-table --vpc-id $VPC_ID | jq -r '.RouteTable.RouteTableId')
fi

<<<<<<< HEAD
if [ "$ROUTE_TABLE_ID" != 1 ]; then
=======
if [ ! -z "$ROUTE_TABLE_ID" ]; then
>>>>>>> 1c86b117a9ccd63ba04824d6eb2344d8f06f5a1d
  #statements
  aws ec2 create-tags --resources $ROUTE_TABLE_ID --tags Key=Name,Value=$STACK_NAME-csye6225-public-route-table
  aws ec2 create-route --route-table-id $ROUTE_TABLE_ID --destination-cidr-block 0.0.0.0/0 --gateway-id $GATEWAY_ID
  echo "Sucess!"
fi
