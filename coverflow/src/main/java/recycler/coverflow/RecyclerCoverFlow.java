package recycler.coverflow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 상속 RecyclerView 다시 쓰기{@link #getChildDrawingOrder(int, int)} 예 Item 도면 순서 제어
 *
 * @author Chen Xiaoping (562818444@qq.com)
 * @version V1.0
 * @Datetime 2017-04-18
 */

public class RecyclerCoverFlow extends RecyclerView {
    /**
     * 눌린 X 축 좌표
     */
    private float mDownX;
    /**
     * 레이아웃 빌더
     */
    private CoverFlowLayoutManager.Builder mManagerBuilder;

    public RecyclerCoverFlow(Context context) {
        super(context);
        init();
    }

    public RecyclerCoverFlow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecyclerCoverFlow(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        createManageBuilder();
        setLayoutManager(mManagerBuilder.build());
        setChildrenDrawingOrderEnabled(true); //재주문 켜기
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    /**
     * 레이아웃 빌더 생성
     */
    private void createManageBuilder() {
        if (mManagerBuilder == null) {
            mManagerBuilder = new CoverFlowLayoutManager.Builder();
        }
    }

    /**
     * 일반 평면 스크롤인지 설정
     * @param isFlat true : 평면 스크롤; false : 오버레이 줌 스크롤
     */
    public void setFlatFlow(boolean isFlat) {
        createManageBuilder();
        mManagerBuilder.setFlat(isFlat);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 항목 회색 그라데이션 설정
     * @param greyItem true:Item 그레이 그래디언트；false:Item 회색조 변경되지 않음
     */
    public void setGreyItem(boolean greyItem) {
        createManageBuilder();
        mManagerBuilder.setGreyItem(greyItem);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 설정 Item 그레이 그래디언트
     * @param alphaItem true:Item 반투명 그라디언트；false:Item 투명도 변경되지 않음
     */
    public void setAlphaItem(boolean alphaItem) {
        createManageBuilder();
        mManagerBuilder.setAlphaItem(alphaItem);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 설정 Item 간격 비율
     * @param intervalRatio Item 간격 비율。
     * 즉：item 넓게 x intervalRatio
     */
    public void setIntervalRatio(float intervalRatio) {
        createManageBuilder();
        mManagerBuilder.setIntervalRatio(intervalRatio);
        setLayoutManager(mManagerBuilder.build());
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (!(layout instanceof CoverFlowLayoutManager)) {
            throw new IllegalArgumentException("The layout manager must be CoverFlowLayoutManger");
        }
        super.setLayoutManager(layout);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int center = getCoverFlowLayout().getCenterPosition()
                - getCoverFlowLayout().getFirstVisiblePosition(); //표시되는 모든 항목의 중간 위치를 계산

        if (center < 0) center = 0;
        else if (center > childCount) center = childCount;
        int order;
        if (i == center) {
            order = childCount - 1;
        } else if (i > center) {
            order = center + childCount - 1 - i;
        } else {
            order = i;
        }
        //Log.d("YJP","getChildDrawingOrder");
        return order;
    }

    /**
     * 입수 LayoutManger，그리고 캐스트 CoverFlowLayoutManger
     */
    public CoverFlowLayoutManager getCoverFlowLayout() {
        return ((CoverFlowLayoutManager)getLayoutManager());
    }

    /**
     * 선택한 항목의 위치를 가져옵니다
     */
    public int getSelectedPos() {
        return getCoverFlowLayout().getSelectedPos();
    }

    /**
     * 선택된 모니터 설정
     * @param l 청취 인터페이스
     */
    public void setOnItemSelectedListener(CoverFlowLayoutManager.OnSelected l) {
        getCoverFlowLayout().setOnSelectedListener(l);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //Log.d("YJP","dispatchTouchEvent");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                getParent().requestDisallowInterceptTouchEvent(true); //슬라이딩 이벤트를 가로 채지 않도록 부모 클래스 설정
                break;
            case MotionEvent.ACTION_MOVE:
                if ((ev.getX() > mDownX && getCoverFlowLayout().getCenterPosition() == 0) ||
                        (ev.getX() < mDownX && getCoverFlowLayout().getCenterPosition() ==
                                getCoverFlowLayout().getItemCount() -1)) {
                    //앞과 끝으로 미끄러지는 경우，오픈 부모 클래스 슬라이딩 이벤트 차단
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    //중간으로 슬라이드，슬라이딩 이벤트를 가로 채지 않도록 부모 클래스 설정
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
