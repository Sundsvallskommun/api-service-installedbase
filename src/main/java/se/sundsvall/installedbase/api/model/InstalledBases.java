package se.sundsvall.installedbase.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Schema(description = "Installed bases response model")
public class InstalledBases {

	@JsonProperty("_meta")
	@Schema(implementation = PagingAndSortingMetaData.class, accessMode = READ_ONLY)
	private PagingAndSortingMetaData metaData;

	@ArraySchema(schema = @Schema(implementation = InstalledBase.class, accessMode = READ_ONLY))
	private List<InstalledBase> installedBases;

	public static InstalledBases create() {
		return new InstalledBases();
	}

	public InstalledBases withMetaData(PagingAndSortingMetaData metaData) {
		this.metaData = metaData;
		return this;
	}

	public PagingAndSortingMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(PagingAndSortingMetaData metaData) {
		this.metaData = metaData;
	}

	public InstalledBases withInstalledBases(List<InstalledBase> installedBases) {
		this.installedBases = installedBases;
		return this;
	}

	public List<InstalledBase> getInstalledBases() {
		return installedBases;
	}

	public void setInstalledBases(List<InstalledBase> installedBases) {
		this.installedBases = installedBases;
	}

	@Override
	public String toString() {
		return "InstalledBases{" +
			"metaData=" + metaData +
			", installedBases=" + installedBases +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		InstalledBases that = (InstalledBases) o;
		return Objects.equals(metaData, that.metaData) && Objects.equals(installedBases, that.installedBases);
	}

	@Override
	public int hashCode() {
		return Objects.hash(metaData, installedBases);
	}
}
