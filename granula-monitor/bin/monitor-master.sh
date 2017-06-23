#!/bin/sh


SERVICE_NAME="Granula monitor-master"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PATH_TO_JAR=$SCRIPT_DIR/../target/granula-monitor-1.0-SNAPSHOT.jar
MAIN_CLASS=science.atlarge.granula.monitor.executor.GranulaMonitorMaster
PNAME=GranulaMonitorMaster
PID_PATH_NAME=$SCRIPT_DIR/../log/`hostname`-master.pid
LOG_FILE=$SCRIPT_DIR/../log/`hostname`-master.log

# build the service and create a log directory.
cd $SCRIPT_DIR/../
# mvn package -DskipTests -q
mkdir -p log

case $1 in
    start)
        echo "Starting $SERVICE_NAME ..."
        if [ ! -f $PID_PATH_NAME ]; then
            nohup java -cp $PATH_TO_JAR  $MAIN_CLASS 2>> $LOG_FILE >> $LOG_FILE &
                        echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started (pid = $!)..."
        else
	    PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME (pid = $PID) is already running ..."
        fi
    ;;
    stop)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME (pid = $PID) stoping ..."
            kill $PID;
            echo "$SERVICE_NAME stopped ..."
            rm $PID_PATH_NAME
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
    restart)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stopping ...";
            kill $PID;
            echo "$SERVICE_NAME stopped ...";
            rm $PID_PATH_NAME
            echo "$SERVICE_NAME starting ..."
            nohup java -jar $PATH_TO_JAR /tmp 2>> /dev/null >> /dev/null &
                        echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
    kill)
        kill -9 `ps aux | grep $PNAME | grep -v grep | awk '{print $2}'` 2>/dev/null
        rm -f $PID_PATH_NAME
    ;;
esac 
