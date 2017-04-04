package it.fcap.example.app.data.repo.custom;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.template.BooleanTemplate;
import it.fcap.example.app.data.model.Customer;
import it.fcap.example.app.data.model.QCustomer;
import it.fcap.example.app.data.repo.custom.strategy.CustomerCriteriaStrategyEnum;
import it.fcap.example.app.data.search.CriteriaBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by F.C. on 04/04/2017.
 */
public class CustomerRepositoryImpl implements CustomerRepositoryCustom<Customer> {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<Customer> searchAllCustomerWithSearchBean(
			CriteriaBean criteria,
			Pageable pageRequest) {

		final String Q_ENTITY_RESULT = "customer";

		JPAQuery jpaQuery = new JPAQuery(em);

		QCustomer customer = new QCustomer(Q_ENTITY_RESULT);

		JPAQuery coreQuery = jpaQuery.from(customer);

		// SEARCH EXPRESSION + EVENTUALI JOIN
		BooleanExpression whereBody = this.buildAdvancedSearchExpression(
				customer, criteria, coreQuery,
				CustomerCriteriaStrategyEnum.values()
		);

		coreQuery = coreQuery.where(whereBody);

		// Costruisco la Paginazione
		Page<Customer> pageResult = this.buildPageForCustomersByQuery(
				pageRequest, customer, coreQuery, Q_ENTITY_RESULT
		);

		return pageResult;
	}

	private BooleanExpression buildAdvancedSearchExpression(
			QCustomer rootQentity,
			CriteriaBean criteria,
			JPAQuery query,
			CustomerCriteriaStrategyEnum... args) {

		BooleanExpression result =  BooleanTemplate.TRUE.eq(Boolean.TRUE);

		for (CustomerCriteriaStrategyEnum strategy: args) {
			strategy.addSearchCondition(rootQentity, query, criteria);
		}

		return result;
	}

	private Page<Customer> buildPageForCustomersByQuery(
			Pageable pageRequest, QCustomer customer, JPAQuery query, String qResultName
	) {

		for (Sort.Order o : pageRequest.getSort()) {
			PathBuilder orderByExpression = new PathBuilder(Object.class, qResultName);

			query.orderBy(new OrderSpecifier(o.isAscending() ? com.mysema.query.types.Order.ASC
					: com.mysema.query.types.Order.DESC, orderByExpression.get(o.getProperty())));
		}

		int pageNumber = pageRequest.getPageNumber();
		int pageSize = pageRequest.getPageSize();
		List<Customer> content = query.offset(pageNumber*pageSize).limit(pageSize).list(customer);
		long count = query.count();
		return new PageImpl<Customer>(
				content, pageRequest, count
		);
	}
}