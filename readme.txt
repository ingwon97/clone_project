2022/08/19
1.Comment 에 생성일자,수정일자가 들어가야 하므로 부득이하게 domain의 Comment 를 수정하게되었음.
-extends Timestamped 를 붙여줬음.
-@Column(nullable = false)
    private String comment;
 을 붙여줬음. 코멘트 내용에 대한 테이블이 없어서 만들어준거임.

2. 댓글 수정,삭제에 url에 게시물id는 왜 넣은거임? 댓글id만 있으면 될거같은데. 우선 명세서대로 필요인수에 넣긴했는데
역시나 쓰이질 않음.