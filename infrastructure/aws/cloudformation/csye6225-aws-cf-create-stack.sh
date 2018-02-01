STACK_NAME=$1
aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-networking.json
