# GITLAB-RUNNER

## Install

```bash
helm repo add gitlab https://charts.gitlab.io
helm repo update

helm upgrade --install gitlab-runner -n infra -f values.yaml gitlab/gitlab-runner --version 0.66.0
```