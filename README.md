# NewLife
快速开发安卓基础框架

使用com.alibaba:arouter-api分模块开发

关联base模块BaseActivity基础类，标题的基础类CmTitleBarActivity

    /**
     * 布局res文件，需要的参数为R.layout.xxx，和调用setContentView(R.layout.xxx)效果一致
     *
     * @return
     */
    protected abstract int getLayoutRes();

    protected abstract boolean needButterKnife();
    
    QuickRecyclerAdapter是RecyclerView的分装泛型的适配器减少多个Adapter类创建，QuickAdapter是listview适配器同样的道理
    
    LazyViewPager是代替了ViewPager减少对页面滑动更新和接口请求
    
    网络使用okhttp、Rxjava
    
    task包TaskHelper的减少handle、thread使用减少内存溢出
