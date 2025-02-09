#!/usr/bin/env bash

ABSPATH=$(readlink -f "$0")
ABSDIR=$(dirname "$ABSPATH")

# profile.sh 파일 확인 후 실행
if [ -f "$ABSDIR/profile.sh" ]; then
  source "$ABSDIR/profile.sh"
else
  echo "> 오류: profile.sh 파일을 찾을 수 없습니다! ($ABSDIR/profile.sh)"
  exit 1
fi

function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    # IDLE_PORT 값 검증
    if [[ -z "$IDLE_PORT" || ! "$IDLE_PORT" =~ ^[0-9]+$ ]]; then
      echo "> 오류: 유효하지 않은 IDLE_PORT 값입니다. (IDLE_PORT=$IDLE_PORT)"
      exit 1
    fi

    echo "> 전환할 port: $IDLE_PORT"

    # Nginx 프록시 설정 변경
    CONFIG_FILE="/etc/nginx/conf.d/service-url.inc"

    echo "> 엔진엑스 프록시 설정 변경: $CONFIG_FILE"
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee "$CONFIG_FILE"

    echo "> 저장된 설정 확인:"
    cat "$CONFIG_FILE"

    # Nginx 설정 테스트 후 reload
    echo "> Nginx 설정 테스트"
    if sudo nginx -t; then
      echo "> 엔진엑스 Reload"
      sudo systemctl reload nginx
    else
      echo "> 오류: Nginx 설정 테스트 실패. 프록시 전환 중단."
      exit 1
    fi
}