package com.sevenpick.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;

/**
 * Auto Scroll View Pager
 * <ul>
 * <strong>Basic Setting and Usage</strong>
 * <li>{@link #startAutoScroll()} start auto scroll, or {@link #startAutoScroll(int)} start auto
 * scroll delayed</li>
 * <li>{@link #stopAutoScroll()} stop auto scroll</li>
 * <li>{@link #setInterval(long)} set auto scroll time in milliseconds, default is
 * {@link #DEFAULT_INTERVAL}</li>
 * </ul>
 * <ul>
 * <strong>Advanced Settings and Usage</strong>
 * <li>{@link #setDirection(int)} set auto scroll direction</li>
 * <li>{@link #setCycle(boolean)} set whether automatic cycle when auto scroll reaching the last or
 * first item, default is true</li>
 * <li>{@link #setSlideBorderMode(int)} set how to process when sliding at the last or first item</li>
 * <li>{@link #setStopScrollWhenTouch(boolean)} set whether stop auto scroll when touching, default
 * is true</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-12-30
 */
public class AutoScrollViewPager extends ViewPager {

  public static final int DEFAULT_INTERVAL = 1500;

  public static final int LEFT = 0;
  public static final int RIGHT = 1;

  /** do nothing when sliding at the last or first item **/
  public static final int SLIDE_BORDER_MODE_NONE = 0;
  /** cycle when sliding at the last or first item **/
  public static final int SLIDE_BORDER_MODE_CYCLE = 1;
  /** deliver event to parent when sliding at the last or first item **/
  public static final int SLIDE_BORDER_MODE_TO_PARENT = 2;

  /** auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL} **/
  private long interval = AutoScrollViewPager.DEFAULT_INTERVAL;
  /** auto scroll direction, default is {@link #RIGHT} **/
  private int direction = AutoScrollViewPager.RIGHT;
  /** whether automatic cycle when auto scroll reaching the last or first item, default is true **/
  private boolean isCycle = true;
  /** whether stop auto scroll when touching, default is true **/
  private boolean stopScrollWhenTouch = true;
  /**
   * how to process when sliding at the last or first item, default is
   * {@link #SLIDE_BORDER_MODE_NONE}
   **/
  private int slideBorderMode = AutoScrollViewPager.SLIDE_BORDER_MODE_NONE;
  /** whether animating when auto scroll at the last or first item **/
  private boolean isBorderAnimation = true;

  private Handler handler;
  private boolean isAutoScroll = false;
  private boolean isStopByTouch = false;
  private float touchX = 0f, downX = 0f;
  private float touchY = 0f, downY = 0f;
  private CustomDurationScroller scroller = null;

  public static final int SCROLL_WHAT = 0;

  public AutoScrollViewPager(final Context paramContext) {
    super(paramContext);
    init();
  }

  public AutoScrollViewPager(final Context paramContext, final AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init();
  }

  private void init() {
    handler = new MyHandler();
    setViewPagerScroller();
  }

  /**
   * start auto scroll, first scroll delay time is {@link #getInterval()}
   */
  public void startAutoScroll() {
    isAutoScroll = true;
    sendScrollMessage(interval);
  }

  /**
   * start auto scroll
   * 
   * @param delayTimeInMills first scroll delay time
   */
  public void startAutoScroll(final int delayTimeInMills) {
    isAutoScroll = true;
    sendScrollMessage(delayTimeInMills);
  }

  /**
   * stop auto scroll
   */
  public void stopAutoScroll() {
    isAutoScroll = false;
    handler.removeMessages(AutoScrollViewPager.SCROLL_WHAT);
  }

  /**
   * set the factor by which the duration of sliding animation will change
   */
  public void setScrollDurationFactor(final double scrollFactor) {
    scroller.setScrollDurationFactor(scrollFactor);
  }

  private void sendScrollMessage(final long delayTimeInMills) {
    /** remove messages before, keeps one message is running at most **/
    handler.removeMessages(AutoScrollViewPager.SCROLL_WHAT);
    handler.sendEmptyMessageDelayed(AutoScrollViewPager.SCROLL_WHAT, delayTimeInMills);
  }

