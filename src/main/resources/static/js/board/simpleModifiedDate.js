function formatRelativeDate(date) {
    const now = new Date();
    const diffInSeconds = Math.floor((now - date) / 1000);

    if (diffInSeconds < 60) {
        return "방금 전";
    } else if (diffInSeconds < 60 * 60) {
        const minutes = Math.floor(diffInSeconds / 60);
        return `${minutes}분 전`;
    } else if (diffInSeconds < 60 * 60 * 24) {
        const hours = Math.floor(diffInSeconds / (60 * 60));
        return `${hours}시간 전`;
    } else if (diffInSeconds < 60 * 60 * 24 * 365) {
        const days = Math.floor(diffInSeconds / (60 * 60 * 24));
        return `${days}일 전`;
    } else {
        const years = Math.floor(diffInSeconds / (60 * 60 * 24 * 365))
        return `${years}년 전`
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const modifiedDateElements = document.querySelector(".modifiedDate");

    if (modifiedDateElements) {
        const modifiedDate = new Date(modifiedDateElements.textContent);
        modifiedDateElements.textContent = formatRelativeDate(modifiedDate) + " 수정됨";
    }
});