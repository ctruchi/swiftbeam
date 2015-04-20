package swiftbeam.serialization;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import restx.jackson.Views;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface ResolvedIn {

    public Class<? extends Views.Public>[] value();
}
