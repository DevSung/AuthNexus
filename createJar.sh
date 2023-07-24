#!/bin/bash

# 프로필 그룹 추출
PROFILE_GROUP=$1

# 프로필 그룹이 비어있는지 확인
if [ -z "$PROFILE_GROUP" ]; then
  echo "프로필 그룹을 지정해야 합니다."
  exit 1
elif [ "$PROFILE_GROUP" != "local" ] && [ "$PROFILE_GROUP" != "dev" ] && [ "$PROFILE_GROUP" != "prod" ]; then
  echo "존재하지 않는 프로필 그룹입니다."
  exit 1
fi

# root
ROOT_PATH=/Users/smart-tn-055/AuthNexus

SERVER_USER=sungsin
SERVER_PATH=20.249.85.149
SERVER_KEY=/Users/smart-tn-055/server_key.pem
PORT=8003

# 디렉토리 이동
cd $ROOT_PATH || {
  echo "디렉토리가 존재하지 않습니다."
  exit 1
}

# .jar 생성
./gradlew build || {
  echo "jar 파일 생성에 실패했습니다."
  exit 1
}

# .jar 파일 경로
JAR_PATH="$ROOT_PATH/build/libs/*.jar"

if [ "$PROFILE_GROUP" = "prod" ]; then
  scp -i $SERVER_KEY $JAR_PATH $SERVER_USER@$SERVER_PATH:/home/sungsin/java_source

  if [ $? -eq 0 ]; then
    echo "jar 파일을 개인 서버로 전송했습니다."
  else
    echo "jar 파일 전송에 실패했습니다."
    exit 1
  fi

# port 확인
PID=$(ssh -i $SERVER_KEY $SERVER_USER@$SERVER_PATH "lsof -t -i :$PORT")

if [ -n "$PID" ]; then
  echo "프로세스 $PID를 종료합니다."
  ssh -i $SERVER_KEY $SERVER_USER@$SERVER_PATH "kill $PID"
  sleep 3
fi

ssh -i $SERVER_KEY $SERVER_USER@$SERVER_PATH "java -jar /home/sungsin/java_source/*.jar --spring.profiles.active=$PROFILE_GROUP"
echo "$PROFILE_GROUP jar 파일이 실행됐습니다."

fi





