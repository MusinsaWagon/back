#!/bin/bash

cd /home/hong/app/pricewagon-blue-green

# 로그 파일 경로 설정
DEPLOY_LOG="/home/hong/app/nginx-deploy.log"
DOCKER_APP_NAME=nginx-proxy

# 배포 시작 시간 기록
echo "Nginx 배포 시작일자 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG

# Nginx 컨테이너가 이미 실행 중인지 확인
EXIST_NGINX=$(docker ps --filter "name=${DOCKER_APP_NAME}" --filter "status=running" -q)

if [ -z "$EXIST_NGINX" ]; then
  # Nginx가 실행 중이 아닌 경우, 새로 배포
  echo "Nginx 컨테이너가 존재하지 않으므로 새로 배포합니다." >> $DEPLOY_LOG
  docker-compose -p ${DOCKER_APP_NAME} -f docker-compose.yml up -d --build nginx

  # Nginx 컨테이너 실행 확인
  NGINX_STATUS=$(docker-compose -p ${DOCKER_APP_NAME} -f docker-compose.yml ps | grep nginx | grep Up)
  if [ -z "$NGINX_STATUS" ]; then
    echo "Nginx 배포 실패: $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
  else
    echo "Nginx 배포 성공: $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
  fi
else
  echo "Nginx 컨테이너가 이미 실행 중입니다." >> $DEPLOY_LOG
fi

# 배포 종료 시간 기록
echo "Nginx 배포 종료: $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $DEPLOY_LOG
cho "===================== 배포 완료 =====================" >>  $DEPLOY_LOG