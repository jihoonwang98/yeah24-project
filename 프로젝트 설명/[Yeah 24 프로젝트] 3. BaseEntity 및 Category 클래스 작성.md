# [Yeah 24 프로젝트] 3. BaseEntity 및 Category 클래스 작성



## 1) BaseEntity 클래스 작성

우리 프로젝트에서는 모든 엔티티 클래스에 등록일자, 수정일자를 넣을 것이다. 

그리고 Join Table을 매핑하는 엔티티는 복합키를 넣고, 그 외의 모든 엔티티는 인공키를 넣어줄 것이다.

이때 공통 속성을 정의한 엔티티를 @MappedSuperclass로 정의하고 상속받아 사용하면 중복되는 코드를 줄일 수 있다.



그런데 여기서 Join Table을 매핑하는 엔티티는 인공키를 사용하지 않고 그 외의 엔티티는 인공키를 사용하므로

**등록일자, 수정일자**만을 갖고 있는 `AuditingEntity`와 **등록일자, 수정일자, 인공키**를 갖고 있는 `BaseEntity` 두 개의 클래스를 작성해서

 Join Table 엔티티는 `AuditingEntity`를 상속받게 하고 그 외의 엔티티는 `BaseEntity`를 상속받게 하자.







**AuditingEntity 설정**

```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class AuditingEntity {

    @CreatedDate
    private LocalDateTime regDate;

    @LastModifiedDate
    private LocalDateTime modDate;
}
```

```java
@Configuration
@EnableJpaAuditing  // 애너테이션 추가
public class BasicConfiguration {

    ...
}
```

- `AuditingEntity` 클래스는 모든 엔티티 클래스에 공통적으로 필요한 **등록일자(regDate), 수정일자(modDate)** 컬럼을 넣어주기 위한 클래스다.
- Join Table을 매핑하는 엔티티는 이 `AuditingEntity` 클래스를 상속받아 작성한다.
- `@Configuration` 설정 클래스에 `@EnableJpaAuditing` 애너테이션을 추가해주자.





**BaseEntity 설정**

```java
@MappedSuperclass
@Getter
public abstract class BaseEntity extends AuditingEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
```

- `BaseEntity` 클래스는 `AuditingEntity`를 상속받고, `IDENTITY` 전략을 사용한 인공키 컬럼을 추가한다.
- Join Table을 매핑하는 엔티티 외의 모든 엔티티는 이 `BaseEntity` 클래스를 상속받아 작성한다.







## 2) Category 클래스 작성

**Category**

```java
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
```

- 연관관계 편의 메서드를 통해 양방향 매핑 시에 데이터가 불일치되는 경우가 없도록 한다.
- @ManyToOne, @OneToMany 모두 `fetch` 전략을 `LAZY` 로딩으로 두었다. (@OneToMany는 기본값이 `LAZY`)

- 연관관계를 parent 쪽에서 한꺼번에 관리하기 위해서 `cascade` 타입을 `PERSIST`로 두고, `orphanRemoval` 속성을 ` true`로 두었다.
  - `cascade` 타입 `PERSIST`의 효과
    - 부모 카테고리 하나만 저장해도 부모가 가지고 있는 자식 카테고리들에게 영속성이 전이된다.(자식들도 저장된다)
  - `orphanRemoval=true`의 효과
    - 부모 카테고리가 관리하는 자식 카테고리 `List`에서 자식 카테고리를 삭제하면 자동으로 DB에서도 자식 카테고리가 삭제된다.

- `removeChildCategory()` 메서드
  - 부모 카테고리에서 자식 카테고리를 삭제할 때, 아래와 같은 행동을 동시에 처리하기 위해 작성한 메서드
  
    1. 부모 카테고리가 관리하는 `List`에서 자식 카테고리를 삭제
    2. 자식 카테고리의 parent 필드에 null 대입


- `depth` 필드

  - 카테고리의 깊이를 나타내는 필드다.

  - 예를 들어, 카테고리 분류가 **국내도서 > 소설/시/희곡 > 한국소설 *>* 한국 장편소설**인 경우

    **국내도서**의 depth: 1

    **소설/시/희곡**의 depth: 2

    **한국소설**의 depth: 3

    **한국 장편소설**의 depth: 4로 정의한다.

- `Category`를 생성할 때 하나 주의할 점은 가장 상위(부모) 카테고리의 경우 `new Category("이름", null)`로 생성한다는 점이다.





**Category 테스트**

```java
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
```

