package com.el3asas.note.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.viewbinding.ViewBinding
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.el3asas.note.R
import com.el3asas.note.databinding.RecyclerItemNoteBinding
import com.el3asas.utils.binding.RecyclerAdapterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class HomeAdapter(
    private val coroutineScope: CoroutineScope,
    private val noteItemIntents: Channel<NoteItemIntents>,
    override val bindingInflater: (LayoutInflater) -> ViewBinding = RecyclerItemNoteBinding::inflate
) : RecyclerAdapterBinding<RecyclerItemNoteBinding>() {

    private var dataList = ArrayList<NoteItemStateUi>()

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<NoteItemStateUi>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MainViewHolder<RecyclerItemNoteBinding>, position: Int) {
        val noteItemStateUi = dataList[position]
        holder.binding.apply {
            item.apply {
                setContent {
                    SetUpRecyclerViewItem(
                        holder.binding.root,
                        position,
                        noteItemStateUi = noteItemStateUi
                    )
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteNote(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun pinNote(position: Int) {
        dataList.add(0, dataList.removeAt(position))
        notifyDataSetChanged()
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun SetUpRecyclerViewItem(
        v: View,
        position: Int,
        noteItemStateUi: NoteItemStateUi
    ) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .combinedClickable(
                    onLongClick = {
                        pinNote(position)
                    },
                    onClick = {
                        coroutineScope.launch {
                            noteItemIntents.send(
                                NoteItemIntents.OpenNote(
                                    v,
                                    noteItemStateUi.id
                                )
                            )
                        }
                    }
                ),
            shape = RoundedCornerShape(8.dp),
            color = Color(noteItemStateUi.colorHex.toULong())
        ) {
            ConstraintLayout {
                val (title, description, img, deleteBtn) = createRefs()
                Text(
                    text = noteItemStateUi.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(title) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(deleteBtn.start)
                        }
                        .padding(24.dp, 8.dp, 8.dp, 8.dp),
                    color = Color(0xB5000000),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )

                Text(
                    text = noteItemStateUi.description,
                    modifier = Modifier
                        .padding(8.dp, 5.dp, 8.dp, 8.dp)
                        .constrainAs(description) {
                            top.linkTo(title.bottom)
                            start.linkTo(parent.start)
                        },
                    maxLines = 3,
                    color = Color(0x54000000),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "delete",
                    modifier = Modifier
                        .padding(8.dp)
                        .constrainAs(deleteBtn) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        .clickable(role = Role.Image) {
                            deleteNote(position)
                            coroutineScope.launch {
                                noteItemIntents.send(NoteItemIntents.DeleteNote(noteItemStateUi.id))
                            }
                        }
                )

                noteItemStateUi.imageString?.let {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(it)
                            .crossfade(true).build(),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .constrainAs(img) {
                                top.linkTo(description.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .clip(
                                CircleShape.copy(
                                    topStart = CornerSize(0.dp),
                                    topEnd = CornerSize(0.dp),
                                    CornerSize(8.dp),
                                    CornerSize(8.dp)
                                )
                            ),
                        contentDescription = "note image")
                }
            }
        }
    }

}