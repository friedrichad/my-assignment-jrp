<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>MyAssignmentPrj</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sakura.css">
    <style>
        /* Reset mặc định để tránh margin/padding thừa */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            background-color: #f0f2f5;
            display: flex;
            justify-content: center;
            min-height: 100vh;
            margin: 0;
            max-width: none !important; /* override Sakura max-width */
            width: 100%;
            overflow-x: hidden; /* tránh thanh cuộn ngang */
        }

        /* === HEADER CHUNG === */
        header {
            background-color: #ffffff;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            display: flex;
            flex-direction: column;
            align-items: center;
            border-radius: 0;
            position: relative;
            width: 100%;
        }

        /* === LOGO === */
        .logo {
            font-size: 30px;
            font-weight: bold;
            color: #4a90e2;
            text-decoration: none;
            margin: 15px 0;
        }

        /* === NAVBAR === */
        .navbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 100%;
            padding: 0 5vw;
            border-top: 1px solid #eee;
        }

        nav ul {
            list-style: none;
            display: flex;
            gap: 32px;
        }

        nav ul li {
            position: relative;
        }

        nav ul li a {
            text-decoration: none;
            color: #333;
            font-weight: 500;
            transition: color 0.2s;
            padding: 10px 6px;
            display: inline-block;
        }

        nav ul li a:hover {
            color: #4a90e2;
        }

        /* === DROPDOWN CHO DỊCH VỤ === */
        .dropdown-content {
            display: none;
            position: absolute;
            top: 38px;
            left: 0;
            background-color: #fff;
            min-width: 200px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            border-radius: 8px;
            overflow: hidden;
            z-index: 1000;
        }

        .dropdown-content a {
            display: block;
            padding: 10px 15px;
            text-decoration: none;
            color: #333;
        }

        .dropdown-content a:hover {
            background-color: #f0f8ff;
            color: #4a90e2;
        }

        nav ul li:hover .dropdown-content {
            display: block;
        }

        /* === USER MENU === */
        .user-menu {
            position: relative;
            display: inline-block;
        }

        .user-name {
            cursor: pointer;
            font-weight: 600;
            color: #4a90e2;
        }

        .user-dropdown {
            display: none;
            position: absolute;
            right: 0;
            top: 30px;
            background-color: #fff;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            border-radius: 8px;
            overflow: hidden;
            min-width: 180px;
            z-index: 1000;
        }

        .user-dropdown a {
            display: block;
            padding: 10px 15px;
            text-decoration: none;
            color: #333;
        }

        .user-dropdown a:hover {
            background-color: #f0f8ff;
            color: #4a90e2;
        }

        .user-menu.active .user-dropdown {
            display: block;
        }

        /* === RESPONSIVE === */
        @media (max-width: 768px) {
            nav ul {
                flex-direction: column;
                background-color: white;
                position: absolute;
                top: 60px;
                left: 0;
                width: 100%;
                display: none;
                padding: 15px 0;
            }

            nav ul li {
                text-align: center;
            }

            nav.active ul {
                display: flex;
            }

            .navbar {
                flex-direction: column;
                align-items: flex-start;
            }
        }
    </style>
</head>
<body>
<header>
    <a href="${pageContext.request.contextPath}/index.jsp" class="logo">MyAssignmentPrj</a>

    <div class="navbar">
        <!-- NAV LEFT -->
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a></li>
                <li><a href="${pageContext.request.contextPath}/about.jsp">Giới thiệu</a></li>

                <!-- DROPDOWN DỊCH VỤ -->
                <li>
                    <a href="${pageContext.request.contextPath}/service.jsp">Dịch vụ ▾</a>
                    <div class="dropdown-content">
                        <a href="${pageContext.request.contextPath}/service.jsp#it">Dịch vụ CNTT</a>
                        <a href="${pageContext.request.contextPath}/service.jsp#hr">Dịch vụ Nhân sự</a>
                        <a href="${pageContext.request.contextPath}/service.jsp#finance">Dịch vụ Tài chính</a>
                    </div>
                </li>
            </ul>
        </nav>

        <!-- USER INFO DROPDOWN -->
        <div class="user-menu" id="userMenu">
            <%
                String username = (String) session.getAttribute("username");
                if (username == null) username = "Khách";
            %>
            <span class="user-name"><%= username %> ▾</span>
            <div class="user-dropdown">
                <a href="${pageContext.request.contextPath}/account/info.jsp">Thông tin tài khoản</a>
                <a href="${pageContext.request.contextPath}/account/security.jsp">Bảo mật</a>
                <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </div>
        </div>
    </div>
</header>

<script>
    document.addEventListener('DOMContentLoaded', () => {
        const userMenu = document.getElementById('userMenu');
        userMenu.addEventListener('click', () => {
            userMenu.classList.toggle('active');
        });

        document.addEventListener('click', (e) => {
            if (!userMenu.contains(e.target)) {
                userMenu.classList.remove('active');
            }
        });
    });
</script>
</body>
</html>
