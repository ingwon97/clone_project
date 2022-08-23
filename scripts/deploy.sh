DEPLOY_LOG_PATH="/home/ubuntu/app/$PROJECT_NAME/deploy.log"
DEPLOY_ERR_LOG_PATH="/home/app/$PROJECT_NAME/deploy_err.log"
APPLICATION_LOG_PATH="/home/app/$PROJECT_NAME/application.log"


echo "===== 배포 시작 : $(date +%c) =====" >> $DEPLOY_LOG_PATH


echo "> 현재 동작중인 어플리케이션 pid 체크" >> $DEPLOY_LOG_PATH

CURRENT_PID=$(lsof -t -i:8080)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 동작중인 어플리케이션 존재 X" >> $DEPLOY_LOG_PATH
else

  echo "> 현재 동작중인 어플리케이션 존재 O" >> $DEPLOY_LOG_PATH
  echo "> 현재 동작중인 어플리케이션 강제 종료 진행" >> $DEPLOY_LOG_PATH
  echo "> kill -9 $CURRENT_PID" >> $DEPLOY_LOG_PATH
  kill $(lsof -t -i:8080)
fi


echo "> DEPLOY_JAR 배포" >> $DEPLOY_LOG_PATH
nohup java -jar /home/ubuntu/app/foocebak/build/libs/clone-project-0.0.1-SNAPSHOT.jar --spring.config.location=./application.properties --server.port=8080 >> $APPLICATION_LOG_PATH 2> $DEPLOY_ERR_LOG_PATH &

sleep 3

echo "> 배포 종료 : $(date +%c)" >> $DEPLOY_LOG_PATH