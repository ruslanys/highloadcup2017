package me.ruslanys.highloadcup.annotation;

import java.util.Comparator;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public class OrderedComparator implements Comparator<Class<?>> {
    @Override
    public int compare(Class<?> o1, Class<?> o2) {
        int order1 = 0;
        int order2 = 0;

        Ordered o1Annotation = o1.getAnnotation(Ordered.class);
        if (o1Annotation != null) {
            order1 = o1Annotation.value();
        }

        Ordered o2Annotation = o2.getAnnotation(Ordered.class);
        if (o2Annotation != null) {
            order2 = o2Annotation.value();
        }

        if (order1 == order2) {
            return o1.getName().compareTo(o2.getName());
        } else {
            return order1 < order2 ? -1 : 1;
        }

    }
}
