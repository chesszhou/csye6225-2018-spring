<<<<<<< HEAD

#!/bin/sh

STACK_NAME=$1

SUCCESS=1

SUCCESS=$(aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-networking.json --parameters ParameterKey=StackName,ParameterValue=$STACK_NAME)
=======
#!/bin/sh

STACK_NAME=$1
SUCCESS=1

SUCCESS=$(aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-networking.json)
>>>>>>> 5687c821f56c3248dc145fa6dd9252cf1005409c

if [ "$SUCCESS" != 1 ]; then
  #statements
  echo "Success!"
else
<<<<<<< HEAD
  echo" Failed!"
=======
  echo "Failed!"
>>>>>>> 5687c821f56c3248dc145fa6dd9252cf1005409c
fi
