# CERT-MANAGER

- https://github.com/cert-manager/cert-manager

## Instalacia

```
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.17.1/cert-manager.crds.yaml

helm repo add jetstack https://charts.jetstack.io
helm repo update

helm upgrade --install cert-manager --namespace cert-manager --version v1.17.1 jetstack/cert-manager --version 1.17.1

kubectl apply -f letsencrypt-cluster-issuer.yaml

# Pokial chceme vymazat helm-release
helm delete cert-manager -n cert-manager

kubectl delete -f https://github.com/cert-manager/cert-manager/releases/download/v1.17.1/cert-manager.crds.yaml
```
