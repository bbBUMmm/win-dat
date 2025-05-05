# FSA Keycloak

## Prerekvizity

[comment]: <# TODO Je toto este potrebne?>
- Pred spustenim instalacie KC cez helm, je potrebne aplikovat ConfigMap-u zo suboru `keycloak-java-config.yaml`.
  - Touto config mapou je vyrieseny problem s certifikatmi - Azure Flexible Database for PostgreSQL pouziva deprecated
    certifikat, ktory KC nepodporuje by default.

## Instalacia 

```
helm repo add codecentric https://codecentric.github.io/helm-charts
helm repo update

kubectl apply -f keycloak-java-config.yaml

helm upgrade --install keycloak -n app -f values.yaml codecentric/keycloakx --version 2.3.0

helm uninstall keycloak -n app
```
