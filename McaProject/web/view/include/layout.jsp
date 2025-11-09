<%-- 
    Document   : layout.jsp
    Created on : Nov 6, 2025
    Author     : Hiro
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${pageTitle != null ? pageTitle : 'Quản lý nhân viên'}" /></title>
    <script src="https://unpkg.com/alpinejs@3.x.x/dist/cdn.min.js" defer></script>

    <style>
        :root {
            --color-primary: #2563eb;
            --color-primary-dark: #1e40af;
            --sidebar-bg: #111827;
            --sidebar-hover: #1f2937;
            --text-light: #f9fafb;
            --text-muted: #9ca3af;
            --main-bg: #f3f4f6;
            --card-bg: #ffffff;
            --shadow: 0 4px 12px rgba(0,0,0,0.15);
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            font-family: "Segoe UI", Arial, sans-serif;
            background: var(--main-bg);
            color: #111;
            overflow-x: hidden;
        }

        header {
            background: linear-gradient(90deg, var(--color-primary-dark), var(--color-primary));
            color: white;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 14px 25px;
            font-size: 1.1em;
            box-shadow: var(--shadow);
            position: sticky;
            top: 0;
            z-index: 999; /* nâng cao để nổi trên main */
        }

        .header-left {
            display: flex;
            align-items: center;
            gap: 8px;
            font-weight: 600;
        }

        .header-right {
            position: relative;
        }

        .account-btn {
            background: transparent;
            border: none;
            color: #fff;
            font-size: 1em;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 6px;
            transition: 0.3s;
        }

        .account-btn:hover { opacity: 0.85; }

        .dropdown {
            position: absolute;
            right: 0;
            top: 45px;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 6px 20px rgba(0,0,0,0.25);
            overflow: hidden;
            min-width: 180px;
            z-index: 2000; /* fix: nổi hẳn lên trên */
        }

        .dropdown a {
            display: block;
            color: #374151;
            text-decoration: none;
            padding: 10px 15px;
            transition: 0.3s;
        }

        .dropdown a:hover {
            background: #f3f4f6;
            color: var(--color-primary);
        }

        .container {
            display: flex;
            min-height: calc(100vh - 70px);
            overflow: visible; /* fix: tránh cắt dropdown */
        }

        aside {
            width: 230px;
            background: var(--sidebar-bg);
            color: var(--text-light);
            display: flex;
            flex-direction: column;
            padding: 20px 0;
            box-shadow: 2px 0 8px rgba(0,0,0,0.2);
            z-index: 10;
        }

        aside h2 {
            text-align: center;
            color: var(--text-muted);
            font-size: 0.95em;
            margin-bottom: 12px;
        }

        aside a, summary {
            color: var(--text-light);
            text-decoration: none;
            padding: 10px 20px;
            display: block;
            transition: 0.3s;
            border-left: 4px solid transparent;
            font-size: 0.95em;
            cursor: pointer;
        }

        aside a:hover, summary:hover, aside a.active {
            background: var(--sidebar-hover);
            border-left-color: var(--color-primary);
            color: #fff;
        }

        details.menu-group summary::-webkit-details-marker { display: none; }

        .submenu {
            background: #1f2937;
            display: flex;
            flex-direction: column;
        }

        .submenu a {
            padding-left: 45px;
            font-size: 0.9em;
            border-left: none;
        }

        main {
            flex: 1;
            background: var(--main-bg);
            margin: 20px;
            border-radius: 12px;
            box-shadow: var(--shadow);
            padding: 25px;
            overflow-x: auto;
        }

        footer {
            text-align: center;
            padding: 15px;
            color: #6b7280;
            font-size: 0.9em;
        }

        [x-cloak] { display: none !important; }
    </style>
</head>

<body>
<header>
    <div class="header-left">
        🌸 <strong>Hệ thống quản lý nhân viên</strong>
    </div>

    <div class="header-right" x-data="{ open: false }" x-cloak>
        <button @click="open = !open" class="account-btn">
            👤 <c:out value="${sessionScope.user.username}" />
        </button>

        <div x-show="open" @click.away="open = false"
             x-transition:enter="transition ease-out duration-200"
             x-transition:enter-start="opacity-0 translate-y-1"
             x-transition:enter-end="opacity-100 translate-y-0"
             x-transition:leave="transition ease-in duration-150"
             x-transition:leave-start="opacity-100 translate-y-0"
             x-transition:leave-end="opacity-0 translate-y-1"
             class="dropdown">
            <a href="${pageContext.request.contextPath}/account/profile">🔧 Thông tin cá nhân</a>
            <a href="${pageContext.request.contextPath}/logout">🚪 Đăng xuất</a>
        </div>
    </div>
</header>

<div class="container">
    <aside class="sidebar">
    <h2>MENU</h2>

    <!-- Dashboard có menu con -->
    <details class="menu-group" ${pageTitle == 'Dashboard' ? 'open' : ''}>
        <summary>🏠 Dashboard</summary>
        <a href="${pageContext.request.contextPath}/debug">🧩 Debug</a>
        <a href="${pageContext.request.contextPath}/controller/dashboard">🛠 Controller Dashboard</a>
    </details>
    <!-- Đơn nghỉ phép -->
    <details class="menu-group" ${pageTitle == 'Đơn nghỉ phép' ? 'open' : ''}>
        <summary>📝 Đơn nghỉ phép</summary>
        <a href="${pageContext.request.contextPath}/request/create"
           class="${pageTitle == 'Tạo đơn' ? 'active' : ''}">➕ Tạo đơn</a>
        <a href="${pageContext.request.contextPath}/request/list"
           class="${pageTitle == 'Danh sách đơn' ? 'active' : ''}">📋 Danh sách</a>
        <a href="${pageContext.request.contextPath}/calendar"
           class="${pageTitle == 'Lịch nghỉ' ? 'active' : ''}">📅 Lịch nghỉ</a>
    </details>

</aside>

    <main>
        <jsp:include page="${contentPage}" />
    </main>
</div>

<footer>
    © 2025 - Quản lý nhân viên | Thiết kế bởi Hiro 🌸
</footer>
</body>
</html>
