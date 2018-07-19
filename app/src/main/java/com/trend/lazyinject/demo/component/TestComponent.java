package com.trend.lazyinject.demo.component;

import com.trend.lazyinject.annotation.Component;
import com.trend.lazyinject.annotation.Inject;
import com.trend.lazyinject.annotation.Provide;
import com.trend.lazyinject.demo.model.BaseModel;
import com.trend.lazyinject.demo.model.ModelA;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by swift_gan on 2018/4/17.
 */
@Component
public interface TestComponent<T extends Serializable & List> {
    @Provide
    List<String> provide1();
    @Provide
    ArrayList<Integer> provide2();
    @Provide
    ArrayList<? extends BaseModel> provide3();
    @Provide
    ModelA provide4(@Inject Map<String,BaseModel> strings);
    @Provide
    Map<String,BaseModel> provide5();
    @Provide
    Map<String, ? extends ModelA> provide6();
    @Provide
    Map<String,T> provide7();
}
