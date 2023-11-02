package se.sundsvall.installedbase.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Installed base item model")
public class InstalledBaseItem {

	@Schema(description = "Type", example = "Fjärrvärme", accessMode = READ_ONLY)
	private String type;

	@Schema(description = "Facility id", example = "735999109270751042", accessMode = READ_ONLY)
	private String facilityId;

	@Schema(description = "Placement id", example = "5263", accessMode = READ_ONLY)
	private int placementId;

	@Schema(description = "Facility commitment start date", example = "2020-04-01", accessMode = READ_ONLY)
	private LocalDate facilityCommitmentStartDate;

	@Schema(description = "Facility commitment end date", example = "2020-09-30", accessMode = READ_ONLY)
	private LocalDate facilityCommitmentEndDate;

	@Schema(description = "Last date for modification of item (or null if no modification has been done)", example = "2020-06-01", accessMode = READ_ONLY)
	private LocalDate lastModifiedDate;

	@Schema(implementation = InstalledBaseItemAddress.class, accessMode = READ_ONLY)
	private InstalledBaseItemAddress address;

	@ArraySchema(schema = @Schema(implementation = InstalledBaseItemMetaData.class, accessMode = READ_ONLY), maxItems = 1000)
	private List<InstalledBaseItemMetaData> metaData;

	public static InstalledBaseItem create() {
		return new InstalledBaseItem();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public InstalledBaseItem withType(String type) {
		this.type = type;
		return this;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public InstalledBaseItem withFacilityId(String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	public int getPlacementId() {
		return placementId;
	}

	public void setPlacementId(int placementId) {
		this.placementId = placementId;
	}

	public InstalledBaseItem withPlacementId(int placementId) {
		this.placementId = placementId;
		return this;
	}

	public LocalDate getFacilityCommitmentStartDate() {
		return facilityCommitmentStartDate;
	}

	public void setFacilityCommitmentStartDate(LocalDate facilityCommitmentStartDate) {
		this.facilityCommitmentStartDate = facilityCommitmentStartDate;
	}

	public InstalledBaseItem withFacilityCommitmentStartDate(LocalDate facilityCommitmentStartDate) {
		this.facilityCommitmentStartDate = facilityCommitmentStartDate;
		return this;
	}

	public LocalDate getFacilityCommitmentEndDate() {
		return facilityCommitmentEndDate;
	}

	public void setFacilityCommitmentEndDate(LocalDate facilityCommitmentEndDate) {
		this.facilityCommitmentEndDate = facilityCommitmentEndDate;
	}

	public InstalledBaseItem withFacilityCommitmentEndDate(LocalDate facilityCommitmentEndDate) {
		this.facilityCommitmentEndDate = facilityCommitmentEndDate;
		return this;
	}

	public LocalDate getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public InstalledBaseItem withLastModifiedDate(LocalDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
		return this;
	}

	public List<InstalledBaseItemMetaData> getMetaData() {
		return metaData;
	}

	public void setMetaData(List<InstalledBaseItemMetaData> metaData) {
		this.metaData = metaData;
	}

	public InstalledBaseItem withMetaData(List<InstalledBaseItemMetaData> metaData) {
		this.metaData = metaData;
		return this;
	}

	public InstalledBaseItemAddress getAddress() {
		return address;
	}

	public void setAddress(InstalledBaseItemAddress address) {
		this.address = address;
	}

	public InstalledBaseItem withAddress(InstalledBaseItemAddress address) {
		this.address = address;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, facilityCommitmentEndDate, facilityCommitmentStartDate, facilityId, lastModifiedDate, metaData, placementId, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof InstalledBaseItem)) {
			return false;
		}
		InstalledBaseItem other = (InstalledBaseItem) obj;
		return Objects.equals(address, other.address) && Objects.equals(facilityCommitmentEndDate, other.facilityCommitmentEndDate) && Objects.equals(facilityCommitmentStartDate, other.facilityCommitmentStartDate) && Objects.equals(facilityId,
			other.facilityId) && Objects.equals(lastModifiedDate, other.lastModifiedDate) && Objects.equals(metaData, other.metaData) && placementId == other.placementId && Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InstalledBaseItem [type=").append(type).append(", facilityId=").append(facilityId).append(", placementId=").append(placementId).append(", facilityCommitmentStartDate=").append(facilityCommitmentStartDate).append(
			", facilityCommitmentEndDate=").append(facilityCommitmentEndDate).append(", lastModifiedDate=").append(lastModifiedDate).append(", address=").append(address).append(", metaData=").append(metaData).append("]");
		return builder.toString();
	}
}
