# -----------------------------------------------------------------------------
# Secrets 
# -----------------------------------------------------------------------------

# Azure account subscription ID (Required)
subscription_id = "1fe7c931-ab56-4589-8641-3471fbfda6eb"
# subscription_id = "d3c69ca6-2cb1-46d7-ba08-092dbd46a3bc"
tenant_id = "3590242b-a92d-4bb9-9ff9-eb7a1183f511"

# -----------------------------------------------------------------------------
# Global
# -----------------------------------------------------------------------------

location = "northeurope"

resource_group = "prykhodko-fsaterraform"

# -----------------------------------------------------------------------------
# Resource Specific Variables
# -----------------------------------------------------------------------------

resource_name = "prykhodkofsatfstate"

registry_name = "registry"


# -----------------------------------------------------------------------------
# Resource Specific Variables
# -----------------------------------------------------------------------------

dns_prefix = "fsa-pieterr"

nodepool_name = "app"

node_count = "1"

vm_size = "Standard_B2s"

os_disk_size = "30"