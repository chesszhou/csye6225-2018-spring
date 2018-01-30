#!/bin/bash

STACK_NAME=$1

VPC_ID=$(aws ec2 create-vpc --cidr-block 10.0.0.0/16 | jq -r '.Vpc.VpcId')
aws ec2 create-tags --resources $VPC_ID --tags Key=Name,Value=$STACK_NAME

GATEWAY_ID=$(aws ec2 create-internet-gateway | jq -r '.InternetGateway.InternetGatewayId')

aws ec2 attach-internet-gateway --internet-gateway-id $GATEWAY_ID --vpc-id $VPC_ID
