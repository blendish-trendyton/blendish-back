#!/usr/bin/env bash

ABSPATH=$(readlink -f "$0")
ABSDIR=$(dirname "$ABSPATH")

# profile.sh가 존재하는지 확인 후 실행
if [ -f "${ABSDIR}/profile.sh" ]; then
  source "${ABSDIR}/profile.sh"
else
  echo "> 오류: profile.sh 파일을 찾을 수 없습니다! (${ABSDIR}/profile.sh)"
  exit 1
fi

REPOSITORY=/home/ec2-user/app

echo "> Build 파일을 복사합니다."

if ls "$REPOSITORY"/zip/*.jar 1> /dev/null 2>&1; then
  cp "$REPOSITORY"/zip/*.jar "$REPOSITORY/"
else
  echo "> 오류: $REPOSITORY/zip/ 에 jar 파일이 없습니다. 배포를 중단합니다."
  exit 1
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr "$REPOSITORY"/*.jar 2>/dev/null | tail -n 1)

if [ -z "$JAR_NAME" ]; then
  echo "> 오류: 배포할 JAR 파일을 찾을 수 없습니다."
  exit 1
fi

echo "> JAR NAME: $JAR_NAME"

echo "> $JAR_NAME 에 실행 권한을 부여합니다."
chmod +x "$JAR_NAME" || { echo "> 오류: 실행 권한을 부여할 수 없습니다."; exit 1; }

IDLE_PROFILE=$(find_idle_profile)

echo "> 새 애플리케이션을 $IDLE_PROFILE 로 실행합니다."

# 설정 파일의 위치를 지정하고 active profile을 통해 구동될 포트를 지정합니다.
nohup java -jar \
  -Dspring.config.location="$REPOSITORY/config/application.yml,$REPOSITORY/config/application-prod.yml,$REPOSITORY/config/application-$IDLE_PROFILE.yml" \
  -Dspring.profiles.active="$IDLE_PROFILE,prod" \
  "$JAR_NAME" >> "$REPOSITORY/nohup.out" 2>&1 &
