#!/bin/sh

STACK_NAME=$1
<<<<<<< HEAD

=======
>>>>>>> 5687c821f56c3248dc145fa6dd9252cf1005409c
SUCCESS=1

SUCCESS=$(aws cloudformation delete-stack --stack-name $STACK_NAME)

if [ -z "$SUCCESS" ]; then
  #statements
  echo "Success"
else
  echo "Failed"
fi
