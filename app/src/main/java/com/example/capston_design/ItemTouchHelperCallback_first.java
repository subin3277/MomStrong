package com.example.capston_design;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

enum ButtonsState_first{
    GONE, LEFT_VISIBLE, RIGHT_VISIBLE
}
public class ItemTouchHelperCallback_first extends ItemTouchHelper.Callback{
    private ItemTouchHelperListener listener;
    private boolean swipeBack = false;
    private ButtonsState_first buttonsShowedState = ButtonsState_first.GONE;
    private static final float buttonWidth = 115;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currenrtItemViewHolder = null;
    public ItemTouchHelperCallback_first(ItemTouchHelperListener listener) {
        this.listener = listener;
    }
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder) {
        int drag_flags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        int swipe_flags = ItemTouchHelper.START|ItemTouchHelper.END;
        return makeMovementFlags(drag_flags,swipe_flags);
    }
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return listener.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
    }
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onItemSwipe(viewHolder.getAdapterPosition());
    }
    //아이템을 터치하거나 스와이프하거나 뷰에 변화가 생길경우 불러오는 함수
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        //아이템이 스와이프 됐을경우 버튼을 그려주기 위해서 스와이프가 됐는지 확인
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if(buttonsShowedState != ButtonsState_first.GONE){
                if(buttonsShowedState == ButtonsState_first.LEFT_VISIBLE)
                    dX = Math.max(dX, buttonWidth);
                if(buttonsShowedState == ButtonsState_first.RIGHT_VISIBLE)
                    dX = Math.min(dX, -buttonWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }else{ setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            if(buttonsShowedState == ButtonsState_first.GONE){
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive); }
        } currenrtItemViewHolder = viewHolder;
        //버튼을 그려주는 함수
        drawButtons(c, currenrtItemViewHolder);
    }
    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithOutPadding = buttonWidth - 10;
        float corners = 5;
        View itemView = viewHolder.itemView;
        Paint p = new Paint();
        buttonInstance = null;
        //오른쪽으로 스와이프 했을때 (왼쪽에 버튼이 보여지게 될 경우)
        if (buttonsShowedState == ButtonsState_first.LEFT_VISIBLE) {
            RectF leftButton = new RectF(itemView.getLeft() + 10, itemView.getTop() + 10,
                    itemView.getLeft() + buttonWidthWithOutPadding, itemView.getBottom() - 10);
            p.setColor(Color.BLUE);
            c.drawRoundRect(leftButton, corners, corners, p);
            drawText("삭제", c, leftButton, p);
            buttonInstance = leftButton;

            //왼쪽으로 스와이프 했을때 (오른쪽에 버튼이 보여지게 될 경우)
        } else if (buttonsShowedState == ButtonsState_first.RIGHT_VISIBLE) {
            RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithOutPadding,
                    itemView.getTop() + 10, itemView.getRight() - 10, itemView.getBottom() - 10);
            p.setColor(Color.RED);
            c.drawRoundRect(rightButton, corners, corners, p);
            drawText("삭제", c, rightButton, p);
            buttonInstance = rightButton;
        }
    }
    //버튼의 텍스트 그려주기
    private void drawText(String text, Canvas c, RectF button, Paint p){
        float textSize = 25; p.setColor(Color.WHITE);
        p.setAntiAlias(true); p.setTextSize(textSize);
        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX() - (textWidth/2), button.centerY() + (textSize/2), p); }
    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if(swipeBack){
            swipeBack = false; return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(final Canvas c, final RecyclerView recyclerView,
                                  final RecyclerView.ViewHolder viewHolder, final float dX, final float dY,
                                  final int actionState, final boolean isCurrentlyActive){
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if(swipeBack){ if(dX < -buttonWidth) buttonsShowedState = ButtonsState_first.RIGHT_VISIBLE;
                else if(dX > buttonWidth) buttonsShowedState = ButtonsState_first.LEFT_VISIBLE;
                    if(buttonsShowedState != ButtonsState_first.GONE){
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                } return false;
            }
        });
    } @SuppressLint("ClickableViewAccessibility")
    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView ,
                                      final RecyclerView.ViewHolder viewHolder, final float dX,
                                      final float dY , final int actionState,
                                      final boolean isCurrentlyActive){
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                } return false;
            }
        });
    } @SuppressLint("ClickableViewAccessibility")
    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView ,
                                    final RecyclerView.ViewHolder viewHolder, final float dX,
                                    final float dY , final int actionState, final boolean isCurrentlyActive){
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ItemTouchHelperCallback_first.super.onChildDraw(c, recyclerView, viewHolder,
                        0F, dY, actionState, isCurrentlyActive);
                recyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                }); setItemsClickable(recyclerView, true);
                swipeBack = false;
                if(listener != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())){
                    if(buttonsShowedState == ButtonsState_first.LEFT_VISIBLE){
                        listener.onLeftClick(viewHolder.getAdapterPosition(), viewHolder);
                        //new DietActivity.GetSide1().execute();
                        DietActivity.dietAdapter_first.addItem(DietActivity.side1list.get(DietActivity.side1index));
                        DietActivity.recyclerViewfirst.setAdapter(DietActivity.dietAdapter_first);
                        DietActivity.firstname=DietActivity.firstitem.getName();
                        DietActivity.firstlist.add(DietActivity.firstname);
                        DietActivity.side1index +=1 ;
                    }else if(buttonsShowedState == ButtonsState_first.RIGHT_VISIBLE){
                        listener.onRightClick(viewHolder.getAdapterPosition(), viewHolder);
                        //new DietActivity.GetSide1().execute();
                        DietActivity.dietAdapter_first.addItem(DietActivity.side1list.get(DietActivity.side1index));
                        DietActivity.recyclerViewfirst.setAdapter(DietActivity.dietAdapter_first);
                        DietActivity.firstname=DietActivity.firstitem.getName();
                        DietActivity.firstlist.add(DietActivity.firstname);
                        DietActivity.side1index +=1 ;
                    }
                } buttonsShowedState = ButtonsState_first.GONE; currenrtItemViewHolder = null;
                return false;
            }
        });
    } private void setItemsClickable(RecyclerView recyclerView, boolean isClickable){
        for(int i = 0; i < recyclerView.getChildCount(); i++){
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }
}
