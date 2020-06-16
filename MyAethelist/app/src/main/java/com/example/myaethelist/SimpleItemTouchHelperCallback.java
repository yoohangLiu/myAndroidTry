package com.example.myaethelist;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaethelist.adapter.ItemTouchHelperAdapter;


public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final ItemTouchHelperAdapter mAdapter ;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;        //允许上下的拖动
        int swipeFlags = ItemTouchHelper.LEFT;   //只允许从右向左侧滑
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDissmiss(viewHolder.getAdapterPosition());
    }
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
    /**
     * 移动过程中绘制Item
     *
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     *          X轴移动的距离
     * @param dY
     *          Y轴移动的距离
     * @param actionState
     *          当前Item的状态
     * @param isCurrentlyActive
     *          如果当前被用户操作为true，反之为false
     */
   /* @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //滑动时自己实现背景及图片
        if (actionState==ItemTouchHelper.ACTION_STATE_SWIPE){

            //dX大于0时向右滑动，小于0向左滑动
            View itemView=viewHolder.itemView;//获取滑动的view
            Resources resources= ProductListRecyclerViewAdapter.getAppContext().getResources();
            currentIcon = getResources().getDrawable(R.drawable.folder);
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);//获取删除指示的背景图片
            int padding =10;//图片绘制的padding
            int maxDrawWidth=2*padding+bitmap.getWidth();//最大的绘制宽度
            Paint paint=new Paint();
            paint.setColor(resources.getColor(R.color.textColorPrimary));
            int x=Math.round(Math.abs(dX));
            int drawWidth=Math.min(x,maxDrawWidth);//实际的绘制宽度，取实时滑动距离x和最大绘制距离maxDrawWidth最小值
            int itemTop=itemView.getBottom()-itemView.getHeight();//绘制的top位置
            //向右滑动
            if(dX>0){
                //根据滑动实时绘制一个背景
                c.drawRect(itemView.getLeft(),itemTop,drawWidth,itemView.getBottom(),paint);
                //在背景上面绘制图片
                if (x>padding){//滑动距离大于padding时开始绘制图片
                    //指定图片绘制的位置
                    Rect rect=new Rect();//画图的位置
                    rect.left=itemView.getLeft()+padding;
                    rect.top=itemTop+(itemView.getBottom()-itemTop-bitmap.getHeight())/2;//图片居中
                    int maxRight=rect.left+bitmap.getWidth();
                    rect.right=Math.min(x,maxRight);
                    rect.bottom=rect.top+bitmap.getHeight();
                    //指定图片的绘制区域
                    Rect rect1=null;
                    if (x<maxRight){
                        rect1=new Rect();//不能再外面初始化，否则dx大于画图区域时，删除图片不显示
                        rect1.left=0;
                        rect1.top = 0;
                        rect1.bottom=bitmap.getHeight();
                        rect1.right=x-padding;
                    }
                    c.drawBitmap(bitmap,rect1,rect,paint);
                }
                //绘制时需调用平移动画，否则滑动看不到反馈
                itemView.setTranslationX(dX);
            }else {
                //如果在getMovementFlags指定了向左滑动（ItemTouchHelper。START）时则绘制工作可参考向右的滑动绘制，也可直接使用下面语句交友系统自己处理
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }else {
            //拖动时有系统自己完成
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }*/
}
