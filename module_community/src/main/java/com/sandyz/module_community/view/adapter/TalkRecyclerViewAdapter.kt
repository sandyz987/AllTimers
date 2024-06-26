package com.sandyz.module_community.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sandyz.alltimers.common.config.Config
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.utils.TimeUtil
import com.sandyz.api_community.bean.DynamicItem
import com.sandyz.api_community.bean.findEquals
import com.sandyz.module_community.R
import com.sandyz.module_community.view.costom.CommentCountView
import com.sandyz.module_community.view.costom.LikeViewSlim


class TalkRecyclerViewAdapter(
    private var mContext: Context,
    private val onItemClick: (DynamicItem, View) -> Unit

) :
    RecyclerView.Adapter<TalkRecyclerViewAdapter.ViewHolder>() {

    private var mLayoutInflater = LayoutInflater.from(mContext)
    private var mList: ArrayList<DynamicItem> = arrayListOf()

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {

        when (viewType) {
            0 -> return ViewHolder(
                mLayoutInflater.inflate(
                    R.layout.item_talk_big,
                    container,
                    false
                )
            )
        }
        return ViewHolder(mLayoutInflater.inflate(R.layout.item_talk_big, container, false))
    }

    override fun getItemCount(): Int {

        return mList.size

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isPraise = mList[position].praise.findEquals { it.userId == Config.userId }
        holder.imageViewLikeView?.registerLikeView(
            mList[position].dynamicId,
            0,
            isPraise,
            mList[position].praise.size
        )


        holder.textViewUsrName?.text = mList[position].userId
        holder.textViewContent?.text = mList[position].text.take(30)

        holder.textViewContent?.text?.length?.let {
            if (it >= 30) {
                holder.textViewContent.text = holder.textViewContent.text.toString() + "..."
                holder.textViewMore?.visibility = View.VISIBLE
            } else {
                holder.textViewMore?.visibility = View.GONE
            }
        }

        holder.textViewTime?.text = TimeUtil.getChatTimeStr(mList[position].submitTime)
        if (mList[position].picUrl.isNotEmpty()) {
            holder.imageViewPic?.visibility = View.VISIBLE
            Glide.with(mContext).load(mList[position].picUrl[0]).thumbnail(0.2f)
                .into(holder.imageViewPic!!)
        } else {
            holder.imageViewPic?.visibility = View.GONE
        }
        holder.textViewUsrName?.text = mList[position].nickname




        holder.imageViewTalkView?.setHint(mList[position].commentList.size.toString())


        holder.imageViewUsrPic?.setOnClickListener {
            // TODO 帖子头像被点击
//            val navController = Navigation.findNavController(holder.itemView)
//            val bundle = Bundle()
//            bundle.putBoolean("isMine", false)
//            bundle.putString("userId", mList[position].userId)
//            navController.navigate(R.id.action_global_fragmentIndividual, bundle)
        }

        holder.itemView.isClickable = true
        holder.itemView.setOnClickAction {
            onItemClick.invoke(mList[position], holder.itemView)

        }
        holder.imageViewTalkView?.setOnClickListener {
            onItemClick.invoke(mList[position], it)
        }


    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUsrName: TextView? = itemView.findViewById<TextView>(R.id.textViewUsrName)
        val textViewTime: TextView? = itemView.findViewById<TextView>(R.id.textViewTime)
        val textViewContent: TextView? = itemView.findViewById<TextView>(R.id.textViewContent)
        val imageViewUsrPic: ImageView? = itemView.findViewById<ImageView>(R.id.imageViewUsrPic)
        val imageViewPic: ImageView? = itemView.findViewById<ImageView>(R.id.imageViewPic)
        val imageViewSex: ImageView? = itemView.findViewById<ImageView>(R.id.imageViewSex)
        val buttonFollow: Button? = itemView.findViewById<Button>(R.id.buttonFollow)
        val imageViewLikeView: LikeViewSlim? =
            itemView.findViewById<LikeViewSlim>(R.id.imageViewLike)
        val imageViewTalkView: CommentCountView? =
            itemView.findViewById<CommentCountView>(R.id.imageViewTalk)
        val textViewMore: TextView? = itemView.findViewById<TextView>(R.id.textViewMore)

    }

    fun setList(list: ArrayList<DynamicItem>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }


}