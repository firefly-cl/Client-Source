package crack.firefly.com.System.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EventManager {

    private final Map<Class<? extends Event>, ArrayHelper<Data>> REGISTRY_MAP = new HashMap<>();

    public void register(Object o) {
        for (Method method : o.getClass().getDeclaredMethods()) {
            if (!isMethodBad(method)) {
                register(method, o);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void register(Method method, Object o) {
        Class<? extends Event> clazz = (Class<? extends Event>) method.getParameterTypes()[0];
        final Data methodData = new Data(o, method, method.getAnnotation(EventTarget.class).value());

        if (!methodData.target.isAccessible()) {
            methodData.target.setAccessible(true);
        }

        if (REGISTRY_MAP.containsKey(clazz)) {
            if (!REGISTRY_MAP.get(clazz).contains(methodData)) {
                REGISTRY_MAP.get(clazz).add(methodData);
                sortListValue(clazz);
            }
        } else {
            ArrayHelper<Data> dataList = new ArrayHelper<>();
            dataList.add(methodData);
            REGISTRY_MAP.put(clazz, dataList);
        }
    }

    // MÉTODO CALL DE ALTA PERFORMANCE (Remove o lag de rede)
    public void call(Event event) {
        ArrayHelper<Data> dataList = REGISTRY_MAP.get(event.getClass());
        
        if (dataList != null && !dataList.isEmpty()) {
            // Loop indexado para evitar criação de objetos Iterator
            for (int i = 0; i < dataList.size(); i++) {
                Data data = dataList.get(i);
                try {
                    data.target.invoke(data.source, event);
                } catch (Exception e) {
                    // Silencioso para manter FPS estável
                }
            }
        }
    }

    public void unregister(Object o) {
        for (ArrayHelper<Data> array : REGISTRY_MAP.values()) {
            for (int i = 0; i < array.size(); i++) {
                if (array.get(i).source.equals(o)) {
                    array.remove(array.get(i));
                }
            }
        }
        cleanMap(true);
    }

    public void cleanMap(boolean b) {
        REGISTRY_MAP.entrySet().removeIf(entry -> b && entry.getValue().isEmpty());
    }

    private void sortListValue(Class<? extends Event> clazz) {
        ArrayHelper<Data> flexibleArray = new ArrayHelper<>();
        ArrayHelper<Data> current = REGISTRY_MAP.get(clazz);
        
        // Agora usando a classe Priority que está na mesma pasta
        for (byte b : Priority.VALUE_ARRAY) {
            for (int i = 0; i < current.size(); i++) {
                Data res = current.get(i);
                if (res.priority == b) {
                    flexibleArray.add(res);
                }
            }
        }
        REGISTRY_MAP.put(clazz, flexibleArray);
    }

    private boolean isMethodBad(Method method) {
        return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
    }

    public void shutdown() {
        REGISTRY_MAP.clear();
    }
}