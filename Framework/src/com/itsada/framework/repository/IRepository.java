package com.itsada.framework.repository;

import java.util.ArrayList;

public interface IRepository<T> {

	long add(T entity);
	void update(T entity);
	void delete(int id);
	
	ArrayList<T> getAll();
	T getById(int id);
}
