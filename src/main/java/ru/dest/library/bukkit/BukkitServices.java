package ru.dest.library.bukkit;

import org.jetbrains.annotations.NotNull;
import ru.dest.library.service.IService;

import java.util.HashMap;
import java.util.Map;

public class BukkitServices {


    private static final Map<Class<? extends IService>, IService> services = new HashMap<>();
    /**
     * Get registered Service. For example VaultEconomyServices or Some registered by other plugins
     * @param serviceClass  class of service
     * @return null if not found or service registered for this class in other cases
     * @param <S> Service Type
     */
    @SuppressWarnings("unchecked")
    public static  <S extends IService> S getService(@NotNull Class<S> serviceClass) {
        return (S)services.get(serviceClass);
    }

    /**
     *
     * @param cl class of service
     * @param object created object of service
     * @param <S> Service Type
     */
    public static  <S extends IService> void register(@NotNull Class<S> cl, @NotNull S object){
        services.put(cl, object);
    }
}
