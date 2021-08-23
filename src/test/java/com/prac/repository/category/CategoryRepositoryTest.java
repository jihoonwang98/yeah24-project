package com.prac.repository.category;

import com.prac.config.BasicConfiguration;
import com.prac.domain.Category;
import com.prac.domain.QCategory;
import com.prac.dto.CategoryDTO;
import com.prac.dto.CategoryDTO.CategoryBriefDTO;
import com.prac.dto.CategoryDTO.CategoryBriefDTOWithChildren;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

import static com.prac.domain.QCategory.category;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BasicConfiguration.class)
class CategoryRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    CategoryRepository categoryRepository;

    Category parent;
        Category child1;
            Category child1sGrandChild1;
            Category child1sGrandChild2;

        Category child2;
            Category child2sGrandChild1;

        Category child3;
            Category child3sGrandChild1;
            Category child3sGrandChild2;
            Category child3sGrandChild3;

    Category parent2;

    Category parent3;

    @BeforeEach
    void setUp() {
        parent = new Category("parent", null);
            child1 = new Category("child1", parent);
                child1sGrandChild1 = new Category("child1sGrandChild1", child1);
                child1sGrandChild2 = new Category("child1sGrandChild2", child1);
            child2 = new Category("child2", parent);
                child2sGrandChild1 = new Category("child2sGrandChild1", child2);
            child3 = new Category("child3", parent);
                child3sGrandChild1 = new Category("child3sGrandChild1", child3);
                child3sGrandChild2 = new Category("child3sGrandChild2", child3);
                child3sGrandChild3 = new Category("child3sGrandChild3", child3);

        em.persist(parent);


        parent2 = new Category("parent2", null);
        em.persist(parent2);

        parent3 = new Category("parent3", null);
        em.persist(parent3);
    }

    @Test
    void findParentCategories() {
        List<CategoryBriefDTO> parents = categoryRepository.findParentCategories();
        assertThat(parents).hasSize(3);
        assertThat(parents).containsExactlyInAnyOrder(new CategoryBriefDTO(parent), new CategoryBriefDTO(parent2), new CategoryBriefDTO(parent3));
    }

    @Test
    void findParentCategoriesWithChildren() {
        List<CategoryBriefDTOWithChildren> parents = categoryRepository.findParentCategoriesWithChildren();

        CategoryBriefDTOWithChildren parentDTO = parents.stream().filter(c -> c.getName().equals("parent")).findFirst().get();
        assertThat(parentDTO.getId()).isEqualTo(parent.getId());
        assertThat(parentDTO.getName()).isEqualTo(parent.getName());
        assertThat(parentDTO.getDepth()).isEqualTo(parent.getDepth());

        List<CategoryBriefDTO> children = parentDTO.getChildren();
        assertThat(children).hasSize(3);
        assertThat(children).containsExactlyInAnyOrder(new CategoryBriefDTO(child1), new CategoryBriefDTO(child2), new CategoryBriefDTO(child3));


        List<CategoryBriefDTOWithChildren> remainders = parents.stream().filter(c -> !c.getName().equals("parent")).collect(Collectors.toList());
        for (CategoryBriefDTOWithChildren remainder : remainders) {
            assertThat(remainder.getChildren()).hasSize(0);
        }
    }

    @Test
    void findCategoryWithChildren() {
        CategoryBriefDTOWithChildren categoryWithChildren = categoryRepository.findCategoryWithChildren(child3.getId());
        assertThat(categoryWithChildren.getId()).isEqualTo(child3.getId());
        assertThat(categoryWithChildren.getName()).isEqualTo(child3.getName());
        assertThat(categoryWithChildren.getDepth()).isEqualTo(child3.getDepth());


        List<CategoryBriefDTO> children = categoryWithChildren.getChildren();
        assertThat(children).hasSize(3);
        assertThat(children).containsExactlyInAnyOrder(new CategoryBriefDTO(child3sGrandChild1), new CategoryBriefDTO(child3sGrandChild2), new CategoryBriefDTO(child3sGrandChild3));
    }
}