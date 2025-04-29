package greenbuildings.idp.interceptors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableHibernateFilter {
    String filterName() default "";
    
    Param[] params() default {}; // Array of key-value pairs
    
    @interface Param {
        String name(); // Parameter name
        String value(); // Parameter value
    }
}
