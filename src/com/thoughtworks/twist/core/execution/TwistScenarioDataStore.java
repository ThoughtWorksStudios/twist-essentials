package com.thoughtworks.twist.core.execution;

import com.thoughtworks.gauge.datastore.DataStoreFactory;

public class TwistScenarioDataStore {
    public Object put(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException("Key is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        DataStoreFactory.getScenarioDataStore().put(key, value);
        return null;
    }

    public Object get(Object key) {
        return DataStoreFactory.getScenarioDataStore().get(key);
    }

    public Object remove(Object key) {
        return DataStoreFactory.getScenarioDataStore().remove(key);
    }

    public java.util.Set<java.util.Map.Entry<Object, Object>> entrySet() {
        return DataStoreFactory.getScenarioDataStore().entrySet();
    }

}
