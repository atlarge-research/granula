var job = {}

job.meta = (typeof jobMetadata != 'undefined') ? jobMetadata : null;
job.system = (typeof jobOperations != 'undefined') ? jobOperations : null;
job.env = (typeof jobMetrics != 'undefined') ? jobMetrics.map(function (rawdata) { return metricData(rawdata, 1); }) : null;