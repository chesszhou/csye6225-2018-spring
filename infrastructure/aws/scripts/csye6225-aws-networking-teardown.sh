#!/bin/bash

STACK_NAME=$1

VPC_ID=$(aws ec2 describe-vpcs --filters Name=tag:Name,Values=$STACK_NAME-csye6225-vpc | jq -r '.Vpcs[0].VpcId')

ROUTE_TABLE_ID=$(aws ec2 describe-route-tables --filters Name=tag:Name,Values=$STACK_NAME-csye6225-public-route-table | jq -r '.RouteTables[0].RouteTableId')

SECURITY_GROUP_ID=$(aws ec2 describe-security-groups --filters Name=vpc-id,Values=$VPC_ID | jq -r '.SecurityGroups[0].GroupId')

GATEWAY_ID=$(aws ec2 describe-internet-gateways --filters Name=tag:Name,Values=$STACK_NAME-csye6225-InternetGateway | jq -r '.InternetGateways[0].InternetGatewayId')

aws ec2 detach-internet-gateway --internet-gateway-id $GATEWAY_ID --vpc-id $VPC_ID

aws ec2 delete-route-table --route-table-id $ROUTE_TABLE_ID

aws ec2 delete-internet-gateway --internet-gateway-id $GATEWAY_ID

aws ec2 delete-vpc --vpc-id $VPC_ID
