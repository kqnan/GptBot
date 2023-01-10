package me.kqn.gptbot.Bot

import me.kqn.gptbot.debug
import org.apache.http.Consts
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import taboolib.common5.Coerce
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
    var model: Model

    companion object{
        fun instance(api_key:String, model: Model, token_lenght:Int, defaultResponse:String): ChatGPT {
            return ChatGPT(api_key, model, token_lenght, defaultResponse)
        }
    }
    private constructor(api_key:String, model: Model, token_lenght:Int, defaultResponse:String){
        this.api_key=api_key
        this.model=model
        this.max_token=token_lenght
        this.defaultResponse=defaultResponse
    }
    override fun input(vararg text: String): Array<String> {
        debug(text[0])
        return mainChat(text[0]).toTypedArray()
    }
    private fun moderations(input:String):Boolean{
        // create the HTTP client
        val httpClient = HttpClients.createDefault()

        // create the HTTP POST request
        val request = HttpPost("https://api.openai.com/v1/moderations")
        request.addHeader("Content-Type", "application/json")
        request.addHeader("Authorization", "Bearer $api_key")

        val requestBody = StringEntity(
            "{" +
                    "  \"input\": \"${input}\""+
                    "}", Consts.UTF_8
        )
        request.entity=requestBody
        try {
            val response=httpClient.execute(request)
            val result=EntityUtils.toString(response.entity)
            debug(result)
            val json=Configuration.loadFromString(result, Type.JSON)
            val categories=((json.get("results") as List<*>)[0] as LinkedHashMap<*,*>).get("categories") as LinkedHashMap<*,*>
            var res=false
            categories.values.forEach {
                res=res.or(Coerce.toBoolean(it))
            }
            return res
        }catch (e:Exception){

        }
        return true
    }
    private fun mainChat(arg:String):List<String>{
        if(moderations(arg))return listOf(defaultResponse)
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
            debug(result)
            val json=Configuration.loadFromString(result, Type.JSON)
            val text=((json.get("choices") as List<*>)[0] as LinkedHashMap<*,*>).get("text") as String
            text.removePrefix("\n\n")
            return text!!.split("\n")
        }catch (e:Exception){
            //e.printStackTrace()
            return listOf(defaultResponse)
        }
    }
}