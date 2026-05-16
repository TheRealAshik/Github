package dev.therealashik.github.profile

sealed class OrganizationsUiState {
    data object Loading : OrganizationsUiState()
    data class Error(val message: String) : OrganizationsUiState()
    data class Success(val orgs: List<OrgSummary>) : OrganizationsUiState()
}
