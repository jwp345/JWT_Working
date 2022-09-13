# JWT_Working

JWT와 스프링 시큐리티로 인증 인가 구현, OAuth를 통한 인증을 구현한 부분에서 세션을 만드는 대신 짧은 시간 쿠키를 이용해 통신하도록 repository를 override해서 사용하였습니다.
+ samesite속성을 사용하려면 ResponseCookie를 사용해야 하는데, ResponseCookie에 value에는 공백을 포함할 수 없어 부득이 하게 Bearer Prefix를 삭제함.
+ OAuth2SuccessHandler 에서 samesite='Strict' 쿠키를 만드려고 시도할 시에 구글 OAuth에선 생성이 되나 네이버에선 생성이 안되는 문제 발견 네이버에서 지원이 안돼서 생기는 문젠지 연구 필요


+ 참고 자료 : https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-2/
+ 강의 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0