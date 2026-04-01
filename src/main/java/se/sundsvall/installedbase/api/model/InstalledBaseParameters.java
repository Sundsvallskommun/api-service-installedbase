package se.sundsvall.installedbase.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.dept44.models.api.paging.AbstractParameterPagingBase;

public class InstalledBaseParameters extends AbstractParameterPagingBase {

	@Schema(description = "UUID that represents a party", examples = "cf9892ad-69d5-420f-ae98-9631dd1664fe")
	@ValidUuid
	private String partyId;

	@Schema(description = "List of organization ids", examples = "[\"123456789\", \"123456987\"]")
	private List<String> organizationIds;

	@Schema(description = "Filter date", examples = "2025-06-01")
	private LocalDate date;

	@Schema(description = "Column to sort by", examples = "Company")
	private String sortBy;

	public static InstalledBaseParameters create() {
		return new InstalledBaseParameters();
	}

	public InstalledBaseParameters withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public InstalledBaseParameters withOrganizationIds(final List<String> organizationIds) {
		this.organizationIds = organizationIds;
		return this;
	}

	public List<String> getOrganizationIds() {
		return organizationIds;
	}

	public void setOrganizationIds(final List<String> organizationIds) {
		this.organizationIds = organizationIds;
	}

	public InstalledBaseParameters withDate(final LocalDate date) {
		this.date = date;
		return this;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(final LocalDate date) {
		this.date = date;
	}

	public InstalledBaseParameters withSortBy(final String sortBy) {
		this.sortBy = sortBy;
		return this;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(final String sortBy) {
		this.sortBy = sortBy;
	}

	@Override
	public String toString() {
		return "InstalledBaseParameters{" +
			"partyId='" + partyId + '\'' +
			", organizationIds='" + organizationIds + '\'' +
			", date=" + date +
			", sortBy='" + sortBy + '\'' +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;
		InstalledBaseParameters that = (InstalledBaseParameters) o;
		return Objects.equals(partyId, that.partyId) && Objects.equals(organizationIds, that.organizationIds) && Objects.equals(date, that.date) && Objects.equals(sortBy, that.sortBy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), partyId, organizationIds, date, sortBy);
	}
}
