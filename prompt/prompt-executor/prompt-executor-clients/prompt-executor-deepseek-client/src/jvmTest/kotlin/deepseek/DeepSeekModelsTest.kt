package deepseek

import ai.koog.prompt.executor.clients.deepseek.DeepSeekModels
import ai.koog.prompt.executor.clients.list
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import kotlin.test.Test
import kotlin.test.assertSame

class DeepSeekModelsTest {

    @Test
    fun `DeepSeek models should have DeepSeek provider`() {
        val models = DeepSeekModels.list()

        models.forEach { model ->
            assertSame(
                expected = LLMProvider.DeepSeek,
                actual = model.provider,
                message = "DeepSeek model ${model.id} doesn't have DeepSeek provider but ${model.provider}."
            )
        }
    }

    @Test
    fun `GoogleModels models should return all declared models`() {
        val reflectionModels = DeepSeekModels.list().map { it.id }

        val models = DeepSeekModels.models.map { it.id }

        assert(models.size == reflectionModels.size)

        reflectionModels.forEach { model ->
            models shouldContain model
        }
    }

    @Test
    fun `DeepSeek models should include V4_1 Flash entry`() {
        DeepSeekModels.models.map { it.id } shouldContain "deepseek-v4.1-flash"
    }

    @Test
    fun `DeepSeek legacy v4 models should all be deprecated`() {
        val legacyModelIds = listOf(
            "deepseek-v4-flash",
            "deepseek-v4-pro",
        )

        DeepSeekModels.models.map { it.id } shouldContainAll legacyModelIds

        val deprecatedModelIds = DeepSeekModels::class.memberProperties
            .filter { it.findAnnotation<Deprecated>() != null }
            .mapNotNull { (it.javaField?.get(null) as? LLModel)?.id }

        deprecatedModelIds shouldContainAll legacyModelIds
    }
}
