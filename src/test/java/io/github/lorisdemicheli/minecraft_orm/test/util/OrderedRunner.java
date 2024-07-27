package io.github.lorisdemicheli.minecraft_orm.test.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;

import io.github.lorisdemicheli.minecraft_orm.test.annotation.TestOrder;

public class OrderedRunner extends BlockJUnit4ClassRunner {

    public OrderedRunner(Class<?> klass) throws Exception {
        super(klass);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        List<FrameworkMethod> methods = new ArrayList<>(super.computeTestMethods());
        Collections.sort(methods, new Comparator<FrameworkMethod>() {
            public int compare(FrameworkMethod f1, FrameworkMethod f2) {
                TestOrder o1 = f1.getAnnotation(TestOrder.class);
                TestOrder o2 = f2.getAnnotation(TestOrder.class);
                if (o1 != null && o2 != null) {
                    return Integer.compare(o1.value(), o2.value());
                } else if (o1 != null) {
                    return -1;
                } else if (o2 != null) {
                    return 1;
                }
                return 0;
            }
        });
        return methods;
    }
}