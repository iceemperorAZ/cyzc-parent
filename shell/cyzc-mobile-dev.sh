#!/bin/sh
# 晶粮电商平台启动脚本
# 项目启动端口
APP_PORT=8800
# 项目启动环境
# shellcheck disable=SC2209
APP_ENV=dev
# jar包名称
API_NAME=cyzc-mobile-dev-1\.0\.0
# java虚拟机,参数,多个请用"\ "隔开
JVM_PARAM=-Xms1g\ -Xmx2g

JAR_NAME=$API_NAME\.jar
#PID  代表是PID文件
PID=$API_NAME\.pid

#使用说明，用来提示输入参数
usage() {
    echo " Please enter startup parameters [start|stop|restart|status]"
    exit 1
}
 
#检查程序是否在运行
is_exist(){
  pid=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}' `
  #如果不存在返回1，存在返回0     
  if [ -z "${pid}" ]; then
   return 1
  else
    return 0
  fi
}

#启动方法
start(){
  is_exist
  if [ $? -eq "0" ]; then 
    echo ">>> ${JAR_NAME} is already running ENV=${APP_ENV} PORT=${APP_PORT} PID=${pid} <<<"
  else
    nohup java $JVM_PARAM -jar -Dspring.profiles.active=$APP_ENV -Dserver.port=$APP_PORT  $JAR_NAME >cyzc-mobile.out 2>&1 &
    echo $! > $PID
    echo ">>> start $JAR_NAME successed ENV=${APP_ENV} PORT=${APP_PORT} PID=$! <<<"
   fi
  }

#停止方法
stop(){
  #is_exist
  pidf=$(cat $PID)
  #echo "$pidf"  
  echo ">>> api PID = $pidf begin kill $pidf <<<"
  kill -15 $pidf
  rm -rf $PID
  sleep 2
  is_exist
  if [ $? -eq "0" ]; then 
    echo ">>> api 2 PID = $pid begin kill -9 $pid  <<<"
    kill -9  $pid
    sleep 2
    echo ">>> $JAR_NAME process stopped <<<"  
  else
    echo ">>> ${JAR_NAME} is not running <<<"
  fi  
}
 
#输出运行状态
status(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> ${JAR_NAME} is running ENV=${APP_ENV} PORT=${APP_PORT} PID=${pid} <<<"
  else
    echo ">>> ${JAR_NAME} is not running <<<"
  fi
}
 
#重启
restart(){
  stop
  start
}
 
#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  *)
    usage
    ;;
esac
exit 0
