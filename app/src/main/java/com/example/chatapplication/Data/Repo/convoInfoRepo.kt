    package com.example.chatapplication.Data.Repo

    import com.example.chatapplication.Data.DAO.conversationId
    import com.example.chatapplication.Data.local.tables.CinversationId
    import com.example.chatapplication.Data.network.ApiService
    import com.example.chatapplication.Data.network.request.ConversationRequest
    import com.example.chatapplication.Data.network.request.conversationIdRequest
    import com.example.chatapplication.Data.network.request.getOneConversation
    import com.example.chatapplication.Data.network.response.ConversationResponse
    import io.github.jan.supabase.realtime.PostgresAction
    import kotlinx.serialization.json.jsonPrimitive
    import retrofit2.Response

    class convoInfoRepo(
        private var work: conversationId,
        private var api: ApiService
    ) {
        var getingConvoInfo=work.getAllConvoInfo()

        val privateConversations = work.getPrivateConversations()

        val groupConversations = work.getGroupConversations()

    //    suspend fun insertConvoInfo(info: CinversationId){
    //        work.putingInfo()
    //    }

        suspend fun syncConversations(id:String) {
            val request = conversationIdRequest(
                p_user_id = id
            )
            val response = api.getConvoInfo(request)
            if (response.isSuccessful) {
    //            val conversations = response.body() ?: emptyList()
                val conversations = response.body() ?: emptyList()
                    val localConversation = conversations.map{convo->
                        CinversationId(
                            conversationId = convo.conversation_id,
                            type = convo.type,
                            name = convo.name,
                            lastMessage = convo.lastMessage,
                            lastTime = convo.lastTime,
                            Image = convo.Image,
                            last_message_id  = convo.last_message_id,
                            unread_count= convo.unread_count
                        )
                    }
                    work.putingInfo(localConversation)
                }
             else {
                println("CONVERSATION SYNC ERROR = ${response.errorBody()?.string()}")
            }
        }
        suspend fun getConvoInfo(
            conversationId: String
        ): Response<List<ConversationResponse>> {
            return api.getConvoInfo(
                conversationIdRequest(conversationId)
            )
        }
        suspend fun getConversationById(
            conversationId: String
        ): Response<List<ConversationResponse>> {
            return api.getConversationById(
                getOneConversation(conversationId)
            )
        }
        suspend fun handleConversationMemberEvent(
            event: PostgresAction.Insert,
            myUserId: String
        ) {
            println("========== REALTIME GROUP DEBUG ==========")

            val record = event.record

            val userId =
                record["user_id"]?.jsonPrimitive?.content

            val conversationId =
                record["conversation_id"]?.jsonPrimitive?.content

            println("REALTIME GROUP: EVENT USER = $userId")
            println("REALTIME GROUP: MY USER  = $myUserId")
            println("REALTIME GROUP: CONVERSATION ID = $conversationId")

            if (userId != myUserId) {
                println("REALTIME GROUP: NOT MY MEMBERSHIP EVENT")
                println("=========================================")
                return
            }

            if (conversationId.isNullOrBlank()) {
                println("REALTIME GROUP: CONVERSATION ID IS NULL")
                println("=========================================")
                return
            }

            println("REALTIME GROUP: FETCHING CONVERSATION")

            val response = getConversationById(conversationId)

            println("REALTIME GROUP: API CODE = ${response.code()}")
            println("REALTIME GROUP: API BODY = ${response.body()}")
            println(
                "REALTIME GROUP: API ERROR = ${
                    response.errorBody()?.string()
                }"
            )

            if (!response.isSuccessful) {
                println("REALTIME GROUP: API FAILED")
                println("=========================================")
                return
            }

            val conversations = response.body() ?: emptyList()

            println(
                "REALTIME GROUP: CONVERSATIONS RETURNED = ${conversations.size}"
            )

            if (conversations.isEmpty()) {
                println("REALTIME GROUP: API RETURNED EMPTY LIST")
                println("=========================================")
                return
            }

            val localConversation = conversations.map { convo ->

                println(
                    "REALTIME GROUP: INSERTING CONVERSATION = " +
                            "${convo.conversation_id}, " +
                            "type=${convo.type}, " +
                            "name=${convo.name}"
                )

                CinversationId(
                    conversationId = convo.conversation_id,
                    type = convo.type,
                    name = convo.name,
                    lastMessage = convo.lastMessage,
                    lastTime = convo.lastTime,
                    Image = convo.Image,
                    last_message_id = convo.last_message_id,
                    unread_count = convo.unread_count
                )
            }

            println(
                "REALTIME GROUP: PUTTING ${localConversation.size} INTO ROOM"
            )

            work.putingInfo(localConversation)

            println("REALTIME GROUP: ROOM UPSERT COMPLETE")
            println("=========================================")
        }


            suspend fun updateLastMessage(
                conversationId: String,
                messageId: String,
                message: String,
                time: String
            ) {
                work.updateLastMessage(
                    conversationId = conversationId,
                    messageId = messageId,
                    message = message,
                    time = time
                )
            }

    }