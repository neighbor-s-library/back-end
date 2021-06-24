# RestApi 명세

**회원가입**

- 메소드 : POST
- url : localhost:8080/hellobook/join
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
    "pubDate": "2021-06-15 17:05:18"
}
```

**로그인**

- 메소드 : POST
- url : localhost:8080/hellobook/login
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
   "pubDate": "2021-06-15 17:05:18"
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZGF0YSI6IjEyMzQiLCJleHAiOjE2MjQ1MDQ2ODF9.maW7hxN4uXIvggNFgSttQO3lc80geYRjusTgZgFt3-4"
}
```

- 메소드 : GET

**로그아웃**

- url : localhost:8080/hellobook/logout
- input : 필요없음 front 에서 처리

**회원정보 수정** 

- 메소드 : PUT
- url : localhost:8080/hellobook/users
- input : JSON
- 예)

```json
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

****비밀번호 변경 이메일 전송** **

- 메소드 : POST
- url : localhost:8080/hellobook/users/pwfind
- input : JSON
- 예)

```json
//Param
{
		"email" : "이메일 (비밀번호 변경 URL이 전송된다)"
}

//response
{
    "result": "Success",
    "pubDate": "2021-06-15 17:05:18"
}
```

**정보 조회 (point, 닉네임)** 

- 메소드 : GET
- url : localhost:8080/hellobook/users/{id}
- Input : Header에 Token 값
- 예)

```json
//Header
key : Token
value : 'token 값'

//response
{
    "result": "Success",
    "sub": "user",
    "item": {
        "id": 20,
        "email": "hyejiii",
        "point": 0,
        "address": "주소",
        "nickname": "비밀번호1234",
        "created_at": null,
        "updated_at": null,
        "pw": null,
        "tel": null,
        "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZGF0YSI6Imh5ZWppaWkiLCJleHAiOjE2MjQzNDkxNDl9.J-3ksYNOAUg4CPlsf0uGYY5udmis3bRuRV9_U33wALc"
    },
    "data": "hyejiii",
    "exp": 1624349149,
    "pubDate": "2021-06-22 16:41:37"
}
```

---

**책 등록 (구현완료)(토큰필요)**

- 메소드 : POST
- url : localhost:8080/hellobook/books
- input : JSON
- 예)

```json
//input
{
	"title" : "제목",
	"writer" : "지은이",
	"pub" : "출판사",
  "deposit" : "보증금",
	"detail" : "상세설명",
	"genre" : "fiction",
	"img" : "사진저장경로",
	"user_id" : "4" //같이 JSON으로 받는것으로 수정됨 06.21
}

//response
{
    "result": "Success",
    "successed": "true",
    "pubDate": "2021-06-24 10:21:59"
}
```

**책 수정(구현완료)(토큰필요)**

bookno 로 책 수정

- 메소드 :PUT
- url: http://localhost:8080/hellobook/books
- input: JSON

```json
{
    "id": 11,
    "title": "제목수정",
    "writer": "지은이수정",
    "deposit" : "보증금",
    "pub": "출판사수정",
    "detail": "상세설명수정",
    "genre": "non-fiction",
    "isrent": "N",
    "hide": "N",
    "img": "사진저장경로"
}
//response
{
    "result": "Success",
    "successed": "true",
    "pubDate": "2021-06-24 10:21:59"
}
```

**# 조회 1 : 특정 회원이 등록한 책 (구현완료)**

자신의 user_id으로 본인이 등록한 책 조회

- 메소드 : GET
- url : http://localhost:8080/hellobook/users/books?user_id=
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
.............
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


**# 조회 2 : 책 검색 (구현완료)**

검색어로 bookno 조회

- 메소드 : GET
- url : http://localhost:8080/hellobook/books/:keyword
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
        } ....
       
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

**# 조회 3 : books list(구현완료)**

메인 페이지 최근 등록 책 리스트 (전체 리스트)

- 메소드 : GET
- url : http://localhost:8080/hellobook/books?page={page_number}
- input : 필요없음

```json
 http://localhost:8080/hellobook/books&page=3
//response
{
	"title" : "검색어 책 제목",
	"writer" : "지은이",
	"pub" : "출판사",
	"detail" : "상세설명"
.
.
.
}
...
```

**# 조회 4 : 도서 상세 페이지(구현완료)**

특정 도서의 상세 페이지

- 메소드 : GET
- url : http://localhost:8080/hellobook/books/:book_id
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
        "hide": "N"
    },
    "pubDate": "2021-06-17 11:31:32"
}
```

---

**책 대여(토큰필요)**

- 메소드 : POST
- url : localhost:8080/hellobook/rental
- input : JSON
- 예)

```json
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
    "pubDate": "2021-06-21 17:15:23"
}
```

**대여 상태 수정(토큰필요)**

- 메소드 : PUT
- url : localhost:8080/hellobook/rental
- input : JSON
- 예)

```json
//Param
{
	"id" : "Rental 번호",
	"isrent" : "대여 상태('Y' or 'N')",
	"returndate" : "반납일자"
}

//response
{
   "result": "Success",
	 "pubDate": "2021-06-21 17:15:23"
}
```

**대여 목록 조회** 

자신의 user_id으로 rental조회 - 개수 조회 추가 수정 완료

- 메소드 : GET
- url : http://localhost:8080/hellobook/rental/{user_id}
- output : JSON

```json

//response
{
	  "result": "Success",
    "ownerList": [
        {
            "id": 2,
            "book_id": 37,
            "owner_id": 10,
            "renter_id": 9,
            "isrent": "N",
            "created_at": "2021-06-21 07:47:44",
            "returndate": "2021-07-30",
            "updated_at": "2021-06-21 08:15:23"
        }
    ],
    "renterList": [
        {
            "id": 3,
            "book_id": 38,
            "owner_id": 5,
            "renter_id": 10,
            "isrent": "Y",
            "created_at": "2021-06-21 07:50:21",
            "returndate": "2021-07-20"
        }
    ],
    "pubDate": "2021-06-21 17:18:35"
} 
...
```
