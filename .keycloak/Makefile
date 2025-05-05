
IMAGE := fsa-keycloak
TAG := 23.0

.PHONY:
build-local:
	docker build --pull --no-cache -t ${IMAGE}:${TAG} . --build-arg VERSION=${TAG}

build:
	docker build --platform linux/amd64 --pull --no-cache -t ${ACR}/${IMAGE}:${TAG} . --build-arg VERSION=${TAG}

push:
	docker login -u ${ACR_USER} -p ${ACR_PASSWORD} ${ACR} ; \
	docker push ${ACR}/${IMAGE}:${TAG}

all:
	make build push
