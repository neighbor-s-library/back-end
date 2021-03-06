# 개발환경
 - Java 11
 - Eclipse 2020-12 
 - Spring
 - AWS RDS MySql server
 - Maven
# 실행 방법 
 - ERD에 따라 DB환경을 구성하고, *WEB-INF/spring/root-context.xml 의 DB연동에 필요한 환결설정 값을 변경한다. 이후 톰켓에 연동하여 실행한다.
# RestApi 명세

**회원가입**

- 메소드 : POST
- url : /join
- input : JSON
- 예)

```json
//Param
{
	"email" : "이메일",
	"address" : "주소",
	"nickname" : "닉네임",
	"pw" : "비밀번호",
	"tel" : "tel"
}

//response
{
    "result": "Success", 
    "successed": "true",
    "pubDate": "2021-06-23 17:32:16"
}
```

**로그인**

- 메소드 : POST
- url : /login
- input : JSON
- 예)

```json
//Param
{
	"email" : "이메일",
	"pw" : "비밀번호"
}

//response
{
    "result": "Success", 
    "successed": "true", 
    "pubDate": "2021-06-23 17:34:00",
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIsImV4cCI6MTYyNDQ0NDQ0MH0.zS4WOU_xWyDhvz0P3FuohrCJg4ZLePBM4apumBX9114"
}
```

- 메소드 : GET

**이메일로 회원 유무 확인**

- 메소드 : GET
- url : localhost:8080/hellobook/email/{email}
- input : JSON
- 예)

```json
//response
{
    "result": "Success",
    "successed": "true",
    "id": 10,
    "pubDate": "2021-06-28 13:34:43"
}
```

**회원정보 수정** 

- 메소드 : PUT
- url : /users
- input : JSON
- 예)

```json
//Header
key : Token
value : 'token 값'

//Param
{
	"id" : "13",
	"address" : "주소",
	"nickname" : "닉네임",
  	"pw" : "비밀번호",
	"tel" : "전화번호"
}

//response
{
    "result": "Success",
    "successed": "true",
    "pubDate": "2021-06-24 10:18:47"
}
```

**비밀번호 변경 이메일 전송**

- 메소드 : POST
- url : /users/pwfind
- input : JSON
- 예)

```json
//Header
key : Token
value : 'token 값'

//Param
{
	"email" : "이메일 (비밀번호 변경 URL이 전송된다)"
}

//response
{
    "result": "Success", 
    "successed": "true",
    "pubDate": "2021-06-23 17:47:01"
}
```

**정보 조회 (point, 닉네임)** 

- 메소드 : GET
- url : /users/{id}
- Input : Header에 Token 값
- 예)

```json
//Header
key : Token
value : 'token 값'

//response
{
    "result": "Success",
    "item": {
        "id": 23,
        "email": "qwerty3@gmail.com",
        "point": 0,
        "address": "주소",
        "nickname": "닉네임1234",
        "created_at": null,
        "updated_at": "2021-06-23 17:45:49",
        "pw": null,
        "tel": null,
        "token": null
    },
    "successed": "true",
    "pubDate": "2021-06-23 17:50:04"
}
```

---

**책 등록 **

- 메소드 : POST
- url : /books
- input : JSON
- 예)

```json
//Header
key : Token
value : 'token 값'

//input
{
	"title" : "제목",
	"writer" : "지은이",
	"pub" : "출판사",
 	"deposit" : "보증금",
	"detail" : "상세설명",
	"genre" : "fiction",
	"img" : "사진저장경로",
	"user_id" : "4"
}

//response
{
    "result": "Success",
    "id": 18,
    "successed": "true",
    "pubDate": "2021-06-30 14:41:41"
}
```

**책 수정(구현완료)(토큰필요)**

bookno 로 책 수정

- 메소드 :POST
- url: /books/edit
- input: JSON

```json
//Header
key : Token
value : 'token 값'

{  
   "id" : "69",
   "title" : "나루토 64",
   "writer" : "키시모토 마사시",
   "pub" : "대원씨아이",
   "deposit" : "4000",
   "detail" : "한 때 동료였던 오비토가 가면 쓴 남자의 정체였음을 알고 동요하는 카카시. 그러나 나루토의 말이 카카시를 다시 일으켜 세운다. 동료는 절대 죽게 내버려 두지 않아. 공세에 들어가는 나루토와 카카시, 하지만 결국 십미가 부활하는데..",
   "genre" : "comic",
   "img" : "https://image.aladin.co.kr/product/3062/26/cover500/8968226415_1.jpg",
   "user_id" : "18"
   
}
//response
{
    "result": "Success",
    "successed": "true",
    "pubDate": "2021-06-24 10:21:59"
}
```

**# 조회 1 : 특정 회원이 등록한 책 **

자신의 user_id으로 본인이 등록한 책 조회

- 메소드 : GET
- url : /users/books/:user_id
- input : parameter user_id
- output : JSON

