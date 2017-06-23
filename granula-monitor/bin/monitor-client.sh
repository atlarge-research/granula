#!/bin/sh


SERVICE_NAME="Granula monitor-client"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PATH_TO_JAR=$SCRIPT_DIR/../target/granula-monitor-1.0-SNAPSHOT.jar
MAIN_CLASS=science.atlarge.granula.monitor.executor.GranulaMonitorClient
PNAME=GranulaMonitorClient

cd $SCRIPT_DIR/../
java -cp $PATH_TO_JAR $MAIN_CLASS
