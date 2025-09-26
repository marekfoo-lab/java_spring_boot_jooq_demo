podman cp create_user_db.sql onup-pg:/tmp/create_user_db.sql
podman exec -i onup-pg psql -U SIMBA_USER -d SIMBA_USER -f /tmp/create_user_db.sql
