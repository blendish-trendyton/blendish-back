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
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl --fail -s http://127.0.0.1:${IDLE_PORT}/profile || echo "")

  if [ -z "${RESPONSE}" ]; then
    echo "> Health Check 실패: 서버 응답 없음"
  else
    UP_COUNT=$(echo "${RESPONSE}" | grep 'real' | wc -l)

    if [[ "${UP_COUNT}" -ge 1 ]]; then
      echo "> Health Check 성공"

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
