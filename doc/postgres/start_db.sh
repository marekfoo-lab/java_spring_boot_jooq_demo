podman stop simba-pg | podman rm simba-pg | podman volume rm simba_data |podman volume create simba_data \
podman run -d \
  --name simba-pg \
  -e POSTGRES_PASSWORD=SIMBA_USER \
  -e POSTGRES_USER=SIMBA_USER \
  -e POSTGRES_DB=SIMBA_DB \
  -p 5432:5432 \
  --mount type=volume,src=simba_data,target=/var/lib/postgresql/data \
  docker.io/library/postgres:17.5