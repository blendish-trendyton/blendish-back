#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source $ABSDIR/profile.sh

function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    # IDLE_PORT 유효성 검사
    if [[ -z "$IDLE_PORT" || ! "$IDLE_PORT" =~ ^[0-9]+$ ]]; then
        echo "❌ 오류: 유효하지 않은 IDLE_PORT 값입니다. (IDLE_PORT=$IDLE_PORT)"
        exit 1
    fi

    echo "> 전환할 port: $IDLE_PORT"

    # Nginx 프록시 설정 변경
    CONFIG_FILE="/etc/nginx/conf.d/service-url.inc"

    echo "> 엔진엑스 프록시 설정 변경: $CONFIG_FILE"
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee "$CONFIG_FILE"

    echo "> 저장된 설정 확인:"
    cat "$CONFIG_FILE"

    # Nginx 설정 테스트
    echo "> Nginx 설정 테스트"
    if sudo nginx -t; then
        echo "> 엔진엑스 Reload"
        sudo systemctl reload nginx
        echo "✅ Nginx가 $IDLE_PORT로 전환되었습니다."
    else
        echo "❌ 오류: Nginx 설정 테스트 실패. 프록시 전환 중단."
        exit 1
    fi
}
