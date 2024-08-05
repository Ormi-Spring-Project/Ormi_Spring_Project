document.addEventListener('DOMContentLoaded', function() {
    const categoryItems = document.querySelectorAll('.category-item');
    const articleGrid = document.querySelector('.article-grid');
    const sliderLeft = document.querySelector('.slider-arrow.left');
    const sliderRight = document.querySelector('.slider-arrow.right');
    const categoryItemSlider = document.querySelector('.category-item-slider');
    const seeAllLink = document.querySelector('.see-all');

    // 카테고리 클릭 이벤트
    categoryItems.forEach(item => {
        item.addEventListener('click', function() {
            categoryItems.forEach(i => i.classList.remove('active'));
            this.classList.add('active');
            const categoryId = this.getAttribute('category-id');
            const categoryName = this.getAttribute('category-name');
            loadPostsByCategory(categoryId, categoryName);
        });
    });

    // 슬라이더 기능
    let scrollAmount = 0;
    const scrollStep = 1010;

    sliderLeft.addEventListener('click', function() {
        scrollAmount = Math.max(scrollAmount - scrollStep, 0);
        categoryItemSlider.scrollTo({
            left: scrollAmount,
            behavior: 'smooth'
        });
    });

    sliderRight.addEventListener('click', function() {
        scrollAmount = Math.min(scrollAmount + scrollStep, categoryItemSlider.scrollWidth - categoryItemSlider.clientWidth);
        categoryItemSlider.scrollTo({
            left: scrollAmount,
            behavior: 'smooth'
        });
    });

    function loadPostsByCategory(categoryId, categoryName) {
        fetch(`/v1/posts/article-items?categoryId=${categoryId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(posts => {
                updateArticleCards(posts,categoryId);
                updateSeeAllLink(categoryId, categoryName)
            })
            .catch(error => {
                console.error('Error:', error);
                // 에러 처리 로직
            });
    }

    function updateArticleCards(posts, categoryId) {
        articleGrid.innerHTML = '';
        // 최대 4개의 게시글만 표시
        const postsToShow = posts.slice(0, 4);
        postsToShow.forEach(post => {
            const articleCard = createArticleCard(post, categoryId);
            articleGrid.appendChild(articleCard);
        });
    }

    function createArticleCard(post, categoryId) {
        const card = document.createElement('a');
        card.href = `/v1/posts/post/${post.id}?categoryId=${categoryId}`; // 게시글 상세 페이지 링크
        card.className = 'article-card';
        card.innerHTML = `
            <img class="article-img" src="${post.imageUrl || '/images/cat-icon.png'}" alt="${post.title}">
            <h4>${post.title}</h4>
            <div class="article-info">
                <p class="article-author">${post.authorName}</p>
                <p class="article-createAt">${formatDate(post.createdAt)}</p>
            </div>
        `;
        return card;
    }

    function formatDate(dateString) {
        const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
        return new Date(dateString).toLocaleDateString('ko-KR', options);
    }

    function updateSeeAllLink(categoryId, categoryName) {
        seeAllLink.href = `/v1/posts?categoryId=${categoryId}`;
        seeAllLink.textContent = `See All ${categoryName} Articles >`;
    }

    // 초기 로드 시 첫 번째 카테고리의 게시글 로드
    if (categoryItems.length > 0) {
        const firstCategoryItem = categoryItems[0];
        const firstCategoryId = firstCategoryItem.getAttribute('category-id');
        const firstCategoryName = firstCategoryItem.getAttribute('category-name');
        loadPostsByCategory(firstCategoryId, firstCategoryName);
        firstCategoryItem.classList.add('active');
    }
});