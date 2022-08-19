2022/08/19
1.Comment 에 생성일자,수정일자가 들어가야 하므로 부득이하게 domain의 Comment 를 수정하게되었음.
-extends Timestamped 를 붙여줬음.
-@Column(nullable = false)
    private String content;
 을 붙여줬음. 코멘트 내용에 대한 테이블이 없어서 만들어준거임.