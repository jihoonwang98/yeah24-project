package com.prac.config;

import com.prac.domain.Author;
import com.prac.domain.Book;
import com.prac.domain.Category;
import com.prac.domain.User;
import com.prac.domain.etc.Address;
import com.prac.domain.etc.Gender;
import com.prac.domain.etc.Role;
import com.prac.domain.joinTable.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitialization implements CommandLineRunner {

    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        log.info("CommandLineRunner running!!");

            List<Category> parentCategories = new ArrayList<>();

            Category it = new Category("IT 모바일", null);
            Category game = new Category("게임", it);
            Category gameDevelop = new Category("게임 개발", game);
            Category gameDesign = new Category("게임 기획", game);
            Category mobileGame = new Category("모바일 게임", game);

            Category cs = new Category("컴퓨터 공학", it);
            Category os = new Category("운영체제", cs);
            Category ai = new Category("인공지능", cs);
            Category blockChain = new Category("블록체인", cs);
            Category db = new Category("데이터베이스", cs);
            Category dsAlgo = new Category("자료구조/알고리즘", cs);
            Category architecture = new Category("컴퓨터구조", cs);

            Category pl = new Category("프로그래밍 언어", it);
            Category java = new Category("자바", pl);
            Category cLanguage = new Category("C", pl);
            Category cplpl = new Category("C++", pl);
            Category js = new Category("JavaScript", pl);
            Category python = new Category("파이썬", pl);
            Category ruby = new  Category("Ruby", pl);

            Category office = new Category("오피스 활용", it);
            Category hangul = new Category("한글", office);
            Category excel = new Category("엑셀", office);
            Category powerPoint = new Category("파워포인트", office);
            Category word = new Category("워드", office);


            Category novelPoetDrama = new Category("소설/시/희곡", null);
            Category genreNovel = new Category("장르소설", novelPoetDrama);
            Category mystery = new Category("추리/미스터리", genreNovel);
            Category thriller = new Category("공포/스릴러", genreNovel);
            Category fantasy = new Category("판타지", genreNovel);
            Category sf = new Category("SF", genreNovel);
            Category historyNovel = new Category("역사소설", genreNovel);

            Category poetDrama = new Category("시/희곡", novelPoetDrama);
            Category koreaPoet = new Category("한국 시", poetDrama);
            Category oldPoet = new Category("한시/옛시", poetDrama);
            Category drama = new Category("희곡/시나리오", poetDrama);

            Category oldNovel = new Category("고전문학", novelPoetDrama);
            Category koreanOldNovel = new Category("한국 고전문학", oldNovel);
            Category orientalOldNovel = new Category("동양 고전문학", oldNovel);
            Category westernOldNovel = new Category("서양 고전문학", oldNovel);


            Category selfDevelopment = new Category("자기계발", null);
            Category relationships = new Category("인간관계", selfDevelopment);
            Category success = new Category("성공학/경력관리", selfDevelopment);
            Category brain = new Category("창조적사고/두뇌계발", selfDevelopment);


            Category naturalScience = new Category("자연과학", null);
            Category engineering = new Category("공학", naturalScience);
            Category machine = new Category("기계/기계공학", engineering);
            Category electronics = new Category("전기/전자공학", engineering);
            Category building = new Category("토목/건축공학", engineering);

            Category science = new Category("과학", naturalScience);
            Category scienceHistory = new Category("과학사/과학사전", science);
            Category scientist = new Category("과학자", science);
            Category generalScience = new Category("과학 일반", science);

            Category math = new Category("수학", naturalScience);
            Category geometric = new Category("기하학/미분적분학", math);
            Category generalMath = new Category("수학 일반", math);
            Category mathHistory = new Category("수학사", math);
            Category probability = new Category("확률/통계학", math);


            Category history = new Category("역사", null);
            Category koreaHistory = new Category("한국사/한국문화", history);
            Category chosun = new Category("조선시대", koreaHistory);
            Category generalKoreaHistory = new Category("한국사일반", koreaHistory);
            Category threeNation = new Category("삼국/통일신라/발해", koreaHistory);
            Category modernHistory = new Category("한국근대사", koreaHistory);

            Category orientalHistory = new Category("동양사/동양문화", history);
            Category chinaHistory = new Category("중국역사/문화", orientalHistory);
            Category japanHistory = new Category("일본역사/문화", orientalHistory);
            Category generalOrientalHistory = new Category("동양사/동양문화 일반", orientalHistory);

            Category westernHistory = new Category("서양사/서양문화", history);
            Category britainHistory = new Category("영국역사/문화", westernHistory);
            Category franceHistory = new Category("프랑스역사/문화", westernHistory);
            Category germanyHistory = new Category("독일역사/문화", westernHistory);
            Category usHistory = new Category("미국역사/문화", westernHistory);

            Category worldHistory = new Category("세계사/세계문화", history);



            Category art = new Category("예술", null);
            Category movie = new Category("영화/드라마", art);
            Category music = new Category("음악", art);
            Category photo = new Category("사진", art);
            Category painting = new Category("미술", art);


            // 카테고리 저장
            em.persist(it);
            em.persist(novelPoetDrama);
            em.persist(selfDevelopment);
            em.persist(naturalScience);
            em.persist(history);
            em.persist(art);


            List<Book> books = new ArrayList<>();


            Author seojongwon = new Author("서종원", "로블록스를 국내에 전파하고 있으며 2021년에 Roblox Featured Educator로 선정된 ‘와글와글팩토리’의 공장장입니다. 교육대학원에서 융합인재교육을 전공하고, 청소년들이 스스로 지식을 연결하고 확장해 나갈 수 있도록 이와 관련된 활동에 힘쓰고 있습니다");
            Author kimyeonho = new Author("김연호", "지금은 ‘와글와글팩토리’에서 3D 모델링 교육부터 로블록스 책 출판까지 다양한 경험을 하고 있습니다. 필자에게 게임은 독서의 연장선이었습니다. 게임 속에서 지구도 구하고 나라를 일으켰으며 공주를 구하기 위해 마왕도 무찔렀습니다. 이 책을 읽는 모든 사람들이 자기가 만든 게임을 여러 사람과 함께 나누는 경험을 많이 해 봤으면 좋겠습니다.");
            Author kangeunsuk = new Author("강은숙", "청소년들에게 코딩 교육을 하는 것에 보람을 느끼며 인공지능, 로블록스 등 다양한 코딩 교육 콘텐츠를 만들고 강의하는 줄리엣 선생님입니다. 유튜브에서 로블록스 기초 강좌인 ‘로블록스 게임 메이커’ 강의를 진행하였습니다. 메타버스를 활용한 교육에 관심이 많아 학습자 중심의 인터랙티브한 교육 콘텐츠를 연구하고 있습니다");


            Book b1 = Book.builder()
                    .bio("나만의 로블록스 게임을 단계별로 따라 하며 만들 수 있는 튜토리얼 교재\n" +
                            "국내 최초 로블록스 공식 교육자(Roblox Featured Eucator)로 선정된 저자 집필!\n" +
                            "\n" +
                            "이 책은 나만의 로블록스 게임을 만들 수 있도록 기초부터 차근차근 안내하는 게임 제작 입문서입니다. 국내 최초로 로블록스 공식 교육자(Roblox Featured Eucator)로 선정된 저자가 집필하여 로블록스 게임 제작이 처음인 사람의 눈높이에서 단계별로 따라 할 수 있도록 안내합니다. 이 책 한 권이면 누구나 로블록스 설치부터 루아 코딩 기초, 파트(part) 디자인, 효과 연출, GUI 구현, 수익화까지 게임 제작의 전 과정을 경험할 수 있습니다. 《로블록스 게임 제작 무작정 따라하기》와 함께 ‘게임을 하는 아이에서 만드는 아이로’ 변화를 이끌어 줄 새로운 경험에 도전해 보세요!")
                    .authors(new ArrayList<Author>() {
                        {
                            add(seojongwon);
                            add(kimyeonho);
                            add(kangeunsuk);
                        }
                    })
                    .isbn("1165215624")
                    .highestDepthCategory(gameDevelop)
                    .publicationDate(LocalDate.of(2021, 6, 7))
                    .name("로블록스 게임 제작 무작정 따라하기")
                    .price(18000)
                    .stockQuantity(10)
                    .imgSrc("https://yeah24-image.s3.ap-northeast-2.amazonaws.com/2021/08/23/roblox.jfif")
                    .build();

            books.add(b1);


            Author leejemin = Author.builder()
                    .name("이제민")
                    .bio("2,000명 이상의 유료 온라인 수강생을 가진 게임 개발 트레이너입니다. 해외 온라인 강의 플랫폼 유데미(Udemy)의 베스트셀러 유니티 강의 「retr0의 유니티 게임 프로그래밍 에센스」를 제작했습니다. 이는 현재까지 유데미에서 가장 평점과 판매량이 높은 한국어 강의입니다. 또한 <메이드 아가씨> 등 유명 서브컬처 게임을 만든 독립 개발자이기도 합니다. 유튜브에 무료 게임 프로그래밍 교육 채널 retr0를 운영 중입니다")
                    .imgSrc("https://yeah24-image.s3.ap-northeast-2.amazonaws.com/2021/08/23/%EC%9E%91%EA%B0%80-%EC%9D%B4%EC%A0%9C%EB%AF%BC.gif")
                    .build();

            Book b2 = Book.builder()
                    .bio("소문난 명강사 ‘레트로’가 게임 개발 입문자에게 보내는 선물 같은 책\n" +
                            "\n" +
                            "게임을 만드는 ‘완벽한 준비’를 위해 시간을 낭비하지 말자. 이 책은 기본을 빠르게 익히고 나서 게임을 직접 만들며 필요한 기능을 알아가는 입문 + 활용서로서 여러분의 시간을 아껴준다. C#을 몰라도, 유니티 엔진을 몰라도 게임을 만들 수 있습니다. C# 입문 + 유니티 에디터 + 실전 게임 개발을 한 권으로 전달하니까요. 또한 아주 낮은 눈높이로 설명하고 차츰차츰 높은 난도의 게임을 완성해나가기 때문에 초보자가 책을 완독하는 데 무리가 없다. 이 책에서 제공하는 4가지 게임을 만들다 보면 유니티로 게임을 개발하는 데 필요한 실무 능력을 제대로 갖추게 될 것이다.")
                    .authors(new ArrayList<Author>() {
                        {
                            add(leejemin);
                        }
                    })
                    .isbn("1162241519")
                    .name("레트로의 유니티 게임 프로그래밍 에센스")
                    .highestDepthCategory(gameDevelop)
                    .price(60000)
                    .stockQuantity(20)
                    .publicationDate(LocalDate.of(2019, 2, 20))
                    .imgSrc("2021\\08\\07\\retro-unity.jfif")
                    .build();

            books.add(b2);

            Author dakuya = new Author("아라카와 다쿠야", "일본공학원 전문학교 게임 크리에이터과 교수. 학생들의 게임 제작을 지원하고 게임 엔진(유니티, 언리얼) 수업을 담당하고 있다. 『초보자를 위한 유니티 입문(개정판)』(한빛미디어, 2019)을 집필했으며 다른 대학과 직장인을 위해서도 강의한다. 현재는 VR 관련 콘텐츠에 관심이 많다.");

            Book b3 = Book.builder()
                    .bio("유니티 초보자도 게임을 만들 수 있다. 2D 게임부터 3D 게임, 모바일 게임까지!\n" +
                            "\n" +
                            "이 책은 유니티 설치부터 2D, 3D 게임 제작까지 다룬다. 초보자가 게임 개발에 필요한 사용법을 습득하는 데 집중할 수 있도록 설치, 설정, 에디터 사용법, 프로젝트와 씬 구성, 오브젝트 설정 등을 쉽게 설명한다. 유니티 기본기를 익혀 3D 공 굴리기 게임과 2D 대포 게임을 만들고, 유니티 UI 시스템을 이용해 게임 UI도 제작한다. 마지막으로 3D 장애물 달리기 게임을 만들고, 스마트폰용 게임으로 손쉽게 변경하는 방법도 살펴본다.")
                    .authors(new ArrayList<Author>(){
                        {
                            add(dakuya);
                        }
                    })
                    .isbn("1162241659")
                    .name("초보자를 위한 유니티 입문")
                    .highestDepthCategory(gameDevelop)
                    .publicationDate(LocalDate.of(2019, 4,1))
                    .stockQuantity(15)
                    .price(28000)
                    .imgSrc("2021\\08\\07\\unity-beginner.jfif")
                    .build();

            books.add(b3);

            Book b4 = Book.builder()
                    .bio("코드 한 줄 몰라도 할 수 있다!\n" +
                            "언리얼 엔진 블루프린트로 게임 개발하기\n" +
                            "\n" +
                            "게임 개발 전수의 달인이 언리얼 엔진 4를 알기 쉽게 설명한다. 설치부터 게임 제작까지 간단한 예제를 만들며 언리얼 엔진을 다뤄본 경험이 없는 초보자도 게임 개발의 기초를 익힐 수 있다. 저자가 제공하는 풍부한 예제로 직접 만들면서 편집기에서 모델 가져오기, 블루프린트, AI 사용법, 물리 연산, UI까지 살펴봅니다. 언리얼 엔진의 비주얼 스크립팅 시스템인 블루프린트로 코드 없이도 게임을 개발할 수 있다.")
                    .authors(new ArrayList<Author>(){
                        {
                            add(dakuya);
                        }
                    })
                    .isbn("1162243449")
                    .name("초보자를 위한 언리얼 엔진 4 입문")
                    .highestDepthCategory(gameDevelop)
                    .publicationDate(LocalDate.of(2020, 10,1))
                    .stockQuantity(12)
                    .price(35000)
                    .imgSrc("2021\\08\\07\\unreal-beginner.jfif")
                    .build();

            books.add(b4);

            Author leejinhee = new Author("이진희", "원래 꿈은 콘솔 게임 디렉터였다. 영화를 전공한 것도 시나리오 비중이 높은 게임을 만들고 싶다는 생각에서 출발했다. 온라인 게임 위주인 국내 게임 업계의 현실에 꿈을 잠시 접어두었지만 그렇다고 완전히 포기하진 않았다.\n" +
                    "\n" +
                    "인생의 모토는 성장이며, 성장 제일주의를 지향한다. NC소프트의 대작 MMORPG〈 블레이드 앤 소울〉의 퀘스트 기획자로 일하며 많은 것을 배웠다. ‘고인 물’이 되지 않기 위해 NC소프트를 그만두고 작은 회사를 찾아다니며 다양한 장르의 게임에 참여했다. 그때 쌓은 경험을 게임 개발자 컨퍼런스에 공유해왔으며, 그 내용을 『이론과 실전으로 배우는 게임 시나리오』라는 책으로 엮었다. 지금은 국내 최초의 게임 시나리오 컨설팅 회사인 ‘놈게임스토리’를 창업하고 게임 시나리오 컨설턴트로 활동 중이다. 안정이 주는 나태함을 싫어해서 앞으로도 비정규직 지식 노동자의 삶을 살아가려 한다.");

            Book b5 = Book.builder()
                    .bio("텍스트 의존은 그만! 게임 플레이를 통해 전달되는 ‘진짜’ 스토리 작성법\n" +
                            "\n" +
                            "『이론과 실전으로 배우는 게임 시나리오』는 ‘게임의 시스템으로 게임 스토리를 전달하는 방법’을 다룬다. 다르게 말하면 ‘플레이를 통해 스토리를 전달하는 법’에 관한 내용이다. 스토리텔링의 개념을 이해하고 실무에 적용하는 방법을 스스로 찾을 수 있을 것이다.")
                    .authors(new ArrayList<Author>(){
                        {
                            add(leejinhee);
                        }
                    })
                    .isbn("1162241292")
                    .name("이론과 실전으로 배우는 게임 시나리오")
                    .highestDepthCategory(gameDesign)
                    .publicationDate(LocalDate.of(2018, 12,1))
                    .stockQuantity(8)
                    .price(27000)
                    .imgSrc("2021\\08\\07\\이론과-실전으로-배우는-게임-시나리오.jfif")
                    .build();

            books.add(b5);


            Book b6 = Book.builder()
                    .bio("게임 개발과 콘텐츠 창작을 위한 궁극의 비기를 전수한다!\n" +
                            "게임 업계를 꿈꾸는 이들을 위한 실전 가이드\n" +
                            "\n" +
                            "게임 시나리오 작가의 일은 스토리 창작에 그치지 않는다. 만든 스토리가 게임 문법으로 구현될 수 있는가를 끊임없이 고민해야 하고, 구현을 위한 장치도 마련해야 한다. 저자는 그런 점에서 게임 시나리오 작업은 기획이며, ‘게임 시나리오 작가’라는 명칭보다는 ‘게임 시나리오 기획자’라는 표현이 더 적절하다고 주장한다. 또 생각한 이미지와는 다를지 몰라도 게임 시나리오 기획은 여전히 매력 있고 장래 충분히 더 유망해질 수 있는 일이라고 말한다.\n" +
                            "\n" +
                            "『게임 시나리오 기획자의 생각법』은 게임 시나리오 작가는 게임과 게임 시나리오를 어떤 관점으로 바라보는가를 다루고 있다. ‘게임 시나리오에 관한 오해와 진실’ ‘게임 시나리오 기획 업무의 실제’ ‘스토리 창작의 기술’ ‘직업 전망과 입문’ 등의 내용을 담고 있다. 게임 시나리오 기획 업무에 관심을 가지고 있거나 업계 입문을 희망하는 사람부터 이미 현직에 있는 주니어 시나리오 기획자까지 다양한 범주의 사람들이 읽고 필요한 정보를 얻을 수 있다. 꼭 게임 시나리오 기획자나 지망생이 아니더라도 게임 업계에 관심을 가지고 있거나 몸담고 있는 사람이라면 누구에게나 유용하다. 게임 제작은 본질적으로 공동 작업이기에 서로의 작업 프로세스를 이해하고 있으면 그만큼 더 유기적으로 일할 수 있기 때문이다.")
                    .authors(new ArrayList<Author>() {
                        {
                            add(leejinhee);
                        }
                    })
                    .isbn("1159256527")
                    .name("게임 시나리오 기획자의 생각법")
                    .highestDepthCategory(gameDesign)
                    .publicationDate(LocalDate.of(2021, 6, 21))
                    .stockQuantity(9)
                    .price(16000)
                    .imgSrc("2021\\08\\07\\게임-시나리오-기획자의-생각법.jfif")
                    .build();

            books.add(b6);


            Author jujinyoung = new Author("주진영", "99년부터 게임 개발을 시작한 게임 디자이너.\n" +
                    "운이 좋아서 게임 개발이 호황일 때 팀에 들어갔고, 운이 좋아서 대규모 프로젝트 팀의 일원으로 참여할 수 있었다. 그렇게 해서 리니지2(엔씨소프트), 블레이드앤 소울(엔씨소프트), 테라(블루홀 스튜디오) 등의 게임 개발에 참여했으며, 다양한 게임에 대한 개발을 시도하는 시기에 개발자였기에 퍼즐게임과 소셜게임, RPG 게임을 만들 수 있었고, 게임 플레이에 대해서 다양한 변화가 일어나던 시기였기에 테이블탑 보드게임과 웹게임, 온라인 게임, 모바일 게임 등을 만들 수 있었다. 그리고 무엇보다도 운이 좋게도 개발에 참여했던 많은 게임들이 상용화되었다.\n" +
                    "게임 개발 환경이 발전하면서 게임을 구현하기 위해 필요한 기술 기반도 마련되었고, 멋진 그래픽도 표현할 수 있으니 이젠 게임 디자인이 발전해야 할 때라고 생각하고 게임을 즐겁게 플레이했던 이들이 좋은 게임을 만들기 위해 도전하기를 기대하고 있다.");


            Book b7 = Book.builder()
                    .bio("게임 디자이너를 위한 문서 작성 기술은\n" +
                            "\n" +
                            "게임 디자이너를 지망하는 많은 이들이 문서 정리의 효용성에 대해서 궁금해 한다. 많은 경험자들은 문서를 팀원들이 모두 꼼꼼하게 읽지 않는다는 것을 알고 있고, 문서에 쓸 문장을 고민하는 것보다 게임 플레이를 한 번이라도 더 해보는 게 낫다고 생각한다.\n" +
                            "\n" +
                            "가장 좋은 경험은 역시 게임을 만들어 보는 것이지만 경험이 없는 이들이 아이디어가 떠오른다고 해서 그것을 곧바로 구현할 수 있을까? 게임은 꽤 복잡한 구성요소를 가지고 있으며 전체적인 모습을 정리하는 것은 나름 훈련이 필요하다. 그리고 무언가를 만들기 위해서는 생각을 정리하는 과정도 필요하다. 문서를 정리하는 것은 자신의 생각을 정리하면서 게임의 형태를 완성시켜볼 수 있는 좋은 연습 과정이 될 수 있다.")
                    .authors(new ArrayList<Author>() {
                        {
                            add(jujinyoung);
                        }
                    })
                    .isbn("8931555407")
                    .name("게임 디자이너를 위한 문서 작성 기술")
                    .highestDepthCategory(gameDesign)
                    .publicationDate(LocalDate.of(2021, 6, 14))
                    .stockQuantity(5)
                    .price(16800)
                    .imgSrc("2021\\08\\07\\게임-디자이너를-위한-문서-작성-기술.jfif")
                    .build();

            books.add(b7);


            Book b8 = Book.builder()
                    .bio("최고의 게임을 만들고 싶다면, 최고의 게임 디자이너가 되어라!!\n" +
                            "\n" +
                            "게임 개발에서 제일 중요한 것은 게임 기획, 게임 디자인이며 좋은 게임 디자인은 기본에 충실하고, 게임을 즐기는 마음에서 나온다.\n" +
                            "\n" +
                            "흔히 게임 기획이라고 말하는 분야는 개발팀 내에서는 게임 디자인이라고 불린다. 말 그대로 게임을 디자인하는 곳입니다. 어떤 게임을 만들 것인지 결정하고, 게임에 대해 전반적인 설계를 합니다. 이 책은 그런 게임 디자인에 대한 내용을 담고 있다. 일부 내용들은 이견이 있을 수도 있습니다. 하지만 이렇게 게임 디자이너들이 서로의 생각을 이야기하고 공유할 수 있는 자리가 좀 더 많아졌으면 좋겠다.")
                    .authors(new ArrayList<Author>() {
                        {
                            add(jujinyoung);
                        }
                    })
                    .isbn("8931556632")
                    .name("게임 디자이너되기")
                    .highestDepthCategory(gameDesign)
                    .publicationDate(LocalDate.of(2021, 1, 14))
                    .stockQuantity(3)
                    .price(18000)
                    .imgSrc("2021\\08\\07\\게임-디자이너되기.jfif")
                    .build();

            books.add(b8);


            Author johnDoran = new Author("존 도란", "일리노이주 피오리아에 거주하는 열정적이고 노련한 테크니컬 게임 디자이너, 소프트웨어 엔지니어, 작가다. 게임 디자이너부터 리드 UI 프로그래머까지 10년 이상 다양한 역할을 맡으며 게임 개발에 관한 광범위하고 전문적인 실무 지식을 쌓았다. 싱가포르, 한국, 미국에서 게임 개발 교육 관련 강의를 해왔으며, 지금까지 게임 개발에 관련된 10권 이상의 책을 썼다. 현재 브래들리 대학교(Bradley University)에서 강사로 일하고 있으며, 이전에는 수상 경력이 있는 비디오 작가였다.\n" +
                    "\n" +
                    "열정적이고 숙련된 기술을 가진 게임 디자이너, 소프트웨어 엔지니어다. 미국 일리노이주 피오리아에 살고 있다. 10년 동안 게임 개발에서 게임 디자이너, 책임 UI 프로그래머 업무를 수행하면서 다양한 실무 경험을 쌓았다. 싱가포르와 대한민국, 미국에서 게임 개발 교육 과정 강의를 했다. 최근까지 게임 개발 관련 책 10여 권에 저자로 참여했다. 현재 브래들리대학교에서 강사로 일하고 있다. 수상 경력이 있는 비디오 촬영 작가이기도 하다.");


            Book b9 = Book.builder()
                    .bio("유니티 엔진으로 모바일 게임을 개발할 때 반드시 알아야 할 기본 사항을 예제와 함께 세심하게 다룬다. 프로토타입 형식의 게임을 같이 구성하면서 최신 모바일 게임이 포함해야 할 트렌디한 기능을 구현한다. 유저 트랙킹, 테스트, SNS, 마케팅까지 다루면서 단순히 게임 개발에서 멈추는 것이 아니라 하나의 종합 패키지의 관점에서 바라본다. 유니티로 모바일 게임을 개발을 만들고자 하는 사람에게 탄탄한 기반을 제공한다.")
                    .authors(new ArrayList<Author>() {
                        {
                            add(johnDoran);
                        }
                    })
                    .isbn("1161755527")
                    .name("유니티 모바일 게임 개발 2/e")
                    .highestDepthCategory(mobileGame)
                    .publicationDate(LocalDate.of(2021, 8, 6))
                    .stockQuantity(15)
                    .price(36000)
                    .imgSrc("2021\\08\\07\\유니티-모바일-게임-개발-2e.jfif")
                    .build();

            books.add(b9);


            Author parkhyungseon = new Author("박형선", "대학교에서 경영학, 마케팅을 공부했고, 20여 개 정도의 정부와 기업 공모전에서 입상했습니다. 퍼블리싱 사업을 하는 게임 사업 PM으로 게임업계에 입문했고 대기업과 중소기업에서 10년 넘게 게임 기획,PD 등 다양한 경험을 했습니다. 직접 기획한 대표작은 ‘우파루마운틴’, ‘라인파이터즈’이며 히어로즈 인 더 스카이의 사업 PM을 맡았습니다.");
            Author minjunhong = new Author("민준홍", "대학교에서 프로그래밍을 공부하다 현재는 철학을 배우는 기획자입니다. 2015년도부터 게임 기획자로서 업계의 문을 두드렸고 현재까지 기획자로 일하고 있습니다. 포트리스M과 포트리스 배틀로얄의 기획을 맡았습니다.");
            Author yusuyeon = new Author("유수연", "2020년 입사한 신입 퍼즐게임 기획자입니다.\n" +
                    "대학에서는 기획에 도움이 될 것이라 생각해 인문학을 공부하였습니다.");

            Book b10 = Book.builder()
                    .bio("이 책은 게임 기획에 사전지식이 없더라도, 하나씩 따라 해 보면서 역기획서와 시스템 기획서를 작성할 수 있도록 해 준다. 게임 기획 업무에서 문서 작성과 커뮤니케이션이 가장 많은 비중을 차지하는데, 그 중에 기본이 되는 문서 작성에 대해서 방향성을 잘 잡고, 준비를 잘할 수 있도록 이 책이 도와줄 것이다.")
                    .authors(new ArrayList<Author>() {
                        {
                            add(parkhyungseon);
                            add(minjunhong);
                            add(yusuyeon);
                        }
                    })
                    .isbn("1165920298")
                    .name("유저를 끌어당기는 모바일 게임 기획")
                    .highestDepthCategory(mobileGame)
                    .publicationDate(LocalDate.of(2020, 12, 18))
                    .stockQuantity(21)
                    .price(26000)
                    .imgSrc("2021\\08\\07\\유저를-끌어당기는-모바일-게임-기획.jfif")
                    .build();

            books.add(b10);


            Author leejaehwan = new Author("이재환", "대학교에서 회계학을 전공했지만 SI로 첫 직장생활을 시작해 지금까지 18년간 개발자로 일해왔다. 은행 폰뱅킹 및 카드사 인터넷 온라인 시스템 등을 개발했으며, 1999년 무렵 우리나라에 메일 서비스 경쟁이 한창 심화될 때는 자바로 자체 제작한 메일 엔진 서버로 기업, 대학 및 관공서에 납품 및 수출까지 했다. 지난 3년간은 아이폰 및 안드로이드 앱을 만들며 애플 앱스토어와 구글 마켓에 본인 및 외주 건으로 약 35여건을 등록했다. 현재는 프리랜서로 아이폰 및 안드로이드 앱 개발을 하고 있으며, 다수의 앱센터에서 아이폰 개발을 강의하고 있다. T아카데미에서 아이폰 증강현실 과정을 강의하고 있으며, 비트교육센터에서 모바일 게임 개발 과정을 강의하고 있다.");


            Book b11 = Book.builder()
                    .bio("이 책은 게임 기획에 사전지식이 없더라도, 하나씩 따라 해 보면서 역기획서와 시스템 기획서를 작성할 수 있도록 해 준다. 게임 기획 업무에서 문서 작성과 커뮤니케이션이 가장 많은 비중을 차지하는데, 그 중에 기본이 되는 문서 작성에 대해서 방향성을 잘 잡고, 준비를 잘할 수 있도록 이 책이 도와줄 것이다.")
                    .authors(new ArrayList<Author>() {
                        {
                            add(leejaehwan);
                        }
                    })
                    .isbn("8998139162")
                    .name("시작하세요! Cocos2d-x 프로그래밍")
                    .highestDepthCategory(mobileGame)
                    .publicationDate(LocalDate.of(2013, 2, 28))
                    .stockQuantity(28)
                    .price(32000)
                    .imgSrc("2021\\08\\07\\시작하세요!-Cocos2d-x-프로그래밍.jpg")
                    .build();

            books.add(b11);


            Author abraham = new Author("Abraham Silberschatz", "『운영체제』 저자이다.");
            Author peter = new Author("Peter Baer Galvin", "『운영체제』 저자이다.");
            Author greg = new Author("Greg Gagne", "『운영체제』 저자이다.");

            Book b12 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(abraham);
                            add(peter);
                            add(greg);
                        }
                    })
                    .name("운영체제")
                    .price(39000)
                    .stockQuantity(38)
                    .publicationDate(LocalDate.of(2020, 2, 28))
                    .isbn("1185475575")
                    .highestDepthCategory(os)
                    .bio("공룡책")
                    .imgSrc("2021\\08\\07\\운영체제.jfif")
                    .build();


            books.add(b12);


            Author josungho = new Author("조성호", "중학교 때 아버지에게 애플 II 컴퓨터를 선물 받은 것을 계기로 한국외국어대학교 전산학과에 진학한 후 고려대학교 컴퓨터학과에서 석사와 박사 학위를 취득했다. 기계만 보면 직접 만져보고 뜯어봐야 직성이 풀리고, 컴퓨터, 카메라, 자동차, 오디오 등에 관심이 많으며, 프로그램으로 무언가 만드는 일을 좋아한다. 2002년에는 신소프트웨어 대상 정보통신부 장관상을 받아 프로그래밍 실력을 인정받기도 했다. 현재 한신대학교 정보통신학부 교수로 재직 중이며, 강의와 신기술 평가 보고서 작성 작업을 열정적으로 하고 있다.");

            Book b13 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(josungho);
                        }
                    })
                    .name("쉽게 배우는 운영체제")
                    .price(28000)
                    .stockQuantity(17)
                    .publicationDate(LocalDate.of(2018, 6, 30))
                    .isbn("1156644070")
                    .highestDepthCategory(os)
                    .bio("명쾌한 비유와 사례로 쉽게 배우는\n" +
                            "운영체제의 구조와 원리\n" +
                            "\n" +
                            "컴퓨터 관련 학과 학생을 대상으로 운영체제의 구조와 원리를 설명한 책이다. 주요 개념은 일상생활의 사례를 통해 쉽게 이해하고, 개념 간의 관계는 다양한 그림과 표로 명확히 정리할 수 있다. 20년 가까이 강의해온 저자의 내공이 고스란히 녹아 있어 복잡하고 어려운 내용이지만 차근차근 공부할 수 있다.\n" +
                            "\n" +
                            "※ 본 도서는 대학 강의용 교재로 개발되었으므로 연습문제 해답은 제공하지 않습니다.")
                    .imgSrc("2021\\08\\07\\쉽게-배우는-운영체제.jfif")
                    .build();

            books.add(b13);


            Author leemanwoo = new Author("이만우", "숭실대학교 컴퓨터학부를 졸업한 후 삼성전자에서 임베디드 소프트웨어를 개발하였다. 이후 미국 회사로 이직하여 지금은 실리콘밸리에 정착했으며, 여전히 임베디드 소프트웨어 엔지니어로 일하고 있다. 〈프로그램 세계〉에 리눅스 관련 강좌를 다수 연재했고, 〈마이크로소프트웨어〉에도 강좌를 연재했다. 저서로는 『도전! 임베디드 OS 만들기』(인사이트, 2009), 『컴퓨터 아나토미』(한울아카데미, 2012)가 있다.");

            Book b14 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(leemanwoo);
                        }
                    })
                    .name("임베디드 OS 개발 프로젝트")
                    .price(24000)
                    .stockQuantity(9)
                    .publicationDate(LocalDate.of(2019, 12, 24))
                    .isbn("8966262546")
                    .highestDepthCategory(os)
                    .bio("나만의 임베디드 운영체제를 만들어 보자.\n" +
                            "\n" +
                            "이 책은 펌웨어 개발 과정을 실시간 운영체제(RTOS)를 만들어 가며 설명한다. 임베디드 운영체제를 개발 환경 구성에서 시작해 최종적으로 RTOS를 만드는 과정(부트로더 제작, 하드웨어 제어, 태스크 간 동기화 등)을 하나하나 추가하며 설명하고 있다. 이렇게 만들어 가는 과정에서 ARM 아키텍처와 운영체제의 핵심 이론을 설명하고 있어 이미 운영체제 이론을 공부한 사람에게는 공부한 내용이 어떻게 다른 방식으로 구현되는지 혹은 어떤 요소가 공통되는지를 알 수 있는 좋은 기회가 될 것이다. 또한 운영체제 이론을 공부하지 않은 사람은 운영체제 이론을 실전으로 공부해 볼 수 있다.")
                    .imgSrc("2021\\08\\07\\임베디드-OS-개발-프로젝트.jfif")
                    .build();

            books.add(b14);


            Author parkhaeseon = Author.builder()
                    .name("박해선")
                    .bio("기계공학을 전공했으나 졸업 후엔 줄곧 코드를 읽고 쓰는 일을 했다. 지금은 ML GDE(Machine Learning Google Developer Expert)로 활동하고 있고, 머신러닝과 딥러닝에 관한 책을 집필하고 번역하면서 소프트웨어와 과학의 경계를 흥미롭게 탐험하고 있다.\n" +
                            "『핸즈온 머신러닝 2판』(한빛미디어, 2020)을 포함해서 여러 권의 머신러닝, 딥러닝 책을 우리말로 옮겼고 『Do it! 딥러닝 입문』(이지스퍼블리싱, 2019)을 집필했다.\n" +
                            "\n" +
                            "ML GDE(Machine Learning Google Developer Expert). 기계공학을 전공했지만 졸업 후엔 줄곧 코드를 읽고 쓰는 일을 했다. 블로그(tensorflow.blog)에 글을 쓰고 텐서플로 문서 번역에 기여하면서 소프트웨어와 과학의 경계를 흥미롭게 탐험하고 있다. 『Do it! 딥러닝 입문』(이지스퍼블리싱, 2019)을 집필하고, 『핸즈온 머신러닝(2판)』(한빛미디어, 2020), 『미술관에 GAN 딥러닝 실전 프로젝트』(한빛미디어, 2019), 『파이썬을 활용한 머신러닝 쿡북』(한빛미디어, 2019), 『머신 러닝 교과서 with 파이썬, 사이킷런, 텐서플로』(길벗, 2019), 『파이썬 라이브러리를 활용한 머신러닝』(한빛미디어, 2019), 『케라스 창시자에게 배우는 딥러닝』(길벗, 2018), 『핸즈온 머신러닝』(한빛미디어, 2018), 『텐서플로 첫걸음』(한빛미디어, 2016)을 우리말로 옮겼다.")
                    .imgSrc("2021\\08\\07\\작가-박해선.jpg")
                    .build();


            Book b15 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(parkhaeseon);
                        }
                    })
                    .name("혼자 공부하는 머신러닝+딥러닝")
                    .price(26000)
                    .stockQuantity(42)
                    .publicationDate(LocalDate.of(2020, 12, 21))
                    .isbn("116224366X")
                    .highestDepthCategory(ai)
                    .bio("- 혼자 해도 충분하다! 1:1 과외하듯 배우는 인공지능 자습서\n" +
                            "이 책은 수식과 이론으로 중무장한 머신러닝, 딥러닝 책에 지친 ‘독학하는 입문자’가 ‘꼭 필요한 내용을 제대로’ 학습할 수 있도록 구성했다. 구글 머신러닝 전문가(Google ML expert)로 활동하고 있는 저자는 여러 차례의 입문자들과 함께한 머신러닝&딥러닝 스터디와 번역·집필 경험을 통해 ‘무엇을’ ‘어떻게’ 학습해야 할지 모르는 입문자의 막연함을 이해하고, 과외 선생님이 알려주듯 친절하게 핵심적인 내용을 콕콕 집어준다. 컴퓨터 앞에서 [손코딩]을 따라하고, 확인 문제를 풀다 보면 그간 어렵기만 했던 머신러닝과 딥러닝을 개념을 스스로 익힐 수 있을 것이다!\n" +
                            "\n" +
                            "- 베타리더가 함께 만든 입문서\n" +
                            "베타리딩 과정을 통해 입문자에게 적절한 난이도, 분량, 학습 요소 등을 고민하고 반영했다. 어려운 용어와 개념은 한 번 더 풀어 쓰고, 복잡한 설명은 눈에 잘 들어오는 그림으로 풀어 냈다. ‘혼자 공부해본’ 여러 입문자의 마음과 눈높이가 책 곳곳에 반영된 것이 이 책의 가장 큰 장점이다.")
                    .build();

            books.add(b15);

            Book b16 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(parkhaeseon);
                        }
                    })
                    .name("Do it! 딥러닝 입문")
                    .price(19800)
                    .stockQuantity(12)
                    .publicationDate(LocalDate.of(2019, 9, 20))
                    .isbn("1163031097")
                    .highestDepthCategory(ai)
                    .bio("정직하게 코딩하며\n" +
                            "딥러닝을 빠르게 정면 돌파하자!\n" +
                            "\n" +
                            "이 책은 어설픈 지름길을 담지 않았다. 공부는 했는데 남는 게 없으면 안 되니까! 실무에서 제대로 알고 써야 하니까! 국내 6명뿐인 구글 인증 머신러닝 전문가(ML GDE; Machine Learning Google Developer Experts)이자 인공지능 분야 서적, 최다 번역을 진행한 박해선 선생이 이번에는 딥러닝 입문서를 집필했다.\n" +
                            "\n" +
                            "이 책은 개념 한 걸음, 수식 한 걸음 그리고 코딩 한 걸음. 가장 적당한 보폭과 올곧은 방향으로 독자를 딥러닝으로 안내한다. 또한 그래프, 삽화, 도해는 100개가 넘어 추상적인 개념도 쉽고 빠르게 받아들일 수 있다. 프로그램 설치 없이 웹 브라우저에 접속하기만 하면 실습을 바로 시작할 수 있다는 점도 이 책만의 특징이다.\n" +
                            "\n" +
                            "편안하게 이론을 이해한 다음 직접 코딩하며 눈으로 딥러닝 대표 문제 4가지를 정복하니 딥러닝의 교과서로 부족함이 없다. 꼭 짚고 넘어가야 할 개념이나 용어는 본문 중간에 나오는 ‘잠깐! 다음으로 넘어가려면’ 코너와 장 마지막에‘기억 카드’ 코너로 2번 복습하여 학습 효과를 높였다. 『Do it! 딥러닝 입문』과 함께 딥러닝을 빠르게 정면 돌파해 보자.")
                    .build();

            books.add(b16);


            Author jerong = new Author("오렐리앙 제롱", "머신러닝 컨설턴트. 2013년에서 2016년까지 구글에서 유튜브 동영상 분류팀을 이끌었다. 2002년에서 2012년까지 프랑스의 모바일 ISP 선두 주자인 Wifirst를 설립하고 CTO로 일했다. 2001년에는 Polyconseil을 설립하고 CTO로 일했다. 이 회사는 현재 전기차 공유 서비스인 Autolib’을 운영한다. 그 전에는 재무(JP 모건과 소시에테 제네랄), 방위(캐나다 DOD), 의료(수혈) 등 다양한 분야에서 엔지니어로 일했다. C++, WiFi, 인터넷 구조에 대한 몇 권의 기술 서적을 썼으며 한 프랑스 공과대학교에서 컴퓨터 과학을 가르쳤다.");
            Book b17 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(jerong);
                        }
                    })
                    .name("핸즈온 머신러닝")
                    .price(55000)
                    .stockQuantity(19)
                    .publicationDate(LocalDate.of(2020, 5, 4))
                    .isbn("1162242965")
                    .highestDepthCategory(ai)
                    .bio("머신러닝 전문가로 이끄는 최고의 실전 지침서\n" +
                            "텐서플로 2.0을 반영한 풀컬러 개정판\n" +
                            "\n" +
                            "『핸즈온 머신러닝』은 지능형 시스템을 구축하려면 반드시 알아야 할 머신러닝, 딥러닝 분야 핵심 개념과 이론을 이해하기 쉽게 설명한다. 사이킷런, 케라스, 텐서플로를 이용해 실전에서 바로 활용 가능한 예제로 모델을 훈련하고 신경망을 구축하는 방법을 상세하게 안내한다. 장마다 제공하는 연습문제를 풀며 익힌 내용을 확인하고 응용할 수도 있다. 머신러닝을 배우고 싶지만 어디서부터 시작해야 할지 막막하다면, 이 책이 인공지능 마스터로 가는 길에 좋은 친구가 될 것이다.\n" +
                            "\n" +
                            "이번에 출간된 2판은 텐서플로 2를 반영하고 비지도 학습, 자연어 처리, 생성적 적대 신경망(GAN) 등 최신 기법을 추가했다. 사이킷런과 텐서플로 2에 더해 케라스를 사용하며, 예제 코드도 소프트웨어 최신 버전에 맞춰 갱신했다. 1부(머신러닝)에는 1개 장이 추가되었고, 2부(신경망과 딥러닝)는 최신 딥러닝 기법을 방대하게 수록하여 대폭 개정되었다. 부록 2개 장이 추가되었으며, 시각적 편의를 위해 전면 컬러로 인쇄했다.")
                    .build();

            books.add(b17);


            Author saitogoki = new Author("사이토 고키", "1984년 나가사키 현 쓰시마 태생. 도쿄공업대학교 공학부를 졸업하고 도쿄대학대학원 학제정보학부 석사 과정을 수료했다. 현재는 기업에서 컴퓨터 비전과 기계학습 관련 연구·개발에 매진하고 있다. 오라일리재팬에서 『밑바닥부터 시작하는 딥러닝』을 집필했으며, 『실천 파이썬 3』, 『컴퓨터 시스템의 이론과 구현』, 『실천 기계학습 시스템』 등을 번역했다.");
            Book b18 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(saitogoki);
                        }
                    })
                    .name("밑바닥부터 시작하는 딥러닝")
                    .price(24000)
                    .stockQuantity(9)
                    .publicationDate(LocalDate.of(2017, 1, 2))
                    .isbn("8968484635")
                    .highestDepthCategory(ai)
                    .bio("직접 구현하고 움직여보며 익히는 가장 쉬운 딥러닝 입문서\n" +
                            "\n" +
                            "이 책은 라이브러리나 프레임워크에 의존하지 않고, 딥러닝의 핵심을 ‘밑바닥부터’ 직접 만들어보며 즐겁게 배울 수 있는 본격 딥러닝 입문서이다. 술술 읽힐 만큼 쉽게 설명하였고, 역전파처럼 어려운 내용은 ‘계산 그래프’ 기법으로 시각적으로 풀이했다. 무엇보다 작동하는 코드가 있어 직접 돌려보고 요리조리 수정해보면 어려운 이론도 명확하게 이해할 수 있다. 딥러닝에 새롭게 입문하려는 분과 기초를 다시금 정리하고 싶은 현업 연구자와 개발자에게 최고의 책이 될 것이다.")
                    .build();

            books.add(b18);


            Author leebyeonguk = new Author("이병욱", "(주)크라스랩 대표 이사이자 서울과학종합대학원 디지털금융 MBA 주임교수를 맡고 있다. 한국과학기술원(KAIST) 전산학과 계산이론 연구실에서 학사 및 석사학위를 취득했다. 공학을 전공한 금융 전문가로 세계 최초의 핸드 헬드-PC(Handheld-PC) 개발에 참여해 한글 윈도우 CE 1.0과 2.0을 마이크로 소프트 사에서 공동 개발했다. 1999년에는 전 보험사 보험료 실시간 비교 서비스를 제공하는 핀테크 전문회사인 (주)보험넷을 창업해 업계에 큰 반향을 불러일으켰다. 이후 삼성생명을 비롯한 생명 및 손해 보험사에서 CMO(마케팅 총괄 상무), CSMO(영업 및 마케팅 총괄 전무) 등을 역임하면서 혁신적인 상품과 서비스를 개발 및 총괄했다. 세계 최초로 파생 상품 ELS를 기초 자산으로 한 변액 보험을 개발해 단일 보험 상품으로 5,000억 원 이상 판매되는 돌풍을 일으켰고, 매일 분산 투자하는 일 분산 투자(daily averaging) 변액 보험을 세계 최초로 개발해 상품 판매 독점권을 획득했다. 최근에는 머신러닝 기반의 금융 분석과 블록체인에 관심을 갖고 다양한 활동을 하고 있다. 저서로는 『블록체인 해설서』(에이콘, 2019)와 『비트코인과 블록체인, 가상자산의 실체 2/e』(에이콘, 2020)이 있다.");
            Book b19 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(leebyeonguk);
                        }
                    })
                    .name("비트코인과 블록체인, 가상자산의 실체 2/e")
                    .price(24000)
                    .stockQuantity(7)
                    .publicationDate(LocalDate.of(2020, 9, 29))
                    .isbn("1161754598")
                    .highestDepthCategory(blockChain)
                    .bio("『비트코인과 블록체인, 탐욕이 삼켜버린 기술』의 2판 출간!\n" +
                            "대한민국학술원선정 2019 교육부 우수학술도서 『블록체인 해설서』 저자 신간,\n" +
                            "블록체인과 가상자산의 실체를 파헤쳤다!\n" +
                            "\n" +
                            "블록체인과 암호화폐 그리고 새로이 법적으로 정의되는 가상자산의 개념에 대해 설명한다. 2판에서는 1판보다 더 많은 개념과 이론, 사례를 다루며, 비유를 통해 쉽게 이해할 수 있도록 설명한다. 블록체인의 범용성이란 어떤 것인지, 블록체인의 세부 작동 원리는 어떤 것인지, 그 진정한 효용은 무엇인지 모두 알려준다. 또한 새로이 법적으로 정의되고 있는 가상자산에 대해서도 자세히 알아본다.").build();

            books.add(b19);


            Book b20 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(leebyeonguk);
                        }
                    })
                    .name("블록체인 해설서")
                    .price(19500)
                    .stockQuantity(6)
                    .publicationDate(LocalDate.of(2019, 2, 28))
                    .isbn("1161752706")
                    .highestDepthCategory(blockChain)
                    .bio("블록체인 기술의 실체가 무엇인지 명확히 설명해 주는 해설서다. 블록체인이 만들어지게 된 역사적 배경은 물론 그 작동 원리를 상세히 설명해 준다. 이를 통해 진정한 효용이 무엇인지 알려준다. 수많은 블록체인들 중 가장 대표적인 비트코인, 이더리움 그리고 하이퍼레저의 작동 원리를 각각 설명 및 비교하고, 이들이 과연 어떤 효용을 가지고 있는 것인지 미래에는 어떨 것인지 명쾌한 해답을 던져 준다. 현재 블록체인 프로젝트에 관여하고 있거나, 도입을 검토 중인 회사의 일원이라면 반드시 읽어보기를 권한다. 또 블록체인 기술이라는 것이 도대체 무엇인지, 미래에는 어떠할 것인지 그 실체를 명확히 알고 싶은 모든 독자에게 명쾌한 해답을 줄 것이다.")
                    .build();

            books.add(b20);

            Author bina = new Author("비나 라마머시", "장애 복원력이 있는 분산 시스템으로 박사 학위를 받았으며, 암호학, P2P 네트워킹, 분산 시스템을 주제로 30년간 강의를 해왔다. 코세라(Coursera) MOOC 플랫폼에 개설되어 있는, 블록체인 기술에 대한 네 개의 버팔로대학교 전문코스 과정을 만들었으며, 강의도 직접 하고 있다. 2019년 뉴욕주립대(SUNY) 우수교수상을 수상했다.");
            Book b21 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(bina);
                        }
                    })
                    .name("블록체인 인 액션")
                    .price(30000)
                    .stockQuantity(4)
                    .publicationDate(LocalDate.of(2021, 7, 26))
                    .isbn("1191600122")
                    .highestDepthCategory(blockChain)
                    .bio("블록체인에 관심 있는 모든 사람을 위한 훌륭한 출발점!\n" +
                            "Dapp 개발에 대한 명확하고 통찰력이 가득한 책!\n" +
                            "\n" +
                            "《블록체인 인 액션》은 블록체인 기반 탈중앙화 애플리케이션을 디자인하고 개발하기 위한 포괄적인 안내서다. 이 책의 내용을 숙지하면 스마트 컨트랙트와 블록체인 애플리케이션 개발을 바로 시작할 수 있다. 블록체인을 이해하기 위해 다른 이론적인 자료들을 다시 참조할 필요가 없을 정도로 매우 상세한 설명을 제공한다.").build();

            books.add(b21);


            Author woojaenam = new Author("우재남", "서강대학교에서 정보시스템 전공으로 석사 과정을 마친 후 다양한 IT 관련 분야에서 실전 업무를 수행했고, 대학교에서 프로그래밍, 데이터베이스, 운영체제 등을 강의해 왔습니다. 현재 디티솔루션의 공간데이터베이스 연구소장으로, 공간 정보와 IT 융합 학문인 유시티 IT 분야의 공학박사 학위도 취득했습니다. 지금도 한양사이버대학교 컴퓨터공학과와 삼성, LG, 현대, CJ, KT, SK, 대한상공회의소 등에서 인공지능 및 IT 전문 분야를 강의하고 있습니다. 자신이 체험한 다양한 IT 실무 경험과 지식을 최대한 쉽고 빠르게 수강생과 독자에게 전달하는 것을 모토로 강의와 집필을 하고 있습니다. 한빛미디어와 한빛아카데미에서 『뇌를 자극하는 Redhat Fedora: 리눅스 서버 & 네트워크』(2005)를 시작으로 『IT CookBook, 코틀린을 활용한 안드로이드 프로그래밍』(2020) 등 40여 권을 집필했으며, 『Head First HTML and CSS(개정판)』(2013)를 번역했습니다.\n" +
                    "\n" +
                    "서강대학교에서 정보시스템 전공으로 석사 과정을 마치고, 줄곧 다양한 IT 관련 분야에서 실무를 경험하며 대학에서 데이터베이스, 운영체제, 프로그래밍 등의 과목을 강의해왔다. 현재는 디티솔루션의 공간데이터베이스 연구소장으로 재직 중이며, 공간정보와 IT의 융합 학문인 유시티 IT 분야의 공학박사 학위도 취득했다. 저자는 자신이 체험한 다양한 IT 실무 경험과 지식을 최대한 쉽고 빠르게 수강생과 독자에게 전달하는 것을 강의와 집필의 모토로 삼고 있다.\n" +
                    "\n" +
                    "저서로는 (한빛미디어/한빛아카데미)\n" +
                    "『이것이 MariaDB다』(2019)\n" +
                    "『이것이 Fedora 리눅스다』(2019)\n" +
                    "『이것이 오라클이다』(2018)\n" +
                    "『이것이 Windows Server다』(2017)\n" +
                    "『이것이 우분투 리눅스다』(2017)\n" +
                    "『이것이 SQL Server다』(2016)\n" +
                    "『뇌를 자극하는 Windows Server 2012 R2』(2016)\n" +
                    "『IT CookBook, Android Studio를 활용한 안드로이드 프로그래밍(5판)』(2020)\n" +
                    "『IT Cookbook for Beginner, 파이썬(2판)』(2020)\n" +
                    "『IT Cookbook for Beginner, 리눅스』(2020)\n" +
                    "『IT Cookbook for Beginner, 데이터베이스』(2019)\n" +
                    "『IT Cookbook for Beginner, 자바 프로그래밍』(2018)번역서(한빛미디어)\n" +
                    "『Head First HTML and CSS(개정판)』(2013)등이 있다.");

            Book b22 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(woojaenam);
                        }
                    })
                    .name("이것이 MySQL이다")
                    .price(32000)
                    .stockQuantity(7)
                    .publicationDate(LocalDate.of(2020, 5, 10))
                    .isbn("1162242787")
                    .highestDepthCategory(db)
                    .bio("『이것이 MySQL이다』 개정판\n" +
                            "최신 8.0 버전 반영! 파이썬 연동 내용 수록!\n" +
                            "\n" +
                            "2016년 출간 후 많은 사랑을 받아 온 『이것이 MySQL이다』가 MySQL 8.0 버전을 반영하여 개정되었다. 특히 ‘파이썬 기초 및 파이썬과 데이터베이스의 연동’, ‘ 파이썬으로 공간 데이터 응용 프로그래밍 작성하기’ 등의 내용을 추가하여 더욱 실무 밀착형 교재로 업그레이드 되었다.\n" +
                            "\n" +
                            "이 책은 실무에서 바로 적용 가능한 80여 가지 다양한 실습 예제를 수록하여 현업에서 이뤄지고 있는 데이터베이스 개발과 운영을 모두 체험할 수 있게 구성하였다. 또 어려운 실습도 쉽게 익힐 수 있도록 40여 개의 [저자 동영상 강의]를 무료로 제공하고 있다. 그래도 어려운 부분이 있다면 [이것이 MySQL이다 네이버 카페]에 질문을 올려보자. 저자가 직접 모든 질문에 1:1로 답변을 달아주며, MySQL 관련 최신 기술 소식도 공유할 수 있다.")
                    .imgSrc("2021\\08\\07\\이것이MySQL이다.jfif")
                    .build();

            books.add(b22);


            Author nadongbin = new Author("나동빈", "욕심 많은 그는 개발자, 유튜버, 강사 그리고 대학원생까지 1인 4역을 소화하고 있다. 한국인터넷진흥원(KISA), 프로그래머스, 패스트캠퍼스, 삼성멀티캠퍼스, KG에듀원, 국내 소프트웨어 마이스터고등학교 등 다양한 현장에서 코딩 테스트를 비롯해 IT 관련 주제를 강의했다. 학부 시절에는 각종 IT 대회에 출전해 경험을 쌓았고, 졸업 후 개발자로 직장 생활을 하다가 공부에 대한 부족함과 욕구가 생겨 현재 포항공과대학에서 석사 과정 중이다. 2015년부터 유튜브에서 개발 채널을 운영해왔고, 어느덧 9만 명이 넘는 구독자가 참여하고 있다.");

            Book b23 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(nadongbin);
                        }
                    })
                    .name("이것이 취업을 위한 코딩 테스트다 with 파이썬")
                    .price(34000)
                    .stockQuantity(11)
                    .publicationDate(LocalDate.of(2020, 8, 5))
                    .isbn("1162243074")
                    .highestDepthCategory(dsAlgo)
                    .bio("나동빈 저자의 유튜브 라이브 방송 https://www.youtube.com/c/dongbinna\n" +
                            "\n" +
                            "IT 취준생이라면 누구나 입사하고 싶은 카카오 · 삼성전자 · 네이버 · 라인!\n" +
                            "\n" +
                            "취업의 성공 열쇠는 알고리즘 인터뷰에 있다!\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "IT 취준생이라면 누구나 가고 싶어 하는 카카오, 라인, 삼성전자의 2016년부터 2020년까지의 코딩 테스트와 알고리즘 대회의 기출문제를 엄선하여 수록했다. 최근 5년간의 코딩 테스트 기출문제를 분석하여 반드시 알아야 하는 알고리즘을 8가지로 정리했다. 8가지 핵심 알고리즘 이론을 쉽게 설명하고, 관련 실전 문제를 풀이했다. 출제 유형 분석, 이론 설명, 기출문제 풀이까지! 어떤 코딩 테스트도 대비할 수 있을 것이다. 코딩 테스트에서 주로 선택하는 파이썬을 기반으로 설명되어 있으며, 파이썬 코드 외에도 C/C++, 자바 코드를 추가로 제공한다.")
                    .imgSrc("2021\\08\\07\\이것이코딩테스트다.jfif")
                    .build();

            books.add(b23);


            Author jonathansteinheart = new Author("조너선 스타인하트", "1960년대부터 엔지니어링을 해왔다. 중학교 때 하드웨어 설계를 시작했고, 고등학교 때 소프트웨어 설계를 해본 경험 덕분에 여름 단기 아르바이트로 벨 전화 연구소에서 일하는 기회를 얻을 수 있었다. 클라크슨 대학교에서 1977년 전자 공학 및 컴퓨터 과학 학사(BSEE)를 취득했으며, 졸업 후 텍트로닉스(Tektronix)에서 일하다가 스타트업을 창업했다. 그래픽스 하드웨어와 소프트웨어를 설계하고, CAD 시스템, 그래픽스 워크스테이션, 회로 시뮬레이터, 발전소, IC 설계용 언어 등을 만들었으며, 1987년에는 안전이 중요한 크리티컬 시스템 엔지니어링에 초점을 맞춘 컨설턴트가 되어 애플, 인텔, 썬, 웰치알린, 룰루 등의 기업을 컨설팅했다. 1990년대부터는 전업을 조금 줄이고, 포 윈즈 비냐드(Four Winds Vineyard)라는 와인 농장 경영도 겸업하고 있다.");

            Book b24 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(jonathansteinheart);
                        }
                    })
                    .name("한 권으로 읽는 컴퓨터 구조와 프로그래밍")
                    .price(35000)
                    .stockQuantity(14)
                    .publicationDate(LocalDate.of(2021, 4, 5))
                    .isbn("1189909286")
                    .highestDepthCategory(architecture)
                    .bio("대부분의 개발자들은 자신이 만든 프로그램을 움직이는 하부 기술에 대해 잘 알지 못한다. 코드가 잘 도는데 구태여 근원적인 하부 기술에 신경을 써야 할까? 그렇다. 하부 기술을 밑바닥부터 이해하면, 프로그램이 잘 작동하게 만들 수 있고 찾기 어려운 버그에 당황하지 않게 된다. 자신이 작성한 코드가 보안 문제로 인해 9시 뉴스에 나오기를 원하는 사람은 없을 것이다. 수많은 기술에 대한 상세 자료가 이미 온라인에 존재한다. 하지만 대부분 이런 자료들은 잘 정리되어 있지 않으며, 한꺼번에 정리해 모아둔 곳을 찾기도 어렵다.\n" +
                            "\n" +
                            "백전노장 엔지니어인 조너선 스타인하트가 쓴 『한 권으로 읽는 컴퓨터 구조와 프로그래밍』은 컴퓨터의 토대가 되는 개념을 밀도 있게 탐구한다. 또한 컴퓨터 하드웨어의 내부 구조는 물론, 하드웨어 위에서 소프트웨어가 작동하는 원리, 그리고 소프트웨어 기술 발전과 함께 역사 속에서 사람들은 기술을 사용해 어떻게 문제를 풀어왔는지 등의 다양하고도 심도 있는 내용을 다룬다. 저자는 컴퓨터라는 기계에서 프로그램 코드가 실행될 때 벌어지는지 일들에 대해 여러분이 미처 몰랐던, 혹은 잊었던 이야기들을 들려준다. 더 나은 소프트웨어와 효율적인 코드를 작성하는 법을 깨우치는 데 필요한 탄탄한 기초를 다질 수 있는 책이다.")
                    .imgSrc("2021\\08\\07\\한권컴퓨터구조.jfif")
                    .build();

            books.add(b24);


            Author shinyongkwon = new Author("신용권", "저자는 20년 동안 시스템 제어 및 애플리케이션 개발자로 활동한 베테랑 개발자이자, IT 전문 교육자이기도 하다. 메카트로닉스를 전공했으며 삼성항공 시스템 설계 파트에서 하드웨어 제어용 소프트웨어 개발을 담당했다. 1998년부터 지금까지 자바 개발자로, 그리고 자바 강사로 활동하고 있다. 삼성멀티캠퍼스(SDS)에서 자바 전임교수로 근무했고, 유수한 교육기관에서 재직자 및 전문가 위탁교육을 진행해 왔다. 또한 국내 최초로 재직자를 위한 ‘HTML5 모바일 웹앱 & 하이브리드앱’ 교육과정을 개설하여 많은 전문 개발자를 양성했다. 최근에는 오픈 소스 프레임워크, 안드로이드, HTML5 하이브리드앱 등의 분야에서 대학전산망 및 현업 재직자를 위한 소프트웨어 역량 강화 사업의 전임강사로 활동했다. 현재는 SK Planet에서 운영하는 T 아카데미에서 자바, 웹, 안드로이드 앱 개발 강사로 활동하고 있다.");


            Book b25 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(shinyongkwon);
                        }
                    })
                    .name("혼자 공부하는 자바")
                    .price(24000)
                    .stockQuantity(17)
                    .publicationDate(LocalDate.of(2019, 6, 10))
                    .isbn("116224187X")
                    .highestDepthCategory(java)
                    .bio("혼자 해도 충분하다!\n" +
                            "1:1 과외하듯 배우는 자바 프로그래밍 자습서 (JAVA 8 &11 지원)\n" +
                            "\n" +
                            "이 책은 독학으로 자바를 배우는 입문자가 ‘꼭 필요한 내용을 제대로’ 학습할 수 있도록 구성했다. ‘무엇을’ ‘어떻게’ 학습해야 할지 조차 모르는 입문자의 막연한 마음을 살펴, 과외 선생님이 알려주듯 친절하게, 그러나 핵심적인 내용만 콕콕 집어준다. 책의 첫 페이지를 펼쳐서 마지막 페이지를 덮을 때까지, 혼자서도 충분히 자바를 배울 수 있다는 자신감과 확신이 계속될 것이다!")
                    .imgSrc("2021\\08\\07\\혼공자.jfif")
                    .build();

            books.add(b25);


            Book b26 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(shinyongkwon);
                        }
                    })
                    .name("이것이 자바다")
                    .price(30000)
                    .stockQuantity(16)
                    .publicationDate(LocalDate.of(2015, 1, 5))
                    .isbn("9788968481475")
                    .highestDepthCategory(java)
                    .bio("15년 이상 자바 언어를 교육해온 자바 전문강사의 노하우를 아낌 없이 담아낸 자바 입문서. 저자 직강의 인터넷 강의와 Q/A를 위한 커뮤니티(네이커 카페)까지 무료로 제공하여 자바 개발자로 가는 길을 안내한다.\n" +
                            "\n" +
                            "동영상 링크 : https://www.youtube.com/playlist?list=PLVsNizTWUw7FPokuK8Cmlt72DQEt7hKZu")
                    .imgSrc("2021\\08\\07\\이것이자바다.jfif")
                    .build();

            books.add(b26);


            Author yunseongwoo = new Author("윤성우", "벤처회사에서 개발자로 일하다가 IT분야의 집필과 강의로 처음 이름이 알려진 그는 2000년대 초반까지는 소프트웨어 아키텍트(Architect)로 일을 하다가, 2004년부터 지금까지 OpenGL-ES 그래픽스 라이브러리의 구현과 3D 가속 칩의 개발 및 크로노스 그룹(모바일 국제 표준화 컨소시엄)의 표준안에 관련된 일에 참여하였다.\n" +
                    "\n" +
                    "또한 핸드폰용 DMB 칩의 개발에도 참여하였으며, 현재는 ㈜액시스소프트의 CTO로 있으면서 웹 기반 솔루션 개발에 관심을 갖고 있다. 시간이 날 때마다 조깅을 하며 다양한 구상을 하는 저자는, 방법론에 근거한 소프트웨어 설계 전문가가 모든 분야에서 활발히 활동하여 소프트웨어 발전에 기여해야 한다는 생각을 갖고 있다.\n" +
                    "\n" +
                    "프로그래밍을 공부한다면 모르는 사람이 없을 것 같은 베스트셀러 저자이다. 여전히 쉽게 설명하는 방법에 대해 고민하고 있으며 그 고민 결과를 바탕으로 책을 집필하고 있다. 프로그래머라는 직업에 충실하기위해 적지않은 기간동안 집필활동이 없었지만 그간 축적된 에너지를 바탕으로 다수의 책을 쓸 계획을가지고 집필에 임하고 있다.");


            Book b27 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(yunseongwoo);
                        }
                    })
                    .name("윤성우의 열혈 C 프로그래밍")
                    .price(25000)
                    .stockQuantity(40)
                    .publicationDate(LocalDate.of(2010, 11, 1))
                    .isbn("8996094056")
                    .highestDepthCategory(cLanguage)
                    .bio("2003년도에 출간된 윤성우 저 「열혈강의 C 프로그래밍」의 개정판이다. 출간 이후 가장 많은 독자들이 선택해온 C언어 기본서로서 그 자리를 계속해서 이어가기에 부족함이 없도록 개정되었다.\n" +
                            "본 도서는 강의가 필요 없을 만큼 쉽게 설명되어 있는 책이다. 하지만 강의가 필요한 독자들을 위해서 저자가 직접 본 도서의 내용을 강의한다. 책만큼 유명한 이 강의와 함께 한다면 누구나 C언어를 쉽게 공부할 수 있을 것이다.\n" +
                            "\n" +
                            "그 사이 저자가 여러 서적을 집필하면서 쌓아온 경험과 독자들의 다양한 의견을 바탕으로 새롭게 태어난 이번 개정판은 여러분이 C언어를 공부하는데 있어서 가장 든든한 친구로 함께할 것이다.")
                    .imgSrc("2021\\08\\07\\열혈C.jfif")
                    .build();

            books.add(b27);


            Book b28 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(yunseongwoo);
                        }
                    })
                    .name("윤성우의 열혈 C++ 프로그래밍")
                    .price(27000)
                    .stockQuantity(42)
                    .publicationDate(LocalDate.of(2010, 5, 12))
                    .isbn("8996094048")
                    .highestDepthCategory(cplpl)
                    .bio("2004년도에 출간된 윤성우 저자 「열혈강의 C++ 프로그래밍」의 개정판이다. C언어를 이해하고 있는 독자들을 대상으로 한 C++ 기본서로서, 초보자에게 적절한 설명과 예제를 통해서 C++ 학습에 재미를 더하고 있다. 개정판에서는 초판에는 없었던 내용들이 약 120 페이지 분량으로 추가하였으며, 예제도 시대의 흐름에 맞게 수정하였다.\n" +
                            "\n" +
                            "총 4개의 파트로, 객체지향의 완성-전개-도입-C++로의 전환을 다루고 있다. 각 장이 끝날때 마다 연습문제를 수록하여 내용의 이해 정도를 스스로 확인해볼 수 있다. C언어 기반의 C++에 대한 기본적인 이해부터 클래스, 복사 생성자, 상속, 연산자 등의 세부 내용까지 다루어 C++프로그래밍에 대한 자신감을 갖게 한다.")
                    .imgSrc("2021\\08\\07\\열혈C플플.jfif")
                    .build();

            books.add(b28);


            Author leewoongmo = new Author("이웅모", "일본에서 컴퓨터공학을 전공한 후 일본의 자동차 연구소 공용 웹 프레임워크 개발 프로젝트를 시작으로 프로그래밍 세계에 발을 들여 놓았다. 이후 외국계 IT 기업에서 소프트웨어 컨설턴트로 재직하였고 현재 소프트웨어 개발사의 대표를 맡고 있다. 프런트엔드 튜토리얼 poiemaweb.com의 운영자이며 패스트캠퍼스에서 자바스크립트를 강의 중이다. 지은 책으로 《Angular Essentials》(루비페이퍼, 2018)가 있다.");


            Book b29 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(leewoongmo);
                        }
                    })
                    .name("모던 자바스크립트 Deep Dive")
                    .price(45000)
                    .stockQuantity(29)
                    .publicationDate(LocalDate.of(2020, 9, 25))
                    .isbn("1158392230")
                    .highestDepthCategory(js)
                    .bio("『모던 자바스크립트 Deep Dive』에서는 자바스크립트를 둘러싼 기본 개념을 정확하고 구체적으로 설명하고, 자바스크립트 코드의 동작 원리를 집요하게 파헤친다. 따라서 여러분이 작성한 코드가 컴퓨터 내부에서 어떻게 동작할 것인지 예측하고, 명확히 설명할 수 있도록 돕는다. 또한 최신 자바스크립트 명세를 반영해 안정적이고 효율적인 코드를 작성할 수 있는 기본기를 다지고, 실전에서 쓰이는 모던 자바스크립트 프레임워크나 도구를 완벽하게 이해하고 활용할 수 있게 도와준다.")
                    .imgSrc("2021\\08\\07\\모던자스딥다이브.jfif")
                    .build();

            books.add(b29);


            Author daveThomas = new Author("데이브 토머스", "[소개 없음]");

            Book b30 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(daveThomas);
                        }
                    })
                    .name("프로그래밍 루비")
                    .price(44000)
                    .stockQuantity(10)
                    .publicationDate(LocalDate.of(2015, 11, 12))
                    .isbn("8966261671")
                    .highestDepthCategory(ruby)
                    .bio("“프로그래밍 언어 루비의 기초부터 심연까지”\n" +
                            "\n" +
                            "1993년 처음 고안된 이후로 루비는 레일스(Rails), 셰프(Chef), 퍼핏(Puppet) 등 유명 소프트웨어를 구현하는 데 쓰였고 트위터, 깃허브 등 IT 회사에서 초창기에 빠르고 효율적으로 서비스를 구축하는 데 큰 몫을 한 프로그래밍 언어다. 루비 사용자 사이에서 ‘곡괭이 책’으로 잘 알려진 『프로그래밍 루비』는 지난 20여 년간 발전해 온 루비의 거의 모든 것을 담고 있는 루비 해설서다. 이번 개정판에서는 특히 루비 20주년 기념으로 발표된 루비 2를 중심으로 루비 2의 새로운 문법을 비롯해 여러 가지 새로운 기능을 다루고 있다.\n" +
                            "\n" +
                            "이 책에서 다루는 내용\n" +
                            "* 루비 기본 문법\n" +
                            "* 블록, 반복자, 믹스인 등 루비의 장점 활용하기\n" +
                            "* 루비 패키지 만들기, 루비 문서화\n" +
                            "* 마이크로소프트 윈도 환경에서 루비 사용하기\n" +
                            "* 메타프로그래밍 등 고급 기법")
                    .imgSrc("2021\\08\\07\\루비.jpg")
                    .build();

            books.add(b30);



            Author kimchanghyun = new Author("김창현", "저자 김창현은 프로그래머가 아니라 연구원이다. 서울대학교 지리학과를 거쳐 동 대학원에서 지리학 박사 학위를 받았다. 고등학교 2학년 때 《월간 인물과 사상》에 ‘한 고등학생의 《태백산맥》 읽기’라는 글로 글쓰기를 시작했다. 2006년 다녀온 유라시아 여행기로 첫 책을 출간하였고(《질러, 유라시아!》, 2011), 한 때 대치동에서 논술학원 강사로 글을 가르치기도 했다. 또한 팟캐스트를 운영한 경험을 살려 팟캐스트와 유튜브 실전 사용법 책을 출간했다(《된다! 팟캐스트&유튜브 실전 제작법》, 2016). 2017년부터 2019년까지는 <충남일보> 고정 칼럼니스트로 철학, 부동산, 블록체인, 지역경제 등을 다룬 칼럼을 연재했다. 2014년 박사 논문을 끝낼 때쯤 파이썬을 처음 만난 뒤로 주말에는 업무에 필요한 파이썬 프로그램을 혼자 개발할 만큼 파이썬 프로그래밍에 푹 빠졌다. 우연히 출판사에 놀러 갔다가 덜컥 계약해 주말을 반납하고 1년 동안 밤잠 줄여 가며 이 책을 썼다. 처음에는 업무에 필요한 간단한 프로그램을 파이썬으로 만들어 쓰다가 지금은 파이썬을 활용한 데이터 가공, 통계 처리, 웹 정보 수집, 자연어 처리로 점점 관심사를 넓혀가고 있다. 지금은 한 대학 소속 타당성 조사 기관에서 일하며, 언젠가 파이썬이 보고서를 대신 써줄 날이 올 것이라 믿고 보고서 자동화 모듈을 틈틈이 개발하고 있다.");

            Book b31 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(kimchanghyun);
                        }
                    })
                    .name("Do it! 파이썬 생활 프로그래밍")
                    .price(20000)
                    .stockQuantity(31)
                    .publicationDate(LocalDate.of(2020, 7, 27))
                    .isbn("1163031739")
                    .highestDepthCategory(python)
                    .bio("뼛속까지 문과생인 지리학 박사가 집필한 파이썬 생활 프로그래밍 책!\n" +
                            "웹 크롤링부터 데이터 분석까지, 11가지 프로그램을 내 손으로 직접 만든다!\n" +
                            "\n" +
                            "생활 프로그래머 김창현 박사가 일상과 업무에서 파이썬 프로그래밍을 활용한 경험을 바탕으로 만들어졌다. 가장 쉬운 활용법인 계산기부터 미드 ‘프렌즈’에서 대사 추출, 아파트 실거래가 통계 분석, 포털 사이트 기사 수집까지 총 11개 프로그램을 직접 만들어 볼 수 있다. 이 프로그램들은 파이썬 문법을 한 번이라도 배운 적이 있는 사람이라면 누구나 따라 할 수 있는 수준으로 설계했다.\n" +
                            "\n" +
                            "또한, 각각의 프로그램은 우리 삶과 밀접한 생활 속 주제이므로 프로그램의 동작 방식을 이해하려고 억지로 애쓰지 않아도 된다. 파이썬 입문서와 함께 보면서 프로그래밍 감각을 기르는 사람, 또는 공부나 업무에 필요한 프로그램을 직접 개발해 보고 싶은 사람에게 적합하다. 이미 다른 언어에 익숙한 개발자가 파이썬을 빠르게 습득하고 싶을 때 지루한 문법서보다 즐겁게 배울 수 있다. ‘구슬이 서 말이어도 꿰어야 보배’라는 속담처럼 파이썬 문법 공부를 마쳤다면 이제 나만의 유용한 프로그램을 직접 만들어 보자.")
                    .imgSrc("2021\\08\\07\\pythonbook.jfif")
                    .build();

            books.add(b31);


            Author hanjeonghee = new Author("한정희", "현재 부산을 포함한 경남 지역 관공서, 기업, 대학 출강 전문 업체인 이한아이티의 공동 대표를 맡고 있다. 낮에는 강의장에서, 밤에는 블로그와 유튜브 영상으로 다양한 직종의 사람들을 만나는 열정 넘치는 컴퓨터 교육 전문가로, 유튜브 채널과 블로그 〈짤막한 강좌〉 를 운영 중에 있다.");

            Book b32 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(hanjeonghee);
                        }
                    })
                    .name("된다! 7일 실무 엑셀")
                    .price(20000)
                    .stockQuantity(12)
                    .publicationDate(LocalDate.of(2019, 7, 25))
                    .isbn("1163030937")
                    .highestDepthCategory(excel)
                    .bio("7일이면 기초 떼고 VLOOKUP, 피벗 테이블까지!\n" +
                            "조회수 300만! ‘짤막한 강좌’ 한쌤의 특별 과외\n" +
                            "\n" +
                            "구독자들의 감동과 감사 댓글이 가득한 유튜브 채널 ‘짤막한 강좌’ 한정희 선생님이 드디어 책을 냈다. 《된다! 7일 실무 엑셀》은 바쁜 직장인을 위해 단 7일 만에 엑셀을 배울 수 있게 구성했다. 1일, 2일 차엔 지출 기안서와 같은 양식을 만들며 엑셀 기본을 익힌다. 3일, 4일 차엔 엑셀의 꽃, 수식과 함수를 배운다. 특히 실무에서 가장 많이 사용하는 IF 함수와 VLOOKUP 함수를 완벽 정복한다. 5일~7일 차에는 데이터를 요약하고 시각화하는 방법, 그리고 엑셀의 끝판왕이라 할 수 있는 피벗 테이블까지 배운다. 여기에 시간을 조금 더 할애해 차근차근 엑셀을 익히고 싶은 사람을 위해 16일 코스도 준비했다. 글로 읽고 따라 하는 게 어렵다면, 책에 삽입된 QR 코드를 스캔하고 동영상 강의를 시청하자. 조회수 300만 인기 강좌를 무료로 들을 수 있다.")
                    .imgSrc("2021\\08\\07\\엑셀책.jfif")
                    .build();


            books.add(b32);


            Author leejihoon = new Author("이지훈(이지쌤)", "파워포인트 지식을 최대한 쉽게 알려주자는 모토로 지금까지 1,000여 개의 콘텐츠를 만들어서 전 세계 구독자들에게 소개했다. 2017년부터 2021년까지 Microsoft MVP(Most Valuable Professional)에 4회 연속 선정되어 파워포인트 전문가로 활동 중이다. 대기업, 대학, 공공기관, 해외 등 다양한 곳에서 파워포인트 강의를 하고 있다.");


            Book b33 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(leejihoon);
                        }
                    })
                    .name("퇴근이 1시간 빨라지는 초간단 파워포인트")
                    .price(17000)
                    .stockQuantity(9)
                    .publicationDate(LocalDate.of(2021, 2, 4))
                    .isbn("8950994046")
                    .highestDepthCategory(powerPoint)
                    .bio("칼퇴 보장! 파워포인트 실속 레슨\n" +
                            "실전에서 바로 써먹는 상황별 × 6단계 × 디자인 50\n" +
                            "\n" +
                            "1분 1초가 아쉬운 직장인에게는 따로 실무를 배울 틈이 없다. 기본기를 다질 여유가 없다면, 역으로 실전에 부딪히면서 배워 보면 어떨까? 《퇴근이 1시간 빨라지는 초간단 파워포인트》는 보고서부터 제안서, 포트폴리오, SNS 마케팅 콘텐츠까지 실제 직장 생활에서 가장 많이 사용하는 파워포인트 디자인을 상황별로 정리한 ‘디자인 레시피북’이다.\n" +
                            "\n" +
                            "요리의 기본기를 몰라도 레시피북을 따라 하다 보면 어느새 제법 그럴듯한 요리가 완성되듯이, 파워포인트의 수많은 기능과 옵션을 전부 마스터하지 않아도 괜찮다. 마치 요리책처럼, 하나의 슬라이드 디자인이 완성되는 과정을 각각 6단계 레시피로 정리하여 초보자도 쉽게 따라 만들며 파워포인트에 익숙해질 수 있도록 했다.\n" +
                            "\n" +
                            "유튜브 13만 명의 구독자를 보유한 채널 [이지쌤] 운영, 2017~2021 Microsoft MVP 선정, 기업·학교·공공기관 등에서 강의하며 파워포인트 전문가로 활동해 온 ‘이지쌤’과 함께 오늘은 야근 말고 정시 퇴근에 도전해 보자.")
                    .imgSrc("2021\\08\\07\\파워포인트책.jfif")
                    .build();


            books.add(b33);


            Author songyunhee = new Author("송윤희", "Microsoft Power BI MVP. MCSA(BI Reporting), Excel BI, Power BI를 활용한 데이터 분석 및 시각화 컨설팅 및 강의. 저서로는 『Microsoft Power BI 기본+활용』 『돈과 시간을 아껴주는 엑셀 2016』 『돈과 시간을 아껴주는 MOS 2016 Word』등이 있다.");


            Book b34 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(songyunhee);
                        }
                    })
                    .name("돈과 시간을 아껴주는 MOS 2016 Word")
                    .price(12000)
                    .stockQuantity(19)
                    .publicationDate(LocalDate.of(2018, 7, 5))
                    .isbn("1125446323")
                    .highestDepthCategory(word)
                    .bio("유형 분석 : 평가 항목 및 기출 문제를 분석하여 필수 기능들만 따라하며 익힐 수 있도록 구성하였습니다.\n" +
                            "실전 문제 : 연습은 실전처럼! 실전 감각을 키울 수 있도록 기출 유형 모의 고사 문제를 제공합니다.\n" +
                            "문제 해설 : 상세한 문제 해설로 작업 과정을 확인하고 복습할 수 있습니다.")
                    .imgSrc("2021\\08\\07\\워드책.jfif")
                    .build();


            books.add(b34);


            Author rosassam = new Author("김로사(로사쌤)", "컴퓨터 학원, 방과 후 컴퓨터 교실, 직업전문학교, 각 기업과 학교 등 다양한 곳에서 많은 사람들과 만나며 20년 이상 컴퓨터 강사로 활동하였고, 한컴마스터 4기로 활동하였다. 오랜 강의 경력으로 수강생의 어려움을 정확히 파악하여 항상 이해하기 쉬운 교재를 만들어 수업하였고, 현재는 '로사쌤의 컴교실' 블로그와 유튜브를 통해 수업 내용을 공유하며, 어려움을 해결해 드리고 있다.\n" +
                    "\n" +
                    "컴퓨터 학원, 방과 후 컴퓨터 교실, 직업전문학교 등 다양한 곳에서 다양한 분들과 만나며 15년 이상 컴퓨터 강사로 활동하였다. 현재는 주민자치센터와 노인 회관에서 주부와 어르신들의 컴퓨터/스마트폰 교육을 진행하고 있다. 수업을 통해 수강생의 어려움을 직접 접하고 이를 반영한 교재를 제작하고 있으며, ‘로사쌤의 컴교실(happynut.blog.me)’을 통해 수업 자료를 공유하고, 많은 분들의 컴퓨터와 스마트폰에 관한 어려움을 해결해드리기 위해 노력하고 있다.");


            Book b35 = Book.builder()
                    .authors(new ArrayList<Author>() {
                        {
                            add(rosassam);
                        }
                    })
                    .name("회사 실무에 힘을 주는 한글 2020")
                    .price(18000)
                    .stockQuantity(8)
                    .publicationDate(LocalDate.of(2020, 12, 15))
                    .isbn("8956748993")
                    .highestDepthCategory(hangul)
                    .bio("알짜만 모아 놓은 한글 2020의 모든 것!\n" +
                            "\n" +
                            "항상 최신 버전이 출시되는 한글 프로그램은 처음 잘 익혀두면 여러 용도로 활용이 가능한 만능 프로그램이다. 이 책은 최신 버전의 한글 2020의 기본 기능부터 시작하여 활용하는 방법, 그리고 특별한 기능들과 실무에서 사용되는 문서들까지 자세하게 소개하고 있다. 특히 장황한 설명이 아닌 사전식 구성으로 내가 필요한 기능이 무엇인지 목차에서 보고 그 페이지로 바로 이동할 수 있으며, 학습 중 많이 궁금해 하는 부분이나 저자의 노하우를 포인트와 팁으로 구분하여 실력을 더욱 업그레이드 할 수 있게 구성하였다. 한글 메뉴의 구성부터 시작하여 스스로 입력해보고 자료를 수정하다보면 한글의 무궁무진한 기능에 감탄할 것이다.")
                    .imgSrc("2021\\08\\07\\한글책.jfif")
                    .build();


            books.add(b35);



            for (Book book : books) {
                em.persist(book);
            }


            User user1 = User.builder()
                    .name("user1")
                    .address(Address.builder().postcode("01079").extraAddr("(수유동)").jibunAddr("서울 강북구 수유동 130-99").roadAddr("서울 강북구 수유로17가길 4-10").detailAddr("1층 파란 대문").build())
                    .email("user1@naver.com")
                    .gender(Gender.MALE)
                    .nickname("멍멍이")
                    .username("user1ID")
                    .password(passwordEncoder.encode("user1PWD"))
                    .provider("basic-join")
                    .profileImg("2021\\08\\07\\30d0d174-fc05-408a-8fe6-e6dbf9e44d56_1d0b53122cd442e8bde63a80a5d2342f.jfif")
                    .role(Role.ROLE_USER)
                    .build();

            User user2 = User.builder()
                    .name("user2")
                    .address(Address.builder().postcode("13473").roadAddr("경기 성남시 분당구 경부고속도로 409").jibunAddr("경기 성남시 분당구 삼평동 421").extraAddr("(삼평동)").detailAddr(".").build())
                    .email("user2@naver.com")
                    .gender(Gender.FEMALE)
                    .nickname("냥냥이")
                    .username("user2ID")
                    .password(passwordEncoder.encode("user2PWD"))
                    .provider("basic-join")
                    .profileImg("2021\\08\\07\\c950c203-60e8-4767-b7b4-5bca84471ab8_lets-do-multithreaded-programming-on-coke.jpg")
                    .role(Role.ROLE_USER)
                    .build();

            User user3 = User.builder()
                    .name("user3")
                    .address(Address.builder().postcode("16418").roadAddr("경기 수원시 장안구 서부로2126번길 109").jibunAddr("경기 수원시 장안구 천천동 543").detailAddr("지관 기숙사").extraAddr("(천천동)").build())
                    .email("user3@naver.com")
                    .gender(Gender.MALE)
                    .nickname("돌고래")
                    .username("user3ID")
                    .password(passwordEncoder.encode("user3PWD"))
                    .provider("basic-join")
                    .profileImg("2021\\08\\07\\3abd930f-c573-4153-9164-bac6d3fdf5e6_돌고래.jpg")
                    .role(Role.ROLE_USER)
                    .build();

            User user4 = User.builder()
                    .name("user4")
                    .address(Address.builder().postcode("16419").roadAddr("경기 수원시 장안구 서부로 2066").jibunAddr("경기 수원시 장안구 천천동 300").detailAddr(".").extraAddr("(천천동)").build())
                    .email("user4@naver.com")
                    .gender(Gender.FEMALE)
                    .nickname("강아지")
                    .username("user4ID")
                    .password(passwordEncoder.encode("user4PWD"))
                    .provider("basic-join")
                    .profileImg("2021\\08\\07\\95402cc3-f913-47fc-ae24-3f2bee29ded2_3d921f5ec0ee99b9fedab91cc31ecd65.jpeg")
                    .role(Role.ROLE_USER)
                    .build();

            em.persist(user1);
            em.persist(user2);
            em.persist(user3);
            em.persist(user4);


            Review review = new Review(user1, b1, "[책 리뷰 / 로블록스 게임 제작 무작정 따라하기]", 2.0, "완전 구려요!");
            Review review2 = new Review(user1, b2, "[책 리뷰 / 레트로의 유니티 게임 프로그래밍 에센스]", 4.5, "책의 구성이 참 알차네요!");
            Review review3 = new Review(user1, b3, "[책 리뷰 / 초보자를 위한 유니티 입문]", 3.0, "유니티를 배우기 좋다..");
            Review review4 = new Review(user1, b4, "[책 리뷰 / 초보자를 위한 언리얼 엔진 4 입문]", 5.0, "아주 좋다!");

            Review review5 = new Review(user2, b1, "로블록스 책 리뷰합니다~", 1.0, "돈이 아깝네요");
            Review review6 = new Review(user2, b2, "유니티 책 리뷰합니다~", 3.0, "평범합니다.");
            Review review7 = new Review(user2, b3, "유니티 입문 책 리뷰합니다~", 2.5, "그냥 그저 그렇습니다.");


            Review review8 = new Review(user3, b1, "로블록스 책을 보고..", 4.0, "다른 분들 평이 안좋은데 저는 재밌게 봤습니다...");
            Review review9 = new Review(user3, b2, "유니티 책을 보고..", 2.0, "웁스...");


            em.persist(review);
            em.persist(review2);
            em.persist(review3);
            em.persist(review4);
            em.persist(review5);
            em.persist(review6);
            em.persist(review7);
            em.persist(review8);
            em.persist(review9);
    }
}
