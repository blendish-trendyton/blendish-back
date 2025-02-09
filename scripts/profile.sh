#!/usr/bin/env bash

# 쉬고 있는 profile 찾기: real1이 사용 중이면 real2가 쉬고 있고 반대면 real1이 쉬고 있음
function find_idle_profile() {
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" https://junyeongan.store/profile || echo "500") # 요청 실패 시 500 반환

    if [ "${RESPONSE_CODE}" -ge 400 ]; then
      CURRENT_PROFILE="real2"
    else
      CURRENT_PROFILE=$(curl -s https://junyeongan.store/profile || echo "real1") # 요청 실패 시 기본값 설정
    fi

    if [ "${CURRENT_PROFILE}" == "real1" ]; then
      IDLE_PROFILE="real2"
    else
      IDLE_PROFILE="real1"
    fi

    echo "${IDLE_PROFILE}" # bash에서는 return 대신 echo를 사용하여 값을 반환함
}

# 쉬고 있는 profile의 port 찾기
function find_idle_port() {
  IDLE_PROFILE=$(find_idle_profile)

  if [ "${IDLE_PROFILE}" == "real1" ]; then
    echo "8081"
  else
    echo "8082"
  fi
}
