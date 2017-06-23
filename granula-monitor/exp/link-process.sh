PROCESS_ID=$1
JOB_ID=$2

task='{"type":"Monitor", "state":"LinkProcess", "jobId":"'$JOB_ID'", "processId":"'$PROCESS_ID'"}'
echo $task >/dev/tcp/localhost/2656

