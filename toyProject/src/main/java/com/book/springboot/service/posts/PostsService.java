package com.book.springboot.service.posts;


import com.book.springboot.domain.files.Files;
import com.book.springboot.domain.files.FilesRepository;

import com.book.springboot.domain.posts.Posts;
import com.book.springboot.domain.posts.PostsRepository;
import com.book.springboot.web.dto.PostsUpdateRequestDto; //
import com.book.springboot.web.dto.PostsResponseDto;
import com.book.springboot.web.dto.PostsSaveRequestDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.book.springboot.web.dto.PostsListResponseDto;

import java.util.List;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id="+id));
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional
    public void delete (Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ id));
        postsRepository.delete(posts);
    }

    public PostsResponseDto findById (Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id="+id));

        return new PostsResponseDto(entity);
    }

    //게시글 목록 조회
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    //게시글 조회
    @Transactional
    public PostsSaveRequestDto getPost(Long id) {
        Posts posts = postsRepository.findById(id).get();

        PostsSaveRequestDto postsSaveRequestDto = PostsSaveRequestDto.builder()
                .id(posts.getId())
                .author(posts.getAuthor())
                .title(posts.getTitle())
                .content(posts.getContent())
                .fileId(posts.getFileId())
                .build();

        return postsSaveRequestDto;
    }

}
