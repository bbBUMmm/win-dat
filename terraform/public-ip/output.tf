output "public-ip-name" {
  value = azurerm_public_ip.fsa_public_ip.name
}

output "public-ip-address" {
  value = azurerm_public_ip.fsa_public_ip.ip_address
}