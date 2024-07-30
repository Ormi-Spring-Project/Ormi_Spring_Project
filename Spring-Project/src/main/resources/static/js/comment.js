   function addComment() {
    const content = document.getElementById('newCommentContent').value;
    if (!content.trim()) return;

    const commentContainer = document.createElement('div');
    commentContainer.className = 'comment-container';
    commentContainer.innerHTML = `
        <div class="author-createdAt-delete-update-container">
            <div class="author-createdAt-container">
                <span class="author">사용자</span>
                <span class="createdAt">${new Date().toISOString().split('T')[0]}</span>
            </div>
            <div class="delete-update-container">
                <span onclick="editComment(this)">수정</span>
                <span class="delete" onclick="deleteComment(this)">삭제</span>
            </div>
        </div>
        <div class="content-container">
            <p class="comment-content">${content}</p>
        </div>
    `;

    document.querySelector('.comment-write').before(commentContainer);
    document.getElementById('newCommentContent').value = '';
}

    function editComment(button) {
    const commentContainer = button.closest('.comment-container');
    const contentContainer = commentContainer.querySelector('.content-container');
    const content = contentContainer.querySelector('.comment-content').textContent;

    const textarea = document.createElement('textarea');
    textarea.value = content;
    contentContainer.innerHTML = '';
    contentContainer.appendChild(textarea);

    const saveButton = document.createElement('button');
    saveButton.textContent = '저장';
    saveButton.onclick = function() {
    const newContent = textarea.value;
    contentContainer.innerHTML = `<p class="comment-content">${newContent}</p>`;
};
    contentContainer.appendChild(saveButton);
}

    function deleteComment(button) {
    if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
    const commentContainer = button.closest('.comment-container');
    commentContainer.remove();
}
}