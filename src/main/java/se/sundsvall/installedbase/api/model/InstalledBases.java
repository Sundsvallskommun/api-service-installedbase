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
	private List<InstalledBase> installedBaseList;

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

	public InstalledBases withInstalledBaseList(List<InstalledBase> installedBases) {
		this.installedBaseList = installedBases;
		return this;
	}

	public List<InstalledBase> getInstalledBaseList() {
		return installedBaseList;
	}

	public void setInstalledBaseList(List<InstalledBase> installedBaseList) {
		this.installedBaseList = installedBaseList;
	}

	@Override
	public String toString() {
		return "InstalledBases{" +
			"metaData=" + metaData +
			", installedBaseList=" + installedBaseList +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		InstalledBases that = (InstalledBases) o;
		return Objects.equals(metaData, that.metaData) && Objects.equals(installedBaseList, that.installedBaseList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(metaData, installedBaseList);
	}
}
