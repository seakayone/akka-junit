package de.dynamight.akka.junit;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(AkkaJunitExtension.class)
@AkkaJunitExtensionConfig()
class AkkaJunitExtensionWithMaterializerTest {
  
  @Test
  void actorsystem_has_default_name(ActorSystem system) {
    assertThat(system.name()).isEqualTo("default");
  }

  @Test
  void actormaterializer_is_present(ActorMaterializer materializer) {
    assertThat(materializer).isNotNull();
  }

}
