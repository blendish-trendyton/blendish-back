#!/usr/bin/env bash

ABSPATH=$(readlink -f "$0")
ABSDIR=$(dirname "$ABSPATH")

# profile.sh 및 switch.sh 파일이 존재하는지 확인 후 실행
if [ -f "${ABSDIR}/profile.sh" ]; then
  source "${ABSDIR}/profile.sh"
else
  echo "> 오류: profile.sh 파일을 찾을 수 없습니다! (${ABSDIR}/profile.sh)"
  exit 1
fi

if [ -f "${ABSDIR}/switch.sh" ]; then
  source "${ABSDIR}/switch.sh"
else
  echo "> 오류: switch.sh 파일을 찾을 수 없습니다! (${ABSDIR}/switch.sh)"
  exit 1
fi

IDLE_PORT=$(find_idle_port)

echo "> Health Check Start!"
echo "> IDLE_PORT: $IDLE_PORT"
echo "> curl -s http://127.0.0.1:$IDLE_PORT/profile"

# 1️⃣ IDLE_PORT에서 애플리케이션이 실행되고 있는지 확인
if ! lsof -i :$IDLE_PORT > /dev/null; then
  echo "❌ 오류: $IDLE_PORT 포트에서 실행 중인 애플리케이션이 없습니다."
  exit 1
fi

# 2️⃣ 애플리케이션이 완전히 기동될 시간을 확보하기 위해 대기 시간 증가
sleep 20

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl --fail -s http://127.0.0.1:${IDLE_PORT}/profile || echo "")

  if [ -z "${RESPONSE}" ]; then
    echo "> Health Check 실패: 서버 응답 없음"
    echo "> 서버가 실행되지 않았거나, '/profile' 엔드포인트가 정상적으로 동작하지 않습니다."
  else
    echo "> Health Check 응답: ${RESPONSE}"

    UP_COUNT=$(echo "${RESPONSE}" | grep -c 'real')

    if [[ "${UP_COUNT}" -ge 1 ]]; then
      echo "> Health Check 성공"

      # 3️⃣ switch_proxy 실행 가능 여부 확인 후 실행
      if command -v switch_proxy >/dev/null 2>&1; then
        switch_proxy
      else
        echo "> 오류: switch_proxy 명령어를 찾을 수 없습니다."
        exit 1
      fi

      break
    else
      echo "> Health Check 응답이 예상과 다릅니다."
      echo "> Health Check: ${RESPONSE}"
    fi
  fi

  if [[ "${RETRY_COUNT}" -eq 10 ]]; then
    echo "> Health check 실패. "
    echo "> 엔진엑스에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo "> Health check 연결 실패. 재시도... (${RETRY_COUNT}/10)"
  sleep 10
done
