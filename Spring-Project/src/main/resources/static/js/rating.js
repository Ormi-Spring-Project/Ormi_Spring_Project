let selectedRating = 0;

function initializeRating() {
    document.querySelectorAll('.star').forEach(star => {
        star.addEventListener('click', function() {
            selectedRating = parseInt(this.getAttribute('data-value'));
            highlightStars(selectedRating);
        });
    });
}

function highlightStars(rating) {
    document.querySelectorAll('.star').forEach(star => {
        const starValue = parseInt(star.getAttribute('data-value'));
        if (starValue <= rating) {
            star.classList.add('selected');
        } else {
            star.classList.remove('selected');
        }
    });
}

function getSelectedRating() {
    return selectedRating;
}

function resetRating() {
    selectedRating = 0;
    highlightStars(0);
}

function loadAverageRating(postId) {
    fetch(`/api/ratings/post/${postId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(averageRating => {
            console.log('Average rating:', averageRating);
            const averageRatingElement = document.getElementById('averageRating');
            const averageRatingStarsElement = document.getElementById('averageRatingStars');

            if (averageRatingElement && averageRatingStarsElement) {
                if (averageRating > 0) {
                    const roundedRating = Math.round(averageRating * 2) / 2;
                    averageRatingElement.textContent = averageRating.toFixed(1);
                    averageRatingStarsElement.textContent = '★'.repeat(Math.floor(roundedRating)) + '☆'.repeat(5 - Math.floor(roundedRating));
                } else {
                    averageRatingElement.textContent = '아직 별점이 없어요!';
                    averageRatingStarsElement.textContent = '☆☆☆☆☆';
                }
            } else {
                console.error('Rating elements not found');
            }
        })
        .catch(error => {
            console.error('Error loading average rating:', error);
            const averageRatingElement = document.getElementById('averageRating');
            if (averageRatingElement) {
                averageRatingElement.textContent = '오류발생';
            }
        });
}

window.ratingModule = {
    initializeRating,
    getSelectedRating,
    resetRating,
    loadAverageRating
};