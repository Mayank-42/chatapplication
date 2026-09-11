package com.example.chatapplication.ui.Screen.Main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.core.content.ContextCompat

private val ChatBlack = Color(0xFF000000)
private val ChatWhite = Color(0xFFFFFFFF)
private val ChatBlue = Color(0xFF3B82F6)
private val ChatMuted = Color(0xFF9CA3AF)
private val ChatIncoming = Color(0xFFF4F4F4)
private val ChatInput = Color(0xFFFFFFFF)

@Composable
fun ShowMicPage(closeSpeachBox:()->Unit){

    var isClicked by rememberSaveable{mutableStateOf(false)}

    var Texted by rememberSaveable{mutableStateOf("")}

    var finalText by rememberSaveable { mutableStateOf("") }

    var IconClicked by rememberSaveable{mutableStateOf(false)}
    var DefaultMsg= if(isClicked) "पाठ लिखने के लिए बोलें" else "Speak for writing the text.."
    var isListening by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isEditing by rememberSaveable { mutableStateOf(false) }

    var textFieldValue by remember { mutableStateOf(
        TextFieldValue(
                text = Texted,
                selection = TextRange(Texted.length)
            )
        )
    }

    val audioLevel = remember {
        mutableStateOf(0f)
    }
//    val micScale by androidx.compose.animation.core.animateFloatAsState(
//        targetValue = if (IconClicked) {
//            1f + (audioLevel * 0.8f)
//        } else {
//            1f
//        },
//        animationSpec = androidx.compose.animation.core.tween(
//            durationMillis = 100
//        ),
//        label = "micScale"
//    )
    val microphonePermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                IconClicked = true
            }
        }
    val speechRecognizer = remember {
        SpeechRecognizer.createSpeechRecognizer(context)
    }

    DisposableEffect(Unit) {
        onDispose {
            speechRecognizer.destroy()
        }
    }
    val recognitionListener = remember {
        object : RecognitionListener {

            override fun onReadyForSpeech(params: Bundle?) {
                isListening = true
                IconClicked = true

                println("SPEECH: Ready")
            }

            override fun onBeginningOfSpeech() {
                println("SPEECH: Started")
            }

            override fun onRmsChanged(rmsdB: Float) {

                val level = ((rmsdB + 2f) / 12f)
                    .coerceIn(0f, 1f)

                audioLevel.value = level

                println("RMS: $rmsdB LEVEL: $level")
            }
            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                IconClicked = false
                audioLevel.value = 0f
                println("SPEECH: Ended")
            }

            override fun onError(error: Int) {
                IconClicked = false
                audioLevel.value = 0f
                println("SPEECH: Error = $error")
            }
            override fun onResults(results: Bundle?) {

                val matches = results?.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION
                )

                if (!matches.isNullOrEmpty()) {

                    val newText = matches[0]

                    finalText = if (finalText.isBlank()) {
                        newText
                    } else {
                        "$finalText $newText"
                    }

                    Texted = finalText

                    textFieldValue = TextFieldValue(
                        text = finalText,
                        selection = TextRange(finalText.length)
                    )

                    println("SPEECH FINAL: $finalText")
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {

                val matches = partialResults?.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION
                )

                if (!matches.isNullOrEmpty()) {

                    val partialText = if (finalText.isBlank()) {
                        matches[0]
                    } else {
                        "$finalText ${matches[0]}"
                    }

                    Texted = partialText

                    textFieldValue = TextFieldValue(
                        text = partialText,
                        selection = TextRange(partialText.length)
                    )

                    println("SPEECH PARTIAL: $partialText")
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }
        fun startSpeechRecognition() {

            speechRecognizer.setRecognitionListener(recognitionListener)

            val intent = Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH
            ).apply {
                putExtra(
                    android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )

                putExtra(
                    android.speech.RecognizerIntent.EXTRA_LANGUAGE,
                    if (isClicked) {
                        "hi-IN"
                    } else {
                        "en-IN"
                    }
                )

                putExtra(
                    android.speech.RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                    true
                )
            }

            speechRecognizer.startListening(intent)
        }


//    Box(modifier=Modifier.fillMaxSize().background(Color.Black)){
    Box(
        modifier = Modifier
            .fillMaxWidth(0.88f)
            .height(520.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
    ) {
            Column() {
                IconButton(onClick = {closeSpeachBox()}){
                    Icon(
                        imageVector=Icons.Default.Cancel,
                        contentDescription = null,

                        )
                }

                Spacer(modifier = Modifier.height(50.dp))
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .clip(RoundedCornerShape(50.dp))
                        .background(ChatBlue)
                ) {
                    Row(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Hindi",
                            modifier = Modifier.clickable {
                                isClicked = true
                            } .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isClicked) {
                                        Color.Magenta

                                    } else {
                                        Color.Transparent
                                    }
                                ) .padding(5.dp),
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Text(
                            text = "English",
                            modifier = Modifier.clickable {
                                isClicked = false
                            }
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isClicked) {

                                        Color.Transparent
                                    } else {
                                        Color.Magenta
                                    }
                                )
                                .padding(5.dp),
                            color = Color.White,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start=20.dp,end=20.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .border(1.dp,Color.Black,RoundedCornerShape(30.dp)),
//                        .background(Color.Cyan),
                    contentAlignment=Alignment.Center
                ) {
                    TextField(
                        value = textFieldValue,
                        onValueChange = {
                            textFieldValue = it
                            Texted = it.text},
                        modifier = Modifier.focusRequester(focusRequester),
                        readOnly = !isEditing,
                        placeholder = {
                            Text(text=DefaultMsg)
                        },
                        maxLines = 10,

                        colors= TextFieldDefaults.colors(
                            unfocusedContainerColor = ChatWhite,
                            focusedContainerColor = ChatWhite,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,

                            )
                    )
                }
                LaunchedEffect(isEditing) {
                    if (isEditing) {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }
                }
//                Spacer(modifier=Modifier.height(10.dp))
                Row(modifier=Modifier.fillMaxWidth().padding(end=40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ){
                    Text(
                        text = "Clear",
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .clickable {
                                Texted = ""
                                finalText = ""
                                textFieldValue = TextFieldValue("")
                                keyboardController?.hide()
                                isEditing = false
                            },
                        color = ChatBlue
                    )
                    Text(
                        text = "Edit",
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .clickable {
                                isEditing = true
                            },
                        color = ChatBlue
                    )
                    Text(text="Send")
                }
                val audioLevel = audioLevel.value

                Box(
                    modifier = Modifier
                        .size(180.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .size(
                                80.dp + (audioLevel * 80.dp)
                            )
                            .border(
                                width = 4.dp,
                                color = ChatBlue,
                                shape = CircleShape
                            )
                    )

                    IconButton(
                        onClick = {
                            if (
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                if (IconClicked) {
                                    speechRecognizer.stopListening()
                                    IconClicked = false
                                } else {
                                    IconClicked = true
                                    startSpeechRecognition()
                                }
                            } else {
                                microphonePermissionLauncher.launch(
                                    Manifest.permission.RECORD_AUDIO
                                )
                            }
                        },
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(
                            imageVector = if (IconClicked) {
                                Icons.Default.Mic
                            } else {
                                Icons.Default.MicOff
                            },
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
            }
                }
            }




//}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun showScreen(){
     ShowMicOnChatScreen({},{})
}