package ru.dest.library.storage;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class SeparatedFileStorage<KEY, VALUE extends DataObject<KEY>> implements DataStorage<KEY, VALUE> {

    protected final Map<KEY, VALUE> data = new HashMap<>();

    @Getter
    protected final File dataFile;

    public SeparatedFileStorage(@NotNull File data) throws IOException{
        if(data.exists() && data.isFile()) {
            throw new IOException("File must be a folder");
        }

        if(!data.exists()){
            data.mkdirs();
        }

        this.dataFile = data;
    }

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
    public void save(VALUE value) throws Exception{
        File f = getFileName(value);
        if(!f.exists()) f.createNewFile();

        save(f, value);
    }

    protected abstract VALUE load(File f) throws Exception;
    protected abstract File getFileName(VALUE val);
    protected abstract void save(File f, VALUE val) throws Exception;

    @Override
    public void load() throws Exception{
        File[] files = dataFile.listFiles();
        if(files == null) return;

        for(File f : files){
            VALUE val = this.load(f);
            this.data.put(val.getKey(), val);
        }

        JsonObject o;

    }

    @Override
    public void save() throws Exception{
        for (VALUE value : data.values()) {
            save(value);
        }
    }
}
