package se.sundsvall.installedbase.api.model.validation;

import org.apache.commons.lang3.StringUtils;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

public final class ValidatorUtil {

	private ValidatorUtil() {}

	/**
	 * Check that at least one of the parameters owner or delegatedTo is provided
	 * 
	 * @param owner       owner of the facility delegation
	 * @param delegatedTo the party to which the facility is delegated
	 */
	public static void validateDelegationParameters(String owner, String delegatedTo) {
		if (StringUtils.isBlank(owner) && StringUtils.isBlank(delegatedTo)) {
			throw Problem.builder()
				.withTitle("Invalid search parameters")
				.withDetail("Either owner or delegatedTo must be provided")
				.withStatus(Status.BAD_REQUEST)
				.build();
		}
	}
}
