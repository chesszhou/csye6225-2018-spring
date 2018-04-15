
#!/bin/sh

STACK_NAME=$1
STACKID=""
STATUS=""

STACKID=$(aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-networking.json )
aws cloudformation wait stack-create-complete --stack-name $STACK_NAME
STATUS=$(aws cloudformation describe-stack-events --stack-name $STACK_NAME | jq -r '.StackEvents[0].ResourceStatus')

if [ ! -z "$STACKID" ] && [ "$STATUS" = "CREATE_COMPLETE" ]; then
  #statements
  echo "Success!"
else
  echo "Failed!"
fi
