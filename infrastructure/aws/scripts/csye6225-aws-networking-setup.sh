#!/bin/bash

STACK_NAME=$1

VPC_ID=$(aws ec2 create-vpc --cidr-block 10.0.0.0/16 | jq -r '.Vpc.VpcId')
aws ec2 create-tags --resources $VPC_ID --tags Key=Name,Value=$STACK_NAME-csye6225-vpc
