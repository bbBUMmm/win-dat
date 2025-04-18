FROM quay.io/keycloak/keycloak:23.0
# Must add json files before import, volume is not mounted yet
ADD ./realms/realm-fsa.json /opt/keycloak/data/import/realm.json
# Import all files
RUN /opt/keycloak/bin/kc.sh import --dir=/opt/keycloak/data/import/ --override true; exit 0
# Start image

ENV KEYCLOAK_ADMIN admin
ENV KEYCLOAK_ADMIN_PASSWORD admin

EXPOSE 8080

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "-v", "start-dev", "--import-realm"]