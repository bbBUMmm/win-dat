output "registry_admin_url" {
  value = azurerm_container_registry.fsa_prykhodko.login_server
}

output "registry_admin_user" {
  value = azurerm_container_registry.fsa_prykhodko.admin_username
}

output "registry_admin_pass" {
  value     = azurerm_container_registry.fsa_prykhodko.admin_password
  sensitive = true
}

output "registry_token" {
  value     = azurerm_container_registry_token.fsa_pieterr_acr_token.id
  sensitive = true
}