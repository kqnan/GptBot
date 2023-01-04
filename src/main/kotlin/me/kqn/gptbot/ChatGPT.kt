package me.kqn.gptbot

import org.apache.http.Consts
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.bukkit.Bukkit
import taboolib.common.util.asList
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type

class ChatGPT : IChatBot {
    enum class  Model(id:String){
        TEXT_DAVINCI_003("text-davinci-003"),
        TEXT_DAVINCI_002("text-davinci-002"),
        TEXT_DAVINCI_001("text-davinci-001"),
        DAVINCI("davinci");
        val id=id
    }
    var defaultResponse:String
    var max_token: Int
    var api_key: String
    var model:Model
    constructor(api_key:String,model:Model,token_lenght:Int,defaultResponse:String){
        this.api_key=api_key
        this.model=model
        this.max_token=token_lenght
        this.defaultResponse=defaultResponse
    }
    override fun input(vararg text: String): Array<String> {
        return mainChat(text[0]).toTypedArray()
    }
    private fun mainChat(arg:String):List<String>{
        // create the HTTP client
        val httpClient = HttpClients.createDefault()

        // create the HTTP POST request
        val request = HttpPost("https://api.openai.com/v1/completions")
        request.addHeader("Content-Type", "application/json")
        request.addHeader("Authorization", "Bearer $api_key")

        // create the request body as a JSON object


        val requestBody = StringEntity(
            "{" +
                    "  \"model\": \"${model.id}\"," +
                    "  \"prompt\": \"$arg\"," +
                    "  \"max_tokens\": $max_token" +
                    "}", Consts.UTF_8
        )
        request.entity = requestBody
        // send the request and get the response


        try {
            val response = httpClient.execute(request)
            val result = EntityUtils.toString(response.entity)
            Bukkit.getLogger().info(result)
            val json=Configuration.loadFromString(result, Type.JSON)
            val text=((json.get("choices") as List<*>)[0] as LinkedHashMap<*,*>).get("text") as String
            text.removePrefix("\n\n")
            return text!!.split("\n")
        }catch (e:Exception){
            e.printStackTrace()
            return listOf(defaultResponse)
        }
    }
}