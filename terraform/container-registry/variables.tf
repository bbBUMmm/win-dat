# -----------------------------------------------------------------------------
# Azure auth - service principal - autentifikacia do azure
# -----------------------------------------------------------------------------

variable "subscription_id" {
  description = "Subscription ID"
  type        = string
}

variable "tenant_id" {
  description = "Tenant ID"
  type        = string
}

# -----------------------------------------------------------------------------
# Globalne nastavenia projektu
# -----------------------------------------------------------------------------

variable "location" {
  description = "Pouzije sa na vytvorenie resource_group, ostatne resouces dedia tieto nastavenia."
  default     = "northeurope"
}

variable "resource_group" {
  description = "Nazov resource groupy"
  default     = "prykhodko-fsatfstate"
}

variable "environment" {
  description = "Identifikator prostredia."
  default     = "test"
}

variable "resource_name" {
  description = "Nazov resource"
  default     = "prykhodkofsatfstate"
}

variable "registry_name" {
  description = "Nazov registry"
  default     = "registry"
}
