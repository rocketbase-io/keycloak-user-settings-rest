#!/usr/bin/env bash

mkdir -p target/docker
cp src/test/docker/* target/docker/
cp target/profile.jar target/docker/

docker build --pull -t "rocketbaseio/keycloak-user-settings-rest" target/docker