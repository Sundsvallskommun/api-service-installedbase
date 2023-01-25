package se.sundsvall.installedbase.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Installed base item metadata model")
public class InstalledBaseItemMetaData {

	@Schema(description = "Key", example = "netarea", accessMode = READ_ONLY)
	private String key;

	@Schema(description = "Value", example = "Sundsvall tätort", accessMode = READ_ONLY)
	private String value;

	@Schema(description = "Type", example = "location", accessMode = READ_ONLY)
	private String type;

	@Schema(description = "Displayname", example = "Nätområde", accessMode = READ_ONLY)
	private String displayName;

	public static InstalledBaseItemMetaData create() {
		return new InstalledBaseItemMetaData();
	}

	public String getKey() {
		return key;
	}

	public InstalledBaseItemMetaData withKey(String key) {
		this.key = key;
		return this;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public InstalledBaseItemMetaData withValue(String value) {
		this.value = value;
		return this;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public InstalledBaseItemMetaData withType(String type) {
		this.type = type;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public InstalledBaseItemMetaData withDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(displayName, key, type, value);
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
		InstalledBaseItemMetaData other = (InstalledBaseItemMetaData) obj;
		return Objects.equals(displayName, other.displayName) && Objects.equals(key, other.key) && Objects.equals(type, other.type) && Objects.equals(value, other.value);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InstalledBaseItemMetaData [key=").append(key).append(", value=").append(value).append(", type=").append(type).append(", displayName=").append(displayName).append("]");
		return builder.toString();
	}
}
