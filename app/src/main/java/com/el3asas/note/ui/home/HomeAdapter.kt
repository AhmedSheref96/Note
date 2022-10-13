package com.el3asas.note.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.viewbinding.ViewBinding
import com.el3asas.note.databinding.RecyclerItemNoteBinding
import com.el3asas.note.utils.stringToBitMap
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
        dataList=list
    }

    override fun onBindViewHolder(holder: MainViewHolder<RecyclerItemNoteBinding>, position: Int) {
        val noteItemStateUi = dataList[position]
        holder.binding.item.apply {
            setContent {
                setUpRecyclerViewItem(this, noteItemStateUi = noteItemStateUi)
            }
        }
    }

    private fun setUpRecyclerViewItem(composeView: ComposeView, noteItemStateUi: NoteItemStateUi) {
        val imageBitmap = stringToBitMap(noteItemStateUi.imageString)?.asImageBitmap()
        composeView.setContent {
            Surface {
                ConstraintLayout(Modifier.padding(16.dp)) {

                    val (title, description, img) = createRefs()
                    Text(text = noteItemStateUi.title, modifier = Modifier.constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                    Text(
                        text = noteItemStateUi.description,
                        modifier = Modifier.constrainAs(description) {
                            top.linkTo(title.bottom)
                            start.linkTo(parent.start)
                        },
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    imageBitmap?.let {
                        Image(
                            bitmap = it,
                            contentDescription = "",
                            modifier = Modifier.constrainAs(img) {
                                top.linkTo(description.bottom)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                            })
                    }

                }
            }
        }
    }

}