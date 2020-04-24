#! /bin/sh

docker run --name sistramproxy --hostname=sistramproxy --network arcanjo \
    -e FEDERATION_NAME=ArcanjoFederation \
    -e FEDERATE_NAME=sistramproxy \
	-v /etc/localtime:/etc/localtime:ro \
	-p 36003:8080 \
	-d projetoarcanjo/sistramproxy:1.0	



