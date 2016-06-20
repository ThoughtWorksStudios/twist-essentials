package com.thoughtworks.twist.migration.brt;

import org.junit.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class BRTMigrator {

	/**
	 * This simulates the Business Rules Table execution by calling the corresponding setter/getter in the given object.
	 * It executes the setUp and teardown methods before execution of each table row.
	 * @param table the input table with all data rows
	 * @param obj the object of the class in which BRT is defined
	 * @throws ClassNotFoundException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	public void BRTExecutor(com.thoughtworks.gauge.Table table, Object obj)
			throws ClassNotFoundException, InvocationTargetException,
			IllegalAccessException, NoSuchMethodException {
		List<String> columnNames = table.getColumnNames();
		List<List<String>> allRows = table.getRows();

		for (List<String> row : allRows) {
			invokeHook(obj, "setUp");
			for (int i = 0; i < row.size(); i++) {
				String col = columnNames.get(i);
				if (col.endsWith("?")) {
					assertValue(row.get(i), col, obj);
				} else {
					invokeSetter(row.get(i), col, obj);
				}
			}
			invokeHook(obj, "tearDown");
		}
	}

	public void assertValue(String expected, String columnName, Object obj)
			throws NoSuchMethodException, InvocationTargetException,
			IllegalAccessException, ClassNotFoundException {
		Class cls = Class.forName(obj.getClass().getName());
		String methodName = camelCase(columnName.substring(0,
				columnName.length() - 1));
		Method meth = cls.getMethod(methodName);
		Object obtained = meth.invoke(obj);
		Assert.assertEquals(expected, obtained);
	}

	public void invokeSetter(String rowValue, String columnName, Object obj)
			throws NoSuchMethodException, InvocationTargetException,
			IllegalAccessException, ClassNotFoundException {
		Class cls = Class.forName(obj.getClass().getName());
		Class paramType[] = new Class[] { String.class };

		String methodName = "set".concat(capitalize(camelCase(columnName)));
		Method meth = cls.getMethod(methodName, paramType);
		Object args[] = new Object[] { rowValue };
		meth.invoke(obj, args);
	}

	private void invokeHook(Object obj, String methodName)
			throws ClassNotFoundException, InvocationTargetException,
			IllegalAccessException {
		Class cls = Class.forName(obj.getClass().getName());
		Method meth = null;
		try {
			meth = cls.getMethod(methodName);
		} catch (NoSuchMethodException e) {
			return;
		}
		meth.invoke(obj, new Object[0]);
	}

	/**
	 * @param text input text
	 * @return text in camel case
	 * If input is "hello world", it returns "helloWorld"
	 */
	String camelCase(String text) {
		StringBuilder sb = new StringBuilder();
		String[] words = text.trim().split("\\s+");

		for (int i = 0; i < words.length; i++) {
			if (i == 0) {
				sb.append(words[i].substring(0, 1).toLowerCase()).append(words[0].substring(1));
			} else {
				sb.append(capitalize(words[i]));
			}
		}
		return sb.toString();
	}
	
	private String capitalize(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}

}
