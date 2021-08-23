package com.prac.utils;

public class PageUtils {

    // 한 번에 보여줄 페이지 개수
    private int pagePerView;

    private int currPage;

    private int totalPages;

    public PageUtils(int currPage, int totalPages) {
        this(currPage, totalPages, 3);
    }

    public PageUtils(int currPage, int totalPages, int pagePerView) {
        this.currPage = currPage;
        this.totalPages = totalPages;
        this.pagePerView = pagePerView;
    }

    public int endPage() {
        int tempEnd = (int) Math.ceil((double)currPage / pagePerView) * pagePerView;
        return (totalPages <= tempEnd) ? totalPages : tempEnd;
    }

    public int startPage() {
        int tempEnd = (int) Math.ceil((double)currPage / pagePerView) * pagePerView;
        return tempEnd - (pagePerView - 1);
    }

    public boolean prev() {
        return (startPage() != 1);
    }

    public boolean next() {
        return (endPage() != totalPages);
    }

}
