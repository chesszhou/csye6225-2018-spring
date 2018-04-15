#!/bin/sh
#!/bin/sh

STACK_NAME=$1
STACKID=""
STATUS=""

STACKID=$(aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-ci-cd.json --parameters ParameterKey=userName,ParameterValue=travis ParameterKey=nameOfBucket,ParameterValue=code-deploy.csye6225-spring2018-guobei.me ParameterKey=nameOfCodeDeployApplication,ParameterValue=csye6225-spring-2018 ParameterKey=nameOfDeploymentGroup,ParameterValue=csye6225-deployment-group ParameterKey=TagKey,ParameterValue=Name ParameterKey=TagValue,ParameterValue=csye6225-EC2Instance ParameterKey=TargetGroupName,ParameterValue=MyTargetGroup --capabilities CAPABILITY_NAMED_IAM)
aws cloudformation wait stack-create-complete --stack-name $STACK_NAME
STATUS=$(aws cloudformation describe-stack-events --stack-name $STACK_NAME | jq -r '.StackEvents[0].ResourceStatus')

if [ ! -z "$STACKID" ] && [ "$STATUS" = "CREATE_COMPLETE" ]; then
  #statements
  echo "Success!"
 else
   echo "Failed!"
 fi
