# FSA Kubernetes

- [01-namespace.yaml](./01-namespace.yaml) - Vytvorenie `namespace (NS)` v k8s clustri.
- [02-secrets.yaml](./02-secrets.yaml) - Manifest na vytvorenie `citlivych udajov (secrets)` v k8s clustri. Tieto citlivé údaje sa ďalej používajú v aplikačných manifestoch.
- [Keycloak](./keycloak/) - Manifest na vytvorenie `Keycloak` inštancie spolu so `service`, cez ktorú bude Keycloak pristupný.
- [Application Frontend](./app-fe/) - Manifest na vytvorenie inštancie `aplikačného frontend-u` spolu so `service`, cez ktorú bude UI aplikácie dostupné (aj interne vrámci k8s clustra, aj z internetu).
- [Application Backend](./app-be/) - Manifest na vytvorenie inštancie `aplikačného backend-u` spolu so `service`, cez ktorú bude aplikácia dostupná (interne vramci clustra).

---

## Navod na attach AKS to ACR

- [Navod](https://learn.microsoft.com/en-us/azure/aks/cluster-container-registry-integration?tabs=azure-cli#attach-an-acr-to-an-existing-aks-cluster)

```
az aks update -n fsa2025-pieterr-aks -g fsa2025-pieterr --attach-acr fsa2025pieterrregistry
```

---

## Sifrovanie secrets pre K8s manifesty

- Existuju 2 sposoby ukladania secrets v K8s manifestoch:
  - Sifrovane - cez `data` objekt v manifeste - PREFEROVANY SPOSOB.
  - Nesifrovane - cez `stringData` objekt v manifeste.

Na vytvorenie sifrovanych secretov potrebujeme hesla v plaintexte zasifrovat cez `base64`.
```
# Spustenim tohto prikazu zasifrujete akykolvek `string`.

echo -n "<PASSWORD>" | base64
```