```json
{
    "result": "Success",
    "item": [
        {
            "id": 22,
            "title": "책의 제목입니다",
            "writer": "작가의 이름",
            "pub": "출판사 이름",
            "detail": "상세설명을 써야합니다",
            "genre": "fiction",
            "created_at": "2021-06-21 03:22:26",
            "updated_at": null,
            "isrent": "N",
            "hide": "N",
            "img": null,
            "user_id": 13,
            "deposit": 0,
            "email": "김영일",
            "nickname": "닉임"
        }

    ],
    "meta": {
        "nowPage": 1,
        "totalCount": 11,
        "listCount": 10,
        "groupCount": 5,
        "totalPage": 2,
        "startPage": 1,
        "endPage": 2,
        "prevPage": 0,
        "nextPage": 0,
        "offset": 0
    },
    "keyword": 13,
    "pubDate": "2021-06-22 16:36:10"
}
```

- 


**# 조회 2 : 책 검색 **

검색어로 bookno 조회

- 메소드 : GET
- url : /books/:keyword
- input : key = keyword

```json

input : 
http://localhost:8080/hellobook/books?keyword=조

//response
{
    "rt": "OK",
    "item": [
        {
            "id": 33,
            "title": "조현석의 책4",
            "writer": "조현석",
            "pub": "출판사",
            "detail": "상세설명",
            "genre": "fiction",
            "created_at": "2021-06-17 10:56:26",
            "isrent": "N",
            "hide": "N"
        } 
       
    ],
    "meta": {
        "nowPage": 1,
        "totalCount": 4,
        "listCount": 10,
        "groupCount": 5,
        "totalPage": 1,
        "startPage": 1,
        "endPage": 1,
        "prevPage": 0,
        "nextPage": 0,
        "offset": 0
    },
    "keyword": "조",
    "pubDate": "2021-06-17 11:24:55"
}
...
```

**# 조회 3 : books list**

메인 페이지 최근 등록 책 리스트 (전체 리스트)

- 메소드 : GET
- url : /books?page={page_number}
- input : 필요없음

```json
 http://localhost:8080/hellobook/books&page=3
//response
{
	"title" : "검색어 책 제목",
	"writer" : "지은이",
	"pub" : "출판사",
	"detail" : "상세설명"

}
...
```

**# 조회 4 : 도서 상세 페이지**

특정 도서의 상세 페이지

- 메소드 : GET
- url : /books/:book_id
- input : 필요없음

```json
http://localhost:8080/hellobook/books/29
//response
{
    "response": "OK",
    "item": {
        "id": 29,
        "title": "제목",
        "writer": "지은이",
        "pub": "출판사",
        "detail": "상세설명",
        "genre": "fiction",
        "created_at": "2021-06-17 10:41:48",
        "isrent": "N",
        "user_id" : "18",
        "hide": "N"
    },
    "pubDate": "2021-06-17 11:31:32"
}
```

---

**책 대여**

- 메소드 : POST
- url : /rental
- input : JSON
- 예)

```json
//Header
key : Token
value : 'token 값'

//Param
{
	"book_id" : "책 번호",
	"owner_id" : "빌려주는 Userno",
	"renter_id" : "빌리는 Userno",
	"returndate" : "반납일자"
}

//response - Owner 입장에서의 빌려준 모든 책 목록
{
    "result": "Success",
    "successed": "true",
    "pubDate": "2021-06-23 18:03:51"
}
```

**대여 상태 수정**

- 메소드 : PUT
- url : /rental
- input : JSON
- 예)

```json
//Header
key : Token
value : 'token 값'

//Param
{
	"id" : "Rental 번호",
	"isrent" : "대여 상태('Y' or 'N')",
	"returndate" : "반납일자"
}

//response
{
    "result": "Success",
    "successed": "true",
    "pubDate": "2021-06-23 18:13:40"
}
```

**대여 목록 조회 - Owner (빌려준 목록 조회)**

자신의 user_id으로 rental조회 

- 메소드 : GET
- url : /owner/{user_id}
- output : JSON

```json
//Header
key : Token
value : 'token 값'

//response
{ 
    "result": "Success",
    "output": [    
        {
            "title": "6",
            "img": null,
            "created_at": "2021-06-23 18:03:53"
        },
        {
            "title": "책의 제목입니다",
            "img": null,
            "created_at": "2021-06-23 18:03:53"
        }
    ],
    "successed": "true",
    "pubDate": "2021-06-23 18:59:48"
}
```

**대여 목록 조회 - Renter (빌린 목록 조회)**

자신의 user_id으로 rental조회

- 메소드 : GET
- url : /renter/{user_id}
- output : JSON

```json
//Header
key : Token
value : 'token 값'

//response
{
    "result": "Success",
    "output": [
        {
            "title": "제목",
            "img": null,
            "created_at": "2021-06-21 07:47:44"
        }
    ],
    "successed": "true",
    "pubDate": "2021-06-23 19:14:38"
}

```
