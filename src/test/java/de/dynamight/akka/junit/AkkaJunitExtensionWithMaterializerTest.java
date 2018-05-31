package de.dynamight.akka.junit;

import akka.stream.ActorMaterializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(AkkaJunitExtension.class)
@AkkaJunitExtensionConfig(name = "test")
class AkkaJunitExtensionWithMaterializerTest {

  @Test
  void actormaterializer_is_present(ActorMaterializer materializer) {
    assertThat(materializer).isNotNull();
  }

}
