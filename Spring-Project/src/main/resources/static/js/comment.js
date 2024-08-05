let postId, currentUserId;

const ratingModule = {
    selectedRating: 0,

    // 초기 별점 설정
    initializeRating: function() {
        const stars = document.querySelectorAll('.rating-stars .star');
        stars.forEach(star => {
            star.addEventListener('click', this.setRating.bind(this));
        });
    },

    // 별점 설정
    setRating: function(event) {
        this.selectedRating = parseInt(event.target.getAttribute('data-value'));
        this.updateStars();
    },

    // 별점 UI 업데이트
    updateStars: function() {
        const stars = document.querySelectorAll('.rating-stars .star');
        stars.forEach((star, index) => {
            if (index < this.selectedRating) {
                star.classList.add('selected');
            } else {
                star.classList.remove('selected');
            }
        });
    },

    // 선택된 별점 반환
    getSelectedRating: function() {
        return this.selectedRating;
    },

    // 별점 초기화
    resetRating: function() {
        this.selectedRating = 0;
        this.updateStars();
    },

    // 평균 별점 로드
    loadAverageRating: function(postId) {
        fetch(`/v1/posts/${postId}/comments/average-rating`)
            .then(response => response.json())
            .then(averageRating => {
                const averageRatingElement = document.getElementById('averageRating');
                const averageRatingStarsElement = document.getElementById('averageRatingStars');
                if (averageRatingElement && averageRatingStarsElement) {
                    averageRatingElement.textContent = averageRating.toFixed(1);
                    averageRatingStarsElement.textContent = this.renderStars(averageRating);
                }
            });
    },

    // 별점 렌더링
    renderStars: function(rating) {
        return '★'.repeat(Math.round(rating)) + '☆'.repeat(5 - Math.round(rating));
    },

    // 댓글 수정 시 별점 편집 기능
    editRating: function(commentId, initialRating) {
        this.selectedRating = initialRating;
        const ratingContainer = document.querySelector(`#comment-${commentId} .edit-rating-container`);
        const stars = ratingContainer.querySelectorAll('.star');

        stars.forEach(star => {
            star.addEventListener('click', (event) => {
                this.selectedRating = parseInt(event.target.getAttribute('data-value'));
                this.updateEditStars(stars);
            });
        });

        this.updateEditStars(stars);
    },

    // 댓글 수정 시 별점 UI 업데이트 기능
    updateEditStars: function(stars) {
        stars.forEach((star, index) => {
            if (index < this.selectedRating) {
                star.classList.add('selected');
            } else {
                star.classList.remove('selected');
            }
        });
    }
};

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    postId = document.getElementById('postId')?.value;
    currentUserId = document.getElementById('currentUserId')?.value;

    if (postId && currentUserId) {
        loadComments();
        ratingModule.loadAverageRating(postId);
        ratingModule.initializeRating();
    } else {
        console.error('Required data is missing');
    }
});

// 댓글 로드 기능
function loadComments() {
    fetch(`/v1/posts/${postId}/comments`)
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

// 댓글 요소 생성 기능
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
            <p>${comment.rating ? ratingModule.renderStars(comment.rating) : '평점 없음'}</p>
        </div>
        <div class="content-container">
            <p>${comment.content}</p>
        </div>
    `;

    return commentDiv;
}

// 댓글 제출 기능
function submitComment() {
    const content = document.getElementById('commentContent').value;
    const selectedRating = ratingModule.getSelectedRating();

    if (!content.trim()) {
        alert('댓글 내용을 입력해주세요.');
        return;
    }
    if (selectedRating === 0) {
        alert('별점을 선택해주세요.');
        return;
    }

    fetch(`/v1/posts/${postId}/comments`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content: content, rating: selectedRating }),
    })
        .then(response => response.json())
        .then(comment => {
            document.getElementById('commentContent').value = '';
            ratingModule.resetRating();
            loadComments();
            ratingModule.loadAverageRating(postId);
        });
}

// 댓글 수정 UI 생성 기능
function editComment(commentId) {
    const commentDiv = document.getElementById(`comment-${commentId}`);
    const contentContainer = commentDiv.querySelector('.content-container');
    const contentP = contentContainer.querySelector('p');
    const originalContent = contentP.textContent;
    const originalRating = commentDiv.querySelector('.rating-container p').textContent.trim().length;

    const textarea = document.createElement('textarea');
    textarea.value = originalContent;
    contentP.replaceWith(textarea);

    const ratingContainer = document.createElement('div');
    ratingContainer.className = 'edit-rating-container';
    ratingContainer.innerHTML = `
        <div class="rating-stars">
            ${['★', '★', '★', '★', '★'].map((star, index) =>
        `<span class="star ${index < originalRating ? 'selected' : ''}" data-value="${index + 1}">${star}</span>`
    ).join('')}
        </div>
    `;
    contentContainer.insertBefore(ratingContainer, textarea);

    const buttonContainer = document.createElement('div');
    buttonContainer.className = 'edit-button-container';
    buttonContainer.innerHTML = `
        <button class="edit-button save-button" onclick="saveEditedComment(${commentId})">저장</button>
        <button class="edit-button cancel-button" onclick="cancelEdit(${commentId}, '${originalContent}', ${originalRating})">취소</button>
    `;
    contentContainer.appendChild(buttonContainer);

    // [수정] 별점 수정 기능 초기화
    ratingModule.editRating(commentId, originalRating);
}

// [수정] 수정된 댓글 저장 기능
function saveEditedComment(commentId) {
    const commentDiv = document.getElementById(`comment-${commentId}`);
    const textarea = commentDiv.querySelector('textarea');
    const newContent = textarea.value;
    const rating = ratingModule.getSelectedRating();

    fetch(`/v1/posts/${postId}/comments/${commentId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content: newContent, rating: rating }),
    })
        .then(response => response.json())
        .then(comment => {
            loadComments();
            ratingModule.loadAverageRating(postId);
            ratingModule.resetRating();
        });
}

// 댓글 수정 취소 기능
function cancelEdit(commentId, originalContent, originalRating) {
    const commentDiv = document.getElementById(`comment-${commentId}`);
    const contentContainer = commentDiv.querySelector('.content-container');
    const textarea = contentContainer.querySelector('textarea');
    const contentP = document.createElement('p');
    contentP.textContent = originalContent;
    textarea.replaceWith(contentP);

    const ratingContainer = commentDiv.querySelector('.rating-container');
    ratingContainer.innerHTML = `<p>${'★'.repeat(originalRating)}</p>`;

    const editRatingContainer = contentContainer.querySelector('.edit-rating-container');
    if (editRatingContainer) {
        editRatingContainer.remove();
    }

    const buttonContainer = contentContainer.querySelector('.edit-button-container');
    buttonContainer.remove();
}

// 댓글 삭제 기능
function deleteComment(commentId) {
    if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
        fetch(`/v1/posts/${postId}/comments/${commentId}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    loadComments();
                    ratingModule.loadAverageRating(postId);
                }
            });
    }
}
//ratingModule에 editRating과 updateEditStars 메서드 추가
// editComment 함수에서 ratingModule.editRating 호출
// saveEditedComment 함수에서 ratingModule.getSelectedRating() 사용