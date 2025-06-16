package se.sundsvall.installedbase.integration.db.specification;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder<T> {

	public Specification<T> buildEqualFilter(final String attribute, final String value) {
		return (entity, cq, cb) -> {
			if (value != null) {
				return cb.equal(entity.get(attribute), value);
			}
			return cb.and();
		};
	}
}
