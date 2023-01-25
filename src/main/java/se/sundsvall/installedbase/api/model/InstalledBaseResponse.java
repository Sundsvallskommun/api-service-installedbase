package se.sundsvall.installedbase.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import java.util.List;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Installed base response model")
public class InstalledBaseResponse {

	@ArraySchema(schema = @Schema(implementation = InstalledBaseCustomer.class, accessMode = READ_ONLY))
	private List<InstalledBaseCustomer> installedBaseCustomers;

	public static InstalledBaseResponse create() {
		return new InstalledBaseResponse();
	}

	public List<InstalledBaseCustomer> getInstalledBaseCustomers() {
		return installedBaseCustomers;
	}

	public void setInstalledBaseCustomers(List<InstalledBaseCustomer> installedBaseCustomers) {
		this.installedBaseCustomers = installedBaseCustomers;
	}

	public InstalledBaseResponse withInstalledBaseCustomers(List<InstalledBaseCustomer> installedBaseCustomers) {
		this.installedBaseCustomers = installedBaseCustomers;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(installedBaseCustomers);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		InstalledBaseResponse other = (InstalledBaseResponse) obj;
		return Objects.equals(installedBaseCustomers, other.installedBaseCustomers);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InstalledBaseResponse [installedBaseCustomers=").append(installedBaseCustomers).append("]");
		return builder.toString();
	}
}
