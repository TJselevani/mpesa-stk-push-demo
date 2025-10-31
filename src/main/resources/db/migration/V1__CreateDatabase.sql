-- This script creates the database if it doesn’t already exist
DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'mpesa_db') THEN
      PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE mpesa_db');
   END IF;
END
$$;
