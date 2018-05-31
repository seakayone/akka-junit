package de.dynamight.akka.junit;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.testkit.javadsl.TestKit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

@AkkaJunitExtensionConfig
public class AkkaJunitExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

  private static final Namespace AKKA_JUNIT = Namespace.create(AkkaJunitExtension.class);
  private static final AkkaJunitExtensionConfig DEFAULT_CONFIG =
    AkkaJunitExtensionConfig.class.getAnnotation(AkkaJunitExtensionConfig.class);

  @Override
  public void beforeAll(ExtensionContext context) {
    final Store store = getStore(context);
    final AkkaJunitExtensionConfig config = getAkkaConfig(context);
    final ActorSystem system = ActorSystem.create(config.name());
    store.put(ActorSystem.class, system);
    if (config.materializer()) {
      store.put(ActorMaterializer.class, ActorMaterializer.create(system));
    }
  }

  private AkkaJunitExtensionConfig getAkkaConfig(ExtensionContext context) {
    return context.getElement()
      .filter(elem -> elem.isAnnotationPresent(AkkaJunitExtensionConfig.class))
      .map(elem -> elem.getAnnotation(AkkaJunitExtensionConfig.class))
      .orElse(DEFAULT_CONFIG);
  }

  private Store getStore(ExtensionContext context) {
    return context.getStore(AKKA_JUNIT);
  }

  @Override
  public void afterAll(ExtensionContext context) {
    final Store store = getStore(context);
    final AkkaJunitExtensionConfig config = getAkkaConfig(context);
    if (config.materializer()) {
      getActorMaterializer(store).shutdown();
    }
    TestKit.shutdownActorSystem(getActorSystem(store));
  }

  private ActorSystem getActorSystem(Store store) {
    return (ActorSystem) store.get(ActorSystem.class);
  }

  private ActorMaterializer getActorMaterializer(Store store) {
    return (ActorMaterializer) store.get(ActorMaterializer.class);
  }

  @Override
  public boolean supportsParameter(ParameterContext paramContext, ExtensionContext context) throws ParameterResolutionException {
    final Class<?> type = paramContext.getParameter().getType();
    return type.equals(ActorSystem.class) || type.equals(ActorMaterializer.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    final Class<?> parameterType = parameterContext.getParameter().getType();
    return extensionContext.getStore(AKKA_JUNIT).get(parameterType, parameterType);
  }
}
