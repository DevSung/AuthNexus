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
ROOT_PATH=/home/sungsin/java_source

SERVER_USER=sungsin
SERVER_HOST=$SERVER_HOST
SERVER_KEY=$SERVER_KEY
PORT=8003

# port 확인
PID=$(ssh -i $SERVER_KEY $SERVER_USER@$SERVER_HOST "lsof -t -i :$PORT")

if [ -n "$PID" ]; then
  echo "프로세스 $PID를 종료합니다."
  ssh -i $SERVER_KEY $SERVER_USER@$SERVER_HOST "kill $PID"
  sleep 3
fi

if [ -z "$PID" ]; then
  # JAR 파일 실행
  ssh -i $SERVER_KEY $SERVER_USER@$SERVER_HOST "java -jar build/libs/*.jar --spring.profiles.active=prod"
  echo "$PROFILE_GROUP jar 파일이 실행됐습니다."
fi
