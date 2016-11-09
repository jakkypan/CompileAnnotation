/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface InjectView {
	int value();
}
