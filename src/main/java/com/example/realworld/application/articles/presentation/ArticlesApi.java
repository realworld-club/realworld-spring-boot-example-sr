package com.example.realworld.application.articles.presentation;

import com.example.realworld.application.articles.business.ArticleService;
import com.example.realworld.application.articles.business.CommentService;
import com.example.realworld.application.articles.dto.*;
import com.example.realworld.application.favorites.business.FavoriteArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticlesApi {

    public static final String EMAIL = "email";
    private final FavoriteArticleService favoriteArticleService;
    private final ArticleService articleService;
    private final CommentService commentService;

    /**
     * 글 리스트, 인증 옵션
     *
     * @return List Articles
     */
    @GetMapping
    public ResponseEntity<ResponseMultiArticles> getArticles(RequestPageCondition condition) {

        ResponseMultiArticles articles = articleService.searchPageArticles(condition);

        return ResponseEntity.status(HttpStatus.OK).body(articles);
    }

    /**
     * 글 피드, 인증 필요
     *
     * @return List Articles
     */
    @GetMapping(value = "/feed")
    public ResponseEntity<ResponseMultiArticles> feedArticle(
            HttpSession session, @PageableDefault(value = 20) Pageable pageable) {

        String email = (String) session.getAttribute(EMAIL);
        ResponseMultiArticles feedArticles = articleService.getFeedArticles(email, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(feedArticles);
    }

    /**
     * 단일 글 조회, 인증 불필요
     *
     * @param slug unique uri article
     * @return Article
     */
    @GetMapping(value = "/{slug}")
    public ResponseEntity<ResponseArticle> getArticle(@PathVariable("slug") String slug) {

        ResponseArticle article = articleService.getArticle(slug);

        return ResponseEntity.status(HttpStatus.OK).body(article);
    }

    /**
     * 글 생성, 인증 필요
     *
     * @return Article
     */
    @PostMapping
    public ResponseEntity<ResponseArticle> createArticle(HttpSession session, RequestSaveArticle saveArticle) {

        String email = (String) session.getAttribute(EMAIL);
        ResponseArticle savedArticle = articleService.postArticle(email, saveArticle);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    /**
     * 글 수정, 인증 필요
     *
     * @param slug unique uri article
     * @return Article
     */
    @PutMapping(value = "/{slug}")
    public ResponseEntity<ResponseArticle> updateArticle(
            HttpSession session, @PathVariable("slug") String slug, RequestUpdateArticle updateArticle) {

        String email = (String) session.getAttribute(EMAIL);
        ResponseArticle updatedArticle = articleService.updateArticle(email, slug, updateArticle);

        return ResponseEntity.status(HttpStatus.OK).body(updatedArticle);
    }

    /**
     * 글 지우기, 인증 필요
     *
     * @param slug unique uri article
     */
    @DeleteMapping(value = "/{slug}")
    public ResponseEntity<Void> deleteArticle(
            HttpSession session, @PathVariable("slug") String slug) {

        String email = (String) session.getAttribute(EMAIL);
        articleService.deleteArticle(email, slug);

        return ResponseEntity.noContent().build();
    }

    /**
     * 글에 커멘트 달기, 인증 필요
     *
     * @return single comment
     */
    @PostMapping(value = "/{slug}/comments")
    public ResponseEntity<ResponseSingleComment> addCommentsToArticle(
            HttpSession session, @PathVariable("slug") String slug, RequestSaveComment saveComment) {

        String email = (String) session.getAttribute(EMAIL);
        ResponseSingleComment savedComment = commentService.postComment(email, slug, saveComment);

        return ResponseEntity.status(HttpStatus.OK).body(savedComment);
    }

    /**
     * 글에 달린 커멘트 가져오기, 인증 옵션
     *
     * @return multiple comments
     */
    @GetMapping(value = "/{slug}/comments")
    public ResponseEntity<ResponseMultiComments> getCommentsFromAnArticle(@PathVariable("slug") String slug) {

        ResponseMultiComments comments = commentService.getCommentsByArticle(slug);

        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    /**
     * 커멘트 삭제, 인증 필요
     *
     * @param slug      unique uri article
     * @param commentId comment PK
     */
    @DeleteMapping(value = "/{slug}/comments/{id}")
    public ResponseEntity<Void> deleteComments(
            HttpSession session, @PathVariable("slug") String slug, @PathVariable("id") Long commentId) {

        String email = (String) session.getAttribute(EMAIL);
        commentService.deleteComment(email, slug, commentId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 글 좋아요 요청, 인증 필요
     *
     * @param slug unique uri article
     * @return Article
     */
    @PostMapping(value = "/{slug}/favorite")
    public ResponseEntity<ResponseArticle> favoriteArticle(
            HttpSession session, @PathVariable("slug") String slug) {

        String email = (String) session.getAttribute(EMAIL);
        ResponseArticle followArticle = favoriteArticleService.favoriteArticle(email, slug);

        return ResponseEntity.status(HttpStatus.OK).body(followArticle);
    }

    /**
     * 글 좋아요 취소, 인증 필요
     *
     * @param slug unique uri article
     * @return Article
     */
    @DeleteMapping(value = "/{slug}/favorite")
    public ResponseEntity<ResponseArticle> unFavoriteArticle(
            HttpSession session, @PathVariable("slug") String slug) {

        String email = (String) session.getAttribute(EMAIL);
        ResponseArticle unfollowArticle = favoriteArticleService.unFavoriteArticle(email, slug);

        return ResponseEntity.status(HttpStatus.OK).body(unfollowArticle);
    }
}