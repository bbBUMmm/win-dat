# INGRESS-NGINX

- https://github.com/kubernetes/ingress-nginx/blob/main/README.md
- Co je `reverse proxy`? - https://www.cloudflare.com/en-gb/learning/cdn/glossary/reverse-proxy/

## Instalacia

- Pred inštaláciou je potrebné upraviť na riadkoch 451 a 452 názov resource `Public IP` v Azure a resource Group-u, v ktorej je resource `Public IP` vytvorená.

```
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update

helm upgrade --install ingress-nginx -n ingress-nginx -f values.yaml ingress-nginx/ingress-nginx --version 4.8.1


# Po nainstalovani ingress-nginx musime aplikovat ingress konfiguraciu
kubectl apply -f ingress.yaml

# Pokial chceme vymazat helm-release
helm delete ingress-nginx -n ingress-nginx
```

## Uprava ingress.yaml konfiguracie

- V testovacej sú aplikácie vypublikované v rámci clustra cez services na určených portoch. Pokiaľ plánujete používať iné porty, je potrebné upraviť v [ingress.yaml](./ingress.yaml).
