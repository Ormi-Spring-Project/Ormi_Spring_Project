let postId, currentUserId;

document.addEventListener('DOMContentLoaded', function() {
    postId = document.getElementById('postId')?.value;
    currentUserId = document.getElementById('currentUserId')?.value;

    if (postId && currentUserId) {
        loadComments();
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

    commentDiv.innerHTML = `
        <div class="author-createdAt-delete-update-container">
            <div class="author-createdAt-container">
                <p class="author">${comment.authorNickname}</p>
                <p class="createdAt">${formattedDate}</p>
            </div>
            <div class="delete-update-container">
                ${currentUserId == comment.userId ? `
                    <p class="update" onclick="editComment(${comment.id})">수정</p>
                    <p class="delete" onclick="deleteComment(${comment.id})">삭제</p>
                ` : ''}
            </div>
        </div>
        <div class="content-container">
            <p>${comment.content}</p>
        </div>
    `;

    return commentDiv;
}

function submitComment() {
    const content = document.getElementById('commentContent').value;
    fetch(`/api/posts/${postId}/comments`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content: content }),
    })
        .then(response => response.json())
        .then(comment => {
            document.getElementById('commentContent').value = '';
            loadComments();
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
                }
            });
    }
}