# [Yeah 24 프로젝트] 9. Spring Security 적용하기



## 1. SecurityConfiguration 클래스 작성

**BasicConfiguration**

```java
@Configuration
@EnableJpaAuditing
public class BasicConfiguration {

    @Bean
    public JPAQueryFactory queryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
```



**SecurityConfiguration**

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;
    private final PrincipalDetailsService principalDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**"); // 파라미터로 넘겨준 URL들은 Spring Security 룰을 무시하게 한다.
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                .authorizeRequests()  // 보호된 리소스 URI에 접근할 수 있는 "권한"을 설정
                    .anyRequest().permitAll()  // 우리 프로젝트에서는 모두 허용해놓는다
                    .and()
                .formLogin()  // form 태그 기반의 로그인 설정
                    .loginPage("/auth/login")   // 로그인 페이지 설정 (GET)
                    .loginProcessingUrl("/auth/login")  // 실제 로그인 처리 URL 설정 (POST)
                    .defaultSuccessUrl("/", false)  // 로그인 성공시 URL 설정
                    .permitAll()  // 로그인 페이지 모두 접속 허용
                    .and()
                .logout()
                    .logoutSuccessUrl("/?logoutSuccess") // 로그아웃 성공시 URL 설정
                    .logoutUrl("/auth/logout")  // 로그아웃 처리 URL 설정
                    .invalidateHttpSession(true)
                    .and()
                .exceptionHandling()
                    .accessDeniedPage("/auth/denied") // 접근 권한 없을 시 이동할 페이지 설정
                    .and()
                .oauth2Login()
                    .loginPage("/auth/login") // 로그인 페이지 설정 (GET)
                    .userInfoEndpoint()  // 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                    .userService(principalOauth2UserService); // 소셜 로그인 성공 이후 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailsService).passwordEncoder(passwordEncoder);
    }
}
```

- `http.authorizeRequests().anyRequest().permitAll()`
  - 우리 프로젝트에서는 일단은 위처럼 모든 요청 권한을 허용해놓는다. 그리고 `@EnableGlobalMethodSecurity` 애너테이션을 적용해 `@PreAuthorize` 애너테이션을 활성화시켜서 메서드 레벨에서 권한 설정을 할 것이다.





## 2. UserDetailsService 구현체 작성



### (1) 일반 form 로그인

**PrincipalDetailsService**

```java
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 Username을 가진 User가 존재하지 않습니다."));
        return new PrincipalDetails(new UserPrincipalDTO(user));
    }

}
```

- 일반 form 로그인을 위한 Service







### (2) OAuth2 로그인

**OAuth2UserInfo 인터페이스**

```java
public interface OAuth2UserInfo {

    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
    String getUsername();
    String getPassword();
    String getRole();
}
```

- 네이버, 구글 User Info를 추출하기 위해 공통 속성을 추상화함



**GoogleUserInfo 구현체**

```java
@AllArgsConstructor
@Getter
public class GoogleUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return (String) this.attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return (String) this.attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) this.attributes.get("name");
    }

    @Override
    public String getUsername() {
        return getProvider() + "_" + getProviderId();
    }

    @Override
    public String getPassword() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getRole() {
        return "ROLE_USER";
    }
}
```



**NaverUserInfo 구현체**

```java
@AllArgsConstructor
@Getter
public class NaverUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return (String) this.attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return (String) this.attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) this.attributes.get("name");
    }

    @Override
    public String getUsername() {
        return getProvider() + "_" + getProviderId();
    }

    @Override
    public String getPassword() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getRole() {
        return "ROLE_USER";
    }

}
```







**PrincipalOauth2UserService**

```java
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo userInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            log.info("구글 로그인 요청");
            userInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            log.info("네이버 로그인 요청");
            userInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        }


        Optional<User> optionalUser = userRepository.findUserByUsername(userInfo.getUsername());
        User userEntity = null;
        if (optionalUser.isPresent()) {
            log.info("로그인한적이 있습니다.");
            userEntity = optionalUser.get();
        } else {
            log.info("최초 로그인이므로 회원 가입을 진행합니다..");
            userEntity = User.builder()
                    .username(userInfo.getUsername())
                    .name(userInfo.getName())
                    .email(userInfo.getEmail())
                    .password(passwordEncoder.encode(userInfo.getPassword()))
                    .role(Role.valueOf(userInfo.getRole()))
                    .nickname(new RandomNicknameUtils().getNickname())
                    .provider(userInfo.getProvider())
                    .providerId(userInfo.getProviderId())
                    .build();

            userRepository.save(userEntity);
        }

        return new PrincipalDetails(new UserPrincipalDTO(userEntity), oAuth2User.getAttributes());
    }
}
```

- 구글, 네이버 로그인을 위한 Service
- 최초 로그인인 경우 자동으로 회원가입을 진행시킨다.