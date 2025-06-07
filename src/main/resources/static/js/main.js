// js/main.js
document.addEventListener('DOMContentLoaded', function() {
    console.log("main.js loaded and DOMContentLoaded fired!");

    // ==================== Loading Overlay Functions ====================
    window.showLoadingOverlay = function() {
        const loadingOverlay = document.getElementById('loadingOverlay');
        if (loadingOverlay) {
            loadingOverlay.classList.remove('d-none');
            document.body.style.overflow = 'hidden'; // Ngăn cuộn trang khi loading
        }
    };

    window.hideLoadingOverlay = function() {
        const loadingOverlay = document.getElementById('loadingOverlay');
        if (loadingOverlay) {
            loadingOverlay.classList.add('d-none');
            document.body.style.overflow = ''; // Cho phép cuộn lại
        }
    };

    // Ẩn overlay khi trang tải xong (ví dụ sau redirect form submission)
    window.addEventListener('load', window.hideLoadingOverlay);

    // ==================== Sidebar Toggle (Mobile) ====================
    const navbarToggler = document.querySelector('.navbar-toggler');
    const sidebar = document.querySelector('.sidebar');
    const pageWrapper = document.querySelector('.page-wrapper');

    if (navbarToggler && sidebar && pageWrapper) {
        navbarToggler.addEventListener('click', function() {
            sidebar.classList.toggle('show');
            if (sidebar.classList.contains('show')) {
                const overlay = document.createElement('div');
                overlay.id = 'sidebar-overlay';
                overlay.style.cssText = 'position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 990;';
                document.body.appendChild(overlay);
                overlay.addEventListener('click', function() {
                    sidebar.classList.remove('show');
                    overlay.remove();
                });
            } else {
                const overlay = document.getElementById('sidebar-overlay');
                if (overlay) overlay.remove();
            }
        });
    }

    // ==================== Hàm gọi API cơ bản ====================
    // API_BASE_URL: Cần được định nghĩa trong mỗi file HTML cụ thể (ví dụ: /product-list.html)
    // Hoặc bạn có thể đặt cố định nếu backend của bạn chạy ở một địa chỉ khác:
    // const API_BASE_URL = 'http://localhost:8080';
    // const API_BASE_URL = window.location.origin; // Nếu frontend và backend cùng domain/port

    // Hàm này sẽ dùng chung cho tất cả các trang
    window.callApi = async function(endpoint, method = 'GET', data = null) {
        showLoadingOverlay();
        try {
            // Đảm bảo endpoint bắt đầu bằng '/' để nối với /api
            const url = `${window.location.origin}/api${endpoint.startsWith('/') ? endpoint : '/' + endpoint}`;
            
            const options = {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                    // Thêm header Authorization nếu bạn có dùng JWT token
                    // 'Authorization': 'Bearer ' + localStorage.getItem('authToken')
                }
            };

            if (data) {
                options.body = JSON.stringify(data);
            }

            const response = await fetch(url, options);

            if (!response.ok) {
                // Cố gắng đọc thông báo lỗi từ body response nếu có
                const errorText = await response.text();
                let errorMessage = `API call failed with status: ${response.status} - ${response.statusText}`;
                try {
                    const errorData = JSON.parse(errorText);
                    errorMessage = errorData.message || errorData.error || errorMessage;
                } catch (e) {
                    // Nếu không phải JSON, dùng nguyên văn
                    errorMessage = errorText || errorMessage;
                }
                throw new Error(errorMessage);
            }

            // Đối với DELETE hoặc các request không có nội dung trả về
            if (response.status === 204 || response.headers.get('Content-Length') === '0') {
                return null;
            }

            // Nếu có nội dung trả về, parse JSON
            return await response.json();

        } catch (error) {
            console.error('API call error:', error);
            alert('Đã có lỗi xảy ra: ' + error.message);
            throw error; // Ném lại lỗi để logic cụ thể của trang có thể bắt
        } finally {
            hideLoadingOverlay();
        }
    };

    // Global event listener for confirmation dialogs
    document.body.addEventListener('click', function(event) {
        if (event.target.classList.contains('confirm-action')) {
            const message = event.target.dataset.confirmMessage || 'Bạn có chắc chắn muốn thực hiện thao tác này?';
            if (!confirm(message)) {
                event.preventDefault(); // Ngăn hành động mặc định
            }
        }
    });
});