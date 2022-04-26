#! /bin/sh

docker run --name sistramproxy --hostname=sistramproxy --network arcanjo \
    -e FEDERATION_NAME=ArcanjoFederation \
    -e FEDERATE_NAME=sistramproxy \
	-e USE_PROXY=true \
	-e PROXY_USER=nouser \
	-e PROXY_PASSWORD=nopass \
	-e PROXY_HOST=172.22.100.61 \
	-e PROXY_PORT=6160 \
	-e NON_PROXY_HOSTS="127.0.0.1" \	
	-v /etc/localtime:/etc/localtime:ro \
	-p 36003:8080 \
	-d projetoarcanjo/sistramproxy:1.0	



