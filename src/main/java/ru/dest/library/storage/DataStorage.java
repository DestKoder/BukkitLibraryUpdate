package ru.dest.library.storage;

public interface DataStorage<KEY, VALUE extends DataObject<KEY>> {



    VALUE get(KEY key);
    boolean has(KEY key);
    boolean has(VALUE value);

    void add(VALUE value);

    void save(VALUE value) throws Exception;

    void load() throws Exception;
    void save() throws Exception;

}
