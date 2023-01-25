package se.sundsvall.installedbase.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Installed base item address model")
public class InstalledBaseItemAddress {

	@Schema(description = "Property designation", example = "Södermalm 1:27", accessMode = READ_ONLY)
	private String propertyDesignation;

	@Schema(description = "Care of address", example = "Agatha Malm", accessMode = READ_ONLY)
	private String careOf;

	@Schema(description = "Street", example = "Storgatan 9", accessMode = READ_ONLY)
	private String street;

	@Schema(description = "Postal code", example = "85230", accessMode = READ_ONLY)
	private String postalCode;

	@Schema(description = "City", example = "Sundsvall", accessMode = READ_ONLY)
	private String city;

	public static InstalledBaseItemAddress create() {
		return new InstalledBaseItemAddress();
	}

	public String getPropertyDesignation() {
		return propertyDesignation;
	}

	public void setPropertyDesignation(String propertyDesignation) {
		this.propertyDesignation = propertyDesignation;
	}

	public InstalledBaseItemAddress withPropertyDesignation(String propertyDesignation) {
		this.propertyDesignation = propertyDesignation;
		return this;
	}

	public String getCareOf() {
		return careOf;
	}

	public void setCareOf(String careOf) {
		this.careOf = careOf;
	}

	public InstalledBaseItemAddress withCareOf(String careOf) {
		this.careOf = careOf;
		return this;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public InstalledBaseItemAddress withStreet(String street) {
		this.street = street;
		return this;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postCode) {
		this.postalCode = postCode;
	}

	public InstalledBaseItemAddress withPostalCode(String postCode) {
		this.postalCode = postCode;
		return this;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public InstalledBaseItemAddress withCity(String city) {
		this.city = city;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(careOf, city, postalCode, propertyDesignation, street);
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
		InstalledBaseItemAddress other = (InstalledBaseItemAddress) obj;
		return Objects.equals(careOf, other.careOf) && Objects.equals(city, other.city) && Objects.equals(postalCode, other.postalCode) && Objects.equals(propertyDesignation, other.propertyDesignation) && Objects.equals(street, other.street);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InstalledBaseItemAddress [propertyDesignation=").append(propertyDesignation).append(", careOf=").append(careOf).append(", street=").append(street).append(", postalCode=").append(postalCode).append(", city=").append(city)
			.append("]");
		return builder.toString();
	}
}
