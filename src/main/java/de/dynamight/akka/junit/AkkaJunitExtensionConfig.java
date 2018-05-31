package de.dynamight.akka.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AkkaJunitExtensionConfig {

  String name() default "default";

  boolean materializer() default true;
}
