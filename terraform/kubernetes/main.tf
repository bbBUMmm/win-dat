provider "azurerm" {
  features {}

  subscription_id = var.subscription_id
  tenant_id       = var.tenant_id
}

# -----------------------------------------------------------------------------

resource "azurerm_kubernetes_cluster" "fsa_prykhodko" {
  name                = "${var.resource_name}-aks"
  location            = var.location
  resource_group_name = var.resource_group
  node_resource_group = "mc_fsa-aks"
  dns_prefix         = "${var.dns_prefix}-k8s"
  kubernetes_version = 1.31

  default_node_pool {
    name            = var.nodepool_name
    node_count      = var.node_count
    vm_size         = var.vm_size
    os_disk_size_gb = var.os_disk_size
  }

  identity {
    type = "SystemAssigned"
  }

  # ak chceme povolit RBAC
  # role_based_access_control {
  #   enabled = true
  # }

  tags = {
    environment = var.environment
  }
}

# resource "azurerm_role_assignment" "fsa_container_registry_assignment" {
#   principal_id                     = azurerm_kubernetes_cluster.fsa_aks.kubelet_identity.0.object_id
#   role_definition_name             = "AcrPull"
#   scope                            = data.azurerm_container_registry.fsa_container_registry.id
#   skip_service_principal_aad_check = true
#   depends_on                       = [azurerm_kubernetes_cluster.fsa_aks]
# }