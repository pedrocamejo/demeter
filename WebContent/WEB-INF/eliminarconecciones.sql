SELECT pg_terminate_backend(procpid) from pg_stat_activity where datname ='db_demeter';
