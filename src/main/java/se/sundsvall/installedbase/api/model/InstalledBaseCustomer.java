package se.sundsvall.installedbase.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import java.util.List;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Installed base owner model")
public class InstalledBaseCustomer {

	@Schema(description = "Customer number", example = "10007", accessMode = READ_ONLY)
	private String customerNumber;

	@Schema(description = "Party-ID", example = "cf9892ad-69d5-420f-ae98-9631dd1664fe", accessMode = READ_ONLY)
	private String partyId;

	@ArraySchema(schema = @Schema(implementation = InstalledBaseItem.class, accessMode = READ_ONLY))
	private List<InstalledBaseItem> items;

	public static InstalledBaseCustomer create() {
		return new InstalledBaseCustomer();
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public InstalledBaseCustomer withCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
		return this;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public InstalledBaseCustomer withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public List<InstalledBaseItem> getItems() {
		return items;
	}

	public void setItems(List<InstalledBaseItem> items) {
		this.items = items;
	}

	public InstalledBaseCustomer withItems(List<InstalledBaseItem> items) {
		this.items = items;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerNumber, items, partyId);
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
		InstalledBaseCustomer other = (InstalledBaseCustomer) obj;
		return Objects.equals(customerNumber, other.customerNumber) && Objects.equals(items, other.items) && Objects.equals(partyId, other.partyId);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InstalledBaseCustomer [customerNumber=").append(customerNumber).append(", partyId=").append(partyId).append(", items=").append(items).append("]");
		return builder.toString();
	}
}
