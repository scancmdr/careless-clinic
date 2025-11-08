#/bin/bash
docker run --rm \
  --name careless_db \
   -p 5432:5432 \
   -e POSTGRES_USER=careless \
   -e POSTGRES_PASSWORD=${DB_PASSWORD} \
   postgres:18
   #-v "$DATA_DIR/postgresql/data:/var/lib/postgresql/data" \
