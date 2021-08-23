package com.prac.dto;

import com.prac.utils.PageUtils;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PageResultDTO<DTO> {
    private int totalPages;
    private Long totalElements;
    private int page;
    private int size;

    private int startPage;
    private int endPage;

    private boolean prevBtn;
    private boolean nextBtn;


    public PageResultDTO (Page<DTO> pages) {
        this.totalPages = pages.getTotalPages();
        this.totalElements = pages.getTotalElements();
        this.page = pages.getNumber() + 1;
        this.size = pages.getSize();


        PageUtils pageUtils = new PageUtils(page, totalPages);
        this.startPage = pageUtils.startPage();
        this.endPage = pageUtils.endPage();
        this.prevBtn = pageUtils.prev();
        this.nextBtn = pageUtils.next();
    }

    @Override
    public String toString() {
        return "PageResultDTO{\n" +
                "\ttotalPages=" + totalPages +
                "\n\ttotalElements=" + totalElements +
                "\n\tpage=" + page +
                "\n\tsize=" + size +
                "\n\tstartPage=" + startPage +
                "\n\tendPage=" + endPage +
                "\n\tprevBtn=" + prevBtn +
                "\n\tnextBtn=" + nextBtn +
                '}';
    }
}
