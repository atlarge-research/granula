PROCESS_ID=$1
INTERVAL=$2

task='{"type":"Monitor", "state":"ModifyInterval", "processId":"'$PROCESS_ID'", "interval":"'$INTERVAL'"}'
echo $task >/dev/tcp/localhost/2656

