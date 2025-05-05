# Gitlab custom helm-chart

- Helm chart deployne instanciu Gitlab - https://docs.gitlab.com/ee/install/docker.html

## Templates

- [pvc.yaml](./templates/pvc.yaml) - vytvori PersistentVolumeClaim, ktore Gitlab pouziva ako ulozisko pre data.
- [deployment.yaml](./templates/deployment.yaml) - vytvori instanciu Gitlab-u.
- [service.yaml](./templates/service.yaml) - service pre publikovanie Gitlab-u.

## Values

