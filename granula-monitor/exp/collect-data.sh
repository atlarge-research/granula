JOB_ID=$1
OUT_PATH=$2

echo $OUT_PATH

task='{"state":"Submit","sndWorkerSize":-1,"jobId":"'$JOB_ID'","isRegistered":false,"outPath":"'$OUT_PATH'","type":"Collect"}'
echo $task >/dev/tcp/localhost/2656
