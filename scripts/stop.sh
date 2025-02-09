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

IDLE_PORT=$(find_idle_port)

# IDLE_PORT 검증
if [[ -z "${IDLE_PORT}" || ! "${IDLE_PORT}" =~ ^[0-9]+$ ]]; then
  echo "❌ 오류: IDLE_PORT 값이 유효하지 않습니다. (IDLE_PORT=${IDLE_PORT})"
  exit 1
fi

echo "> $IDLE_PORT 에서 구동 중인 애플리케이션 pid 확인"
IDLE_PID=$(lsof -ti tcp:"${IDLE_PORT}")

if [[ -z "${IDLE_PID}" ]]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> 종료 시도: kill -15 ${IDLE_PID}"
  kill -15 ${IDLE_PID}
  sleep 5

  # 프로세스가 아직 살아있으면 강제 종료
  if ps -p ${IDLE_PID} > /dev/null; then
    echo "⚠️ 프로세스가 종료되지 않아 강제 종료 실행"
    echo "> 강제 종료: kill -9 ${IDLE_PID}"
    kill -9 ${IDLE_PID}
    sleep 5
  fi

  # 종료 확인
  if ps -p ${IDLE_PID} > /dev/null; then
    echo "❌ 오류: 프로세스 종료 실패. 수동 확인 필요."
    exit 1
  fi

  echo "✅ 애플리케이션 정상 종료됨"
fi
