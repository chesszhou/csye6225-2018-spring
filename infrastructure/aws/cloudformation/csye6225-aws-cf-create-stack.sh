
#!/bin/sh

STACK_NAME=$1
<<<<<<< HEAD

SUCCESS=1

SUCCESS=$(aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-networking.json --parameters ParameterKey=StackName,ParameterValue=$STACK_NAME)

if [ $SUCCESS != 1 ]; then
  #statements
  echo "Success!"
else
  echo" Failed!"
=======
SUCCESS=""

SUCCESS=$(aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-networking.json)

if [ ! -z "$SUCCESS" ]; then
  #statements
  echo "Success!"
else
  echo "Failed!"
>>>>>>> 1c86b117a9ccd63ba04824d6eb2344d8f06f5a1d
fi
