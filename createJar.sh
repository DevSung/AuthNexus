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

ROOT_PATH=/home/sungsin/java_source/deploy/
PORT=8003
# /home/sungsin/java_source 이동
cd /home/sungsin/java_source

# port 확인
PID=$(lsof -t -i :8003)

if [ -n "$PID" ]; then
  echo "프로세스 $PID를 종료합니다."
  kill $PID
  sleep 3
fi

# JAR 파일 실행
nohup java -jar /deploy/*.jar --spring.profiles.active=prod &
echo "$PROFILE_GROUP jar 파일이 실행됐습니다."

PID=$(lsof -t -i :8003)
if [ -n "$PID" ]; then
  echo "jar 파일이 실행됐습니다."
else
  echo "배포 실패: JAR 파일 실행 중 오류가 발생했습니다."
  exit 1
fi
