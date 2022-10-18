package com.el3asas.note.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.viewbinding.ViewBinding
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.el3asas.note.databinding.RecyclerItemNoteBinding
import com.el3asas.utils.binding.RecyclerAdapterBinding

class HomeAdapter(override val bindingInflater: (LayoutInflater) -> ViewBinding = RecyclerItemNoteBinding::inflate) :
    RecyclerAdapterBinding<RecyclerItemNoteBinding>() {

    private var dataList = emptyList<NoteItemStateUi>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field=value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = dataList.size

    fun setData(list:List<NoteItemStateUi>){
        dataList = list
    }

    override fun onBindViewHolder(holder: MainViewHolder<RecyclerItemNoteBinding>, position: Int) {
        val noteItemStateUi = dataList[position]
        holder.binding.apply {
            item.apply {
                setContent {
                    SetUpRecyclerViewItem(noteItemStateUi = noteItemStateUi)
                }
            }
        }
    }

    @Composable
    private fun SetUpRecyclerViewItem(noteItemStateUi: NoteItemStateUi) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color(0x57FF8484)
        ) {
            ConstraintLayout {
                val (title, description, img) = createRefs()
                Text(
                    text = noteItemStateUi.title,
                    modifier = Modifier
                        .padding(8.dp)
                        .constrainAs(title) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        },
                    color = Color(0xB5000000),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = noteItemStateUi.description,
                    modifier = Modifier
                        .padding(8.dp, 5.dp, 8.dp, 8.dp)
                        .constrainAs(description) {
                            top.linkTo(title.bottom)
                            start.linkTo(parent.start)
                        },
                    color = Color(0x54000000),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                noteItemStateUi.imageString?.let {
                    AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(it)
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