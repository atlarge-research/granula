JOB_ID=$1

task='{"type":"Monitor", "state":"LinkNode", "jobId":"'$JOB_ID'", "processId":"'$PROCESS_ID'"}'
echo $task >/dev/tcp/localhost/2656

