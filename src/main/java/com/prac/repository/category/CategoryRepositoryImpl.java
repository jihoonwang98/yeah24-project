package com.prac.repository.category;

import com.prac.domain.Category;
import com.prac.domain.QCategory;
import com.prac.dto.CategoryDTO;
import com.prac.dto.CategoryDTO.CategoryBriefDTO;
import com.prac.dto.CategoryDTO.CategoryBriefDTOWithChildren;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static com.prac.domain.QCategory.category;

@Transactional
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final EntityManager em;

    @Override
    public List<CategoryBriefDTOWithChildren> findParentCategoriesWithChildren() {
        List<CategoryBriefDTOWithChildren> results = queryFactory
                .selectFrom(category)
                .where(category.parent.isNull())
                .fetch()
                .stream().map(c -> new CategoryBriefDTOWithChildren(c)).collect(Collectors.toList());
        return results;
    }

    @Override
    public List<CategoryBriefDTO> findParentCategories() {
        List<CategoryBriefDTO> results = queryFactory
                .selectFrom(category)
                .where(category.parent.isNull())
                .fetch()
                .stream().map(c -> new CategoryBriefDTO(c)).collect(Collectors.toList());
        return results;
    }

    @Override
    public CategoryBriefDTOWithChildren findCategoryWithChildren(Long id) {

        Category cateEntity = em.createQuery("select distinct c from Category c left join fetch c.children where c.id=:id", Category.class)
                .setParameter("id", id)
                .getSingleResult();

        /*QCategory child = new QCategory("child");

        Category cateEntity = queryFactory
                .select(category)
                .join(category.children, child).fetchJoin()
                .where(category.id.eq(id))
                .fetchOne();*/

        return new CategoryBriefDTOWithChildren(cateEntity);
    }
}
