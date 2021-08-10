package com.meliora.natujenge;

import com.meliora.natujenge.core.ResourceFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * Hello world!
 */
public class App {
    String packageName = this.getClass().getPackage().getName();
    private static ResourceFactory resourceFactory;

    App() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        System.out.println("About to start all cars in the package " + packageName);
        resourceFactory = new ResourceFactory(packageName);
    }

    public static void main(String[] args) {
        try {
            App app = new App();

        } catch (Exception ex) {
            System.out.println("Exception " + ex);
        }
    }
}
