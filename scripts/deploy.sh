
# shellcheck disable=SC1128

PROJECT_NAME="github_action"
JAR_PATH="/home/ubuntu/app/foocebak/build/libs/*.jar"
DEPLOY_PATH=/home/app/foocebak/
DEPLOY_LOG_PATH="/home/ubuntu/app/foocebak/deploy.log"
DEPLOY_ERR_LOG_PATH="/home/app/foocebak/deploy_err.log"
APPLICATION_LOG_PATH="/home/app/foocebak/application.log"
BUILD_JAR=$(ls $JAR_PATH)
JAR_NAME=$(basename $BUILD_JAR)

echo "===== 배포 시작 : $(date +%c) =====" >> $DEPLOY_LOG_PATH

echo "> build 파일명: $JAR_NAME" >> $DEPLOY_LOG_PATH
echo "> build 파일 복사" >> $DEPLOY_LOG_PATH
cp $BUILD_JAR $DEPLOY_PATH

echo "> 현재 동작중인 어플리케이션 pid 체크" >> $DEPLOY_LOG_PATH
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 동작중인 어플리케이션 존재 X" >> $DEPLOY_LOG_PATH
else
  echo "> 현재 동작중인 어플리케이션 존재 O" >> $DEPLOY_LOG_PATH
  echo "> 현재 동작중인 어플리케이션 강제 종료 진행" >> $DEPLOY_LOG_PATH
  echo "> kill -9 $CURRENT_PID" >> $DEPLOY_LOG_PATH
  kill -9 $CURRENT_PID
fi

nohup java -jar \
        -Dspring.config.location=classpath:/application.yml \
        -Dspring.profiles.active=real \
        $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

sleep 3

echo "> 배포 종료 : $(date +%c)" >> $DEPLOY_LOG_PATH