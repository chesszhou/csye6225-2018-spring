
#!/bin/sh

STACK_NAME=$1
SUCCESS=""

SUCCESS=$(aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-networking.json)

if [ ! -z "$SUCCESS" ]; then
  #statements
  echo "Success!"
else
  echo "Failed!"
fi
