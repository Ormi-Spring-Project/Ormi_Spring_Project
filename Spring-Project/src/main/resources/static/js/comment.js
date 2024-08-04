let postId, currentUserId;

document.addEventListener('DOMContentLoaded', function() {
    postId = document.getElementById('postId')?.value;
    currentUserId = document.getElementById('currentUserId')?.value;

    if (postId && currentUserId) {
        loadComments();
        // 별점 관련 모듈화=>기능설명 추가 필요
        window.ratingModule.loadAverageRating(postId);
        window.ratingModule.initializeRating();
    } else {
        console.error('Required data is missing');
    }
});

function loadComments() {
    fetch(`/api/posts/${postId}/comments`)
        .then(response => response.json())
        .then(comments => {
            const commentContainer = document.getElementById('commentContainer');
            commentContainer.innerHTML = '';
            comments.forEach(comment => {
                const commentElement = createCommentElement(comment);
                commentContainer.appendChild(commentElement);
            });
        });
}

function createCommentElement(comment) {
    const commentDiv = document.createElement('div');
    commentDiv.className = 'comment-container';
    commentDiv.id = `comment-${comment.id}`;

    const formattedDate = new Date(comment.createdAt).toLocaleString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });

    const renderStars = (rating) => {
        return '★'.repeat(rating) + '☆'.repeat(5 - rating);
    };

    commentDiv.innerHTML = `
        <div class="comment-header">
            <div class="author-info">
                <span class="author">${comment.authorNickname}</span>
                <span class="createdAt">${formattedDate}</span>
            </div>
            <div class="delete-update-container">
                ${currentUserId == comment.userId ? `
                    <span class="update" onclick="editComment(${comment.id})">수정</span>
                    <span class="delete" onclick="deleteComment(${comment.id})">삭제</span>
                ` : ''}
            </div>
        </div>
        <div class="rating-container"> 
            <p>${comment.rating ? renderStars(comment.rating) : '평점 없음'}</p>
        </div>
        <div class="content-container">
            <p>${comment.content}</p>
        </div>
    `;

    return commentDiv;
}

function submitComment() {
    const content = document.getElementById('commentContent').value;
    // 평점 선택 추가
    const selectedRating = window.ratingModule.getSelectedRating();

    if (!content.trim()) {
        alert('댓글 내용을 입력해주세요.');
        return;
    }
    if (selectedRating === 0) {
        alert('별점을 선택해주세요.');
        return;
    }

    fetch(`/api/posts/${postId}/comments`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        // 별점추가
        body: JSON.stringify({ content: content, rating: selectedRating }),
    })
        .then(response => response.json())
        .then(comment => {
            document.getElementById('commentContent').value = '';
            // 별점 추가
            window.ratingModule.resetRating();
            loadComments();
            // 평균별점
            window.ratingModule.loadAverageRating(postId);
        });
}

function editComment(commentId) {
    const commentDiv = document.getElementById(`comment-${commentId}`);
    const contentContainer = commentDiv.querySelector('.content-container');
    const contentP = contentContainer.querySelector('p');
    const originalContent = contentP.textContent;

    const textarea = document.createElement('textarea');
    textarea.value = originalContent;
    contentP.replaceWith(textarea);

    const buttonContainer = document.createElement('div');
    buttonContainer.className = 'edit-button-container';
    buttonContainer.innerHTML = `
        <button class="edit-button save-button" onclick="saveEditedComment(${commentId})">저장</button>
        <button class="edit-button cancel-button" onclick="cancelEdit(${commentId}, '${originalContent}')">취소</button>
    `;
    contentContainer.appendChild(buttonContainer);
}

function saveEditedComment(commentId) {
    const commentDiv = document.getElementById(`comment-${commentId}`);
    const textarea = commentDiv.querySelector('textarea');
    const newContent = textarea.value;

    fetch(`/api/posts/${postId}/comments/${commentId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content: newContent }),
    })
        .then(response => response.json())
        .then(comment => {
            loadComments();
        });
}

function cancelEdit(commentId, originalContent) {
    const commentDiv = document.getElementById(`comment-${commentId}`);
    const contentContainer = commentDiv.querySelector('.content-container');
    const textarea = contentContainer.querySelector('textarea');
    const contentP = document.createElement('p');
    contentP.textContent = originalContent;
    textarea.replaceWith(contentP);

    const buttonContainer = contentContainer.querySelector('.edit-button-container');
    buttonContainer.remove();
}

function deleteComment(commentId) {
    if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
        fetch(`/api/posts/${postId}/comments/${commentId}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    loadComments();
                    // 게시글별 평균평점
                    window.ratingModule.loadAverageRating(postId);
                }
            });
    }
}

// 전역 스코프에서 함수 노출=>부가설명 필요
// 웹페이지 어느곳에서는 해당 함수들을 사용할수 있게 해줌
// 굳이 영역 지정된 기능들이라 사용은 불필요->주석처리함
// window.submitComment = submitComment;
// window.editComment = editComment;
// window.saveEditedComment = saveEditedComment;
// window.cancelEdit = cancelEdit;
// window.deleteComment = deleteComment;