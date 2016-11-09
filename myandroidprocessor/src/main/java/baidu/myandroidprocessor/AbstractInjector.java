/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package baidu.myandroidprocessor;

public interface AbstractInjector<T> {
	void inject(Finder finder, T target, Object source);
}
