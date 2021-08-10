package com.meliora.natujenge.core;

import com.meliora.natujenge.annotations.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.reflections.Reflections;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ResourceFactory {
    Map<String, DataSource> dataSources;

    Map<Class<?>, Object> singletons;

    public ResourceFactory(String packageName) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.dataSources = new HashMap<String, DataSource>();
        this.singletons = new HashMap();

        System.out.println("About to initialize all resources...");

        initializeDatasources(packageName);

        initializeRepositories(packageName);

        initializeServices(packageName);

        initializeHelpers(packageName);

        System.out.println("initialized all resources...");
    }

    public void initializeDatasources(String packageName) {
        System.out.println("Starting to initialize all sql datasources...");

        Reflections reflections = new Reflections(packageName);

        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Datasource.class);

        for (Class<?> cl : classes) {

            Datasource datasource = cl.getAnnotation(Datasource.class);

            try {
                HikariConfig config = new HikariConfig();

                HikariDataSource hikariDataSource;
                config.setJdbcUrl(datasource.jdbcUrl());
                config.setUsername(datasource.username());
                config.setPassword(datasource.password());
                config.setDriverClassName(datasource.driverClassName());
                config.addDataSourceProperty("cachePrepStmts", datasource.cachePrepStmts());
                config.addDataSourceProperty("useSSL", datasource.useSSL());

                if (datasource.prepStmtCacheSize() > 0) {
                    config.addDataSourceProperty("prepStmtCacheSize", datasource.prepStmtCacheSize());
                }

                if (datasource.prepStmtCacheSqlLimit() > 0) {
                    config.addDataSourceProperty("prepStmtCacheSqlLimit", datasource.prepStmtCacheSqlLimit());
                }

                if (datasource.minimumIdle() > 0) {
                    config.setMinimumIdle(datasource.minimumIdle());
                }

                if (datasource.maximumPoolSize() > 0) {
                    config.setMaximumPoolSize(datasource.maximumPoolSize());
                }

                hikariDataSource = new HikariDataSource(config);

                this.dataSources.put(datasource.id(), hikariDataSource);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("Initialized all sql datasources... numberOfSqlDatasources :: " + dataSources.size());
    }

    public void initializeRepositories(String packageName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println("Scanning for repositories...");

        Reflections reflections = new Reflections(packageName);

        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Repository.class);

        System.out.println("Found " + classes.size() + " classes with the annotation @Repository");

        for (Class<?> cl : classes) {
            System.out.println("classname: " + cl.getName());

            Object object = cl.getDeclaredConstructor().newInstance();

            this.singletons.put(cl, object);
        }
        System.out.println("Initialized all repositories... numberOfSingletons :: " + singletons.size());
    }

    public void initializeServices(String packageName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println("Scanning for services...");

        Reflections reflections = new Reflections(packageName);

        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Service.class);

        System.out.println("Found " + classes.size() + " classes with the annotation @Service");

        for (Class<?> cl : classes) {
            Object object = cl.getDeclaredConstructor().newInstance();

            this.singletons.put(cl, object);

            this.autowire(cl, object);
        }

        System.out.println("Initialized all services... numberOfSingletons :: " + singletons.size());
    }

    public void initializeHelpers(String packageName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println("Scanning for helpers...");

        Reflections reflections = new Reflections(packageName);

        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Helper.class);

        System.out.println("Found " + classes.size() + " classes with the annotation @Helper");

        for (Class<?> cl : classes) {
            Object object = cl.getDeclaredConstructor().newInstance();

            this.singletons.put(cl, object);

            this.autowire(cl, object);

            cl.getDeclaredMethod("init", null);
        }

        System.out.println("Initialized all helpers... numberOfSingletons :: " + singletons.size());
    }

    public Object getClassInstance(Class<?> tClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        if (this.singletons.containsKey(tClass)) {
            return this.singletons.get(tClass);
        }

        synchronized (singletons) {
            Object object = tClass.getDeclaredConstructor().newInstance();
            this.singletons.put(tClass, object);

            return object;
        }
    }

    public void autowire(Class<?> cl, Object object) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        for (Field field : cl.getDeclaredFields()) {

            if (field.isAnnotationPresent(Autowired.class)) {
                Object fieldInstance = this.getClassInstance(field.getType());
                field.set(object, fieldInstance);
            }
        }
    }

}