  /**
   * set ViewPager scroller to change animation duration when sliding
   */
  private void setViewPagerScroller() {
    try {
      Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
      scrollerField.setAccessible(true);
      Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
      interpolatorField.setAccessible(true);

      scroller =
          new CustomDurationScroller(getContext(), (Interpolator) interpolatorField.get(null));
      scrollerField.set(this, scroller);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  OnPageChangeListener onPageChangeListener;
  public void setPageScrollListeners(OnPageChangeListener onPageChangeListener){
    this.onPageChangeListener = onPageChangeListener;
  }

  /**
   * scroll only once
   */
  public void scrollOnce() {
    PagerAdapter adapter = getAdapter();
    int currentItem = getCurrentItem();
    int totalCount;
    if (adapter == null || (totalCount = adapter.getCount()) <= 1) {
      return;
    }

    int nextItem = direction == AutoScrollViewPager.LEFT ? --currentItem : ++currentItem;
    if (nextItem < 0) {
      if (isCycle) {
        setCurrentItem(totalCount - 1, isBorderAnimation);
      }
    } else if (nextItem == totalCount) {
      if (isCycle) {
        setCurrentItem(0, isBorderAnimation);
      }
    } else {
      setCurrentItem(nextItem, true);
    }
    if(onPageChangeListener!=null){
      onPageChangeListener.onPageSelected(nextItem);
    }
  }

  private double initX;
  private double initY;
  @Override
  public boolean onInterceptTouchEvent(final MotionEvent arg0) {
    getParent().requestDisallowInterceptTouchEvent(true);
    if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
      initX = arg0.getX();
      initY = arg0.getY();
    }
    if(Math.abs(arg0.getX()-initX)< Math.abs(arg0.getY()-initY)){
      getParent().requestDisallowInterceptTouchEvent(false);
    }

    return super.onInterceptTouchEvent(arg0);
  }

  /**
   * <ul>
   * if stopScrollWhenTouch is true
   * <li>if event is down, stop auto scroll.</li>
   * <li>if event is up, start auto scroll again.</li>
   * </ul>
   */
  @Override
  public boolean onTouchEvent(final MotionEvent ev) {
    getParent().requestDisallowInterceptTouchEvent(true);
    if (stopScrollWhenTouch) {
      if (ev.getAction() == MotionEvent.ACTION_DOWN && isAutoScroll) {
        isStopByTouch = true;
        stopAutoScroll();
      } else if (ev.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
        startAutoScroll();
      }
    }


    if (slideBorderMode == AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT
        || slideBorderMode == AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE) {


      touchX = ev.getX();
      touchY = ev.getY();
      if (ev.getAction() == MotionEvent.ACTION_DOWN) {
        downX = touchX;
        downY = touchY;
      }

      int currentItem = getCurrentItem();
      PagerAdapter adapter = getAdapter();
      int pageCount = adapter == null ? 0 : getRealCount();
      /**
       * current index is first one and slide to right or current index is last one and slide to
       * left.<br/>
       * if slide border mode is to parent, then requestDisallowInterceptTouchEvent false.<br/>
       * else scroll to last one when current item is first one, scroll to first one when current
       * item is last one.
       */
      if (currentItem == 0 && downX <= touchX || currentItem == pageCount - 1 && downX >= touchX) {
        if (slideBorderMode == AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT) {} else {
          if (pageCount > 1) {
            setCurrentItem(pageCount - currentItem - 1, isBorderAnimation);
          }
        }
        return super.onTouchEvent(ev);
      }

    }
    return super.onTouchEvent(ev);
  }

  int realCount;

  public int getRealCount() {
    return realCount;
  }

  public void setRealCount(final int realCount) {
    this.realCount = realCount;
  }

  private class MyHandler extends Handler {

    @Override
    public void handleMessage(final Message msg) {
      super.handleMessage(msg);

      switch (msg.what) {
        case SCROLL_WHAT:
          scrollOnce();
          sendScrollMessage(interval);
        default:
          break;
      }
    }
  }

  /**
   * get auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL}
   * 
   * @return the interval
   */
  public long getInterval() {
    return interval;
  }

  /**
   * set auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL}
   * 
   * @param interval the interval to set
   */
  public void setInterval(final long interval) {
    this.interval = interval;
  }

  /**
   * get auto scroll direction
   * 
   * @return {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
   */
  public int getDirection() {
    return direction == AutoScrollViewPager.LEFT
        ? AutoScrollViewPager.LEFT
        : AutoScrollViewPager.RIGHT;
  }

  /**
   * set auto scroll direction
   * 
   * @param direction {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
   */
  public void setDirection(final int direction) {
    this.direction = direction;
  }

  /**
   * whether automatic cycle when auto scroll reaching the last or first item, default is true
   * 
   * @return the isCycle
   */
  public boolean isCycle() {
    return isCycle;
  }

  /**
   * set whether automatic cycle when auto scroll reaching the last or first item, default is true
   * 
   * @param isCycle the isCycle to set
   */
  public void setCycle(final boolean isCycle) {
    this.isCycle = isCycle;
  }

  /**
   * whether stop auto scroll when touching, default is true
   * 
   * @return the stopScrollWhenTouch
   */
  public boolean isStopScrollWhenTouch() {
    return stopScrollWhenTouch;
  }

  /**
   * set whether stop auto scroll when touching, default is true
   * 
   * @param stopScrollWhenTouch
   */
  public void setStopScrollWhenTouch(final boolean stopScrollWhenTouch) {
    this.stopScrollWhenTouch = stopScrollWhenTouch;
  }

  /**
   * get how to process when sliding at the last or first item
   * 
   * @return the slideBorderMode {@link #SLIDE_BORDER_MODE_NONE},
   *         {@link #SLIDE_BORDER_MODE_TO_PARENT}, {@link #SLIDE_BORDER_MODE_CYCLE}, default is
   *         {@link #SLIDE_BORDER_MODE_NONE}
   */
  public int getSlideBorderMode() {
    return slideBorderMode;
  }

  /**
   * set how to process when sliding at the last or first item
   * 
   * @param slideBorderMode {@link #SLIDE_BORDER_MODE_NONE}, {@link #SLIDE_BORDER_MODE_TO_PARENT},
   *        {@link #SLIDE_BORDER_MODE_CYCLE}, default is {@link #SLIDE_BORDER_MODE_NONE}
   */
  public void setSlideBorderMode(final int slideBorderMode) {
    this.slideBorderMode = slideBorderMode;
  }

  /**
   * whether animating when auto scroll at the last or first item, default is true
   * 
   * @return
   */
  public boolean isBorderAnimation() {
    return isBorderAnimation;
  }

  /**
   * set whether animating when auto scroll at the last or first item, default is true
   * 
   * @param isBorderAnimation
   */
  public void setBorderAnimation(final boolean isBorderAnimation) {
    this.isBorderAnimation = isBorderAnimation;
  }

  @Override
  protected void onMeasure(final int arg0, final int arg1) {
    super.onMeasure(arg0, arg1);
  }
}
