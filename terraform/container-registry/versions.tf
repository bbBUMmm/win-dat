terraform {
    required_version = "= 1.8.5"

    required_providers {
        azurerm = {
        source = "hashicorp/azurerm"
        version = "4.26.0"
        }
    }

    backend "azurerm" {
    resource_group_name  = "prykhodko-fsatfstate"
    storage_account_name = "prykhodkofsatfstate"
    container_name       = "windat"
    key                  = "containerRegistry/tfstate/terraform.tfstate"
  }
}
