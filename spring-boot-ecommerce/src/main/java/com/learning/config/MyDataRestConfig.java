package com.learning.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.learning.entity.Country;
import com.learning.entity.Order;
import com.learning.entity.Product;
import com.learning.entity.ProductCategory;
import com.learning.entity.State;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

	private EntityManager entityManager;
	
	@Value("${allowed.origins}")
	private String[] theAllowedOrigins;

	@Autowired
	public MyDataRestConfig(EntityManager theEntityManager) {
		entityManager = theEntityManager;
	}

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
		HttpMethod[] theUnsupportedActions = { HttpMethod.PUT, HttpMethod.POST, 
														HttpMethod.DELETE, HttpMethod.PATCH };
		RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);

		// disable HTTPS methods for Product: PUT, POST & DELETE
		disableHttpMethods(Product.class, config, theUnsupportedActions);

		// disable HTTPS methods for ProductCategory: PUT, POST & DELETE
		disableHttpMethods(ProductCategory.class, config, theUnsupportedActions);

		// disable HTTPS methods for Country: PUT, POST & DELETE
		disableHttpMethods(Country.class, config, theUnsupportedActions);
		
		// disable HTTPS methods for States: PUT, POST & DELETE
		disableHttpMethods(State.class, config, theUnsupportedActions);
		
		// disable HTTPS methods for States: PUT, POST & DELETE
		disableHttpMethods(Order.class, config, theUnsupportedActions);
		
		// call an internal helper method
		exposeIds(config);
		
		//configure the cors mapping
		cors.addMapping(config.getBasePath() + "/**").allowedOrigins(theAllowedOrigins);
		
	}

	private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config,
			HttpMethod[] theUnsupportedActions) {
		config.getExposureConfiguration().forDomainType(theClass)
				.withItemExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
				.withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
	}

	private void exposeIds(RepositoryRestConfiguration config) {
		// expose entity ids

		// get a list of all the entity classes from the entity manager
		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

		// - create an array of the entity types
		List<Class> entityClasses = new ArrayList<>();

		// - get the entity types for the entities

		for (EntityType tempEntityType : entities) {
			entityClasses.add(tempEntityType.getJavaType());
		}

		// -expose the entity ids for the array of entity/domain types
		Class[] domainTypes = entityClasses.toArray(new Class[0]);
		config.exposeIdsFor(domainTypes);
	}

}
