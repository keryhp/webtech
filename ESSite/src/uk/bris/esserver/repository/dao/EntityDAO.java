package uk.bris.esserver.repository.dao;

import java.util.List;
import java.util.Map;

public interface EntityDAO<T> {

	int save(Class<T> clz, T entity);

	int saveOrUpdate(Class<T> clz, T entity);

	void delete(Class<T> clz, String id);

	T findOne(Class<T> clz, String id);

	List<T> findAll(Class<T> clz);
	
	List<T> executeQuery(String query, Map<String, String> queryParams);

	int executeUpdate(String query, Map<String, String> queryParams);
	
}
