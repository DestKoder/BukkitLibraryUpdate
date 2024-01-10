package ru.dest.library.helpers;

import org.bukkit.entity.Player;
import ru.dest.library.Library;
import ru.dest.library.command.ExecutionData;
import ru.dest.library.command.ICommand;
import ru.dest.library.command.SimpleCommand;
import ru.dest.library.command.annotation.ConsoleOnly;
import ru.dest.library.command.annotation.Permission;
import ru.dest.library.command.annotation.PlayerOnly;
import ru.dest.library.command.annotation.RequireArgs;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AnnotationValidator {

    private static final Map<Class<? extends Annotation>, Checker> registry = new HashMap<>();

    static {
        regChecker(Permission.class, (data, ann) -> {
            if(!data.executor().hasPermission(((Permission)ann).p())){
                Library.getInstance().getLang().getMessage("error.permission").send(data.executor());
                return false;
            }
            return true;
        });
        regChecker(PlayerOnly.class, (data, ann) -> {
            if(!(data.executor() instanceof Player)){
                Library.getInstance().getLang().getMessage("error.playeronly").send(data.executor());
                return false;
            }
            return true;
        });
        regChecker(ConsoleOnly.class, (data, ann) -> {
            if(data.executor() instanceof Player){
                Library.getInstance().getLang().getMessage("error.consoleonly").send(data.executor());
                return false;
            }
            return true;
        });
        regChecker(RequireArgs.class, (((data, ann) -> ArgumentValidator.validate(data, ((RequireArgs)ann).args()))));
    }

    public static void regChecker(Class<? extends Annotation> cl, Checker c){
        registry.put(cl, c);
    }

    public static boolean validate(ICommand cmd, ExecutionData data){
        for(Class<? extends Annotation> cl : registry.keySet() ){
            Annotation ann = cmd.getClass().getDeclaredAnnotation(cl);
            if(ann == null) continue;

            if(!registry.get(cl).check(data, ann)) return false;
        }
        return true;
    }

    public interface Checker{
        boolean check(ExecutionData data, Annotation ann);
    }
}
