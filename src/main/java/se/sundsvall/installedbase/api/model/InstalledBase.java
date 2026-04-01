package se.sundsvall.installedbase.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Objects;

@Schema(description = "Installed base model")
public class InstalledBase {

	@Schema(description = "Company", examples = "Test test AB")
	private String company;

	@Schema(description = "Customer number", examples = "10007")
	private String customerId;

	@Schema(description = "type", examples = "House")
	private String type;

	@Schema(description = "Facility id", examples = "735999109270751042")
	private String facilityId;

	@Schema(description = "Placement id", examples = "1234")
	private String placementId;

	@Schema(description = "Care of", examples = "test person")
	private String careOf;

	@Schema(description = "Street", examples = "Testgatan 1")
	private String street;

	@Schema(description = "Postal code", examples = "12345")
	private String postCode;

	@Schema(description = "City", examples = "Sundsvall")
	private String city;

	@Schema(description = "Property designation", examples = "BALDER 123")
	private String propertyDesignation;

	@Schema(description = "Date from", examples = "2025-01-01")
	private LocalDate dateFrom;

	@Schema(description = "Date to", examples = "2026-01-01")
	private LocalDate dateTo;

	@Schema(description = "Last modified date", examples = "2025-06-15")
	private LocalDate dateLatestModified;

	public static InstalledBase create() {
		return new InstalledBase();
	}

	public InstalledBase withCompany(String company) {
		this.company = company;
		return this;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public InstalledBase withCustomerId(String customerId) {
		this.customerId = customerId;
		return this;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public InstalledBase withType(String type) {
		this.type = type;
		return this;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public InstalledBase withFacilityId(String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public InstalledBase withPlacementId(String placementId) {
		this.placementId = placementId;
		return this;
	}

	public String getPlacementId() {
		return placementId;
	}

	public void setPlacementId(String placementId) {
		this.placementId = placementId;
	}

	public InstalledBase withCareOf(String careOf) {
		this.careOf = careOf;
		return this;
	}

	public String getCareOf() {
		return careOf;
	}

	public void setCareOf(String careOf) {
		this.careOf = careOf;
	}

	public InstalledBase withStreet(String street) {
		this.street = street;
		return this;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public InstalledBase withPostCode(String postCode) {
		this.postCode = postCode;
		return this;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public InstalledBase withCity(String city) {
		this.city = city;
		return this;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public InstalledBase withPropertyDesignation(String propertyDesignation) {
		this.propertyDesignation = propertyDesignation;
		return this;
	}

	public String getPropertyDesignation() {
		return propertyDesignation;
	}

	public void setPropertyDesignation(String propertyDesignation) {
		this.propertyDesignation = propertyDesignation;
	}

	public InstalledBase withDateFrom(LocalDate dateFrom) {
		this.dateFrom = dateFrom;
		return this;
	}

	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(LocalDate dateFrom) {
		this.dateFrom = dateFrom;
	}

	public InstalledBase withDateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
		return this;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}

	public void setDateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
	}

	public InstalledBase withDateLatestModified(LocalDate dateLatestModified) {
		this.dateLatestModified = dateLatestModified;
		return this;
	}

	public LocalDate getDateLatestModified() {
		return dateLatestModified;
	}

	public void setDateLatestModified(LocalDate dateLatestModified) {
		this.dateLatestModified = dateLatestModified;
	}

	@Override
	public String toString() {
		return "InstalledBase{" +
			"company='" + company + '\'' +
			", customerId='" + customerId + '\'' +
			", type='" + type + '\'' +
			", facilityId='" + facilityId + '\'' +
			", placementId='" + placementId + '\'' +
			", careOf='" + careOf + '\'' +
			", street='" + street + '\'' +
			", postCode='" + postCode + '\'' +
			", city='" + city + '\'' +
			", propertyDesignation='" + propertyDesignation + '\'' +
			", dateFrom=" + dateFrom +
			", dateTo=" + dateTo +
			", dateLatestModified=" + dateLatestModified +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		InstalledBase that = (InstalledBase) o;
		return Objects.equals(company, that.company) && Objects.equals(customerId, that.customerId) && Objects.equals(type, that.type) && Objects.equals(facilityId, that.facilityId) && Objects.equals(
			placementId, that.placementId) && Objects.equals(careOf, that.careOf) && Objects.equals(street, that.street) && Objects.equals(postCode, that.postCode) && Objects.equals(city, that.city)
			&& Objects.equals(propertyDesignation, that.propertyDesignation) && Objects.equals(dateFrom, that.dateFrom) && Objects.equals(dateTo, that.dateTo) && Objects.equals(dateLatestModified,
				that.dateLatestModified);
	}

	@Override
	public int hashCode() {
		return Objects.hash(company, customerId, type, facilityId, placementId, careOf, street, postCode, city, propertyDesignation, dateFrom, dateTo, dateLatestModified);
	}
}
