PROCESS_ID=$1

task='{"type":"Monitor", "state":"StartMonitorProcess", "processId":"'$PROCESS_ID'"}'
echo $task >/dev/tcp/localhost/2656

