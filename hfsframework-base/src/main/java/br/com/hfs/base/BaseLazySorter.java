package br.com.hfs.base;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class BaseLazySorter <T> implements Comparator<T> {

	/** The sort field. */
	private String sortField;

	/** The sort order. */
	private SortOrder sortOrder;

	/**
	 * Instantiates a new base lazy sorter.
	 *
	 * @param sortField
	 *            the sort field
	 * @param sortOrder
	 *            the sort order
	 */
	public BaseLazySorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	private Object getPropertyValueViaReflection(Object o, String field)
            throws ReflectiveOperationException, IllegalArgumentException, IntrospectionException {
		return new PropertyDescriptor(field, o.getClass()).getReadMethod().invoke(o);
	}

    @Override
    @SuppressWarnings("unchecked")
    public int compare(T entidade1, T entidade2) {
        try {
            Object value1 = this.getPropertyValueViaReflection(entidade1, sortField);
            Object value2 = this.getPropertyValueViaReflection(entidade2, sortField);

            int value = ((Comparable<Object>) value1).compareTo(value2);

            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
	/*
	@SuppressWarnings("unchecked")
	public int compare(T entidade1, T entidade2) {
		try {
			Method metodo1 = entidade1.getClass().getMethod("get" + StringUtils.capitalize(this.sortField),
					new Class[] {});
			Object value1 = metodo1.invoke(entidade1, new Object[] {});

			Method metodo2 = entidade2.getClass().getMethod("get" + StringUtils.capitalize(this.sortField),
					new Class[] {});
			Object value2 = metodo2.invoke(entidade2, new Object[] {});

			int value = ((Comparable<Object>) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	*/
}
