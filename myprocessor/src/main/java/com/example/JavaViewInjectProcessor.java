/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.example;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("com.example.InjectView")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class JavaViewInjectProcessor extends AbstractProcessor {
    private Map<String, ProxyInfo> mProxyMap = new HashMap<>();

    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        elementUtils = env.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        String fqClassName, className, packageName;

        for (Element ele : roundEnv.getElementsAnnotatedWith(InjectView.class)) {

            if (ele.getKind() == ElementKind.CLASS) {
                TypeElement classElement = (TypeElement) ele;

                PackageElement packageElement = (PackageElement) ele
                        .getEnclosingElement();
                fqClassName = classElement.getQualifiedName().toString();
                className = classElement.getSimpleName().toString();
                packageName = packageElement.getQualifiedName().toString();

                int layoutId = classElement.getAnnotation(InjectView.class).value();

                ProxyInfo proxyInfo = mProxyMap.get(fqClassName);
                if (proxyInfo != null) {
                    proxyInfo.setLayoutId(layoutId);
                } else {
                    proxyInfo = new ProxyInfo(packageName, className);
                    proxyInfo.setTypeElement(classElement);
                    proxyInfo.setLayoutId(layoutId);
                    mProxyMap.put(fqClassName, proxyInfo);
                }

            } else if (ele.getKind() == ElementKind.FIELD) {

                VariableElement varElement = (VariableElement) ele;
                TypeElement classElement = (TypeElement) ele.getEnclosingElement();

                fqClassName = classElement.getQualifiedName().toString();
                PackageElement packageElement = elementUtils.getPackageOf(classElement);

                packageName = packageElement.getQualifiedName().toString();

                className = getClassName(classElement, packageName);

                int id = varElement.getAnnotation(InjectView.class).value();
                String fieldName = varElement.getSimpleName().toString();
                String fieldType = varElement.asType().toString();

                ProxyInfo proxyInfo = mProxyMap.get(fqClassName);
                if (proxyInfo == null) {
                    proxyInfo = new ProxyInfo(packageName, className);
                    mProxyMap.put(fqClassName, proxyInfo);
                    proxyInfo.setTypeElement(classElement);
                }
                proxyInfo.putViewInfo(id,
                        new ViewInfo(id, fieldName, fieldType));

            }

        }

        for (String key : mProxyMap.keySet()) {
            ProxyInfo proxyInfo = mProxyMap.get(key);
            try {

                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                        proxyInfo.getProxyClassFullName(),
                        proxyInfo.getTypeElement());
                Writer writer = jfo.openWriter();
                writer.write(proxyInfo.generateJavaCode());
                writer.flush();
                writer.close();
            } catch (IOException e) {

            }

        }

        return true;
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen)
                .replace('.', '$');
    }
}
