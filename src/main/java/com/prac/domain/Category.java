package com.prac.domain;

import com.prac.domain.etc.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString(of = {"name", "depth"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "category_id"))
public class Category extends BaseEntity {

    @Column(unique = true)
    private String name;

    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_to_parent_category"))
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();


    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;

        if (parent == null) {
            this.depth = 1;
        } else {
            parent.addChildCategory(this);
            this.depth = parent.getDepth() + 1;
        }
    }


    //== 연관관계 편의 메서드 ==//
    private void addChildCategory(Category child) {
        this.children.add(child);
    }

    public void removeChildCategory(Category child) {
        if (this.children.contains(child) && child.getParent().equals(this)) {
            this.children.remove(child);
            child.parent = null;
        }
    }
}
