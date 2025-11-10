<%-- 
    Document   : security
    Created on : Nov 10, 2025, 12:35:50 PM
    Author     : Hiro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<script src="https://unpkg.com/alpinejs@3.x.x/dist/cdn.min.js" defer></script>

<style>
    .change-password-container {
        display: flex;
        justify-content: center;
        align-items: center;
        min-height: 85vh;
        background: var(--page-bg, #f5f6fa);
    }

    .change-password-card {
        background: #fff;
        width: 450px;
        border-radius: 12px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        padding: 30px;
        box-sizing: border-box;
        opacity: 0;
        transform: translateY(15px);
        transition: all 0.4s ease;
    }

    .change-password-card.show {
        opacity: 1;
        transform: translateY(0);
    }

    .change-password-card h2, h3 {
        text-align: center;
        color: #333;
        margin-bottom: 25px;
        font-size: 22px;
    }

    .form-group {
        margin-bottom: 18px;
        position: relative;
    }

    .form-group label {
        display: block;
        margin-bottom: 6px;
        color: #555;
        font-weight: 500;
    }

    .form-group input {
        width: 100%;
        padding: 10px 38px 10px 10px;
        border: 1px solid #ccc;
        border-radius: 6px;
        font-size: 15px;
        outline: none;
        transition: 0.2s;
    }

    .form-group input:focus {
        border-color: #3b82f6;
        box-shadow: 0 0 3px rgba(59,130,246,0.5);
    }

    .toggle-eye {
        position: absolute;
        top: 36px;
        right: 10px;
        cursor: pointer;
        color: #888;
    }

    .toggle-eye:hover {
        color: #3b82f6;
    }

    .submit-btn, .btn-danger {
        width: 100%;
        border: none;
        padding: 10px;
        font-size: 16px;
        border-radius: 6px;
        cursor: pointer;
        transition: 0.2s;
    }

    .submit-btn {
        background: #3b82f6;
        color: white;
    }

    .submit-btn:hover {
        background: #2563eb;
    }

    .btn-danger {
        background: #dc2626;
        color: white;
        margin-top: 10px;
    }

    .btn-danger:hover {
        background: #b91c1c;
    }

    .message {
        margin-top: 15px;
        padding: 10px;
        border-radius: 6px;
        text-align: center;
        font-weight: 500;
        transition: all 0.3s ease;
    }

    .message.success {
        background: #d1fae5;
        color: #065f46;
    }

    .message.error {
        background: #fee2e2;
        color: #991b1b;
    }
</style>

<div class="change-password-container"
     x-data="{
         showCard: false,
         showCurrent: false,
         showNew: false,
         showConfirm: false,
         newPass: '',
         confirmPass: '',
         matchError: false
     }"
     x-init="setTimeout(() => showCard = true, 100)">

    <div class="change-password-card" :class="{'show': showCard}">
        <h2>Đổi mật khẩu</h2>

        <!-- Form đổi mật khẩu -->
        <form action='${pageContext.request.contextPath}/account/security' method="post"
              @submit.prevent="
                if (newPass !== confirmPass) {
                    matchError = true;
                } else {
                    $el.submit();
                }
              ">

            <div class="form-group">
                <label>Mật khẩu hiện tại</label>
                <input :type='showCurrent ? "text" : "password"' name="current_password" required>
                <span class="toggle-eye" @click="showCurrent = !showCurrent">
                    <i x-text="showCurrent ? '🙈' : '👁️'"></i>
                </span>
            </div>

            <div class="form-group">
                <label>Mật khẩu mới</label>
                <input :type='showNew ? "text" : "password"' name="new_password" x-model="newPass" required>
                <span class="toggle-eye" @click="showNew = !showNew">
                    <i x-text="showNew ? '🙈' : '👁️'"></i>
                </span>
            </div>

            <div class="form-group">
                <label>Xác nhận mật khẩu mới</label>
                <input :type='showConfirm ? "text" : "password"' name="confirm_password" x-model="confirmPass" required>
                <span class="toggle-eye" @click="showConfirm = !showConfirm">
                    <i x-text="showConfirm ? '🙈' : '👁️'"></i>
                </span>

                <template x-if="matchError">
                    <div class="message error mt-2" x-text="'Mật khẩu xác nhận không khớp!'"></div>
                </template>
            </div>

            <button type="submit" class="submit-btn">Đổi mật khẩu</button>
        </form>

        <hr style="margin: 25px 0; border: none; border-top: 1px solid #e5e7eb;">

        <!-- Form vô hiệu hóa -->
        <h3>Vô hiệu hóa tài khoản</h3>
        <p style="text-align:center; color:#991b1b; font-size:14px;">
            Sau khi vô hiệu hóa, bạn sẽ không thể đăng nhập lại bằng tài khoản này.
        </p>

        <form action="${pageContext.request.contextPath}/account/security" method="post"
              onsubmit="return confirm('Bạn có chắc muốn vô hiệu hóa tài khoản không?');">
            <input type="hidden" name="action" value="deactivate">
            <button type="submit" class="btn-danger">Vô hiệu hóa tài khoản</button>
        </form>

        <!-- Hiển thị thông báo -->
        <c:if test="${not empty message}">
            <div class="message
                 ${message eq 'Đổi mật khẩu thành công!' ? 'success' : 'error'}"
                 x-data="{ show: true }"
                 x-show="show"
                 x-transition.opacity.duration.500ms
                 x-init="setTimeout(() => show = false, 4000)">
                ${message}
            </div>
        </c:if>
    </div>
</div>
