package ru.dest.library.storage;

import lombok.Getter;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class OneFileStorage<KEY, VALUE extends DataObject<KEY>> implements DataStorage<KEY, VALUE>{

    @Getter
    protected final File dataFile;

    protected final Map<KEY, VALUE> data = new HashMap<>();

    @Override
    public VALUE get(KEY key) {
        return data.get(key);
    }

    @Override
    public boolean has(KEY key) {
        return data.containsKey(key);
    }

    @Override
    public boolean has(@NotNull VALUE value) {
        return data.containsKey(value.getKey());
    }

    @Override
    public void add(VALUE value) {
        data.put(value.getKey(), value);
    }

    @Override
    public void save(VALUE value) {
        throw new NotImplementedException("Not available for OneFileStorage");
    }

    public OneFileStorage(@NotNull File data) throws IOException {
        if(data.exists() && !data.isFile()) {
            throw new IOException("File must be a file");
        }

        if(!data.exists()){
            data.createNewFile();
        }

        this.dataFile = data;
    }
}
