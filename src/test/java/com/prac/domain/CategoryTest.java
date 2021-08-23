package com.prac.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    Category parent = new Category("parent", null);
    Category child1 = new Category("child1", parent);
    Category child2 = new Category("child2", parent);
    Category grandChild1 = new Category("grandchild1", child1);
    Category grandChild2 = new Category("grandchild2", child2);


    @Test
    void create() {
        assertThat(parent.getParent()).isNull();
        assertThat(parent.getChildren()).hasSize(2);
        assertThat(parent.getChildren()).containsExactlyInAnyOrder(child1, child2);
        assertThat(parent.getDepth()).isEqualTo(1);

        assertThat(child1.getParent()).isEqualTo(parent);
        assertThat(child1.getChildren()).hasSize(1);
        assertThat(child1.getDepth()).isEqualTo(2);

        assertThat(child2.getParent()).isEqualTo(parent);
        assertThat(child2.getChildren()).hasSize(1);
        assertThat(child2.getDepth()).isEqualTo(2);

        assertThat(grandChild1.getParent()).isEqualTo(child1);
        assertThat(grandChild1.getChildren()).hasSize(0);
        assertThat(grandChild1.getDepth()).isEqualTo(3);

        assertThat(grandChild2.getParent()).isEqualTo(child2);
        assertThat(grandChild2.getChildren()).hasSize(0);
        assertThat(grandChild2.getDepth()).isEqualTo(3);
    }


    @Test
    void removeChild() {
        // when
        parent.removeChildCategory(child1);

        // then
        assertThat(parent.getChildren()).hasSize(1);
        assertThat(parent.getChildren()).containsExactlyInAnyOrder(child2);

        assertThat(child1.getParent()).isNull();

        assertThat(child2.getParent()).isEqualTo(parent);
    }
}