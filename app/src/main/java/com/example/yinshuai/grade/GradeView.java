package com.example.yinshuai.grade;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by yinshuai on 2017/7/7.
 *  1.可以设置星星的数目
 *  2.可以设置选中的数目
 *  3.可以替换选中或不选中的图片
 *  4.定制化高
 */
public class GradeView extends ViewGroup {

    private Bitmap yes;//选中的图片
    private Bitmap no;//没选中的图片
    private Context mContext;
    private int number;//星星的个数
    private OnGradeClickItemListener ongradeClickItemListener;
    private int count;//当前有多少个子view
    private int value=0;//当前选中的位置
    private boolean isOnclick;//是否可点击
    private int selectImage;//选中的图片资源引用
    private int noselectImage;//没选中的图片资源引用

    public GradeView(Context context) {
        super(context);
        this.mContext = context;
        initBitmap(0,0);//初始化图片资源文件
        initImageView();//初始化图片控件
        setWillNotDraw(false);
    }

    public GradeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        //获取自定义属性
        TypedArray typedArray=mContext.obtainStyledAttributes(attrs,R.styleable.Grade);
        isOnclick=typedArray.getBoolean(R.styleable.Grade_isOnclick,false);
        number=typedArray.getInt(R.styleable.Grade_number,5);
        value=typedArray.getInt(R.styleable.Grade_selectValue,0);
        selectImage=typedArray.getResourceId(R.styleable.Grade_selectImage,0);
        noselectImage=typedArray.getResourceId(R.styleable.Grade_noSelectImage,0);

        initBitmap(selectImage,noselectImage);
        initImageView();
        setWillNotDraw(false);
    }

    /**
     * 宽高测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode==MeasureSpec.EXACTLY){
            width=widthSize;
        }else {
            width=no.getWidth()*number;
        }

        if (heightMode==MeasureSpec.EXACTLY){
            height=heightSize;
        }else {
            height=no.getHeight();
        }

        setMeasuredDimension(width,height);
    }

    /**
     * 定义点击事件接口
     */
    public interface OnGradeClickItemListener {
        void currentItem(int pos);
    }

    /**
     * 对外提供点击事件接口
     * @param ongradeClickItemListener
     */
    public void setOnGradeClickItemListener(OnGradeClickItemListener ongradeClickItemListener) {
        this.ongradeClickItemListener = ongradeClickItemListener;
    }

    /**
     * 初始化Bitmap
     * @param selectImage
     * @param noselectImage
     */
    public void initBitmap(int selectImage,int noselectImage) {
        if (selectImage!=0&&noselectImage!=0){//如果没有主动设置选中和没选中的图片的时候 默认使用默认的图片
            yes = BitmapFactory.decodeResource(getResources(), selectImage);
            no = BitmapFactory.decodeResource(getResources(),noselectImage);
        }else {
            yes = BitmapFactory.decodeResource(getResources(), R.mipmap.pingfenyes);
            no = BitmapFactory.decodeResource(getResources(), R.mipmap.pingfen);
        }
    }

    /**
     * 初始化评分控件
     */
    public void initImageView() {
        for (int i = 0; i < number; i++) {//将星星添加到View当中去
            final ImageView imageView = new ImageView(mContext);
            imageView.setImageBitmap(no);
            imageView.setTag(false);
            addView(imageView);
        }
    }

    /**
     * 确定摆放的位置
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        count = getChildCount();
        setSelectvalue(value);

        int right = 0;
        int left = 0;

        //循环的设置子view的位置
        for (int i = 0; i < count; i++) {
            ImageView view = (ImageView) getChildAt(i);
            right += no.getWidth();
            view.layout(left, 0, right, no.getHeight());
            left += no.getWidth();

            final int finalI = i;

            if (isOnclick){//设置是否可点击
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSelectvalue(finalI+1);
                        if (ongradeClickItemListener!=null){
                            ongradeClickItemListener.currentItem(finalI+1);
                        }
                    }
                });
            }
        }
    }

    /**
     * 设置选中的逻辑
     * @param value
     */
    public void setSelectvalue(int value){
        Log.e("-----","value:"+value+"---count:"+count);
        if (value>count){//如果主动设置的选中数值大于星星的数值 那么做保护
           Toast.makeText(mContext,"设定的选择数值大于星星的个数",Toast.LENGTH_SHORT).show();
            return;
        }

        //循环的去设置选中或者不选中
        for (int j=0;j<value;j++){
            ImageView imageView= (ImageView) getChildAt(j);
            imageView.setImageBitmap(yes);
            imageView.setTag(true);
        }
        for (int k=value;k<count;k++){
            ImageView imageView= (ImageView) getChildAt(k);
            imageView.setImageBitmap(no);
            imageView.setTag(false);
        }
    }



    public void setNumber(int number){
        this.number=number;
    }
    /**
     * 对外提供设置选择的接口
     * @param value
     */
    public void setValue(int value){
        this.value=value;
    }

}
