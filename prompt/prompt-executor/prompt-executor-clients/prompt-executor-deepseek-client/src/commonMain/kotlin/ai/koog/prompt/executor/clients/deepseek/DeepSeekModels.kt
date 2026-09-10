package ai.koog.prompt.executor.clients.deepseek

import ai.koog.prompt.executor.clients.LLModelDefinitions
import ai.koog.prompt.executor.clients.deepseek.DeepSeekModels.DeepSeekV4Flash
import ai.koog.prompt.executor.clients.deepseek.DeepSeekModels.DeepSeekV4Pro
import ai.koog.prompt.executor.clients.deepseek.DeepSeekModels.DeepSeekV4_1Flash
import ai.koog.prompt.llm.LLMCapability
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel
import kotlin.collections.plus
import kotlin.jvm.JvmField

/**
 * Object containing a collection of predefined DeepSeek model configurations.
 *
 * DeepSeek provides powerful language models with competitive pricing and advanced reasoning capabilities.
 * All models support JSON output, function calling, and chat prefix completion features.
 *
 * | Name                | Speed  | Price                | Input       | Output      |
 * |---------------------|--------|----------------------|-------------|-------------|
 * | [DeepSeekV4_1Flash] | Fast   | ¥1 / ¥4 per 1M       | Text, Tools | Text, Tools |
 * | [DeepSeekV4Flash]   | Fast   | $0.14 / $0.28 per 1M | Text, Tools | Text, Tools |
 * | [DeepSeekV4Pro]     | Medium | Deprecated           | Text, Tools | Text, Tools |
 *
 * [DeepSeekV4_1Flash] is the recommended model. It is billed at ¥1 / ¥4 per 1M tokens during off-peak
 * hours and ¥2 / ¥8 per 1M tokens during peak hours (Mon-Fri 9:00-12:00 and 14:00-18:00 Beijing time),
 * with cached input charged at ¥0.02 / ¥0.04 per 1M tokens.
 *
 * @see <a href="https://platform.deepseek.com/api-docs/pricing">DeepSeek Pricing Documentation</a>
 */
public object DeepSeekModels : LLModelDefinitions {

    /**
     * DeepSeek V4.1 Flash model optimized for fast, cost-effective generation.
     * It supersedes [DeepSeekV4Pro] and supports both thinking and non-thinking modes in the DeepSeek API.
     *
     * @see <a href="https://api-docs.deepseek.com/api/create-chat-completion/">Chat Completion API</a>
     */
    @JvmField
    public val DeepSeekV4_1Flash: LLModel = LLModel(
        provider = LLMProvider.DeepSeek,
        id = "deepseek-v4.1-flash",
        capabilities = listOf(
            LLMCapability.Completion,
            LLMCapability.Temperature,
            LLMCapability.Tools,
            LLMCapability.ToolChoice,
            LLMCapability.Schema.JSON.Basic,
            LLMCapability.Schema.JSON.Standard,
            LLMCapability.MultipleChoices,
            LLMCapability.Thinking,
        ),
        contextLength = 1_000_000,
        maxOutputTokens = 384_000
    )

    /**
     * DeepSeek V4 Flash model optimized for fast, cost-effective generation.
     * Supports both thinking and non-thinking modes in the DeepSeek API.
     *
     * @see <a href="https://api-docs.deepseek.com/api/create-chat-completion/">Chat Completion API</a>
     */
    @JvmField
    public val DeepSeekV4Flash: LLModel = LLModel(
        provider = LLMProvider.DeepSeek,
        id = "deepseek-v4-flash",
        capabilities = listOf(
            LLMCapability.Completion,
            LLMCapability.Temperature,
            LLMCapability.Tools,
            LLMCapability.ToolChoice,
            LLMCapability.Schema.JSON.Basic,
            LLMCapability.Schema.JSON.Standard,
            LLMCapability.MultipleChoices,
            LLMCapability.Thinking,
        ),
        contextLength = 1_000_000,
        maxOutputTokens = 384_000
    )

    /**
     * DeepSeek V4 Pro model optimized for advanced reasoning and agentic tasks.
     * DeepSeek routes all V4 Pro requests to [DeepSeekV4_1Flash] and bills them at V4.1 Flash rates.
     *
     * @see <a href="https://api-docs.deepseek.com/api/create-chat-completion/">Chat Completion API</a>
     */
    @Deprecated(
        message = "Use DeepSeekV4_1Flash instead. DeepSeek routes V4 Pro requests to V4.1 Flash.",
        replaceWith = ReplaceWith("DeepSeekV4_1Flash")
    )
    @JvmField
    public val DeepSeekV4Pro: LLModel = LLModel(
        provider = LLMProvider.DeepSeek,
        id = "deepseek-v4-pro",
        capabilities = listOf(
            LLMCapability.Completion,
            LLMCapability.Temperature,
            LLMCapability.Tools,
            LLMCapability.ToolChoice,
            LLMCapability.Schema.JSON.Basic,
            LLMCapability.Schema.JSON.Standard,
            LLMCapability.MultipleChoices,
            LLMCapability.Thinking,
        ),
        contextLength = 1_000_000,
        maxOutputTokens = 384_000
    )

    /**
     * List of the supported models by the DeepSeek provider.
     */
    private val supportedModels: List<LLModel> = listOf(
        DeepSeekV4_1Flash,
        DeepSeekV4Flash,
        DeepSeekV4Pro,
    )

    /**
     * List of custom models added to the DeepSeek provider.
     */
    private val customModels: MutableList<LLModel> = mutableListOf()

    override val models: List<LLModel>
        get() = supportedModels + customModels

    override fun addCustomModel(model: LLModel) {
        require(model.provider == LLMProvider.DeepSeek) { "Model provider must be DeepSeek" }
        customModels.add(model)
    }
}
