package com.el3asas.note.ui.add_update

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import coil.compose.rememberAsyncImagePainter
import com.el3asas.note.R
import com.el3asas.note.data.models.NoteEntity
import com.el3asas.note.databinding.FragmentAddUpdateBinding
import com.el3asas.note.utils.FileUtils
import com.el3asas.utils.binding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddUpdateFragment(override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentAddUpdateBinding::inflate) :
    BottomSheetBinding<FragmentAddUpdateBinding>() {
    private val viewModel by viewModels<AddUpdateViewModel>()
    private val args by navArgs<AddUpdateFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mDialog = dialog as BottomSheetDialog
        mDialog.behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isFitToContents = false
        }
        super.onViewCreated(view, savedInstanceState)
        if (args.noteId != -1L) {
            lifecycleScope.launch {
                viewModel.addUpdateIntents.send(AddUpdateIntents.GetNoteData(args.noteId))
            }
        }

        binding.apply {
            mComposeContainer.setContent {
                AddScene()
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun AddScene() {
        val viewState by remember { viewModel.addUpdateViewStates }
        var imageUriForBottomSheet by remember { mutableStateOf(Uri.parse("")) }
        val scaffoldState = rememberBottomSheetScaffoldState()
        var noteColor by remember { mutableStateOf(Color(0xFFB2EC9A)) }
        var imagesList by remember { viewModel.images }
        var titleValue by remember { mutableStateOf("") }
        var noteId by remember { mutableStateOf<Long?>(null) }
        var addOrUpdateIconId by remember {
            if (args.noteId != -1L)
                mutableStateOf(R.drawable.ic_baseline_edit_24)
            else
                mutableStateOf(R.drawable.ic_round_add_24)
        }

        var descriptionValue by remember { mutableStateOf("") }
        if (viewState is AddUpdateViewStates.GetNoteData) {
            val savedEntity = (viewState as AddUpdateViewStates.GetNoteData)
            noteColor = Color(savedEntity.noteEntity.colorHex.toULong())
            imagesList = savedEntity.noteEntity.images?.map {
                Uri.parse(it)
            } as ArrayList<Uri>
            titleValue = savedEntity.noteEntity.title
            descriptionValue = savedEntity.noteEntity.description
            noteId = savedEntity.noteEntity.id
            LaunchedEffect(key1 = AddUpdateIntents.IdleView) {
                viewModel.addUpdateIntents.send(
                    AddUpdateIntents.IdleView
                )
            }
        } else if (viewState is AddUpdateViewStates.CompleteAddNote) {
            noteId = (viewState as AddUpdateViewStates.CompleteAddNote).noteId
            addOrUpdateIconId = R.drawable.ic_baseline_edit_24
        }

        BottomSheetScaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            backgroundColor = noteColor,
            sheetPeekHeight = 0.dp,
            sheetGesturesEnabled = true,
            sheetContent = {
                ShowImage(imageUri = imageUriForBottomSheet)
            }
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxHeight()
                    .padding(it)
            ) {
                val (header, title, description, images, bottomActionBar) = createRefs()
                Box(
                    Modifier
                        .padding(horizontal = 30.dp)
                        .constrainAs(header) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                        }) {
                    Row(horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(
                                Color(0xFF2196F3),
                                RoundedCornerShape(bottomEndPercent = 50, bottomStartPercent = 50)
                            )
                            .padding(8.dp)
                    ) {
                        IconButton(modifier = Modifier
                            .background(Color(0xFFB2EC9A), RoundedCornerShape(80.dp))
                            .border(
                                width = 1.dp,
                                Color(0xffffffff),
                                shape = RoundedCornerShape(80.dp)
                            ),
                            content = {}, onClick = {
                                noteColor = Color(0xFFB2EC9A)
                            })

                        Spacer(modifier = Modifier.padding(16.dp))
                        IconButton(modifier = Modifier
                            .background(Color(0xFFECC49A), RoundedCornerShape(80.dp))
                            .border(
                                width = 1.dp,
                                Color(0xffffffff),
                                shape = RoundedCornerShape(80.dp)
                            ),
                            content = {}, onClick = {
                                noteColor = Color(0xFFECC49A)
                            })

                        Spacer(modifier = Modifier.padding(16.dp))
                        IconButton(modifier = Modifier
                            .background(Color(0xFFEC9A9A), RoundedCornerShape(80.dp))
                            .border(
                                width = 1.dp,
                                Color(0xffffffff),
                                shape = RoundedCornerShape(80.dp)
                            ),
                            content = {},
                            onClick = {
                                noteColor = Color(0xFFEC9A9A)
                            })
                    }
                }

                TextField(
                    value = titleValue,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedLabelColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .constrainAs(title) {
                            top.linkTo(header.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    maxLines = 1,
                    enabled = viewState !is AddUpdateViewStates.Loading,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textDirection = TextDirection.ContentOrLtr
                    ),
                    onValueChange = { text: String ->
                        titleValue = text
                    },
                    placeholder = {
                        Text(text = "Enter Title")
                    })

                TextField(
                    value = descriptionValue,

                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedLabelColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    enabled = viewState !is AddUpdateViewStates.Loading,
                    modifier = Modifier
                        .background(Color.Transparent)
                        .fillMaxWidth()
                        .constrainAs(description) {
                            top.linkTo(title.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    singleLine = false,
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        textDirection = TextDirection.ContentOrLtr
                    ),
                    onValueChange = { text: String ->
                        descriptionValue = text
                    },
                    placeholder = {
                        Text(text = "Enter Description")
                    })

                val coroutineScope = rememberCoroutineScope()
                LazyRow(horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(images) {
                            bottom.linkTo(bottomActionBar.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {

                    items(imagesList) { imgUri ->
                        Image(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(CornerSize(23.dp)))
                                .size(100.dp, 100.dp)
                                .clickable {
                                    imageUriForBottomSheet = imgUri
                                    coroutineScope.launch {
                                        scaffoldState.bottomSheetState.expand()
                                    }
                                }
                                .padding(5.dp),
                            contentScale = ContentScale.Crop,
                            painter = rememberAsyncImagePainter(model = imgUri),
                            contentDescription = ""
                        )
                    }
                }

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x9EFFBEBE), RoundedCornerShape(8.dp))
                        .constrainAs(bottomActionBar) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }) {
                    val (btnAdd, imagesPickerBtn) = createRefs()

                    when (viewState) {
                        is AddUpdateViewStates.Loading -> {
                            CircularProgressIndicator(modifier = Modifier
                                .padding(16.dp)
                                .size(45.dp, 45.dp)
                                .constrainAs(btnAdd) {
                                    top.linkTo(parent.top)
                                    absoluteRight.linkTo(parent.absoluteRight)
                                })
                        }
                        else -> {
                            Image(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(45.dp, 45.dp)
                                    .constrainAs(btnAdd) {
                                        top.linkTo(parent.top)
                                        absoluteRight.linkTo(parent.absoluteRight)
                                    }
                                    .background(Color(0xFFFF7676), RoundedCornerShape(6.dp))
                                    .clickable(role = Role.Image) {
                                        lifecycleScope.launch {
                                            viewModel.addUpdateIntents.send(
                                                AddUpdateIntents.AddOrUpdateNote(
                                                    NoteEntity(
                                                        id = noteId,
                                                        title = titleValue,
                                                        description = descriptionValue,
                                                        colorHex = noteColor.value.toLong(),
                                                        date = SimpleDateFormat(
                                                            "dd/MM/yyyy HH:mm a",
                                                            Locale.ENGLISH
                                                        ).format(
                                                            Calendar.getInstance().time
                                                        ),
                                                        images = imagesList.map { it.toString() }
                                                    )
                                                )
                                            )
                                        }
                                    },
                                painter = painterResource(id = addOrUpdateIconId),
                                contentDescription = ""
                            )
                        }
                    }

                    Image(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(45.dp, 45.dp)
                            .constrainAs(imagesPickerBtn) {
                                top.linkTo(parent.top)
                                absoluteLeft.linkTo(parent.absoluteLeft)
                            }
                            .background(Color(0xFF7698FF), RoundedCornerShape(6.dp))
                            .clickable(role = Role.Image)
                            {
                                openGallery(imagesList)
                            },
                        painter = painterResource(id = R.drawable.ic_outline_image_24),
                        contentDescription = ""
                    )
                }
            }
        }
    }

    private fun openGallery(uriSelected: List<Uri>?) {
        FileUtils(requireContext())
        TedImagePicker.with(requireContext())
            .showCameraTile(true)
            .selectedUri(uriSelected)
            .startMultiImage { uriList ->
                lifecycleScope.launch {
                    viewModel.images.value = uriList
                }
            }
    }

    @Composable
    fun ShowImage(imageUri: Uri) {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Inside,
            painter = rememberAsyncImagePainter(model = imageUri),
            contentDescription = imageUri.toString()
        )
    }

}