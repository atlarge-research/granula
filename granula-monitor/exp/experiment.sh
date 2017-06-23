JOB_ID=job20160439
PROCESS_ID=1
OUT_PATH=/home/wlngai/Large/Granula/monitor/out/

./start-monitor-process.sh $PROCESS_ID;
./link-process.sh $PROCESS_ID $JOB_ID; 
./link-node.sh $JOB_ID;

sleep 5
./modify-interval.sh $PROCESS_ID 100
sleep 5
./modify-interval.sh $PROCESS_ID 1000
sleep 5

./stop-monitor-process.sh $PROCESS_ID;
./collect-data.sh $JOB_ID $OUT_PATH

