provider "azurerm" {
  features {}

  subscription_id = var.subscription_id
  tenant_id       = var.tenant_id
}

# -----------------------------------------------------------------------------

resource "azurerm_postgresql_flexible_server" "prykhodko_db" {
  name                   = "${var.resource_name}-psql"
  resource_group_name    = var.resource_group
  location               = var.location
  version                = "16"
  administrator_login    = "fsaadmin"
  administrator_password = "P@ssword12345"
  sku_name               = "B_Standard_B1ms"
  storage_mb             = 32768
  backup_retention_days  = 7
  zone                   = 1 # TODO: Default hodnota je "2", nasledne nie je mozne zmenit.
  # Error: `zone` can only be changed when exchanged with the zone specified in 
  # `high_availability.0.standby_availability_zone`
  tags = {
    environment = var.environment
  }

  lifecycle {
    prevent_destroy = false
  }
}

resource "azurerm_postgresql_flexible_server_database" "keycloak" {
  name      = "keycloak"
  server_id = azurerm_postgresql_flexible_server.prykhodko_db.id
  collation = "en_US.utf8"
  charset   = "utf8"
}

resource "azurerm_postgresql_flexible_server_database" "app" {
  name      = "fsa-db"
  server_id = azurerm_postgresql_flexible_server.prykhodko_db.id
  collation = "sk_SK.utf8"
  charset   = "utf8"

  lifecycle {
    prevent_destroy = false
  }
}

# https://github.com/hashicorp/terraform-provider-azurerm/issues/14989
# Povolenie Azure Firewall - Allow public connection from Azure resources
# sa musi spravit manualne cez Azure Portal

resource "azurerm_postgresql_flexible_server_firewall_rule" "psql_fw_rules" {
  name             = "all"
  server_id        = azurerm_postgresql_flexible_server.prykhodko_db.id
  start_ip_address = "0.0.0.0"
  end_ip_address   = "255.255.255.255"
}